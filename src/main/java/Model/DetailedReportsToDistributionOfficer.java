package Model;

import Common.DBConnect;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class DetailedReportsToDistributionOfficer {
    private static final Scanner sc = new Scanner(System.in);
    private static final Connection connection = DBConnect.connectDatabase();
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) throws SQLException {
        DetailedReportsToDistributionOfficer.getTask1003OfDung();
    }

    public static void getTask1003OfDung() throws SQLException {
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
                    DetailedReportsToDistributionOfficer.listDetailsDistributionOfficer();
                    displayMenu();
                    break;
                default:
                    System.out.println("\u001B[31m* Warning: Input is invalid. Please try again!\u001B[0m");
                    break;
            }
        } while (choice != 0);
    }

    public static void displayMenu() {
        System.out.printf("| %-50s |%n", "============= Tast 1003 Of Dung Menu =============");
        System.out.printf("| %-50s |%n", "--------------------------------------------------");
        System.out.printf("| %-50s |%n", "0. Exit program");
        System.out.printf("| %-50s |%n", "1. List details of distribution officer");
        System.out.printf("| %-50s |%n", "==================================================");
    }

    public static void listDetailsDistributionOfficer() throws SQLException {
        Statement st = connection.createStatement();

        // Each batch of statistics is a month, enter the start date and end date as one batch
        String startDate = "";
        do {
            System.out.println("Enter start date (yyyy/MM/dd):");
            startDate = checkValidDate();
            if (!isValidDateFormat(startDate)) {
                System.out.println("\u001B[31m* Warning: Please enter according to format (yyyy/MM/dd).\u001B[0m ");
            }
        } while (!isValidDateFormat(startDate));

        String endDate = "";
        do {
            System.out.println("Enter end date (yyyy/MM/dd):");
            endDate = checkValidDate();
            if (!checkDateInput(startDate,endDate)) {
                System.out.println("\u001B[31m* Warning: End date must be after Start date.\u001B[0m");
            } else if (!isValidDateFormat(endDate)) {
                System.out.println("\u001B[31m* Warning: Please enter according to format (yyyy/MM/dd).\u001B[0m ");
            }
        } while (!checkDateInput(startDate,endDate) || !isValidDateFormat(endDate));

        String sql = "SELECT o.name, SUM(d.amount_distribution) AS total_amount, d.date_distribution, od.address_distribution\n" +
                "FROM Officer AS o \n" +
                "LEFT JOIN OfficerDistribution AS od ON o.id = od.officer_id\n" +
                "LEFT JOIN Distribution AS d ON d.id = od.distribution_id\n" +
                "WHERE (d.date_distribution BETWEEN '"+ startDate + "' AND '"+ endDate +"') \n" +
                "   OR (od.address_distribution IS NULL \n" +
                "   AND d.date_distribution IS NULL\n" +
                "   AND d.amount_distribution IS NULL)\n" +
                "GROUP BY o.name, d.date_distribution, od.address_distribution \n" +
                "ORDER BY total_amount desc;";

        ResultSet rs = st.executeQuery(sql);
        System.out.println("\u001B[33m=================================== List details of distribution officer ===================================");
        System.out.printf("| %-20s | %-20s | %-20s | %-35s |%n", "Officer name", "Amount distribution", "Date distribution",
                "Address distribution");
        System.out.printf("| %-20s | %-20s | %-20s | %-35s |\u001B[0m%n", "--------------------", "--------------------", "--------------------",
                "-----------------------------------");
        while (rs.next()) {
            String dateDistributionFormatted = (rs.getDate(3) != null) ? dateFormat.format(rs.getDate(3)) : "null";
            System.out.printf("| %-20s | %-20s | %-20s | %-35s |%n", rs.getString(1),
                    formatFloatingPoint(rs.getFloat(2)), dateDistributionFormatted, rs.getString(4));
            System.out.printf("| %-20s | %-20s | %-20s | %-35s |\u001B[0m%n", "--------------------", "--------------------", "--------------------",
                    "-----------------------------------");
        }
        System.out.println();
    }

    private static String formatFloatingPoint(float value) {
        // Format the floating-point number with desired precision
        return String.format("%.0f", value);
    }

    private static boolean checkDateInput(String startDate, String endDate) {
        boolean isValid = false;
        try {
            Date start = dateFormat.parse(startDate);
            Date end = dateFormat.parse(endDate);
            if (start.before(end)) {
                isValid = true;
            }
        } catch (ParseException e) {
            System.out.println(e);
        }
        return isValid;
    }

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
}