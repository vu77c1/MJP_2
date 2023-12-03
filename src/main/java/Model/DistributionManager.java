package Model;

import Common.InputValidator;
import Common.InputValidator1;
import Common.JDBCQuery1;

import java.lang.reflect.Method;
import java.sql.*;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;


import static Model.Processing.countRecords;
import static Model.Processing.dateFormat;
import static Model.Processing.waitForEnter;
import static Model.Processing1.*;

public class DistributionManager {
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu/MM/dd").withResolverStyle(ResolverStyle.STRICT);
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private static Connection con = JDBCQuery1.connectDatabase();
    private static Scanner sc = new Scanner(System.in);

    public static void handleDistributionManager() {
        DistributionManager distributionManager = new DistributionManager();
        int choice = -1;
        do {

            System.out.println("\t\t\tManage distribution lists");
            System.out.println("\t\t\t1. Show distribution list");
            System.out.println("\t\t\t2. Add distribution information");
            System.out.println("\t\t\t3. Update distribution information");
            System.out.println("\t\t\t4. Delete distribution information");
            System.out.println("\t\t\t0. Return to menu main");
            System.out.println();
            System.out.println("\t\t\tWhat do you want to choose?");


            do {
                try {
                    System.out.print("\t\t\tEnter the program number: (0-4): ");
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException input) {
                    System.out.println("\t\t\t\u001B[31mInvalid character entered!\n\t\t\tPlease re-enter (0-4)!\u001B[0m");
                }
            }
            while (choice == -1);
            switch (choice) {
                case 0:
                    System.out.println("\t\t\tReturn to the main screen");
                    try {
                        Class<?> mainManagerClass = Class.forName("MainManager");
                        Method mainMethod = mainManagerClass.getMethod("main", String[].class);
                        mainMethod.invoke(null, (Object) new String[]{});
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("\t\t\t\u001B[31mAn error occurred during processing!\u001B[0m");
                    }
                    break;
                case 1:
                    printDistribution(con);
                    //printDistribution1(con);
                    break;
                case 2:

                    distributionManager.addDistribution();
                    break;
                case 3:
                    updateDistribution(con);
                    break;
                case 4:
                    deleteDistribution(con);
                    break;
                default:
                    System.out.println("\t\t\t\u001B[31mInvalid function. Please re-enter.\u001B[0m\n");

            }
        }
        while (choice!=0);
    }

    public static Map<Integer, Distribution> getDistribution(Connection con) {
        Map<Integer, Distribution> indexObjectMap = new LinkedHashMap<>();
        try {
            String selectQuery = """
                    SELECT  Distribution.id, precint_name, city_name,province_name,Distribution.commission_id as commission_id , household_id,Ci.is_household_lord,Ci.name as name, amount_distribution, date_distribution
                    FROM Distribution Join dbo.Commission C on C.id= Distribution.commission_id  join House H on H.id= Distribution.household_id\s
                     join Citizen Ci on Ci.house_id=H.id where Ci.is_household_lord=1  order by Distribution.id desc;""";

            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
                ResultSet rs = preparedStatement.executeQuery();
                if (rs != null) {
                    int index = 1;
                    try {
                        while (rs.next()) {
                            Distribution distribution = new Distribution(
                                    rs.getInt("id"),
                                    rs.getInt("commission_id"),
                                    rs.getInt("household_id"),
                                    rs.getDouble("amount_distribution"),
                                    rs.getDate("date_distribution"),
                                    rs.getString("precint_name"),
                                    rs.getString("city_name"),
                                    rs.getString("province_name"),
                                    rs.getInt("is_household_lord"),
                                    rs.getString("name")
                            );

                            indexObjectMap.put(index, distribution);
                            index++;
                        }
                    } catch (SQLException ex) {
                        System.out.println("\t\t\t\u001B[31mThere was an error processing the result set: " + ex.getMessage() + ".\u001B[0m");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + ex.getMessage() + ".\u001B[0m");
        }
        return indexObjectMap;
    }

    public static Map<Integer, Distribution> getDistribution1(Connection con) {
        Map<Integer, Distribution> indexObjectMap = new LinkedHashMap<>();
        try {
            String selectQuery = " select dbo.Commission.id as id, precint_name, city_name,province_name from Commission";

            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
                ResultSet rs = preparedStatement.executeQuery();
                if (rs != null) {
                    int index = 1;
                    try {
                        while (rs.next()) {
                            Distribution distribution = new Distribution(
                                    rs.getInt("id"),
                                    rs.getString("precint_name"),
                                    rs.getString("city_name"),
                                    rs.getString("province_name")

                            );
                            indexObjectMap.put(index, distribution);
                            index++;
                        }
                    } catch (SQLException ex) {
                        System.out.println("\t\t\t\u001B[31mThere was an error processing the result set: " + ex.getMessage() + ".\u001B[0m");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + ex.getMessage() + ".\u001B[0m");
        }
        return indexObjectMap;
    }

    public static Map<Integer, Distribution> getDistribution2(Connection con) {
        Map<Integer, Distribution> indexObjectMap = new LinkedHashMap<>();
        try {
            String selectQuery = "  SELECT distinct household_id ,Ci.is_household_lord as is_household_lord,Ci.name as name\n" +
                    "FROM Distribution Join dbo.Commission C on C.id= Distribution.commission_id  join House H on H.id= Distribution.household_id \n" +
                    " join Citizen Ci on Ci.house_id=H.id where Ci.is_household_lord=1  ";

            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
                ResultSet rs = preparedStatement.executeQuery();
                if (rs != null) {
                    int index = 1;
                    try {
                        while (rs.next()) {
                            Distribution distribution = new Distribution(
                                    rs.getInt("household_id"),
                                    rs.getInt("is_household_lord"),
                                    rs.getString("name")

                            );
                            indexObjectMap.put(index, distribution);
                            index++;
                        }
                    } catch (SQLException ex) {
                        System.out.println("\t\t\t\u001B[31mThere was an error processing the result set: " + ex.getMessage() + ".\u001B[0m");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + ex.getMessage() + ".\u001B[0m");
        }
        return indexObjectMap;
    }

    public static int getIdFromIndex(Connection con, int targetIndex) {
        Map<Integer, Distribution> indexObjectMap = getDistribution(con);

        if (indexObjectMap.containsKey(targetIndex)) {
            Distribution distribution = indexObjectMap.get(targetIndex);
            if (distribution != null) {
                return distribution.getId();
            } else {
                System.out.println("\t\t\t\u001B[31mError: Distribution object is null for index " + targetIndex + "\u001B[0m");
            }
        } else {
            System.out.println("\t\t\t\u001B[31mError: Index " + targetIndex + " is out of bounds.\u001B[0m");
        }
        // Return a default value or throw an exception based on your requirements
        return -1; // or throw new IllegalArgumentException("Invalid index");
    }

    public static int getIdFromIndexCommission(Connection con, int targetIndex) {
        Map<Integer, Distribution> indexObjectMap = getDistribution1(con);

        if (indexObjectMap.containsKey(targetIndex)) {
            Distribution distribution = indexObjectMap.get(targetIndex);
            if (distribution != null) {
                return distribution.getCommissionId();
            } else {
                System.out.println("\t\t\t\u001B[31mError: Distribution object is null for index " + targetIndex + "\u001B[0m");
            }
        } else {
            System.out.println("\t\t\t\u001B[31mError: Index " + targetIndex + " is out of bounds.\u001B[0m");
        }
        // Return a default value or throw an exception based on your requirements
        return -1; // or throw new IllegalArgumentException("Invalid index");
    }

    public static int getIdFromIndexHouseHold(Connection con, int targetIndex) {
        Map<Integer, Distribution> indexObjectMap = getDistribution2(con);

        if (indexObjectMap.containsKey(targetIndex)) {
            Distribution distribution = indexObjectMap.get(targetIndex);
            if (distribution != null) {
                return distribution.getHouseholdID();
            } else {
                System.out.println("\t\t\t\u001B[31mError: Distribution object is null for index " + targetIndex + "\u001B[0m");
            }
        } else {
            System.out.println("\t\t\t\u001B[31mError: Index " + targetIndex + " is out of bounds.\u001B[0m");
        }
        // Return a default value or throw an exception based on your requirements
        return -1; // or throw new IllegalArgumentException("Invalid index");
    }

    public static void printDistribution(Connection con) {
        try {
            Map<Integer, Distribution> distribution = getDistribution(con);
            System.out.println("\t\t\t***************************************************** DISTRIBUTION LIST *********************************************************************");
            System.out.println("\t\t\t._______.______________________________________.____________________._________________._______________________________________________________");
            System.out.println("\t\t\t│    No      │                Commission Name             │               Household             │  Amount Distribution     │  Date Distribution  │ ");
            System.out.println("\t\t\t│____________│____________________________________________│_____________________________________│__________________________│_____________________│  ");
            Statement statement = con.createStatement();
            //  ResultSet resultSet = statement.executeQuery("SELECT Distribution.id,commission_id, precint_name, city_name,province_name, household_id, amount_distribution, date_distribution  FROM Distribution Join dbo.Commission C on C.id= Distribution.commission_id");
            for (Map.Entry<Integer, Distribution> entry : distribution.entrySet()) {
                Integer index = entry.getKey();
                Distribution distribution1 = entry.getValue();
                String precint_name = distribution1.getPrecintName();
                String city_name = distribution1.getCityName();
                String province_name = distribution1.getProvinceName();
                String name = distribution1.getName();
                String amount = String.format("%.0f", distribution1.getAmountReceived());
                Date date_distribution = distribution1.getDateReceived();


                System.out.printf("\t\t\t│%-11S │ %-42s │ %-35s │ %-24s │ %-19s │ \n", index, precint_name + "-" + city_name + "-" + province_name, name, amount, formatter.format(date_distribution));

                System.out.println("\t\t\t│____________│____________________________________________│_____________________________________│__________________________│_____________________│");
            }
            System.out.println();
            System.out.println("\t\t\t***************************************************************** LIST END ******************************************************************");
            System.out.println();
            //waitForEnter();
        } catch (SQLException e) {

            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
            System.out.println(e.getMessage());
        }
    }

    // Hiển thị danh sach uy ban
    public static void printDistribution1(Connection con) {
        try {
            Map<Integer, Distribution> distribution = getDistribution1(con);
            System.out.println("\t\t\t**********************LIST COMMISSION*************************");
            System.out.println("\t\t\t._______.______________________________________.___________");
            System.out.println("\t\t\t│    No      │                Commission Name             │ ");
            System.out.println("\t\t\t│____________│____________________________________________│  ");
            Statement statement = con.createStatement();
            //  ResultSet resultSet = statement.executeQuery("SELECT Distribution.id,commission_id, precint_name, city_name,province_name, household_id, amount_distribution, date_distribution  FROM Distribution Join dbo.Commission C on C.id= Distribution.commission_id");
            for (Map.Entry<Integer, Distribution> entry : distribution.entrySet()) {
                Integer index = entry.getKey();
                Distribution distribution1 = entry.getValue();
                String precint_name = distribution1.getPrecintName();
                String city_name = distribution1.getCityName();
                String province_name = distribution1.getProvinceName();
                System.out.printf("\t\t\t│%-11S │ %-42s │ \n", index, precint_name + "-" + city_name + "-" + province_name);

                System.out.println("\t\t\t│____________│____________________________________________│");
            }
            System.out.println();
            System.out.println("\t\t\t********************** LIST END *********************************");
            System.out.println();
            //waitForEnter();
        } catch (SQLException e) {

            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
            System.out.println(e.getMessage());
        }
    }

    // Hiển thị danh sách index Hộ dân
    public static void printDistribution2(Connection con) {
        try {
            Map<Integer, Distribution> distribution = getDistribution2(con);
            System.out.println("\t\t\t**********************LIST HOUSEHOLD*************************");
            System.out.println("\t\t\t._______.______________________________________.___________");
            System.out.println("\t\t\t│    No      │                HouseHold  Name             │ ");
            System.out.println("\t\t\t│____________│____________________________________________│  ");
            Statement statement = con.createStatement();

            for (Map.Entry<Integer, Distribution> entry : distribution.entrySet()) {
                Integer index = entry.getKey();
                Distribution distribution1 = entry.getValue();
                String name = distribution1.getName();
                System.out.printf("\t\t\t│%-11S │ %-42s │ \n", index, name);

                System.out.println("\t\t\t│____________│____________________________________________│");
            }
            System.out.println();
            System.out.println("\t\t\t********************** LIST END *********************************");
            System.out.println();
            //waitForEnter();
        } catch (SQLException e) {

            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
            System.out.println(e.getMessage());
        }
    }


    private static String formatFloatingPoint(float value) {
        // Format the floating-point number with desired precision
        return String.format("%.2f", value);
    }

    //Lay tat ca thong tin Distribution trong databse khong truyen tham so
    public ArrayList<Distribution> getDistribution() {
        ArrayList<Distribution> infoList = new ArrayList<>();
        try {
            JDBCQuery1.openConnection();
            String sql = "select  * from Distribution";
            ResultSet rs = JDBCQuery1.executeSelectQuery(sql);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        infoList.add(new Distribution(
                                rs.getInt("id"),
                                rs.getInt("commission_id"), rs.getInt("household_id"),
                                rs.getFloat("amount_distribution"), rs.getDate("date_distribution")
                        ));
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();

        } finally {
            JDBCQuery1.closeConnection();

        }
        return infoList;
    }

    //Lay tat ca thong tin Distribution trong database co tham so
    public static Map<Integer, Distribution> getDistribution(String sql) {
        // ArrayList<Distribution> infoList = new ArrayList<>();
        Map<Integer, Distribution> indexObjectMap = new LinkedHashMap<>();
        try {
            JDBCQuery1.openConnection();

            ResultSet rs = JDBCQuery1.executeSelectQuery(sql);
            if (rs != null) {
                try {
                    int index = 1;
                    while (rs.next()) {
                        Distribution distribution = new Distribution(
                                rs.getInt("id"),
                                rs.getInt("commission_id"), rs.getInt("household_id"),
                                rs.getFloat("amount_distribution"), rs.getDate("date_distribution")
                        );
                        indexObjectMap.put(index, distribution);
                        index++;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();

        } finally {
            JDBCQuery1.closeConnection();

        }
        return indexObjectMap;
    }



    //Thêm dữ liêu vào Distribution
    public void addDistribution() {

        String checkQuery = " select * from Distribution where commission_id =? and household_id=? and date_distribution=?";
        String insertQuery = "INSERT INTO Distribution (commission_id, household_id, amount_distribution,date_distribution) VALUES (?, ?, ?, ?)";
        String updateQuery = "UPDATE Distribution SET amount_distribution = amount_distribution + ? where commission_id =? and household_id=? and date_distribution=?";
        printDistribution1(con);
        int targetIndex = 0;
        int identity = -1;
        int targetIndex1 = 0;
        int identity1 = -1;// Initialize identity outside the loop
        Integer comid = null;
        Integer houseid=null;
        do {


            System.out.print("\t\t\tPlease input the sequence number of the record you want to add: ");
            try {
                targetIndex = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid integer
                System.out.println("\t\t\t\u001B[31mInvalid input. Please enter a valid integer.\u001B[0m");
                //continue;  // Continue the loop if the input is not a valid integer
            }
            identity = getIdFromIndexCommission(con, targetIndex);
            if (identity != -1) {
                //System.out.println("\t\t\t\u001B[32mFound ID at index " + targetIndex + ": " + identity + "\u001B[0m");


                do {
                    comid = identity;
                    if (!isCommissionIdExists(comid) == true) {
                        System.out.println("\t\t\tID does not exist. Please enter ID again!");
                    }
                } while (!isCommissionIdExists(comid));
            } else {
                System.out.println("\t\t\t\u001B[31mInvalid index or ID not found.\u001B[0m");
            }
        }  while (identity == -1);
        printDistribution2(con);
        do {

            System.out.print("\t\t\tPlease input the sequence number of the record you want to add: ");
            try {
                targetIndex1 = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid integer
                System.out.println("\t\t\t\u001B[31mInvalid input. Please enter a valid integer.\u001B[0m");
                // continue;  // Continue the loop if the input is not a valid integer
            }
            identity1 = getIdFromIndexHouseHold(con, targetIndex1);
            if (identity1 != -1) {
                //System.out.println("\t\t\t\u001B[32mFound ID at index " + targetIndex1 + ": " + identity1 + "\u001B[0m");

                do {
                    houseid = identity1;
                    if (!isHouseIdExists(houseid) == true) {
                        System.out.println("\t\t\tID does not exist. Please enter ID again!");
                    }

                } while (!isHouseIdExists(houseid));

            } else {
                System.out.println("\t\t\t\u001B[31mInvalid index or ID not found.\u001B[0m");
            }
        } while (identity1 == -1);
        boolean isValid = false;

        Double amount;
        do {
            amount = InputValidator1.validateFloatInput("\t\t\tEnter New Amount Received:  ");
            isValid = InputValidator1.isIntMoreThan0(amount);
            if (isValid == false) {
                System.out.println("Please input > 0");
            }
        } while (isValid == false);


        Date datereceived= InputValidator1.validateDateInputNew("\t\t\tEnter New Date Received with format dd//MM/yyyy:  ");
        // Chuyển đổi từ java.util.Date sang java.sql.Date
        java.sql.Date sqlDate = new java.sql.Date(datereceived.getTime());





        try {

            // Check if a record with the specified  commission_id,household_id, and date_distribution already exists
            PreparedStatement checkStatement = con.prepareStatement(checkQuery);
            checkStatement.setInt(1, comid);
            checkStatement.setInt(2, houseid );
            checkStatement.setDate(3, sqlDate);
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                // A record already exists, so update it

                PreparedStatement updateStatement = con.prepareStatement(updateQuery);
                updateStatement.setDouble(1, amount);
                updateStatement.setInt(2, comid);
                updateStatement.setDouble(3, houseid);
                updateStatement.setDate(4, sqlDate);

                // Execute the update query
                updateStatement.executeUpdate();
                System.out.println("\t\t\t\u001B[32mDistribution information has been updatated to the database.\u001B[0m");
            } else {
                // No record found, so insert a new record
                PreparedStatement insertStatement = con.prepareStatement(insertQuery);
                insertStatement.setInt(1, comid);
                insertStatement.setInt(2, houseid);
                insertStatement.setDouble(3, amount);
                insertStatement.setDate(4, sqlDate);

                // Execute the insert query
                insertStatement.executeUpdate();
                System.out.println("\t\t\t\u001B[32mNew distribution information has been added to the database.\u001B[0m");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            JDBCQuery1.closeConnection();
        }

    }

    //hàm cập nhật lại thông tin
    public static void updateDistribution(Connection con) {
        int flag = 0;
        Integer comid;
        Integer houseid;
        int targetIndex;
        int identity = -1;
        do {
            printDistribution(con);
            // Initialize identity outside the loop


            System.out.print("\t\t\tPlease input the sequence number of the record you want to update: ");
            try {
                targetIndex = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid integer
                System.out.println("\t\t\t\u001B[31mInvalid input. Please enter a valid integer.\u001B[0m");
                continue;  // Continue the loop if the input is not a valid integer
            }
            identity = getIdFromIndex(con, targetIndex);
            if (identity != -1) {
                //System.out.println("\t\t\t\u001B[32mFound ID at index " + targetIndex + ": " + identity + "\u001B[0m");

                System.out.println();
                PreparedStatement pstmt = null;

                if (isIDAlreadyExists(con, identity, "Distribution")) {
                    int choice;
                    //Hien Thi Menu Update
                    do {
                        System.out.println("\t\t\t+-----------------------------------+");
                        System.out.println("\t\t\t│ Which item do you want to update? │");
                        System.out.println("\t\t\t+-----------------------------------+");
                        System.out.printf("\t\t\t│ %-34s|\n\t\t\t│ %-34s│\n\t\t\t│ %-34s│\n", "1. Amount Distribution", "2. Date Distribution ", "3. Return to the Management menu");
                        System.out.println("\t\t\t+-----------------------------------+");
                        System.out.println("\t\t\tFrom Update Menu, Your Choice: ");
                        choice = InputValidator1.validateIntInput("\t\t\tPlease Choice: ");
                        if (choice < 1 || choice > 3) {
                            System.out.println("\t\t\t\u001B[31mInvalid choice! Please enter a number between 0 and 3.\u001B[0m");
                            choice = -1; // Reset choice to -1 to repeat the loop
                        }
                    } while (choice == -1);

                    switch (choice) {
                        case 3:
                            System.out.println("\t\t\tBack to menu");
                            handleDistributionManager();
                            break;
                        //Update Số tiền
                        case 1:
                            double newAmount = InputValidator1.validateDoubleInput("\t\t\tEnter amount: ");
                            String sql1 = "UPDATE Distribution SET amount_distribution =? WHERE id =?";
                            try {
                                pstmt = con.prepareStatement(sql1);
                                pstmt.setDouble(1, newAmount);
                                pstmt.setInt(2, identity);
                                pstmt.executeUpdate();
                                System.out.println("\t\t\t\u001B[32mUpdated the amount successfully!!!\u001B[0m");

                            } catch (Exception e) {
                                System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
                            } finally {
                                try {
                                    if (pstmt != null) pstmt.close();
                                } catch (SQLException se2) {
                                    System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + se2.getMessage() + ".\u001B[0m");
                                }
                                do {
                                    System.out.print("\t\t\tDo you want to continue editing (Y/N)? ");
                                    String c = sc.next();
                                    sc.nextLine(); // Consume the newline character

                                    if ("Y".equalsIgnoreCase(c) || "y".equalsIgnoreCase(c)) {
                                        updateDistribution(con);
                                    } else if ("N".equalsIgnoreCase(c) || "n".equalsIgnoreCase(c)) {
                                        break;
                                    } else {
                                        System.out.println("\t\t\t\u001B[31mThe input selection is incorrect, please re-enter!!!\u001B[0m");
                                    }
                                } while (true);

                            }
                            break;
                        //Update ngày ủng hộ
                        case 2:
                            String sql2 = "UPDATE Distribution SET date_distribution =? WHERE id =?";
                            Date newDistributionDate = InputValidator1.validateDateInputNew("\t\t\tEnter a new receipt date (in dd/MM/yyyy format): ");
                            java.sql.Date sqlDate = new java.sql.Date(newDistributionDate.getTime());

                            try {
                                pstmt = con.prepareStatement(sql2);
                                pstmt.setDate(1, sqlDate);
                                pstmt.setInt(2, identity);
                                pstmt.executeUpdate();
                                System.out.println("\t\t\t\u001B[32mUpdated Distribution date successful!!!\u001B[0m");

                            } catch (Exception e) {
                                System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
                            } finally {
                                try {
                                    if (pstmt != null) pstmt.close();
                                } catch (SQLException se2) {
                                    System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + se2.getMessage() + ".\u001B[0m");
                                }
                                do {
                                    System.out.print("\t\t\tDo you want to continue editing (Y/N)? ");
                                    String c = sc.next();
                                    sc.nextLine(); // Consume the newline character

                                    if ("Y".equalsIgnoreCase(c)) {
                                        updateDistribution(con);
                                    } else if ("N".equalsIgnoreCase(c)) {
                                        break;
                                    } else {
                                        System.out.println("\t\t\t\u001B[31mThe input selection is incorrect, please re-enter!!!\u001B[0m");
                                    }
                                } while (true);

                            }
                            break;

                    }
                } else {
                    System.out.println("\t\t\t\u001B[31mNo Distribution infor found!!!\u001B[0m");

                }
            } else {
                System.out.println("\t\t\t\u001B[31mInvalid index or ID not found.\u001B[0m");
            }
        } while (identity == -1);
    }






    //hàm xóa phân phối theo id
    public static void deleteDistribution(Connection con) {
        printDistribution(con);
        int targetIndex;
        int identity = -1;  // Initialize identity outside the loop

        do {
            System.out.print("\t\t\tPlease input the sequence number of the record you want to delete: ");
            try {
                targetIndex = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid integer
                System.out.println("\t\t\t\u001B[31mInvalid input. Please enter a valid integer.\u001B[0m");
                continue;  // Continue the loop if the input is not a valid integer
            }
            identity = getIdFromIndex(con, targetIndex);
            if (identity != -1) {
                //System.out.println("\t\t\t\u001B[32mFound ID at index " + targetIndex + ": " + identity + "\u001B[0m" );


                System.out.println();
                PreparedStatement pstmt = null;
                String tableName = "Distribution";

                try {
                    //Kiem Tra Su Ton Tai Cua record, Neu Ton Tai Thi Tien Hanh Xoa
                    if (isIDAlreadyExists(con, identity, tableName)) {
                        String choice;
                        System.out.print("\t\t\tAre you sure you want to delete (Y/N)? ");
                        do {
                            choice = sc.nextLine().trim().toLowerCase();  // Convert to lowercase

                            if ("Y".equalsIgnoreCase(choice) ||"y".equalsIgnoreCase(choice)) {
                                // Delete the record
                                String sqlDelete = "DELETE FROM Distribution WHERE ID=?";
                                pstmt = con.prepareStatement(sqlDelete);
                                pstmt.setInt(1, identity);
                                if (pstmt.executeUpdate() != 0) {
                                    System.out.println("\t\t\t\u001B[32mDeleted successfully!!!\u001B[0m");

                                }
                            } else if ("N".equalsIgnoreCase(choice)||"n".equalsIgnoreCase(choice)) {
                                System.out.println("\t\t\t\u001B[31mINFO: No results were deleted!!!\u001B[0m");

                            } else {
                                System.out.print("\t\t\tInvalid choice. Please enter (Y/N): ");
                            }
                        } while ((!("Y".equalsIgnoreCase(choice) ||"y".equalsIgnoreCase(choice))) && (!("N".equalsIgnoreCase(choice)||"n".equalsIgnoreCase(choice))));

                    } else {
                        System.out.println("\t\t\t\u001B[31mThe result does not exist in the Database!!!\u001B[0m");

                    }
                } catch (Exception e) {
                    System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
                } finally {
                    try {
                        if (pstmt != null) pstmt.close();
                    } catch (SQLException se2) {
                        System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + se2.getMessage() + ".\u001B[0m");
                    }
                }
            } else {
                System.out.println("\t\t\t\u001B[31mInvalid index or ID not found.\u001B[0m");
            }
        } while (identity == -1);
    }



    //hiển thị danh sách Phân phối
    public void displayDistribution(Map<Integer, Distribution> distributions) {
        System.out.println("\t\t\tID\t|\tCommissionID|\tHouseHoldID\t|\tAmountReceived\t|\tDateReceived");

        for (Map.Entry<Integer, Distribution> entry : distributions.entrySet()) {
            System.out.println("\t\t\t" + entry.getKey() + "\t|\t\t" + entry.getValue().getCommissionId() + "\t\t|\t\t" +
                    entry.getValue().getHouseholdID() + "\t\t|\t\t" + entry.getValue().getAmountReceived() + "\t\t|\t" + entry.getValue().getDateReceived());
        }
    }


    //Kiểm tra Distribution có tồn tại không

    public boolean isIdExists(int id) {
        boolean exists = false;
        try {
            JDBCQuery1.openConnection();

            String sql = "SELECT COUNT(*) FROM Distribution WHERE id = ?";
            Object[] params = {id};

            ResultSet rs = JDBCQuery1.executeSelectQuery(sql, params);

            if (rs.next()) {
                int count = rs.getInt(1);
                exists = (count > 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBCQuery1.closeConnection();
        }
        return exists;
    }

    //Kiểm tra id commission
    public boolean isCommissionIdExists(int id) {
        boolean exists = false;
        try {
            JDBCQuery1.openConnection();

            String sql = "SELECT COUNT(*) FROM Commission WHERE id = ?";
            Object[] params = {id};

            ResultSet rs = JDBCQuery1.executeSelectQuery(sql, params);

            if (rs.next()) {
                int count = rs.getInt(1);
                exists = (count > 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBCQuery1.closeConnection();
        }
        return exists;
    }

    //Kiểm tra id House tồn tại chưa
    public boolean isHouseIdExists(int id) {
        boolean exists = false;
        try {
            JDBCQuery1.openConnection();
            String sql = "Select count (*) from House where id=?";
            Object[] parms = {id};

            ResultSet rs = JDBCQuery1.executeSelectQuery(sql, parms);
            if (rs.next()) {
                int count = rs.getInt(1);
                exists = (count > 0);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBCQuery1.closeConnection();
        }
        return exists;
    }
    public static boolean isIDAlreadyExists(Connection con, int id, String tableName) {
        boolean idExist = false;
        try {
            // Validate the tableName to ensure it comes from a trusted source
            // You may use a whitelist or other validation methods depending on your requirements

            // Assuming tableName is a valid identifier
            String sql = "SELECT CASE WHEN EXISTS (SELECT * FROM " + tableName + " WHERE id = ?) THEN 1 ELSE 0 END";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        idExist = rs.getInt(1) == 1;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database!\u001B[0m");
        }
        return idExist;
    }


}