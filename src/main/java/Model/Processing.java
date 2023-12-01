package Model;

import Common.DBConnect;
import Common.InputValidator;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Processing {
    private static final Scanner sc = new Scanner(System.in);
    public static Connection con = DBConnect.connectDatabase();
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);

    public static void waitForEnter() {
        while (true) {
            try {
                System.out.print("\t\t\t\u001B[32mPress Enter to continue...\t\u001B[0m");
                String input = sc.nextLine();
                if (input.isEmpty()) {
                    // Nếu người dùng nhấn Enter (để trống input), thoát khỏi vòng lặp
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
                    isValid = false;
                    System.out.println("\t\t\t\u001B[31mError: Please enter a valid integer.\u001B[0m");
                }
            } catch (NumberFormatException ex) {
                System.out.println("\t\t\t\u001B[31mError: Please enter a valid integer.\u001B[0m");
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
            String sql = "SELECT CASE WHEN EXISTS (SELECT * FROM " + tableName + " WHERE id = ?) THEN 1 ELSE 0 END";
            try (PreparedStatement stmt = con.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        idExist = rs.getInt(1) == 1;
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database!\u001B[0m");
        }
        return idExist;
    }

    public static boolean isFloatNumber(String number) {
        try {
            Double.parseDouble(number);
            return true;
        } catch (NumberFormatException ex) {
            //System.out.println("\u001B[31mSố tiền không hợp lệ. Vui lòng nhập lại.\u001B[0m");
        }
        return false;
    }

    //	Kiem Tra Ky Tu Dac Biet
    public static boolean isSpecialCharacter(String ch) {
        for (int i = 0; i < ch.length(); i++) {
            if (Pattern.matches("[@#$%^&*!?<>+=()_`~;.\\\"]", ch.charAt(i) + "")) {
                return true;
            }
        }
        return false;
    }

    public static int countRecords(Connection con, String tableName) {
        String sql = "SELECT COUNT(*) FROM " + tableName;

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                return rs.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database!\u001B[0m");
        }
        return -1; // Return an error indicator instead of throwing an exception
    }

    public static int inputID(Scanner sc, String tableName, String columnName) {
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
