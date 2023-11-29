package Model;

import Common.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

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
        System.out.println(String.format("| %-30s |", "4. Update Commission ID"));
        System.out.println(String.format("| %-30s |", "=============================="));
    }

    // create method 1: Add new Officer
    public static void addNewOfficer() throws SQLException {
        Statement st = connection.createStatement();
        int maxLengthName = 50;
        String name = "";
        boolean isCheckStringLength = false;
        do {
            sc.nextLine();
            System.out.println("Input name:");
            name = sc.nextLine();
            isCheckStringLength = validateStringLength(name, maxLengthName);
            if (isCheckStringLength == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 50 characters. Please try again!\u001B[0m");
            }
        } while (isCheckStringLength == false);

        int maxLengthPhoneNumber = 11;
        String phoneNumber = "";
        boolean isCheckStringLength1 = false;
        do {
            System.out.println("Input phone number:");
            phoneNumber = sc.next();
            isCheckStringLength1 = validateStringLength(phoneNumber, maxLengthPhoneNumber);
            if (isCheckStringLength1 == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 11 characters. Please try again!\u001B[0m");
            }
        } while (isCheckStringLength1 == false);

        int maxLengthAddress = 255;
        String address = "";
        boolean isCheckStringLength2 = false;
        do {
            sc.nextLine();
            System.out.println("Input address:");
            address = sc.nextLine();
            isCheckStringLength2 = validateStringLength(address, maxLengthAddress);
            if (isCheckStringLength2 == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 255 characters. Please try again!\u001B[0m");
            }
        } while (isCheckStringLength2 == false);

        // add commission id
        int commissionId = 0;
        boolean isCheckExistence = false;
        do {
            System.out.println("Input commission id: ");
            commissionId = checkInputInt();
            isCheckExistence = checkCommissionIdExistence(commissionId);
            if (isCheckExistence == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Commission table!\u001B[0m");
            }
        } while (isCheckExistence == false);

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
                case 4:
                    updateCommissionId();
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
        // get the ID value in the Officer table
        ArrayList<Integer> s1 = new ArrayList<>();
        String sql = "SELECT * FROM Officer";
        PreparedStatement pstm = connection.prepareStatement(sql);
        ResultSet rs = pstm.executeQuery();
        while (rs.next()) {
            s1.add(0, rs.getInt(1));
        }
        int count = 0;
        for (int i = 0; i < s1.size(); i++) {
            if (id == s1.get(i)) {
                count++;
            }
        }
        return count > 0;
    }

    // create method 2.1:
    private static void updateOfficerName() throws SQLException {
        String sql = "UPDATE Officer SET name = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        boolean isCheckExistOfficerId = false;
        int id = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            id = checkInputInt();
            isCheckExistOfficerId = checkOfficerIdExistence(id);
            if (isCheckExistOfficerId == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (isCheckExistOfficerId == false);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + id +"\u001B[0m");
        displayOfficerTableById(id);

        int maxLength = 50;
        boolean isCheckStringLength = false;
        String newName = "";
        do {
            sc.nextLine();
            System.out.println("Enter new name:");
            newName = sc.nextLine();
            isCheckStringLength = validateStringLength(newName, maxLength);
            if (isCheckStringLength == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 50 characters. Please try again!\u001B[0m");
            }
        } while (isCheckStringLength == false);
        pstm.setString(1, newName);
        pstm.setInt(2, id);

        int isCheckQuery = pstm.executeUpdate();
        if (isCheckQuery > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 2.2:
    private static void updateOfficerPhoneNumber() throws SQLException {
        boolean isCheckExistOfficerId = false;
        int id = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            id = checkInputInt();
            isCheckExistOfficerId = checkOfficerIdExistence(id);
            if (isCheckExistOfficerId == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (isCheckExistOfficerId == false);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + id +"\u001B[0m");
        displayOfficerTableById(id);

        String sql = "UPDATE Officer SET phone_number = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        int maxLength = 11;
        boolean isCheckStringLength = false;
        String newPhoneNumber = "";
        do {
            System.out.println("Enter new phone number:");
            newPhoneNumber = sc.next();
            isCheckStringLength = validateStringLength(newPhoneNumber, maxLength);
            if (isCheckStringLength == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 11 characters. Please try again!\u001B[0m");
            }
        } while (isCheckStringLength == false);

        pstm.setString(1, newPhoneNumber);
        pstm.setInt(2, id);

        int isCheckQuery = pstm.executeUpdate();
        if (isCheckQuery > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 2.3:
    private static void updateOfficerAddress() throws SQLException {
        boolean isCheckExistOfficerId = false;
        int id = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            id = checkInputInt();
            isCheckExistOfficerId = checkOfficerIdExistence(id);
            if (isCheckExistOfficerId == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (isCheckExistOfficerId == false);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + id +"\u001B[0m");
        displayOfficerTableById(id);

        String sql = "UPDATE Officer SET address = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        int maxLength = 255;
        boolean isCheckStringLength = false;
        String newAddress = "";
        do {
            sc.nextLine();
            System.out.println("Enter new address:");
            newAddress = sc.nextLine();
            isCheckStringLength = validateStringLength(newAddress,maxLength);
            if (isCheckStringLength == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 255 characters. Please try again!\u001B[0m");
            }
        } while (isCheckStringLength == false);

        pstm.setString(1, newAddress);
        pstm.setInt(2, id);

        int isCheckQuery = pstm.executeUpdate();
        if (isCheckQuery > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 2.4: Update Commission ID
    private static void updateCommissionId() throws SQLException {
        int select = 0;
        do {
            System.out.println("If you don't remember Officer ID, press 1 to review the Officer table.");
            System.out.println("If you don't need it, press any number to continue");
            select = checkInputInt();
            if (select == 1) {
                displayCommissionTable();
            }
        } while (false);

        String sql = "UPDATE Officer SET commission_id = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        boolean isCheckExistOfficerId = false;
        int id = 0;
        do {
            System.out.println("Enter the id you want to edit:");
            id = checkInputInt();
            isCheckExistOfficerId = checkOfficerIdExistence(id);
            if (isCheckExistOfficerId == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (isCheckExistOfficerId == false);
        System.out.println("\u001B[32mThis is the table you are accessing whose id is " + id +"\u001B[0m");
        displayOfficerTableById(id);

        int newCommissionId = 0;
        boolean isCheckExistence = false;
        do {
            System.out.println("Input commission id: ");
            newCommissionId = checkInputInt();
            isCheckExistence = checkCommissionIdExistence(newCommissionId);
            if (isCheckExistence == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Commission table!\u001B[0m");
            }
        } while (isCheckExistence == false);

        pstm.setInt(1, newCommissionId);
        pstm.setInt(2, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        }
    }

    // create method 3: Delete Officer by ID
    public static void deleteOfficer() throws SQLException {
        String sql = "DELETE FROM Officer WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        boolean isCheckExistOfficerId = false;
        int id = 0;
        do {
            System.out.println("Enter the id you want to delete:");
            id = checkInputInt();
            isCheckExistOfficerId = checkOfficerIdExistence(id);
            if (isCheckExistOfficerId == false) {
                System.out.println("\u001B[31m* Warning: ID does not exist in the Officer table!\u001B[0m");
            }
        } while (isCheckExistOfficerId == false);
        System.out.println("\u001B[31mThis is the table you want to delete whose id is " + id +"\u001B[0m");
        displayOfficerTableById(id);
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

    // create method 4: Display Officer table
    public static void displayOfficerTable() throws SQLException {
        Statement st = connection.createStatement();
        String sql = "select * from Officer";
        ResultSet rs = st.executeQuery(sql);
        System.out.println("\u001B[33m============================================= Officer Table ==============================================");
        System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-15s |", "ID", "Name", "Phone number", "Address","Commission ID"));
        System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-15s |\u001B[0m", "-----", "--------------------", "---------------",
                "-----------------------------------", "---------------"));
        while (rs.next()) {
            System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-15s |", rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4), rs.getInt(5)));
            System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-15s |", "-----", "--------------------", "---------------",
                    "-----------------------------------", "---------------"));
        }
        System.out.println();
    }

    // create method display Officer table by id
    public static void displayOfficerTableById(int id) throws SQLException {
        Statement st = connection.createStatement();
        String sql = "SELECT * FROM Officer WHERE id = " + id;
        ResultSet rs = st.executeQuery(sql);
        System.out.println("\u001B[33m============================================= Officer Table ==============================================");
        System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-15s |", "ID", "Name", "Phone number", "Address","Commission ID"));
        System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-15s |\u001B[0m", "-----", "--------------------", "---------------",
                "-----------------------------------", "---------------"));
        while (rs.next()) {
            System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-15s |", rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4), rs.getInt(5)));
            System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s | %-15s |", "-----", "--------------------", "---------------",
                    "-----------------------------------", "---------------"));
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
        Statement st = connection.createStatement();
        String sql = "SELECT * FROM Commission";
        ResultSet rs = st.executeQuery(sql);
        System.out.println("\u001B[33m====================== Commission Table =======================");
        System.out.println(String.format("| %-5s | %-15s | %-15s | %-15s |", "ID", "Precinct", "City/District", "Province"));
        System.out.println(String.format("| %-5s | %-15s | %-15s | %-15s |\u001B[0m", "-----", "--------------", "--------------", "--------------"));
        while (rs.next()) {
            System.out.println(String.format("| %-5s | %-15s | %-15s | %-15s |", rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4)));
            System.out.println(String.format("| %-5s | %-15s | %-15s | %-15s |\u001B[0m", "-----", "--------------", "--------------", "--------------"));
        }
        System.out.println();
    }

    // create method check existence of Commission ID
    private static boolean checkCommissionIdExistence(int id) throws SQLException {
        // get the ID value int the Officer Distribution table
        ArrayList<Integer> s1 = new ArrayList<>();
        String sql2 = "Select * from Commission";
        PreparedStatement pstm = connection.prepareStatement(sql2);
        ResultSet rs2 = pstm.executeQuery();
        while (rs2.next()) {
            s1.add(0, rs2.getInt(1));
        }
        int count = 0;
        for (int i = 0; i < s1.size(); i++) {
            if (id == s1.get(i)) {
                count++;
            }
        }
        return count > 0;
    }
}