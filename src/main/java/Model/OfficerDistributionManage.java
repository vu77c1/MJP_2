package Model;

import Common.DBConnect;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class OfficerDistributionManage {
    private static Scanner sc = new Scanner(System.in);
    private static Connection connection = DBConnect.connectDatabase();
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) throws SQLException {
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
        System.out.println(String.format("| %-40s |", "== Officer Distribution Manager Menu =="));
        System.out.println(String.format("| %-40s |", "----------------------------------------"));
        System.out.println(String.format("| %-40s |", "0. Exit program"));
        System.out.println(String.format("| %-40s |", "1. Add new data"));
        System.out.println(String.format("| %-40s |", "2. Update Officer Distribution table"));
        System.out.println(String.format("| %-40s |", "3. Delete data by ID"));
        System.out.println(String.format("| %-40s |", "4. Display Officer Distribution table"));
        System.out.println(String.format("| %-40s |", "========================================"));
    }

    // create method display Update menu
    public static void displayUpdateMenu() {
        System.out.println(String.format("| %-30s |", "======== Update Menu ========="));
        System.out.println(String.format("| %-30s |", "------------------------------"));
        System.out.println(String.format("| %-30s |", "0. Exit program"));
        System.out.println(String.format("| %-30s |", "1. Update Officer ID"));
        System.out.println(String.format("| %-30s |", "2. Update Distribution ID"));
        System.out.println(String.format("| %-30s |", "3. Update date distribution"));
        System.out.println(String.format("| %-30s |", "4. Update address distribution"));
        System.out.println(String.format("| %-30s |", "=============================="));
    }

    // create method 1: Add new data
    public static void addNewData() throws SQLException {
        // add Officer_id into Officer Distribution table
        boolean check1 = false;
        int officerId = 0;
        do {
            System.out.println("Enter Officer id:");
            officerId = checkInputInt();
            // call method checkOfficerIdExistence:
            check1 = checkOfficerIdExistence(officerId);
            if (check1 == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (check1 == false);

        // add Distribution_id into Officer Distribution table
        boolean check2 = false;
        int distributionId = 0;
        do {
            System.out.println("Enter Distribution id:");
            distributionId = checkInputInt();
            // call method checkDistributionIdExistence
            check2 = checkDistributionIdExistence(distributionId);
            if (check2 == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
            }
        } while (check2 == false);

        // add date distribution
        Date date = null;
        boolean isValidDate = false;
        String dateDistribution = "";
        while (!isValidDate) {
            System.out.println("Enter date distribution (yyyy/MM/dd):");
            dateDistribution = sc.next();
            try {
                date = (Date) dateFormat.parse(dateDistribution);
                isValidDate = true;
            } catch (ParseException e) {
                System.out.println("\u001B[31m" + e.toString() + "\u001B[0m");
                System.out.println("\u001B[31m* Warning: Entered is incorrect format!\u001B[0m");
            }
        }

        // add address distribution
        int maxLengthAddress = 255;
        boolean check3 = false;
        String addressDistribution = "";
        do {
            sc.nextLine();
            System.out.println("Enter address distribution");
            addressDistribution = sc.nextLine();
            check3 = validateStringLength(addressDistribution, maxLengthAddress);
            if (check3 == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 255 characters. Please try again!\u001B[0m");
            }
        } while (check3 == false);

        // create sql stament and execute
        Statement st = connection.createStatement();
        String sql = "INSERT INTO OfficerDistribution " +
                "VALUES ("+ officerId +", "+ distributionId +", '"+ dateDistribution +"', '"+ addressDistribution +"');";
        int check = st.executeUpdate(sql);
        if (check > 0) {
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

    // check existence of Officer id
    private static boolean checkOfficerIdExistence(int id) throws SQLException {
        // get the ID value in the Officer table
        ArrayList<Integer> s1 = new ArrayList<>();
        String sql1 = "SELECT * FROM Officer";
        PreparedStatement pstm = connection.prepareStatement(sql1);
        ResultSet rs1 = pstm.executeQuery();
        while (rs1.next()) {
            s1.add(0, rs1.getInt(1));
        }
        int count = 0;
        for (int i = 0; i < s1.size(); i++) {
            if (id == s1.get(i)) {
                count++;
            }
        }
        return count > 0;
    }

    // check existence of Distribution id
    private static boolean checkDistributionIdExistence(int id) throws SQLException {
        // get the ID value int the Distribution table
        ArrayList<Integer> s2 = new ArrayList<>();
        String sql2 = "Select * from Distribution";
        PreparedStatement pstm = connection.prepareStatement(sql2);
        ResultSet rs2 = pstm.executeQuery();
        while (rs2.next()) {
            s2.add(0, rs2.getInt(1));
        }
        int count = 0;
        for (int i = 0; i < s2.size(); i++) {
            if (id == s2.get(i)) {
                count++;
            }
        }
        return count > 0;
    }

    // check existence of OfficerDistribution id
    private static boolean checkOfficerDistributionIdExistence(int id) throws SQLException {
        // get the ID value int the Officer Distribution table
        ArrayList<Integer> s3 = new ArrayList<>();
        String sql2 = "Select * from OfficerDistribution";
        PreparedStatement pstm = connection.prepareStatement(sql2);
        ResultSet rs2 = pstm.executeQuery();
        while (rs2.next()) {
            s3.add(0, rs2.getInt(1));
        }
        int count = 0;
        for (int i = 0; i < s3.size(); i++) {
            if (id == s3.get(i)) {
                count++;
            }
        }
        return count > 0;
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
                    updateOfficerId();
                    displayUpdateMenu();
                    break;
                case 2:
                    updateDistributionId();
                    displayUpdateMenu();
                    break;
                case 3:
                    updateDateDistribution();
                    displayUpdateMenu();
                    break;
                case 4:
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
    public static void updateOfficerId() throws SQLException {
        int select1 = 0;
        do {
            System.out.println("If you don't remember Officer ID, press 1 to review the Officer table.");
            System.out.println("If you don't need it, press any number to continue");
            select1 = checkInputInt();
            if (select1 == 1) {
                OfficerManage.displayOfficerTable();
            }
        } while (false);
        String sql = "UPDATE OfficerDistribution SET officer_id = ? WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        boolean check1 = false;
        int id = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            id = checkInputInt();
            check1 = checkOfficerDistributionIdExistence(id);
            if (check1 == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
            }
        } while (check1 == false);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + id +"\u001B[0m");
        displayOfficerDistributionTableById(id);

        boolean check2 = false;
        int newOfficerId = 0;
        do {
            System.out.println("Enter new Officer id:");
            newOfficerId = checkInputInt();
            // call method checkOfficerIdExistence:
            check2 = checkOfficerIdExistence(newOfficerId);
            if (check2 == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (check2 == false);

        pstm.setInt(1, newOfficerId);
        pstm.setInt(2, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 2.2:
    public static void updateDistributionId() throws SQLException {
        int select1 = 0;
        do {
            System.out.println("If you don't remember Distribution ID, press 1 to review the Distribution table.");
            System.out.println("If you don't need it, press any number to continue");
            select1 = checkInputInt();
            if (select1 == 1) {
                displayDistributionTable();
            }
        } while (false);
        String sql = "UPDATE OfficerDistribution SET distribution_id = ? WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        boolean check1 = false;
        int id = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            id = checkInputInt();
            check1 = checkOfficerDistributionIdExistence(id);
            if (check1 == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
            }
        } while (check1 == false);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + id +"\u001B[0m");
        displayOfficerDistributionTableById(id);

        boolean check2 = false;
        int newDistributionId = 0;
        do {
            System.out.println("Enter Distribution id:");
            newDistributionId = checkInputInt();
            // call method checkDistributionIdExistence
            check2 = checkDistributionIdExistence(newDistributionId);
            if (check2 == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
            }
        } while (check2 == false);

        pstm.setInt(1, newDistributionId);
        pstm.setInt(2, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 2.3:
    public static void updateDateDistribution() throws SQLException {
        String sql = "UPDATE OfficerDistribution SET date_distribution = ? WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        boolean check1 = false;
        int id = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            id = checkInputInt();
            check1 = checkOfficerDistributionIdExistence(id);
            if (check1 == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
            }
        } while (check1 == false);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + id +"\u001B[0m");
        displayOfficerDistributionTableById(id);

        Date date = null;
        boolean isValidDate = false;
        String newDateDistribution = "";
        while (!isValidDate) {
            System.out.println("Enter date distribution (yyyy/MM/dd):");
            newDateDistribution = sc.next();
            try {
                date = (Date) dateFormat.parse(newDateDistribution);
                isValidDate = true;
            } catch (ParseException e) {
                System.out.println("\u001B[31m" + e.toString() + "\u001B[0m");
                System.out.println("\u001B[31m* Warning: Entered is incorrect format!\u001B[0m");
            }
        }
        pstm.setString(1, newDateDistribution);
        pstm.setInt(2, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 2.4:
    public static void updateAddressDistribution() throws SQLException {
        String sql = "UPDATE OfficerDistribution SET address_distribution = ? WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        boolean check1 = false;
        int id = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            id = checkInputInt();
            check1 = checkOfficerDistributionIdExistence(id);
            if (check1 == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
            }
        } while (check1 == false);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + id +"\u001B[0m");
        displayOfficerDistributionTableById(id);

        sc.nextLine();
        System.out.println("Enter new address distribution");
        String newAddress = sc.nextLine();

        pstm.setString(1, newAddress);
        pstm.setInt(2, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 3: Delete Date by ID
    public static void deleteData() throws SQLException {
        String sql = "DELETE FROM OfficerDistribution WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        boolean check1 = false;
        int id = 0;
        do {
            System.out.println("Enter the id you want to delete:");
            id = checkInputInt();
            check1 = checkOfficerDistributionIdExistence(id);
            if (check1 == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer Distribution table!\u001B[0m");
            }
        } while (check1 == false);
        System.out.println("\u001B[31mThis is the table you want to delete whose id is " + id +"\u001B[0m");
        displayOfficerDistributionTableById(id);
        System.out.println("If you are sure you want to delete, press number 1. Otherwise, press any number (except number 1) to return to the main menu");
        int select = checkInputInt();
        if (select == 1) {
            pstm.setInt(1, id);
            int check = pstm.executeUpdate();
            if (check > 0) {
                System.out.println("\u001B[32m* Notification: Delete success\u001B[0m");
            }
        } else {
            System.out.println("\u001B[31mDeletion is not performed. You are return to the main menu\u001B[0m");
        }
    }

    // create method 4: Display Officer Distribution table
    public static void displayOfficerDistributionTable() throws SQLException {
        Statement st = connection.createStatement();
        String sql = "SELECT * FROM OfficerDistribution";
        ResultSet rs = st.executeQuery(sql);
        System.out.println("\u001B[33m=================================== Officer Distribution Table ====================================");
        System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |", "ID", "Officer ID","Distribution ID",
                    "Date Distribution", "Address Distribution"));
        System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |\u001B[0m", "-----", "----------", "---------------",
                    "-----------------", "-----------------------------------"));
        while (rs.next()) {
            System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |", rs.getInt(1), rs.getInt(2),
                    rs.getInt(3) , dateFormat.format(rs.getDate(4)), rs.getString(5)));
            System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |", "-----", "----------", "---------------",
                    "-----------------", "-----------------------------------"));
        }
        System.out.println();
    }

    // create method Display Officer Distribution table by ID
    public static void displayOfficerDistributionTableById(int id) throws SQLException {
        Statement st = connection.createStatement();
        String sql = "SELECT * FROM OfficerDistribution WHERE id = " + id;
        ResultSet rs = st.executeQuery(sql);
        System.out.println("\u001B[33m=================================== Officer Distribution Table ====================================");
        System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |", "ID", "Officer ID","Distribution ID",
                "Date Distribution", "Address Distribution"));
        System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |\u001B[0m", "-----", "----------", "---------------",
                "-----------------", "-----------------------------------"));
        while (rs.next()) {
            System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |", rs.getInt(1), rs.getInt(2),
                    rs.getInt(3) , dateFormat.format(rs.getDate(4)), rs.getString(5)));
            System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |", "-----", "----------", "---------------",
                    "-----------------", "-----------------------------------"));
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

    // create method display Distribution table
    public static void displayDistributionTable() throws SQLException {
        Statement st = connection.createStatement();
        String sql = "SELECT * FROM Distribution";
        ResultSet rs = st.executeQuery(sql);
        System.out.println("\u001B[33m=============================== Distribution Table ================================");
        System.out.println(String.format("| %-5s | %-14s | %-12s | %-18s | %-18s |", "ID", "Commission ID","Household ID",
                "Amount Received", "Date Received"));
        System.out.println(String.format("| %-5s | %-14s | %-12s | %-18s | %-18s |\u001B[0m", "-----", "--------------", "------------",
                "------------------", "------------------"));
        while (rs.next()) {
            System.out.println(String.format("| %-5s | %-14s | %-12s | %-18s | %-18s |", rs.getInt(1), rs.getInt(2),
                    rs.getInt(3), rs.getFloat(4), dateFormat.format(rs.getDate(5))));
            System.out.println(String.format("| %-5s | %-14s | %-12s | %-18s | %-18s |", "-----", "--------------", "------------",
                    "------------------", "------------------"));
        }
    }
}
