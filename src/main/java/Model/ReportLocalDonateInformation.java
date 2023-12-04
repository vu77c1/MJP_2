package Model;

import Common.DBConnect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ReportLocalDonateInformation {
    private static final Scanner sc = new Scanner(System.in);
    private static final Connection connection = DBConnect.connectDatabase();

    public static void main(String[] args) throws SQLException {
        ReportLocalDonateInformation.getTask1002OfDung();
    }

    public static void getTask1002OfDung() throws SQLException {
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
                    ReportLocalDonateInformation.top5Wards();
                    displayMenu();
                    break;
                case 2:
                    ReportLocalDonateInformation.statisticsOfWards();
                    displayMenu();
                    break;
                default:
                    System.out.println("\u001B[31m* Warning: Input is invalid. Please try again!\u001B[0m");
                    break;
            }
        } while (choice != 0);
    }

    // create method display menu
    public static void displayMenu() {
        System.out.printf("| %-50s |%n", "============= Tast 1002 Of Dung Menu =============");
        System.out.printf("| %-50s |%n", "--------------------------------------------------");
        System.out.printf("| %-50s |%n", "0. Exit program");
        System.out.printf("| %-50s |%n", "1. Top 5 wards with the most donate");
        System.out.printf("| %-50s |%n", "2. Statistics of wards that have not been donated");
        System.out.printf("| %-50s |%n", "==================================================");
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

    // create method 1: Top 5 wards with the most donate
    public static void top5Wards() throws SQLException {
        Statement st = connection.createStatement();
        String sql = "WITH CommissionTotalAmount AS (\n" +
                "    SELECT DISTINCT\n" +
                "        c.id AS CommissionID,\n" +
                "        c.precint_name AS Precinct,\n" +
                "        c.city_name AS City,\n" +
                "        c.province_name AS Province,\n" +
                "        SUM(dd.amount) OVER (PARTITION BY c.id) AS TotalAmount\n" +
                "    FROM\n" +
                "        Commission AS c\n" +
                "    JOIN DonateDetail AS dd ON c.id = dd.commission_id\n" +
                ")\n" +
                "SELECT TOP 5\n" +
                "    --CommissionID,\n" +
                "    Precinct,\n" +
                "    City,\n" +
                "    Province,\n" +
                "    TotalAmount\n" +
                "FROM\n" +
                "    CommissionTotalAmount\n" +
                "ORDER BY TotalAmount DESC;";
        ResultSet rs = st.executeQuery(sql);
        System.out.println("\u001B[33m====================== Top 5 wards with the most donate =======================");
        System.out.printf("| %-15s | %-15s | %-18s | %-18s |%n", "Precinct", "District/City",
                "Province", "Total Amount");
        System.out.printf("| %-15s | %-15s | %-18s | %-18s |\u001B[0m%n", "---------------", "---------------",
                "-----------------", "------------------");
        while (rs.next()) {
            System.out.printf("| %-15s | %-15s | %-18s | %-18s |%n", rs.getString(1),
                    rs.getString(2), rs.getString(3), formatFloatingPoint(rs.getFloat(4)));
            System.out.printf("| %-15s | %-15s | %-18s | %-18s |%n", "---------------", "---------------",
                    "-----------------", "------------------");
        }
        System.out.println();
    }

    private static String formatFloatingPoint(float value) {
        // Format the floating-point number with desired precision
        return String.format("%.0f", value);
    }

    // create method 2: Statistics of wards that have not been donated
    public static void statisticsOfWards() throws SQLException {
        Statement st = connection.createStatement();
        String sql = "SELECT\n" +
                "    --c.id,\n" +
                "    c.precint_name,\n" +
                "    c.city_name,\n" +
                "    c.province_name\n" +
                "FROM\n" +
                "    Commission AS c\n" +
                "LEFT JOIN DonateDetail AS dd ON c.id = dd.commission_id\n" +
                "WHERE\n" +
                "    dd.commission_id IS NULL;";
        ResultSet rs = st.executeQuery(sql);
        System.out.println("\u001B[33m===== Statistics of wards that have not been donated =====");
        System.out.printf("| %-15s | %-15s | %-18s |%n", "Precinct", "District/City",
                "Province");
        System.out.printf("| %-15s | %-15s | %-18s |\u001B[0m%n", "---------------", "---------------",
                "-----------------");
        while (rs.next()) {
            System.out.printf("| %-15s | %-15s | %-18s |%n", rs.getString(1),
                    rs.getString(2), rs.getString(3));
            System.out.printf("| %-15s | %-15s | %-18s |%n", "---------------", "---------------",
                    "-----------------");
        }
        System.out.println();
    }
}