package Model;

import Common.DBConnect;

import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OfficerManage {
    private static Scanner sc = new Scanner(System.in);
    private static Connection connection = DBConnect.connectDatabase();
    public static void main(String[] args) throws SQLException {
        OfficerManage.getOfficerManage();
    }

    public static void getOfficerManage() throws SQLException {
        displayMenu();
        int choice = 0;
        do {
            System.out.println("Enter your choice...");
            choice = checkInputInt();

            switch (choice) {
                case 0:
                    connection.close();
                    System.out.println("\u001B[36m* Notification: Program is closed. Thank you for using our program!\u001B[0m");
                    break;
                case 1:
                    OfficerManage.addNewOfficer();
                    displayMenu();
                    break;
                case 2:
                    OfficerManage.updateOfficerTable();
                    displayMenu();
                    break;
                case 3:
                    OfficerManage.deleteOfficer();
                    displayMenu();
                    break;
                case 4:
                    OfficerManage.displayOfficerTable();
                    displayMenu();
                    break;
                default:
                    System.out.println("\u001B[31m* Warning: Input is invalid. Please try again!\u001B[0m");
                    break;
            }
        } while (choice != 0);
    }

    // create method display menu of Officer table
    public static void displayMenu() {
        System.out.println(String.format("| %-25s |", "= Officer Manage Menu ="));
        System.out.println(String.format("| %-25s |", "-------------------------"));
        System.out.println(String.format("| %-25s |", "0. Exit program"));
        System.out.println(String.format("| %-25s |", "1. Add new Officer"));
        System.out.println(String.format("| %-25s |", "2. Update Officer table"));
        System.out.println(String.format("| %-25s |", "3. Delete Officer by ID"));
        System.out.println(String.format("| %-25s |", "4. Display Officer table"));
        System.out.println(String.format("| %-25s |", "========================="));
    }

    public static void displayUpdateMenu() {
        System.out.println(String.format("| %-30s |", "======== Update Menu ========="));
        System.out.println(String.format("| %-30s |", "------------------------------"));
        System.out.println(String.format("| %-30s |", "0. Exit program"));
        System.out.println(String.format("| %-30s |", "1. Update Officer name"));
        System.out.println(String.format("| %-30s |", "2. Update Officer phone number"));
        System.out.println(String.format("| %-30s |", "3. Update Officer address"));
        System.out.println(String.format("| %-30s |", "=============================="));
    }

    // create method 1: Add new Officer
    public static void addNewOfficer() throws SQLException {
        Statement st = connection.createStatement();

        // add officer name
        int maxLengthName = 50;
        String name = "";
        do {
            sc.nextLine();
            System.out.println("Input name:");
            name = sc.nextLine();
            if (!validateStringLength(name, maxLengthName)) {
                System.out.println("\u001B[31m* Warning: You have entered more than 50 characters. Please try again!\u001B[0m");
            }
        } while (!validateStringLength(name, maxLengthName));

        // add officer phone number
        int maxLengthPhoneNumber = 11;
        String phoneNumber = "";
        do {
            System.out.println("Input phone number:");
            phoneNumber = sc.next();
            if (!validateStringLength(phoneNumber, maxLengthPhoneNumber)) {
                System.out.println("\u001B[31m* Warning: You have entered more than 11 characters. Please try again!\u001B[0m");
            }
            if (!checkValidInputStringPhoneNumber(phoneNumber)) {
                System.out.println("\u001B[31m* Warning: Please enter 0-9\u001B[0m");
            }
        } while (!validateStringLength(phoneNumber, maxLengthPhoneNumber) || !checkValidInputStringPhoneNumber(phoneNumber));

        // add officer address
        int maxLengthAddress = 255;
        String address = "";
        do {
            sc.nextLine();
            System.out.println("Input address:");
            address = sc.nextLine();
            if (!validateStringLength(address, maxLengthAddress)) {
                System.out.println("\u001B[31m* Warning: You have entered more than 255 characters. Please try again!\u001B[0m");
            }
        } while (!validateStringLength(address, maxLengthAddress));

        // add commission id
        System.out.println("Add commission ID");
        System.out.println("If you don't remember Commision ID, press 1 to review the Officer table.");
        System.out.println("If you don't need it, press any number to continue");
        int select = checkInputInt();
        if (select == 1) {
            displayCommissionTable();
        }
        int targetIndexCommissionTable = 0;
        do {
            System.out.println("Input commission id: ");
            targetIndexCommissionTable = checkInputInt();
            if (!checkCommissionIdExistence(targetIndexCommissionTable)) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Commission table!\u001B[0m");
            }
        } while (!checkCommissionIdExistence(targetIndexCommissionTable));
        int commissionId = getIdFromIndexCommissionTable(targetIndexCommissionTable);

        // create sql statement and execute
        String sql = "INSERT INTO [dbo].[Officer] VALUES ('" + name + "', '" + phoneNumber + "', '" + address + "', " + commissionId +");";
        int isCheckQuery = st.executeUpdate(sql);
        if (isCheckQuery > 0) {
            System.out.println("\u001B[32m* Notification: Insert success.\u001B[0m");
            System.out.println();
        } else {
            System.out.println("\u001B[31m* Warning: Insert fail.\u001B[0m");
        }
    }

    // create Validate for String:
    private static boolean validateStringLength(String input, int maxLength) { return input.length() <= maxLength; }

    // create method 2: Update Officer table
    public static void updateOfficerTable() throws SQLException {
        displayUpdateMenu();
        int selectUpdate = 0;
        do {
            System.out.println("What information do you want to update?...");
            selectUpdate = checkInputInt();
            switch (selectUpdate) {
                case 0:
                    System.out.println("\u001B[32m* Notification: Update program is closed\u001B[0m");
                    break;
                case 1:
                    updateOfficerName();
                    displayUpdateMenu();
                    break;
                case 2:
                    updateOfficerPhoneNumber();
                    displayUpdateMenu();
                    break;
                case 3:
                    updateOfficerAddress();
                    displayUpdateMenu();
                    break;
                default:
                    System.out.println("\u001B[31m* Warning: Input is invalid. Please try again!\u001B[0m");
                    break;
            }
        } while (selectUpdate != 0);
    }

    // create method check Officer id existence
    private static boolean checkOfficerIdExistence(int id) throws SQLException {
        Map<Integer, Officer> indexOfficerMap = getOfficer();
        return indexOfficerMap.containsKey(id);
    }

    // create method get id from index
    private static int getIdFromIndexOfficerTable(int targetIndex) throws SQLException {
        Map<Integer, Officer> indexOfficerMap = getOfficer();
        if (indexOfficerMap.containsKey(targetIndex)) {
            Officer officer = indexOfficerMap.get(targetIndex);
            if (officer != null) {
                return officer.getId();
            }
        }
        return 0;
    }

    // create method 2.1:
    private static void updateOfficerName() throws SQLException {
        String sql = "UPDATE Officer SET name = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        int targetIndex = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            targetIndex = checkInputInt();
            if (!checkOfficerIdExistence(targetIndex)) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (!checkOfficerIdExistence(targetIndex));
        int id = getIdFromIndexOfficerTable(targetIndex);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + targetIndex +"\u001B[0m");
        displayOfficerTableById(id, targetIndex);

        int maxLength = 50;
        String newName = "";
        do {
            sc.nextLine();
            System.out.println("Enter new name:");
            newName = sc.nextLine();
            if (!validateStringLength(newName, maxLength)) {
                System.out.println("\u001B[31m* Warning: You have entered more than 50 characters. Please try again!\u001B[0m");
            }
        } while (!validateStringLength(newName, maxLength));
        pstm.setString(1, newName);
        pstm.setInt(2, id);

        int isCheckQuery = pstm.executeUpdate();
        if (isCheckQuery > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 2.2:
    private static void updateOfficerPhoneNumber() throws SQLException {
        int targetIndex = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            targetIndex = checkInputInt();
            if (!checkOfficerIdExistence(targetIndex)) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (!checkOfficerIdExistence(targetIndex));
        int id = getIdFromIndexOfficerTable(targetIndex);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + targetIndex +"\u001B[0m");
        displayOfficerTableById(id, targetIndex);

        String sql = "UPDATE Officer SET phone_number = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        int maxLength = 11;
        String newPhoneNumber = "";
        do {
            System.out.println("Enter new phone number:");
            newPhoneNumber = sc.next();
            if (!validateStringLength(newPhoneNumber, maxLength)) {
                System.out.println("\u001B[31m* Warning: You have entered more than 11 characters. Please try again!\u001B[0m");
            }
        } while (!validateStringLength(newPhoneNumber, maxLength));

        pstm.setString(1, newPhoneNumber);
        pstm.setInt(2, id);

        int isCheckQuery = pstm.executeUpdate();
        if (isCheckQuery > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 2.3:
    private static void updateOfficerAddress() throws SQLException {
        int targetIndex = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            targetIndex = checkInputInt();
            if (!checkOfficerIdExistence(targetIndex)) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (!checkOfficerIdExistence(targetIndex));
        int id = targetIndex;
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + targetIndex +"\u001B[0m");
        displayOfficerTableById(id, targetIndex);

        String sql = "UPDATE Officer SET address = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        int maxLength = 255;
        String newAddress = "";
        do {
            sc.nextLine();
            System.out.println("Enter new address:");
            newAddress = sc.nextLine();
            if (!validateStringLength(newAddress,maxLength)) {
                System.out.println("\u001B[31m* Warning: You have entered more than 255 characters. Please try again!\u001B[0m");
            }
        } while (!validateStringLength(newAddress,maxLength));

        pstm.setString(1, newAddress);
        pstm.setInt(2, id);

        int isCheckQuery = pstm.executeUpdate();
        if (isCheckQuery > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 2.4: Update Commission ID
//    private static void updateCommissionId() throws SQLException {
//        System.out.println("If you don't remember Commission ID, press 1 to review the Officer table.");
//        System.out.println("If you don't need it, press any number to continue");
//        int select = checkInputInt();
//        if (select == 1) {
//            displayCommissionTable();
//        }
//
//        String sql = "UPDATE Officer SET commission_id = ?  WHERE id = ?";
//        PreparedStatement pstm = connection.prepareStatement(sql);
//
//        boolean isCheckExistOfficerId = false;
//        int id = 0;
//        do {
//            System.out.println("Enter the id you want to edit:");
//            id = checkInputInt();
//            isCheckExistOfficerId = checkOfficerIdExistence(id);
//            if (isCheckExistOfficerId == false) {
//                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
//            }
//        } while (isCheckExistOfficerId == false);
//        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + id +"\u001B[0m");
//        displayOfficerTableById(id);
//
//        int newCommissionId = 0;
//        boolean isCheckExistence = false;
//        do {
//            System.out.println("Input commission id: ");
//            newCommissionId = checkInputInt();
//            isCheckExistence = checkCommissionIdExistence(newCommissionId);
//            if (isCheckExistence == false) {
//                System.out.println("\u001B[31m* Warning: ID does not exist in the Commission table!\u001B[0m");
//            }
//        } while (isCheckExistence == false);
//
//        pstm.setInt(1, newCommissionId);
//        pstm.setInt(2, id);
//
//        int check = pstm.executeUpdate();
//        if (check > 0) {
//            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
//        }
//    }

    // create method 3: Delete Officer by ID
    public static void deleteOfficer() throws SQLException {
        String sql = "DELETE FROM Officer WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        int targetIndex = 0;
        do {
            System.out.println("Enter the id you want to delete:");
            targetIndex = checkInputInt();
            if (!checkOfficerIdExistence(targetIndex)) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (!checkOfficerIdExistence(targetIndex));
        int id = getIdFromIndexOfficerTable(targetIndex);
        System.out.println("\u001B[31mThis is the table you want to delete whose id is " + targetIndex +"\u001B[0m");
        displayOfficerTableById(id, targetIndex);
        System.out.println("If you are sure you want to delete, press number 1. Otherwise, press any number (except number 1) to return to the main menu");
        int select = checkInputInt();
        if (select == 1) {
            pstm.setInt(1, id);
            int isCheckQuey = pstm.executeUpdate();
            if (isCheckQuey > 0) {
                System.out.println("\u001B[32m* Notification: Delete success\u001B[0m");
            }
        } else {
            System.out.println("\u001B[31mDeletion is not performed. You are return to the main menu\u001B[0m");
        }
    }

    public static Map<Integer, Officer> getOfficer() throws SQLException {
        Map<Integer, Officer> indexObjectMap = new LinkedHashMap<>();
        Statement st = connection.createStatement();
        String sql = "select o.id, o.name, o.phone_number, o.address, c.precint_name, c.city_name, c.province_name\n" +
                "from Officer as o  \n" +
                "JOIN Commission as c\n" +
                "on o.commission_id = c.id\n" +
                "Order by o.id desc";
        ResultSet rs = st.executeQuery(sql);
        int index = 1;
        while (rs.next()) {
            Officer officer = new Officer(rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7)
            );
            indexObjectMap.put(index, officer);
            index++;
        }
        return indexObjectMap;
    }

    // create method 4: Display Officer table
    public static void displayOfficerTable() throws SQLException {
        Map<Integer, Officer> officerMap = getOfficer();
        if (!officerMap.isEmpty()) {
            System.out.println("\u001B[33m======================================================= Officer Table ========================================================");
            System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-35s |", "ID", "Name", "Phone number", "Address","Work Place"));
            System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-35s |\u001B[0m", "-----", "--------------------", "---------------",
                    "-----------------------------------", "-----------------------------------"));
            for (Map.Entry<Integer, Officer> entry : officerMap.entrySet()) {
                Integer index = entry.getKey();
                Officer officer = entry.getValue();
                String name = officer.getName();
                String phoneNumber = officer.getPhoneNumber();
                String address = officer.getAddress();
                String precinct = officer.getPrecinct();
                String city = officer.getCity();
                String province = officer.getProvince();
                System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-35s |", index, name, phoneNumber, address,
                        precinct + ", " + city + ", " + province));
                System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-35s |", "-----", "--------------------", "---------------",
                        "-----------------------------------", "-----------------------------------"));
            }
        }
        System.out.println();
    }

    public static Map<Integer, Officer> getOfficerById(int id, int targetIndex) throws SQLException {
        Map<Integer, Officer> indexObjectMap = new LinkedHashMap<>();
        Statement st = connection.createStatement();
        String sql = "select o.id, o.name, o.phone_number, o.address, c.precint_name, c.city_name, c.province_name\n" +
                "from Officer as o  \n" +
                "JOIN Commission as c\n" +
                "on o.commission_id = c.id\n" +
                "Where o.id = " + id;
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            Officer officer = new Officer(rs.getInt(1),
                    rs.getString(2),
                    rs.getString(3),
                    rs.getString(4),
                    rs.getString(5),
                    rs.getString(6),
                    rs.getString(7)
            );
            indexObjectMap.put(targetIndex, officer);
        }
        return indexObjectMap;
    }

    // create method display Officer table by id
    public static void displayOfficerTableById(int id, int targetIndex) throws SQLException {
        Map<Integer, Officer> officerByIdMap = getOfficerById(id, targetIndex);
        if (!officerByIdMap.isEmpty()) {
            System.out.println("\u001B[33m======================================================= Officer Table ========================================================");
            System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-35s |", "ID", "Name", "Phone number", "Address","Work Place"));
            System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-35s |\u001B[0m", "-----", "--------------------", "---------------",
                    "-----------------------------------", "-----------------------------------"));
            for (Map.Entry<Integer, Officer> entry : officerByIdMap.entrySet()) {
                Integer index = targetIndex;
                Officer officer = entry.getValue();
                String name = officer.getName();
                String phoneNumber = officer.getPhoneNumber();
                String address = officer.getAddress();
                String precinct = officer.getPrecinct();
                String city = officer.getCity();
                String province = officer.getProvince();
                System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-35s |", index, name, phoneNumber, address,
                        precinct + ", " + city + ", " + province));
                System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-35s |", "-----", "--------------------", "---------------",
                        "-----------------------------------", "-----------------------------------"));
            }
        }
        System.out.println();
    }

    // create method check input Int
    public static int checkInputInt() {
        int input = 0;
        boolean isValidInput = false;
        while (!isValidInput) {
            if (sc.hasNextInt()) {
                isValidInput = true;
                return input = sc.nextInt();
            } else {
                System.out.println("\u001B[31m* Warning: Invalid input. Please enter a valid integer.\u001B[0m");
                sc.next();
            }
        }
        return input;
    }

    // create method display Commission table
    private static void displayCommissionTable() throws SQLException {
        Map<Integer, Map<Integer, Object>> commissionMap = getCommission();
        if (!commissionMap.isEmpty()) {
            System.out.println("\u001B[33m====================== Commission Table =======================");
            System.out.println(String.format("| %-5s | %-15s | %-15s | %-15s |", "ID", "Precinct", "City/District", "Province"));
            System.out.println(String.format("| %-5s | %-15s | %-15s | %-15s |\u001B[0m", "-----", "--------------", "--------------", "--------------"));
            for (Map.Entry<Integer, Map<Integer, Object>> entry : commissionMap.entrySet()) {
                Integer index = entry.getKey();
                Map<Integer, Object> rowData = entry.getValue();
                String precinct = (String) rowData.get(2);
                String city = (String) rowData.get(3);
                String province = (String) rowData.get(4);
                System.out.println(String.format("| %-5s | %-15s | %-15s | %-15s |", index, precinct,
                        city, province));
                System.out.println(String.format("| %-5s | %-15s | %-15s | %-15s |\u001B[0m", "-----", "--------------", "--------------", "--------------"));
            }
        }
        System.out.println();
    }

    public static Map<Integer, Map<Integer, Object>> getCommission() throws SQLException {
        Map<Integer, Map<Integer, Object>> indexDataMap = new HashMap<>();
        Statement st = connection.createStatement();
        String sql = "SELECT * FROM Commission Order by id desc;";
        ResultSet rs = st.executeQuery(sql);
        int index = 1;
        while (rs.next()) {
            int id = rs.getInt(1);
            String precinct = rs.getString(2);
            String city = rs.getString(3);
            String province = rs.getString(4);

            Map<Integer, Object> rowData = new HashMap<>();
            rowData.put(1, id);
            rowData.put(2, precinct);
            rowData.put(3, city);
            rowData.put(4, province);

            indexDataMap.put(index, rowData);
            index++;
        }
        return indexDataMap;
    }

    // create method check existence of Commission ID
    private static boolean checkCommissionIdExistence(int id) throws SQLException {
        Map<Integer, Map<Integer, Object>> indexCommissionMap = getCommission();
        return indexCommissionMap.containsKey(id);
    }

    // create method get id from index of Commission table
    private static int getIdFromIndexCommissionTable(int targetIndex) throws SQLException {
        Map<Integer, Map<Integer, Object>> indexCommissionMap = getCommission();
        if (indexCommissionMap.containsKey(targetIndex)) {
            Map<Integer, Object> rowData = indexCommissionMap.get(targetIndex);
            if (rowData != null) {
                return (int) rowData.get(1);
            }
        }
        return 0;
    }

    // create method valid input String phone nummber (0-9)
    private static boolean checkValidInputStringPhoneNumber(String input) {
        String regex = "^[0-9]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }
}
