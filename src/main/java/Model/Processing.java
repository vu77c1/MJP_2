package Model;

import java.sql.*;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Processing {
    private static Scanner scanner = new Scanner(System.in);
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/uuuu").withResolverStyle(ResolverStyle.STRICT);
    public static void waitForEnter() {
        while (true) {
            System.out.println("\u001B[32mNhấn 'Enter' để quay lại menu...\u001B[0m");
            String input = scanner.nextLine();

            if (input.isEmpty()) {
                // Nếu người dùng nhấn Enter (để trống input), thoát khỏi vòng lặp
                scanner.nextLine();
                break;
            }
        }
    }
    public static boolean isIDAlreadyExists(Connection con, String id, String tableName) {
        String sql = "SELECT CASE WHEN EXISTS (SELECT * FROM " + tableName + " WHERE identity_card = ?) THEN 1 ELSE 0 END";
        boolean idExist = false;

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    idExist = rs.getInt(1) == 1;
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }

        return idExist;
    }

    //	Nhap Address Va Validate
    public static String inputAddress(Scanner sc) {
        String address;
        boolean check = true;
        do {
            System.out.print(" Input Address: ");
            address = sc.nextLine().trim().replaceAll("\\s+", " ");

            if ((!Processing.isNumber(address)) && !Processing.isSpecialCharacter(address)) {
                check = true;
            } else if (Processing.isNumber(address)) {
                System.out.println(" ERROR: Address Khong Duoc Chua Toan So  !!!");
                check = false;
            } else {
                System.out.println(" ERROR: Address Name Khong Duoc Chua Ky Tu Dac Biet  !!!");
                check = false;
            }

        } while (!check);
        return address;
    }
    //	Nhap Ho Va Ten Va Validate
    public static String inputFullName(Scanner sc) {
        String fullName;
        boolean check = true;
        do {
            System.out.print(" Input Full Name: ");
            fullName = sc.nextLine().trim().replaceAll("\\s+", " ");
            if (!fullName.isBlank()) {

                if ((!Processing.isNumber(fullName)) && !Processing.isSpecialCharacter(fullName)) {
                    check = true;
                } else if (Processing.isNumber(fullName)) {
                    System.out.println(" ERROR: Full Name Khong Duoc Chua Toan So  !!!");
                    check = false;
                } else {
                    System.out.println(" ERROR: Full Name Khong Duoc Chua Ky Tu Dac Biet  !!!");
                    check = false;
                }
            } else {
                check = false;
                System.out.println(" ERROR: Khong Duoc Bo Trong, Vui Long Nhap Lai!!!");
            }

        } while (!check);
        return fullName;
    }

    //	Nhap So Dien Thoai Va Validate
    public static String inputPhone(Scanner sc) {
        String phone;
        boolean check = true;
        do {
            System.out.print(" Input Phone:");
            phone = sc.nextLine().trim().replaceAll("\\s+", "");
            if (!phone.isBlank()) {
                if (phone.length() == 10 && phone.charAt(0) == '0' && Processing.isNumber(phone)
                        && (!Processing.isSpecialCharacter(phone))) {
                    check = true;
                } else if (phone.charAt(0) != '0') {
                    check = false;
                    System.out.println(" ERROR: So Dien Thoai Bat Dau Bang So 0!!!");
                } else if (Processing.isSpecialCharacter(phone)) {
                    check = false;
                    System.out.println(" ERROR: So Dien Thoai Chua Ky Tu Dac Biet!!!");
                } else {
                    check = false;
                    System.out.println(" ERROR: So Dien Thoai Gom 10 Chu So!!!");
                }
            } else {
                check = false;
                System.out.println(" ERROR: Khong Duoc Bo Trong!!!");
            }
        } while (!check);
        return phone;
    }

    //	Kiem Tra Kieu Du Lieu So Nguyen
    public static boolean isNumber(String number) {
        try {
            Integer.parseInt(number);
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    //	Kiem Tra Ky Tu Dac Biet
    public static boolean isSpecialCharacter(String ch) {
        for (int i = 0; i < ch.length(); i++) {
            if (Pattern.matches("[@#$%^&*!?<>+=()_`~?;.\\\"]", ch.charAt(i) + "")) {
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
            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }
        return -1; // Return an error indicator instead of throwing an exception
    }

}
