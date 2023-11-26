package Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class InputValidator {
    private static Scanner scanner = new Scanner(System.in);

    public static Date validateDateInput(String prompt) {
        Date date = null;
        boolean isValid = false;

        do {
            try {
                System.out.print(prompt);
                String dateString = scanner.nextLine().trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/uuuu");
                dateFormat.setLenient(false);
                date = dateFormat.parse(dateString);
                isValid = true;
            } catch (ParseException ex) {
                System.out.println("\u001B[31m\t\t\tError: Please enter a valid date in the format 'dd/MM/yyyy'.\u001B[0m");
            }
        } while (!isValid);

        return date;
    }

    public static String validateStringInput(String prompt) {
        String userInput = "";
        boolean isValid = false;

        do {
            System.out.print(prompt);
            userInput = scanner.nextLine().trim();

            if (!userInput.isEmpty()) {
                isValid = true;
            } else {
                System.out.println("\u001B[31mError: Input cannot be empty.\u001B[0m");
            }
        } while (!isValid);

        return userInput;
    }
//kiem tra input dau vao PriorityObject
    public static String validateStringPriorityObject(String prompt) {
        String userInput = "";
        boolean isValid = false;

        do {
            System.out.print(prompt);
            userInput = scanner.nextLine().trim();
            if (!userInput.isEmpty()) {
                if (userInput.length() <= 255) {

                    if (!containsSpecialCharacter(userInput)) {
                        isValid = true;
                    } else {
                        System.out.println("\u001B[31m\t\t\tThe string does not contain any special characters.\u001B[0m");
                    }
                } else {
                    System.out.println("\u001B[31m\t\t\tDo not exceed 255 characters\u001B[0m");
                }
            } else {
                System.out.println("\u001B[31m\t\t\tError: Input cannot be empty.\u001B[0m");
            }

        } while (!isValid);

        return userInput;
    }
    public static String validateStringCitizenObject(String prompt) {
        String userInput = "";
        boolean isValid = false;

        do {
            System.out.print(prompt);
            userInput = scanner.nextLine().trim();
            if (!userInput.isEmpty()) {
                if (userInput.length() <= 255) {

                    if (!containsSpecialCharacter(userInput)) {
                        isValid = true;
                    } else {
                        System.out.println("\u001B[31m\t\t\tThe string does not contain any special characters.\u001B[0m");
                    }
                } else {
                    System.out.println("\u001B[31m\t\t\tDo not exceed 255 characters\u001B[0m");
                }
            } else {
                System.out.println("\u001B[31m\t\t\tError: Input cannot be empty.\u001B[0m");
            }

        } while (!isValid);

        return userInput;
    }

    //ham kiểm tra kí tự đặc biệt
    private static boolean containsSpecialCharacter(String str) {
        // Sử dụng biểu thức chính quy để kiểm tra xem chuỗi có chứa ký tự đặc biệt không
        return str.matches(".*[!@#$%^&*()_+{}|\"<>?].*");
    }

    public static double validateDoubleInput(String prompt) {
        double userInput = 0.0;
        boolean isValid = false;

        do {
            try {
                System.out.print(prompt);
                userInput = Double.parseDouble(scanner.nextLine());
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.println("\u001B[31mSố tiền không hợp lệ. Vui lòng nhập lại.\u001B[0m");
            }
        } while (!isValid);

        return userInput;
    }

    public static int validateIntInput(String prompt) {
        int userInput = 0;
        boolean isValid = false;

        do {
            try {
                System.out.print(prompt);
                userInput = Integer.parseInt(scanner.nextLine());
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.println("\u001B[31mError: Please enter a valid integer.\u001B[0m");
            }
        } while (!isValid);

        return userInput;
    }

    public static void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }

}
