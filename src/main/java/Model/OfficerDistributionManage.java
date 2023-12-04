package Model;

import Common.DBConnect;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class OfficerDistributionManage {
    private static final Scanner sc = new Scanner(System.in);
    private static final Connection connection = DBConnect.connectDatabase();
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) throws SQLException {
        OfficerDistributionManage.getOfficerDistributionManage();
    }

    public static void getOfficerDistributionManage() throws SQLException {
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
                    OfficerDistributionManage.addNewData();
                    displayMenu();
                    break;
                case 2:
                    OfficerDistributionManage.updateOfficerDistributionTable();
                    displayMenu();
                    break;
                case 3:
                    OfficerDistributionManage.deleteData();
                    displayMenu();
                    break;
                case 4:
                    OfficerDistributionManage.displayOfficerDistributionTable();
                    displayMenu();
                    break;
                default:
                    System.out.println("\u001B[31m* Warning: Input is invalid. Please try again!\u001B[0m");
                    break;
            }
        } while (choice != 0);
    }

    // create method display menu of Officer Distribution table
    public static void displayMenu() {
        System.out.printf("| %-40s |%n", "=== Officer Distribution Manage Menu ===");
        System.out.printf("| %-40s |%n", "----------------------------------------");
        System.out.printf("| %-40s |%n", "0. Exit program");
        System.out.printf("| %-40s |%n", "1. Add new data");
        System.out.printf("| %-40s |%n", "2. Update Officer Distribution table");
        System.out.printf("| %-40s |%n", "3. Delete data by ID");
        System.out.printf("| %-40s |%n", "4. Display Officer Distribution table");
        System.out.printf("| %-40s |%n", "========================================");
    }

    // create method display Update menu
    public static void displayUpdateMenu() {
        System.out.printf("| %-30s |%n", "======== Update Menu =========");
        System.out.printf("| %-30s |%n", "------------------------------");
        System.out.printf("| %-30s |%n", "0. Exit program");
        System.out.printf("| %-30s |%n", "1. Update date distribution");
        System.out.printf("| %-30s |%n", "2. Update address distribution");
        System.out.printf("| %-30s |%n", "==============================");
    }

    // create method 1: Add new data
    public static void addNewData() throws SQLException {
        // add Officer_id into Officer Distribution table
        System.out.println("Add Officer ID");
        System.out.println("If you don't remember Officer ID, press 1 to review the Officer table.");
        System.out.println("If you don't need it, press any number to continue");
        int isSelectViewOfficerTable = checkInputInt();
        if (isSelectViewOfficerTable == 1) {
            OfficerManage.displayOfficerTable();
        }
        int targetIndex = 0;
        do {
            System.out.println("Enter Officer id:");
            targetIndex = checkInputInt();
            // call method checkOfficerIdExistence:
            if (!checkOfficerIdExistence(targetIndex)) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (!checkOfficerIdExistence(targetIndex));
        int officerId = getIdFromIndexOfficerTable(targetIndex);

        // add Distribution_id into Officer Distribution table
        System.out.println("Add Distribution ID");
        int isSelectViewDistributionTable = 0;
        System.out.println("If you don't remember Distribution ID, press 1 to review the Distribution table.");
        System.out.println("If you don't need it, press any number to continue");
        isSelectViewDistributionTable = checkInputInt();
        if (isSelectViewDistributionTable == 1) {
            displayDistributionTable();
        }
        int targetIndexDistribution = 0;
        do {
            System.out.println("Enter Distribution id:");
            targetIndexDistribution = checkInputInt();
            // call method checkDistributionIdExistence
            if (!checkDistributionIdExistence(targetIndexDistribution)) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
            }
        } while (!checkDistributionIdExistence(targetIndexDistribution));
        int distributionId = getIdFromIndexDistributionTable(targetIndexDistribution);

        // add date distribution
        String dateDistribution = "";
        do {
            System.out.println("Enter date distribution (yyyy/MM/dd):");
            dateDistribution = checkValidDate();
            if (!isValidDateFormat(dateDistribution)) {
                System.out.println("\u001B[31m* Warning: Please enter according to format (yyyy/MM/dd).\u001B[0m ");
            }
        } while (!isValidDateFormat(dateDistribution));

        // add address distribution
        int maxLengthAddress = 255;
        String addressDistribution = "";
        do {
            sc.nextLine();
            System.out.println("Enter address distribution");
            addressDistribution = sc.nextLine();
            if (!validateStringLength(addressDistribution, maxLengthAddress)) {
                System.out.println("\u001B[31m* Warning: You have entered more than 255 characters. Please try again!\u001B[0m");
            }
        } while (!validateStringLength(addressDistribution, maxLengthAddress));

        // create sql stament and execute
        Statement st = connection.createStatement();
        String sql = "INSERT INTO OfficerDistribution " +
                "VALUES ("+ officerId +", "+ distributionId +", '"+ dateDistribution +"', '"+ addressDistribution +"');";
        int isCheckQuery = st.executeUpdate(sql);
        if (isCheckQuery > 0) {
            System.out.println("\u001B[32m* Notification: Insert success\u001B[0m");
            System.out.println();
        } else {
            System.out.println("\u001B[31m* Warning: Insert fail.\u001B[0m");
        }
    }

    // create Validate for String:
    private static boolean validateStringLength(String input, int maxLength) {
        return input.length() <= maxLength;
    }

    private static boolean isValidDateFormat(String dateStr) {
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy/MM/dd");
        dateFormat1.setLenient(false);
        try {
            Date date = dateFormat1.parse(dateStr);
            return dateStr.equals(dateFormat1.format(date));
        } catch (ParseException e) {
            return false;
        }
    }

    // create method check Officer id existence
    private static boolean checkOfficerIdExistence(int id) throws SQLException {
        Map<Integer, Officer> indexOfficerMap = OfficerManage.getOfficer();
        return indexOfficerMap.containsKey(id);
    }

    // create method get id from index
    private static int getIdFromIndexOfficerTable(int targetIndex) throws SQLException {
        Map<Integer, Officer> indexOfficerMap = OfficerManage.getOfficer();
        if (indexOfficerMap.containsKey(targetIndex)) {
            Officer officer = indexOfficerMap.get(targetIndex);
            if (officer != null) {
                return officer.getId();
            }
        }
        return 0;
    }

    // check existence of OfficerDistribution id
    private static boolean checkOfficerDistributionIdExistence(int id) throws SQLException {
        Map<Integer, OfficerDistribution> indexOfficerDistributionMap = getOfficerDistribution();
        return indexOfficerDistributionMap.containsKey(id);
    }

    // create method get id from index
    private static int getIdFromIndexOfOfficerDistributionDTable(int targetIndex) throws SQLException {
        Map<Integer, OfficerDistribution> indexOfficerDistributionMap = getOfficerDistribution();
        if (indexOfficerDistributionMap.containsKey(targetIndex)) {
            OfficerDistribution officerDistribution = indexOfficerDistributionMap.get(targetIndex);
            if (officerDistribution != null) {
                return officerDistribution.getId();
            }
        }
        return 0;
    }

    // create method 2: Update Officer Distribution table
    public static void updateOfficerDistributionTable() throws SQLException {
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
                    updateDateDistribution();
                    displayUpdateMenu();
                    break;
                case 2:
                    updateAddressDistribution();
                    displayUpdateMenu();
                    break;
                default:
                    System.out.println("\u001B[31m* Warning: Input is invalid. Please try again!\u001B[0m");
                    break;
            }
        } while (selectUpdate !=0);
    }

    // create method 2.1:
//    private static void updateOfficerId() throws SQLException {
//        System.out.println("If you don't remember Officer ID, press 1 to review the Officer table.");
//        System.out.println("If you don't need it, press any number to continue");
//        int select = checkInputInt();
//        if (select == 1) {
//            OfficerManage.displayOfficerTable();
//        }
//        String sql = "UPDATE OfficerDistribution SET officer_id = ? WHERE id = ?";
//        PreparedStatement pstm = connection.prepareStatement(sql);
//
//        int id = 0;
//        do {
//            System.out.println("Enter the id you want to edit:");
//            id = checkInputInt();
//            if (!checkOfficerDistributionIdExistence(id)) {
//                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
//            }
//        } while (!checkOfficerDistributionIdExistence(id));
//        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + id +"\u001B[0m");
//        displayOfficerDistributionTableById(id);
//
//        int newOfficerId = 0;
//        do {
//            System.out.println("Enter new Officer id:");
//            newOfficerId = checkInputInt();
//            // call method checkOfficerIdExistence:
//            if (!checkOfficerIdExistence(newOfficerId)) {
//                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
//            }
//        } while (!checkOfficerIdExistence(newOfficerId));
//
//        pstm.setInt(1, newOfficerId);
//        pstm.setInt(2, id);
//
//        int isCheckQuery = pstm.executeUpdate();
//        if (isCheckQuery > 0) {
//            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
//        }
//    }

    // create method 2.2:
//    private static void updateDistributionId() throws SQLException {
//        System.out.println("If you don't remember Distribution ID, press 1 to review the Distribution table.");
//        System.out.println("If you don't need it, press any number to continue");
//        int select = checkInputInt();
//        if (select == 1) {
//            displayDistributionTable();
//        }
//
//        String sql = "UPDATE OfficerDistribution SET distribution_id = ? WHERE id = ?";
//        PreparedStatement pstm = connection.prepareStatement(sql);
//
//        int id = 0;
//        do {
//            System.out.println("Enter the id you want to edit:");
//            id = checkInputInt();
//            if (!checkOfficerDistributionIdExistence(id)) {
//                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
//            }
//        } while (!checkOfficerDistributionIdExistence(id));
//        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + id +"\u001B[0m");
//        displayOfficerDistributionTableById(id);
//
//        int newDistributionId = 0;
//        do {
//            System.out.println("Enter Distribution id:");
//            newDistributionId = checkInputInt();
//            // call method checkDistributionIdExistence
//            if (!checkDistributionIdExistence(newDistributionId)) {
//                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
//            }
//        } while (!checkDistributionIdExistence(newDistributionId));
//
//        pstm.setInt(1, newDistributionId);
//        pstm.setInt(2, id);
//
//        int isCheckQuery = pstm.executeUpdate();
//        if (isCheckQuery > 0) {
//            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
//        }
//    }

    // create method 2.1:
    private static void updateDateDistribution() throws SQLException {
        String sql = "UPDATE OfficerDistribution SET date_received = ? WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        int targetIndex = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            targetIndex = checkInputInt();
            if (!checkOfficerDistributionIdExistence(targetIndex)) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
            }
        } while (!checkOfficerDistributionIdExistence(targetIndex));
        int id = getIdFromIndexOfOfficerDistributionDTable(targetIndex);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + targetIndex +"\u001B[0m");
        displayOfficerDistributionTableById(id, targetIndex);

        String newDateDistribution = "";
        do {
            System.out.println("Enter new date distribution (yyyy/MM/dd):");
            newDateDistribution = checkValidDate();
            if (!isValidDateFormat(newDateDistribution)) {
                System.out.println("\u001B[31m* Warning: Please enter according to format (yyyy/MM/dd).\u001B[0m ");
            }
        } while (!isValidDateFormat(newDateDistribution));

        pstm.setString(1, newDateDistribution);
        pstm.setInt(2, id);

        int isCheckQuery = pstm.executeUpdate();
        if (isCheckQuery > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 2.2:
    private static void updateAddressDistribution() throws SQLException {
        String sql = "UPDATE OfficerDistribution SET address_distribution = ? WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        int targetIndex = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            targetIndex = checkInputInt();
            if (!checkOfficerDistributionIdExistence(targetIndex)) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
            }
        } while (!checkOfficerDistributionIdExistence(targetIndex));
        int id = getIdFromIndexOfOfficerDistributionDTable(targetIndex);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + targetIndex +"\u001B[0m");
        displayOfficerDistributionTableById(id, targetIndex);

        int maxLengthAddress = 255;
        String newAddressDistribution = "";
        do {
            sc.nextLine();
            System.out.println("Enter address distribution");
            newAddressDistribution = sc.nextLine();
            if (!validateStringLength(newAddressDistribution, maxLengthAddress)) {
                System.out.println("\u001B[31m* Warning: You have entered more than 255 characters. Please try again!\u001B[0m");
            }
        } while (!validateStringLength(newAddressDistribution, maxLengthAddress));

        pstm.setString(1, newAddressDistribution);
        pstm.setInt(2, id);

        int isCheckQuery = pstm.executeUpdate();
        if (isCheckQuery > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 3: Delete Date by ID
    public static void deleteData() throws SQLException {
        String sql = "DELETE FROM OfficerDistribution WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        int targetIndex = 0;
        do {
            System.out.println("Enter the id you want to delete:");
            targetIndex = checkInputInt();
            if (!checkOfficerDistributionIdExistence(targetIndex)) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
            }
        } while (!checkOfficerDistributionIdExistence(targetIndex));
        int id = getIdFromIndexOfOfficerDistributionDTable(targetIndex);
        System.out.println("\u001B[31mThis is the table you want to delete whose id is " + targetIndex +"\u001B[0m");
        displayOfficerDistributionTableById(id, targetIndex);
        System.out.println("If you are sure you want to delete, press number 1. Otherwise, press any number (except number 1) to return to the main menu");
        int select = checkInputInt();
        if (select == 1) {
            pstm.setInt(1, id);
            int isCheckQuery = pstm.executeUpdate();
            if (isCheckQuery > 0) {
                System.out.println("\u001B[32m* Notification: Delete success\u001B[0m");
            }
        } else {
            System.out.println("\u001B[31mDeletion is not performed. You are return to the main menu\u001B[0m");
        }
    }

    // create method 4: Display Officer Distribution table
    public static void displayOfficerDistributionTable() throws SQLException {
        Map<Integer, OfficerDistribution> officerDistributionMap = getOfficerDistribution();
        if (!officerDistributionMap.isEmpty()) {
            System.out.println("\u001B[33m========================================== Officer Distribution Table ============================================");
            System.out.printf("| %-5s | %-20s | %-20s | %-18s | %-35s |%n", "ID", "Officer Name","Amount Distribution",
                    "Date Distribution", "Address Distribution");
            System.out.printf("| %-5s | %-20s | %-20s | %-18s | %-35s |\u001B[0m%n", "-----", "--------------------", "--------------------",
                    "-----------------", "-----------------------------------");
            for (Map.Entry<Integer, OfficerDistribution> entry : officerDistributionMap.entrySet()) {
                Integer index = entry.getKey();
                OfficerDistribution officerDistribution = entry.getValue();
                String officerName = officerDistribution.getOfficerName();
                float amoutDistribution = officerDistribution.getAmountDistribution();
                String date = dateFormat.format(officerDistribution.getDateDistribution());
                String address = officerDistribution.getAddressDistribution();
                System.out.printf("| %-5s | %-20s | %-20s | %-18s | %-35s |%n", index, officerName, formatFloatingPoint(amoutDistribution),
                        date, address);
                System.out.printf("| %-5s | %-20s | %-20s | %-18s | %-35s |%n", "-----", "--------------------", "--------------------",
                        "-----------------", "-----------------------------------");
            }
        }
        System.out.println();
    }

    public static Map<Integer, OfficerDistribution> getOfficerDistribution() throws SQLException {
        Map<Integer, OfficerDistribution> indexObjectMap = new LinkedHashMap<>();
        Statement st = connection.createStatement();
        String sql = "SELECT od.id, o.name, d.amount_distribution, \n" +
                "od.date_received, od.address_distribution \n" +
                "FROM OfficerDistribution as od\n" +
                "JOIN Officer as o\n" +
                "on od.officer_id = o.id\n" +
                "JOIN Distribution as d\n" +
                "on od.distribution_id = d.id\n" +
                "ORDER BY od.id desc;";
        ResultSet rs = st.executeQuery(sql);
        int index = 1;
        while (rs.next()) {
            OfficerDistribution officerDistribution = new OfficerDistribution(rs.getInt(1),
                    rs.getString(2),
                    rs.getFloat(3) ,
                    rs.getDate(4),
                    rs.getString(5)
            );
            indexObjectMap.put(index, officerDistribution);
            index++;
        }
        return indexObjectMap;
    }

    // create method Display Officer Distribution table by ID
    public static void displayOfficerDistributionTableById(int id, int targetIndex) throws SQLException {
        Map<Integer, OfficerDistribution> officerDistributionMap = getOfficerDistributionById(id, targetIndex);
        if (!officerDistributionMap.isEmpty()) {
            System.out.println("\u001B[33m========================================== Officer Distribution Table ============================================");
            System.out.printf("| %-5s | %-20s | %-20s | %-18s | %-35s |%n", "ID", "Officer Name","Amount Distribution",
                    "Date Distribution", "Address Distribution");
            System.out.printf("| %-5s | %-20s | %-20s | %-18s | %-35s |\u001B[0m%n", "-----", "--------------------", "--------------------",
                    "-----------------", "-----------------------------------");
            for (Map.Entry<Integer, OfficerDistribution> entry : officerDistributionMap.entrySet()) {
                Integer index = entry.getKey();
                OfficerDistribution officerDistribution = entry.getValue();
                String officerName = officerDistribution.getOfficerName();
                float amoutDistribution = officerDistribution.getAmountDistribution();
                String date = dateFormat.format(officerDistribution.getDateDistribution());
                String address = officerDistribution.getAddressDistribution();
                System.out.printf("| %-5s | %-20s | %-20s | %-18s | %-35s |%n", index, officerName, formatFloatingPoint(amoutDistribution),
                        date, address);
                System.out.printf("| %-5s | %-20s | %-20s | %-18s | %-35s |%n", "-----", "--------------------", "--------------------",
                        "-----------------", "-----------------------------------");
            }
        }
        System.out.println();
    }

    public static Map<Integer, OfficerDistribution> getOfficerDistributionById(int id, int target) throws SQLException {
        Map<Integer, OfficerDistribution> indexObjectMap = new LinkedHashMap<>();
        Statement st = connection.createStatement();
        String sql = "SELECT od.id, o.name, d.amount_distribution, \n" +
                "od.date_received, od.address_distribution \n" +
                "FROM OfficerDistribution as od\n" +
                "JOIN Officer as o\n" +
                "on od.officer_id = o.id\n" +
                "JOIN Distribution as d\n" +
                "on od.distribution_id = d.id\n" +
                "Where od.id = " + id;
        ResultSet rs = st.executeQuery(sql);
        while (rs.next()) {
            OfficerDistribution officerDistribution = new OfficerDistribution(rs.getInt(1),
                    rs.getString(2),
                    rs.getFloat(3) ,
                    rs.getDate(4),
                    rs.getString(5)
            );
            indexObjectMap.put(target, officerDistribution);
        }
        return indexObjectMap;
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

    // create method check Date
    private static String checkValidDate() {
        Date date = null;
        String checkDate = "";
        boolean isValidDate = false;
        while (!isValidDate) {
            checkDate = sc.next();
            try {
                date = dateFormat.parse(checkDate);
                isValidDate = true;
            }  catch (ParseException e) {
                System.out.println("\u001B[31m" + e + "\u001B[0m");
                System.out.println("\u001B[31m* Warning: Entered is incorrect format. Please try again!\u001B[0m");
            }
        }
        return checkDate;
    }

    // create method display Distribution table
    private static void displayDistributionTable() throws SQLException {
        Map<Integer, Map<Integer, Object>> distributionMap = getDistribution();
        if (!distributionMap.isEmpty()) {
            System.out.println("\u001B[33m=============================== Distribution Table ================================");
            System.out.printf("| %-5s | %-14s | %-12s | %-18s | %-18s |%n", "ID", "Commission ID","Household ID",
                    "Amount Received", "Date Received");
            System.out.printf("| %-5s | %-14s | %-12s | %-18s | %-18s |\u001B[0m%n", "-----", "--------------", "------------",
                    "------------------", "------------------");
            for (Map.Entry<Integer, Map<Integer, Object>> entry : distributionMap.entrySet()) {
                Integer index = entry.getKey();
                Map<Integer, Object> rowData = entry.getValue();
                int commissionId = (int) rowData.get(2);
                int houseId = (int) rowData.get(3);
                float amount = (float) rowData.get(4);
                String date = (String) rowData.get(5);
                System.out.printf("| %-5s | %-14s | %-12s | %-18s | %-18s |%n", index, commissionId,
                        houseId, formatFloatingPoint(amount), date);
                System.out.printf("| %-5s | %-14s | %-12s | %-18s | %-18s |%n", "-----", "--------------", "------------",
                        "------------------", "------------------");
            }
        }
        System.out.println();
    }

    public static Map<Integer, Map<Integer, Object>> getDistribution() throws SQLException {
        Map<Integer, Map<Integer, Object>> indexDataMap = new HashMap<>();
        Statement st = connection.createStatement();
        String sql = "SELECT * FROM Distribution Order by id desc;";
        ResultSet rs = st.executeQuery(sql);
        int index = 1;
        while (rs.next()) {
            int id = rs.getInt(1);
            int commissionId = rs.getInt(2);
            int houseId = rs.getInt(3);
            float amount = rs.getFloat(4);
            String date = dateFormat.format(rs.getDate(5));

            Map<Integer, Object> rowData = new HashMap<>();
            rowData.put(1, id);
            rowData.put(2, commissionId);
            rowData.put(3, houseId);
            rowData.put(4, amount);
            rowData.put(5, date);

            indexDataMap.put(index, rowData);
            index++;
        }
        return indexDataMap;
    }

    // check existence of Distribution id
    private static boolean checkDistributionIdExistence(int id) throws SQLException {
        Map<Integer, Map<Integer, Object>> indexDistributionMap = getDistribution();
        return indexDistributionMap.containsKey(id);
    }

    // create method get id from index of Distribution table
    private static int getIdFromIndexDistributionTable(int targetIndex) throws SQLException {
        Map<Integer, Map<Integer, Object>> indexDistributionMap = getDistribution();
        if (indexDistributionMap.containsKey(targetIndex)) {
            Map<Integer, Object> rowData = indexDistributionMap.get(targetIndex);
            if (rowData != null) {
                return (int) rowData.get(1);
            }
        }
        return 0;
    }

    private static String formatFloatingPoint(float value) {
        // Format the floating-point number with desired precision
        return String.format("%.0f", value);
    }
}