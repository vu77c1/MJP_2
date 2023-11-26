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
            try {
                System.out.println("Enter your choice...");
                choice = sc.nextInt();
            } catch (Exception e) {
                System.out.println(e.toString());
                System.out.println("* Warning: Input is incorrect!");
            }
            switch (choice) {
                case 0:
                    connection.close();
                    System.out.println("* Notification: Program is closed. Thank you for using our program!");
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
                    System.out.println("* Warning: Input is invalid. Please try again!");
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
        System.out.println(String.format("| %-30s |", "1. Update date distribution"));
        System.out.println(String.format("| %-30s |", "2. Update address distribution"));
        System.out.println(String.format("| %-30s |", "=============================="));
    }

    // create method 1: Add new data
    public static void addNewData() throws SQLException {
        // add Officer_id into Officer Distribution table
        boolean check1 = false;
        int officerId = 0;
        do {
            System.out.println("Enter Officer id:");
            officerId = sc.nextInt();
            // call method checkOfficerIdExistence:
            check1 = checkOfficerIdExistence(officerId);
            if (check1 == false) {
                System.out.println("* Warning: ID does not exist in the Officer table!");
            }
        } while (check1 == false);

        // add Distribution_id into Officer Distribution table
        boolean check2 = false;
        int distributionId = 0;
        do {
            System.out.println("Enter Distribution id:");
            distributionId = sc.nextInt();
            // call method checkDistributionIdExistence
            check2 = checkDistributionIdExistence(distributionId);
            if (check2 == false) {
                System.out.println("* Warning: ID does not exist in the Officer Distribution table!");
            }
        } while (check2 == false);

        // add date distribution
        Date date = null;
        System.out.println("Enter date distribution (yyyy/MM/dd):");
        String dateDistribution = sc.next();
        try {
            date = (Date) dateFormat.parse(dateDistribution);
        } catch (ParseException e) {
            System.out.println(e.toString());
            System.out.println("* Warning: Entered is incorrect format!");
        }

        // add address distribution
        sc.nextLine();
        System.out.println("Enter address distribution");
        String addressDistribution = sc.nextLine();

        // create sql stament and execute
        Statement st = connection.createStatement();
        String sql = "INSERT INTO OfficerDistribution " +
                "VALUES ("+ officerId +", "+ distributionId +", '"+ dateDistribution +"', '"+ addressDistribution +"');";
        int check = st.executeUpdate(sql);
        if (check > 0) {
            System.out.println("* Notification: Insert success");
            System.out.println();
        } else {
            System.out.println("* Warning: Insert fail.");
        }
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

    // create method 2: Update Officer Distribution table
    public static void updateOfficerDistributionTable() throws SQLException {
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
                    updateDateDistribution();
                    displayUpdateMenu();
                    break;
                case 2:
                    updateAddressDistribution();
                    displayUpdateMenu();
                    break;
                default:
                    System.out.println("* Warning: Input is invalid. Please try again!");
                    break;
            }
        } while (selectUpdate !=0);
    }

    // create method 2.1:
    public static void updateDateDistribution() throws SQLException {
        String sql = "UPDATE OfficerDistribution SET date_distribution = ? WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        Date date = null;
        System.out.println("Enter new date distribution (yyyy/MM/dd):");
        String newDateDistribution = sc.next();
        try {
            date = (Date) dateFormat.parse(newDateDistribution);
        } catch (ParseException e) {
            System.out.println(e.toString());
            System.out.println("* Warning: Entered is incorrect format!");
        }
        pstm.setString(1, newDateDistribution);

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
    public static void updateAddressDistribution() throws SQLException {
        String sql = "UPDATE OfficerDistribution SET address_distribution = ? WHERE id = ?";
        PreparedStatement pstm = connection.prepareStatement(sql);

        sc.nextLine();
        System.out.println("Enter new address distribution");
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

    // create method 3: Delete Date by ID
    public static void deleteData() throws SQLException {
        String sql = "DELETE FROM OfficerDistribution WHERE id = ?";
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

    // create method 4: Display Officer Distribution table
    public static void displayOfficerDistributionTable() throws SQLException {
        Statement st = connection.createStatement();
        String sql = "select * from OfficerDistribution";
        ResultSet rs = st.executeQuery(sql);
        System.out.println("=================================== Officer Distribution Table ====================================");
        System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |", "ID", "Officer ID","Distribution ID",
                    "Date Distribution", "Address Distribution"));
        System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |", "-----", "----------", "---------------",
                    "-----------------", "-----------------------------------"));
        while (rs.next()) {
            System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |", rs.getInt(1), rs.getInt(2),
                    rs.getInt(3) , dateFormat.format(rs.getDate(4)), rs.getString(5)));
            System.out.println(String.format("| %-5s | %-10s | %-15s | %-18s | %-35s |", "-----", "----------", "---------------",
                    "-----------------", "-----------------------------------"));
        }
        System.out.println();
    }
}
