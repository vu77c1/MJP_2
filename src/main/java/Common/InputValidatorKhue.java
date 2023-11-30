package Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class InputValidatorKhue {
    private static Scanner scanner = new Scanner(System.in);

    public static String validateStringInput(String prompt) {
        String userInput = "";
        boolean isValid = false;

        do {
            System.out.print(prompt);
            userInput = scanner.nextLine().trim();

            if (!userInput.isEmpty()) {
                isValid = true;
            } else {
                System.out.println("Error: Input cannot be empty.");
            }
        } while (!isValid);

        return userInput;
    }

    //kiem tra input dau vao PriorityObject
    public static String validateString(String prompt) {
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
                        System.out.println("\t\t\tThe string does not contain any special characters.");
                    }
                } else {
                    System.out.println("\t\t\tDo not exceed 255 characters");
                }
            } else {
                System.out.println("\t\t\tError: Input cannot be empty.");
            }

        } while (!isValid);

        return userInput;
    }

    public static String validateStringNumber(String prompt) {
        String userInput = "";
        boolean isValid = false;

        do {
            System.out.print(prompt);
            userInput = scanner.nextLine().trim();
            if (!userInput.isEmpty()) {
                if (userInput.length() <= 255) {

                    if (!containsSpecialCharacter(userInput)) {
                        if (isNumericString(userInput)) {
                            isValid = true;
                        } else {
                            System.out.println("\t\t\tNo type from 0 to 9");
                        }

                    } else {
                        System.out.println("\t\t\tThe string does not contain any special characters.");
                    }
                } else {
                    System.out.println("\t\t\tDo not exceed 255 characters");
                }
            } else {
                System.out.println("\t\t\tError: Input cannot be empty.");
            }

        } while (!isValid);

        return userInput;
    }


    //ham kiểm tra kí tự đặc biệt
    private static boolean containsSpecialCharacter(String str) {
        // Sử dụng biểu thức chính quy để kiểm tra xem chuỗi có chứa ký tự đặc biệt không
        return str.matches(".*[!@#$%^&*()_+{}|\"<>?].*");
    }

    // Kiểm tra kí tự 0-9
    private static boolean isNumericString(String inputString) {
        return inputString.matches("\\d+");
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
                System.out.println("Error: Please enter a valid double.");
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
                if (userInput > 0) {
                    isValid = true;
                } else {
                    isValid = false;
                    System.out.println("\u001B[31mError: Please enter a valid integer.\u001B[0m");
                }
            } catch (NumberFormatException ex) {
                System.out.println("\u001B[31mError: Please enter a valid integer.\u001B[0m");
            }
        } while (!isValid);

        return userInput;
    }

    public static void closeScanner() {
        scanner.close();
    }

    public static void waitForEnter() {
        while (true) {
            System.out.println("\u001B[32mNhấn 'Enter' để quay lại menu...\u001B[0m");
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                // Nếu người dùng nhấn Enter (để trống input), thoát khỏi vòng lặp
                break;
            }
        }
    }

}
