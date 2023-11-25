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
                    displayMenu();
                    break;
                case 3:
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

    // create method 1: Add new data
    public static void addNewData() throws SQLException {
        // get the ID value in the Officer table
        ArrayList<Integer> s1 = new ArrayList<>();
        String sql1 = "SELECT * FROM Officer";
        PreparedStatement pstm = connection.prepareStatement(sql1);
        ResultSet rs1 = pstm.executeQuery();
        while (rs1.next()) {
            s1.add(0,rs1.getInt(1));
        }

        // add Officer_id into Officer Distribution table
        // fixing...
        System.out.println("Enter Officer id:");
        int officerId = sc.nextInt();
        for (int i = 0; i < s1.size(); i++) {
            if (officerId != s1.get(i)) {
                System.out.println("Input is invalid");
            }
        }

        // get the ID value int the Distribution table
        ArrayList<Integer> s2 = new ArrayList<>();
        String sql2 = "Select * from Distribution";
        ResultSet rs2 = pstm.executeQuery();
        int orriginalDistributionId = 0;
        while (rs2.next()) {
            orriginalDistributionId = rs2.getInt(1);
        }

        // add Distribution_id into Officer Distribution table
        int distributionId = 0;
        do {
            System.out.println("Enter Distribution id:");
            distributionId = sc.nextInt();
        } while (distributionId != orriginalDistributionId);

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
                "VALUES ("+ officerId +", "+ distributionId +", '"+ dateDistribution +"', "+ addressDistribution +");";
        int check = st.executeUpdate(sql);
        if (check > 0) {
            System.out.println("* Notification: Insert success");
            System.out.println();
        } else {
            System.out.println("* Warning: Insert fail.");
        }
    }

    // create mothod 4: Display Officer Distribution table
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
