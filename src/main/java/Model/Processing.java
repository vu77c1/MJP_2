package Model;

import Common.DBConnect;
import Common.InputValidator;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Processing {
    private static final Scanner sc = new Scanner(System.in);
    public static Connection con = DBConnect.connectDatabase();
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static void waitForEnter() {
        while (true) {
            try {
                System.out.print("\t\t\t\u001B[32mPress Enter to continue...\t\u001B[0m");
                String input = sc.nextLine().trim();
                if (input.isEmpty()) {
                    // If the trimmed input is empty, exit the loop
                    break;
                }
            } catch (NoSuchElementException e) {
                // Handle the exception (e.g., print an error message)
                System.out.print("Error reading input: " + e.getMessage());
            }
        }
    }

    public static int validateIntInput(String prompt) {
        int userInput = 0;
        boolean isValid = false;

        do {
            try {
                System.out.print(prompt);
                userInput = Integer.parseInt(sc.nextLine());
                if (userInput >= 0) {
                    isValid = true;
                } else {
                    System.out.println("\t\t\t\u001B[31mError: Please enter a valid integer.\u001B[0m");
                }
            } catch (NumberFormatException ex) {
                System.out.println("\t\t\t\u001B[31mError: Please enter a valid integer.\u001B[0m");
            }
        } while (!isValid);

        return userInput;
    }
    public static double validateDoubleInput(String prompt) {
        double userInput = 0.0;
        boolean isValid = false;

        do {
            try {
                System.out.print(prompt);
                userInput = Double.parseDouble(sc.nextLine().trim());
                if (userInput >= 1000.0 ){
                    isValid = true;
                }else {
                    System.out.println("\t\t\t\u001B[31mInvalid amount. Please re-enter.\u001B[0m");
                }
            } catch (NumberFormatException ex) {
                System.out.println("\t\t\t\u001B[31mInvalid amount. Please re-enter.\u001B[0m");
            }
        } while (!isValid);

        return userInput;
    }
    public static boolean isIDAlreadyExists(Connection con, int id, String tableName) {
        boolean idExist = false;
        try {
            // Validate the tableName to ensure it comes from a trusted source
            // You may use a whitelist or other validation methods depending on your requirements

            // Assuming tableName is a valid identifier
            String sql = "SELECT COUNT(*) FROM ? WHERE id = ?";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setString(1, tableName);
                stmt.setInt(2, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        // Check if the count is greater than 0
                        idExist = rs.getInt(1) > 0;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + "\u001B[0m");
        }
        return idExist;
    }

    public static int countRecords(Connection con, String tableName) {
        // Validate tableName to prevent SQL injection
        if (!isValidTableName(con,tableName)) {
            System.out.println("\t\t\t\u001B[31mInvalid table name: " + tableName + "\u001B[0m");
            return -1;
        }

        try {
            String sql = "SELECT COUNT(*) FROM " + tableName;
            try (PreparedStatement stmt = con.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        } catch (SQLException e) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + "\u001B[0m");
            return -1;
        }
    }

    // Validate table name to prevent SQL injection
    private static boolean isValidTableName(@NotNull Connection con, String tableName) {
        try {
            DatabaseMetaData metaData = con.getMetaData();
            try (ResultSet tables = metaData.getTables(null, null, tableName, null)) {
                return tables.next(); // If the result set has at least one row, the table exists
            }
        } catch (SQLException e) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + "\u001B[0m");
            return false;
        }
    }

    public static int inputID(String tableName, String columnName) {
        int ID;
        do {
            ID = InputValidator.validateIntInput("\t\t\tInput ID  (Refer to the menus \"Manage\"): ");
            //System.out.println();

            if (checkIDExistence(ID, tableName, columnName)) {
                System.out.println("\t\t\t\u001B[31mThe ID does not exist in the table " + tableName + ". Please input again.\u001B[0m");
            }
        } while (checkIDExistence(ID, tableName, columnName));
        return ID;
    }

    private static boolean checkIDExistence(int ID, String tableName, String columnName) {
        String query = "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
            preparedStatement.setInt(1, ID);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return !resultSet.next(); // Returns true if the ID exists in the table
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database!\u001B[0m");
            // Handle the exception according to your needs
            return true; // Return false in case of an exception
        }
    }

    public static LocalDate validateDateInput(String prompt) {
        LocalDate newDonateDate;
        String newDonateDateStr;
        do {
            System.out.print(prompt);
            newDonateDateStr = sc.nextLine();

            try {
                newDonateDate = LocalDate.parse(newDonateDateStr, dateFormat);
                if (newDonateDate.isAfter(LocalDate.now())) {
                    System.out.println("\t\t\t\u001B[31mThe date of receiving support must be earlier than the current date!\n\t\t\tYou can't time travel, right?!\u001B[0m");
                    newDonateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                }
            } catch (DateTimeParseException ex) {
                System.out.println("\t\t\t\u001B[31m Invalid date entered:  \"" + newDonateDateStr + "\"\u001B[0m");
                newDonateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
            }
        } while (newDonateDate == null);

        return newDonateDate;
    }

    public static String validateMonthYearInput(String message) {
        Scanner sc = new Scanner(System.in);
        String userInput;
        boolean isValid;

        do {
            System.out.print(message);
            userInput = sc.nextLine();

            // Validate the input format (MM/yyyy)
            if (userInput.matches("\\d{2}/\\d{4}")) {
                String[] parts = userInput.split("/");
                int month = Integer.parseInt(parts[0]);
                int year = Integer.parseInt(parts[1]);

                // Validate month (1 to 12) and year (positive)
                isValid = month >= 1 && month <= 12 && year > 0;

                if (!isValid) {
                    System.out.println("\u001B[31mMonth must be between 1 and 12 and year must be greater than 0.\u001B[0m");
                }
            } else {
                System.out.println("\u001B[31mInvalid format. Please re-enter in the format MM/yyyy.\u001B[0m");
                isValid = false;
            }
        } while (!isValid);

        return userInput;
    }
}
