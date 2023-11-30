package Model;

import Common.InputValidator;
import Common.JDBCQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CitizenReport {
    //'- Hiển thị thông tin các hộ dân liên quan đến đối tượng ưu tiên X (X nhập từ bàn phím)
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

        } finally {
            //JDBCQuery.closeConnection();

        }
        return rs;
    }

    public void printSearchByCitizenObject() {
        List<Integer> list = new ArrayList<Integer>();
        boolean check = false;
        System.out.println("List citizens who are household lords");
        printResultSet(getListCitizen());

        System.out.println("Search by Citizen Object");
        String name;
        do {
            name = InputValidator.validateStringPriorityObject("Enter object type: ");
            if (name.equals("Binh Thuong")) {
                System.out.println("Error");
            }
        }
        while (name.equals("Binh Thuong"));
        try {
            ResultSet rs = getListCitizen();

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
                    // Check if the object_type contains the specified substring
                    if (rs.getString("type_name_object").toLowerCase().contains(name.toLowerCase())
                            || rs.getString("age_category").toLowerCase().contains(name.toLowerCase())) {
                        for (int i = 1; i <= columnCount; i++) {
                            System.out.printf("%-25s", rs.getString(i));
                            list.add(rs.getInt("house_id"));
                            check = true;

                        }
                        System.out.println();
                    }
                }
                if (check == true) {
                    System.out.println("LIST HOUSE HOLD LORD ");
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

    public void printCitizenObject(int id) {
        try {
            ResultSet rs = getCitizenByHouseId(id);

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
            throw new RuntimeException(e);
        }

    }

    // Liệt kê top 5 hộ có nhiều đối tượng ưu tiên nhất
    public ResultSet getListCitizenTop5() {
        ResultSet rs = null;
        try {
            JDBCQuery.openConnection();
            String sql = """
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
                             or DATEDIFF(YEAR, date_of_birth, GETDATE()) <= 8
                             or DATEDIFF(YEAR, date_of_birth, GETDATE()) >= 60
                          group by house_id
                          order by total desc) as temp on cz.house_id = temp.house_id
                    where cz.is_household_lord = 1
                    order by total desc
                    """;
            rs = JDBCQuery.executeSelectQuery(sql);


        } catch (Exception exception) {
            exception.printStackTrace();

        } finally {
            //JDBCQuery.closeConnection();

        }
        return rs;
    }
    public void printCitizenObjectTop5() {
        try {
            ResultSet rs = getListCitizenTop5();

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
            throw new RuntimeException(e);
        }

    }


}
