package Model;

import Common.DBConnect;
import Common.JdbcConfig;

import java.sql.*;
import java.util.Scanner;

public class OfficerManager {
    private static final Scanner sc = new Scanner(System.in);

    private static final Connection connection = DBConnect.connectDatabase();
    public static void main(String[] args) throws SQLException {
        displayMenu();
        int choice = 0;
        do {
            try {
                System.out.println("Enter your choice...");
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("* Warning: Input is incorrect!");
            }
            switch (choice) {
                case 0:
                    connection.close();
                    System.out.println("* Notification: Program is closed. Thank you for using our program!");
                    break;
                case 1:
                    OfficerManager.addNewOfficer();
                    displayMenu();
                    break;
                case 2:
                    OfficerManager.updateOfficerTable();
                    displayMenu();
                    break;
                case 3:
                    OfficerManager.deleteOfficer();
                    displayMenu();
                    break;
                case 4:
                    OfficerManager.displayOfficerTable();
                    displayMenu();
                    break;
                default:
                    System.out.println("* Warning: Input is invalid. Please try again!");
                    break;
            }
        } while (choice != 0);
    }

    // create method display menu of Officer table
    public static void displayMenu() {
        System.out.printf("| %-25s |%n", "= Officer Manager Menu =");
        System.out.printf("| %-25s |%n", "-------------------------");
        System.out.printf("| %-25s |%n", "0. Exit program");
        System.out.printf("| %-25s |%n", "1. Add new Officer");
        System.out.printf("| %-25s |%n", "2. Update Officer table");
        System.out.printf("| %-25s |%n", "3. Delete Officer by ID");
        System.out.printf("| %-25s |%n", "4. Display Officer table");
        System.out.printf("| %-25s |%n", "=========================");
    }

    public static void displayUpdateMenu() {
        System.out.printf("| %-30s |%n", "======== Update Menu =========");
        System.out.printf("| %-30s |%n", "------------------------------");
        System.out.printf("| %-30s |%n", "0. Exit program");
        System.out.printf("| %-30s |%n", "1. Update Officer name");
        System.out.printf("| %-30s |%n", "2. Update Officer phone number");
        System.out.printf("| %-30s |%n", "3. Update Officer address");
        System.out.printf("| %-30s |%n", "==============================");
    }

    // create method 1: Add new Officer
    public static void addNewOfficer() throws SQLException {
        Statement st = connection.createStatement();
        sc.nextLine();
        System.out.println("Input name:");
        String name = sc.nextLine();
        System.out.println("Input phone number:");
        String phoneNumber = sc.next();
        sc.nextLine();
        System.out.println("Input address:");
        String address = sc.nextLine();

        // create sql statement and execute
        String sql = "INSERT INTO [dbo].[Officer] VALUES ('" + name + "', '" + phoneNumber + "', '" + address + "');";
        int check = st.executeUpdate(sql);
        if (check > 0) {
            System.out.println("* Notification: Insert success");
            System.out.println();
        } else {
            System.out.println("* Warning: Insert fail.");
        }
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
                    System.out.println("* Notification: Update program is closed");
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
                    System.out.println("* Warning: Input is invalid. Please try again!");
                    break;
            }
        } while (selectUpdate != 0);
    }

    // create method 2.1:
    public static void updateOfficerName() throws SQLException {
        String sql = "UPDATE Officer SET name = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        sc.nextLine();
        System.out.println("Enter new name:");
        String newName = sc.nextLine();
        pstm.setString(1, newName);

        System.out.println("Enter the id you want to edit:");
        int id = sc.nextInt();
        pstm.setInt(2, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("* Notification: Update success");
        } else {
            System.out.println("* Warning: Update fail. The ID you entered is invalid.");
        }
    }

    // create method 2.2:
    public static void updateOfficerPhoneNumber() throws SQLException {
        String sql = "UPDATE Officer SET phone_number = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        System.out.println("Enter new phone number:");
        String newPhoneNumber = sc.next();
        pstm.setString(1, newPhoneNumber);

        System.out.println("Enter the id you want to edit:");
        int id = sc.nextInt();
        pstm.setInt(2, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("* Notification: Update success");
        } else {
            System.out.println("* Warning: Update fail. The ID you entered is invalid.");
        }
    }

    // create method 2.3:
    public static void updateOfficerAddress() throws SQLException {
        String sql = "UPDATE Officer SET address = ?  WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        sc.nextLine();
        System.out.println("Enter new address:");
        String newAddress = sc.nextLine();
        pstm.setString(1, newAddress);

        System.out.println("Enter the id you want to edit:");
        int id = sc.nextInt();
        pstm.setInt(2, id);

        int check = pstm.executeUpdate();
        if (check > 0) {
            System.out.println("* Notification: Update success");
        } else {
            System.out.println("* Warning: Update fail. The ID you entered is invalid.");
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
            System.out.println("* Notification: Update success");
        } else {
            System.out.println("* Warning: Delete fail. The ID you entered is invalid.");
        }
    }

    // create method 4: Display Officer table
    public static void displayOfficerTable() throws SQLException {
        Statement st = connection.createStatement();
        String sql = "select * from Officer";
        ResultSet rs = st.executeQuery(sql);
        System.out.println("=====================================Officer Table======================================");
        System.out.printf("| %-5s | %-20s | %-15s | %-35s |%n", "ID", "Name", "Phone number", "Address");
        System.out.printf("| %-5s | %-20s | %-15s | %-35s |%n", "-----", "--------------------", "---------------",
                    "-----------------------------------");
        while (rs.next()) {
            System.out.printf("| %-5s | %-20s | %-15s | %-35s |%n", rs.getInt(1), rs.getString(2),
                    rs.getString(3), rs.getString(4));
            System.out.printf("| %-5s | %-20s | %-15s | %-35s |%n", "-----", "--------------------", "---------------",
                    "-----------------------------------");
        }
        System.out.println();
    }
}
