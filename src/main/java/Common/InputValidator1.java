package Common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.Scanner;

public class InputValidator1 {
    private static final Scanner scanner = new Scanner(System.in);
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    public static Date validateDateInput(String prompt) {
        Date date = null;
        boolean isValid = false;

        do {
            try {
                System.out.print(prompt);
                String dateString = scanner.nextLine().trim();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateFormat.setLenient(false);
                date = dateFormat.parse(dateString);
                isValid = true;
            } catch (ParseException ex) {
                System.out.println("\u001B[31m\t\t\tError: Please enter a valid date in the format 'dd//MM/yyyy'.\u001B[0m");
            }
        } while (!isValid);

        return date;
    }

    public static Date validateDateInputNew(String prompt) {
        Date date = null;
        boolean isValid = false;

        do {
            try {
                System.out.print(prompt);
                String dateString = scanner.nextLine().trim();

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                dateFormat.setLenient(false);

                // Ensure the year is four digits
                if (dateString.matches("\\s*\\d{2}/\\d{2}/\\d{4}\\s*")) {
                    date = dateFormat.parse(dateString);
                    isValid = true;
                } else {
                    throw new ParseException("\t\t\tInvalid year format", 0);
                }
            } catch (ParseException ex) {
                System.out.println("\t\t\t\u001B[31mError: Please enter a valid date in the format 'dd/MM/yyyy'.\u001B[0m");
            }
        } while (!isValid);

        return date;
    }
    public static LocalDate validateDateInput1(String prompt) {
        LocalDate newDonateDate;
        String newDonateDateStr;
        do {
            System.out.print(prompt);
            newDonateDateStr = scanner.nextLine();

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
                if (userInput > 0 ){
                    isValid = true;
                }else {
                    isValid = false;
                    System.out.println("\t\t\t\u001B[31mInvalid amount. Please re-enter.\u001B[0m");
                }
            } catch (NumberFormatException ex) {
                System.out.println("\t\t\t\u001B[31mInvalid amount. Please re-enter.\u001B[0m");
            }
        } while (!isValid);

        return userInput;
    }
    public static double validateFloatInput(String prompt) {
        double userInput = 0.0;
        boolean isValid = false;

        do {
            try {
                System.out.print(prompt);
                userInput = Float.parseFloat(scanner.nextLine());
                isValid = true;
            } catch (NumberFormatException ex) {
                System.out.println("\t\t\t\u001B[31mError: Please enter a valid float.\u001B[0m");
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
                if (userInput>0){
                    isValid = true;
                }else {
                    System.out.println("\t\t\tPlease enter again. Value > 0");
                }
            } catch (NumberFormatException ex) {
                System.out.println("\t\t\t\u001B[31mError: Please enter a valid integer.\u001B[0m");
            }
        } while (!isValid);

        return userInput;
    }

    public static boolean isIntMoreThan0(double a) {
        boolean isValid = false;
//        a = scanner.nextInt();
        if (a > 0 && a > 100000) {
            isValid = true;
        }else {
            System.out.println("\t\t\tThe minimum amount is 100,000 VND");
        }
        return isValid;
    }

    public static void closeScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }

}