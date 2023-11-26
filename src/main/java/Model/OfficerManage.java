package Model;

import Common.DBConnect;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class OfficerManage {
    private static Scanner sc = new Scanner(System.in);
    private static Connection connection = DBConnect.connectDatabase();
    public static void main(String[] args) throws SQLException {
        displayMenu();
        int choice = 0;
        do {
            try {
                System.out.println("Enter your choice...");
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println("\u001B[31m" + e.toString() + "\u001B[0m");
                System.out.println("\u001B[31m* Warning: Input is incorrect!\u001B[0m");
            }
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
        System.out.println(String.format("| %-25s |", "= Officer Manager Menu ="));
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
        int maxLengthName = 50;
        String name = "";
        boolean check1 = false;
        do {
            sc.nextLine();
            System.out.println("Input name:");
            name = sc.nextLine();
            check1 = validateStringLength(name, maxLengthName);
            if (check1 == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 50 characters. Please try again!\u001B[0m");
            }
        } while (check1 == false);

        int maxLengthPhoneNumber = 11;
        String phoneNumber = "";
        boolean check2 = false;
        do {
            System.out.println("Input phone number:");
            phoneNumber = sc.next();
            check2 = validateStringLength(phoneNumber, maxLengthPhoneNumber);
            if (check2 == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 11 characters. Please try again!\u001B[0m");
            }
        } while (check2 == false);

        int maxLengthAddress = 255;
        String address = "";
        boolean check3 = false;
        do {
            sc.nextLine();
            System.out.println("Input address:");
            address = sc.nextLine();
            check3 = validateStringLength(address, maxLengthAddress);
            if (check3 == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 255 characters. Please try again!\u001B[0m");
            }
        } while (check3 == false);

        // create sql statement and execute
        String sql = "INSERT INTO [dbo].[Officer] VALUES ('" + name + "', '" + phoneNumber + "', '" + address + "');";
        int check = st.executeUpdate(sql);
        if (check > 0) {
            System.out.println("\u001B[32m* Notification: Insert success.\u001B[0m");
            System.out.println();
        } else {
            System.out.println("\u001B[31m* Warning: Insert fail.\u001B[0m");
        }
    }

    // create Validate for String:
    private static boolean validateStringLength(String input, int maxLength) {
        return input.length() <= maxLength;
    }

    // create method 2: Update Officer table
    public static void updateOfficerTable() throws SQLException {
        displayUpdateMenu();
        int selectUpdate = 0;
        do {
            System.out.println("What information do you want to update?...");
            selectUpdate = sc.nextInt();
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

    // create method 2.1:
    public static void updateOfficerName() throws SQLException {
        String sql = "UPDATE Officer SET name = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        int maxLength = 50;
        boolean check1 = false;
        String newName = "";
        do {
            sc.nextLine();
            System.out.println("Enter new name:");
            newName = sc.nextLine();
            check1 = validateStringLength(newName, maxLength);
            if (check1 == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 50 characters. Please try again!\u001B[0m");
            }
        } while (check1 == false);
        pstm.setString(1, newName);

        System.out.println("Enter the id you want to edit:");
        int id = sc.nextInt();
        pstm.setInt(2, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        } else {
            System.out.println("\u001B[31m* Warning: Update fail. The ID you entered is invalid.\u001B[0m");
        }
    }

    // create method 2.2:
    public static void updateOfficerPhoneNumber() throws SQLException {
        String sql = "UPDATE Officer SET phone_number = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        int maxLength = 11;
        boolean check1 = false;
        String newPhoneNumber = "";
        do {
            System.out.println("Enter new phone number:");
            newPhoneNumber = sc.next();
            check1 = validateStringLength(newPhoneNumber, maxLength);
            if (check1 == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 11 characters. Please try again!\u001B[0m");
            }
        } while (check1 == false);
        pstm.setString(1, newPhoneNumber);

        System.out.println("Enter the id you want to edit:");
        int id = sc.nextInt();
        pstm.setInt(2, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        } else {
            System.out.println("\u001B[31m* Warning: Update fail. The ID you entered is invalid.\u001B[0m");
        }
    }

    // create method 2.3:
    public static void updateOfficerAddress() throws SQLException {
        String sql = "UPDATE Officer SET address = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        int maxLength = 255;
        boolean check1 = false;
        String newAddress = "";
        do {
            sc.nextLine();
            System.out.println("Enter new address:");
            newAddress = sc.nextLine();
            check1 = validateStringLength(newAddress,maxLength);
            if (check1 == false) {
                System.out.println("\u001B[31m* Warning: You have entered more than 255 characters. Please try again!\u001B[0m");
            }
        } while (check1 == false);
        pstm.setString(1, newAddress);

        System.out.println("Enter the id you want to edit:");
        int id = sc.nextInt();
        pstm.setInt(2, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("\u001B[32m* Notification: Update success\u001B[0m");
        } else {
            System.out.println("\u001B[31m* Warning: Update fail. The ID you entered is invalid.\u001B[0m");
        }
    }

    // create method 3: Delete Officer by ID
    public static void deleteOfficer() throws SQLException {
        String sql = "DELETE FROM Officer WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        System.out.println("Enter the id you want to delete:");
        int id = sc.nextInt();
        pstm.setInt(1, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("\u001B[32m* Notification: Delete success\u001B[0m");
        } else {
            System.out.println("\u001B[31m* Warning: Delete fail. The ID you entered is invalid.\u001B[0m");
        }
    }

    // create method 4: Display Officer table
    public static void displayOfficerTable() throws SQLException {
        Statement st = connection.createStatement();
        String sql = "select * from Officer";
        ResultSet rs = st.executeQuery(sql);
        System.out.println("\u001B[33m=====================================Officer Table======================================");
        System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s |", "ID", "Name", "Phone number", "Address"));
        System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s |\u001B[0m", "-----", "--------------------", "---------------",
                    "-----------------------------------"));
        while (rs.next()) {
            System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s |", rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4)));
            System.out.println(String.format("| %-5s | %-20s | %-15s | %-35s |", "-----", "--------------------", "---------------",
                    "-----------------------------------"));
        }
        System.out.println();
    }
}
