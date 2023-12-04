package Model;

import Common.InputValidator;
import Common.JDBCQuery;
import Common.JdbcConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CitizenReport {
    //'- Hiển thị thông tin các hộ dân liên quan đến đối tượng ưu tiên X (X nhập từ bàn phím)
    public static  int index = 1;
    public ResultSet getListCitizen() {
        ResultSet rs = null;
        try {
            JDBCQuery.openConnection();
            String sql = """
                    SELECT Citizen.house_id, Citizen.name, Citizen.address, Citizen.identity_card, FORMAT(Citizen.date_of_birth, 'dd-MM-yyyy') AS date_of_birth,
                            CASE
                                WHEN DATEDIFF(YEAR, date_of_birth, GETDATE()) <= 8 THEN 'Tre em'
                                WHEN DATEDIFF(YEAR, date_of_birth, GETDATE()) >= 60 THEN 'Nguoi gia'
                                ELSE ''
                                END AS age_category,
                            CO.type_name_object,
                            CASE
                                WHEN is_household_lord=1  THEN 'Chu Ho'
                                ELSE ''
                                END AS household_lord
                                            
                            FROM
                                Citizen
                                    LEFT JOIN
                                dbo.CitizenObject CO ON CO.id = Citizen.citizen_object_id
                                    LEFT JOIN
                                    House h on h.id=Citizen.house_id
                            ORDER BY house_id ASC
                            """;
            rs = JDBCQuery.executeSelectQuery(sql);


        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return rs;
    }

    public void getListCitizenObj() {
        String connectionString = JdbcConfig.JDBC_URL;
        String username = JdbcConfig.USERNAME;
        String password = JdbcConfig.PASSWORD;

        try (
                Connection connection = DriverManager.getConnection(connectionString, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("""
                            SELECT cz.house_id, cz.name, cz.address, co.type_name_object, cz.identity_card,
                                   FORMAT(cz.date_of_birth, 'dd-MM-yyyy') AS date_of_birth,
                                   CASE WHEN is_household_lord=1 THEN 'Chu Ho' ELSE '' END AS household_lord,
                                   CASE WHEN cz.sex = 1 THEN 'Nam' ELSE 'Nu' END AS sex
                            FROM Citizen cz
                                 LEFT JOIN CitizenObject co ON cz.citizen_object_id = co.id
                                 LEFT JOIN House h ON h.id = cz.house_id
                            ORDER BY house_id ASC
""");
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            // Print header
            System.out.printf("%-5s%-25s%-25s%-20s%-20s%-15s%-20s%-10s%n", "ID", "Name", "Address", "Object Type", "Identity Card", "Date of Birth", "Household Lord", "Sex");

            // Counter for the index
            int index = 1;

            while (resultSet.next()) {
                int houseId = resultSet.getInt("house_id");
                String name = resultSet.getString("name");
                String address = resultSet.getString("address");
                String typeNameObject = resultSet.getString("type_name_object");
                String identityCard = resultSet.getString("identity_card");
                String dateOfBirth = resultSet.getString("date_of_birth");
                String householdLord = resultSet.getString("household_lord");
                String sex = resultSet.getString("sex");

                // Print data in a formatted way
                System.out.printf("%-5d%-25s%-25s%-20s%-20s%-15s%-20s%-10s%n", index++,  name, address, typeNameObject, identityCard, dateOfBirth, householdLord, sex);
            }

            // Example search by type_name_object
            System.out.println("Search by Citizen Object");
            String searchObjectType;
            do {
                searchObjectType = InputValidator.validateStringPriorityObject("Enter object type: ");
                if (searchObjectType.equals("Binh Thuong")) {
                    System.out.println("Error");
                }
            }
            while (searchObjectType.equals("Binh Thuong"));
            searchByObjectType(connection, searchObjectType);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchByObjectType(Connection connection, String objectType) {
        List<Integer> list = new ArrayList<Integer>();
        boolean check = false;
        try {
            // SQL query for searching by type_name_object
            String searchQuery = """
                        SELECT cz.house_id, cz.name, cz.address, co.type_name_object, cz.identity_card,
                               FORMAT(cz.date_of_birth, 'dd-MM-yyyy') AS date_of_birth,
                               CASE WHEN is_household_lord=1 THEN 'Chu Ho' ELSE '' END AS household_lord,
                               CASE WHEN cz.sex = 1 THEN 'Nam' ELSE 'Nu' END AS sex
                        FROM Citizen cz
                             LEFT JOIN CitizenObject co ON cz.citizen_object_id = co.id
                             LEFT JOIN House h ON h.id = cz.house_id
                        WHERE co.type_name_object like ?
                        ORDER BY house_id ASC
                    """;

            try (PreparedStatement searchStatement = connection.prepareStatement(searchQuery)) {
                searchStatement.setString(1, "%" + objectType + "%");
                ResultSet searchResult = searchStatement.executeQuery();

                // Print the search result
                System.out.println("\nSearch Results for Object Type: " + objectType);
                //System.out.printf("%-5s%-10s%-20s%-20s%-20s%-20s%-15s%-20s%-10s%n", "Index", "House ID", "Name", "Address", "Object Type", "Identity Card", "Date of Birth", "Household Lord", "Sex");

                int index = 1;
                while (searchResult.next()) {
                    int houseId = searchResult.getInt("house_id");
                    String name = searchResult.getString("name");
                    String address = searchResult.getString("address");
                    String typeNameObject = searchResult.getString("type_name_object");
                    String identityCard = searchResult.getString("identity_card");
                    String dateOfBirth = searchResult.getString("date_of_birth");
                    String householdLord = searchResult.getString("household_lord");
                    String sex = searchResult.getString("sex");
                    list.add(searchResult.getInt("house_id"));
                    check = true;
                    // Print data in a formatted way
                    //  System.out.printf("%-5d%-10d%-20s%-20s%-20s%-20s%-15s%-20s%-10s%n", index++, houseId, name, address, typeNameObject, identityCard, dateOfBirth, householdLord, sex);
                }
                if (check) {
                    System.out.println("LIST HOUSE HOLD LORD ");
                    System.out.printf("\u001B[1m%-5s%-25s%-20s%-20s%-40s%-15s%-5s\u001B[0m%n", "ID",  "NAME", "IDENTITY_CARD", "PHONE_NUMBER", "ADDRESS", "DATE_OF_BIRTH", "SEX");
                    System.out.println();
                    for (Integer houseId : new HashSet<>(list)) {
                        printCitizenObject(houseId);
                    }
                } else {
                    System.out.println("Data Empty");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void printResultSet(ResultSet rs) {
        try {
            // Check if the ResultSet is not null
            if (rs != null) {
                // Get metadata to retrieve column names
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Print column headers
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("\u001B[1m%-25s\u001B[0m", metaData.getColumnLabel(i).toUpperCase());
                }
                System.out.println();

                // Print data
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.printf("%-25s", rs.getString(i));

                    }
                    System.out.println();
                }
            } else {
                System.out.println("Data Empty");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //lay tat ca citizen theo houseId
    public ResultSet getCitizenByHouseId(int houseId) {
        ResultSet rs = null;
        try {
            JDBCQuery.openConnection();
            String sql = """
                    select cz.house_id,cz.name,cz.identity_card,cz.phone_number, cz.address,FORMAT(cz.date_of_birth, 'dd-MM-yyyy') AS date_of_birth,
                     CASE WHEN cz.sex = 1 THEN 'Nam' ELSE 'Nu' END AS sex
                     from Citizen as cz where is_household_lord=1 and cz.house_id=?
                      """;
            Object[] params = {houseId};
            rs = JDBCQuery.executeSelectQuery(sql, params);

        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return rs;
    }

    public void printCitizenObject(int targetHouseId) {
        String connectionString = JdbcConfig.JDBC_URL;
        String username = JdbcConfig.USERNAME;
        String password = JdbcConfig.PASSWORD;

        try (
                Connection connection = DriverManager.getConnection(connectionString, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("""
                SELECT cz.house_id, cz.name, cz.identity_card, cz.phone_number, cz.address,
                       FORMAT(cz.date_of_birth, 'dd-MM-yyyy') AS date_of_birth,
                       CASE WHEN cz.sex = 1 THEN 'Nam' ELSE 'Nu' END AS sex
                FROM Citizen cz
                WHERE is_household_lord = 1 AND cz.house_id = ?
            """)) {

            preparedStatement.setInt(1, targetHouseId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    System.out.printf("%-5d%-25s%-20s%-20s%-40s%-15s%-5s%n",
                            index++,
                            resultSet.getString("name"),
                            resultSet.getString("identity_card"),
                            resultSet.getString("phone_number"),
                            resultSet.getString("address"),
                            resultSet.getString("date_of_birth"),
                            resultSet.getString("sex"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Liệt kê top 5 hộ có nhiều đối tượng ưu tiên nhất


    public void selectTop5() {
        String connectionString = JdbcConfig.JDBC_URL;
        String username = JdbcConfig.USERNAME;
        String password = JdbcConfig.PASSWORD;


        try (
                Connection connection = DriverManager.getConnection(connectionString, username, password);
                PreparedStatement preparedStatement = connection.prepareStatement("""
                        select top 5 cz.house_id,
                                     cz.name,
                                     cz.identity_card,
                                     CASE WHEN cz.sex = 1 then 'Nam' ELSE 'Nu' END as sex,
                                     FORMAT(cz.date_of_birth, 'dd-MM-yyyy')        AS date_of_birth,
                                     cz.address,
                                     total
                              from Citizen cz
                                       inner join
                                   (SELECT top 5 Count(dbo.Citizen.id) AS total, house_id
                                    FROM Citizen
                                             Join dbo.CitizenObject CO on Citizen.citizen_object_id = CO.id
                                             Join dbo.House H on Citizen.house_id = H.id
                                    Where citizen_object_id <> 3
                                    group by house_id
                                    order by total desc) as temp on cz.house_id = temp.house_id
                              where cz.is_household_lord = 1
                              order by total desc
                        """);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            // Print header
            System.out.printf("%-15s%-25s%-20s%-10s%-15s%-30s%n", "ID", "Name", "Identity Card", "Sex", "Date of Birth", "Address");

            // Counter for the index
            int index = 1;

            while (resultSet.next()) {
                // Retrieve and print data from the result set
                String name = resultSet.getString("name");
                String identityCard = resultSet.getString("identity_card");
                String sex = resultSet.getString("sex");
                String dateOfBirth = resultSet.getString("date_of_birth");
                String address = resultSet.getString("address");

                // Print data in a formatted way
                System.out.printf("%-15d%-25s%-20s%-10s%-15s%-30s%n", index++, name, identityCard, sex, dateOfBirth, address);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}