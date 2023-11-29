
import Common.DBConnect;
import Model.OfficerDistributionManage;
import Model.OfficerManage;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.Scanner;


public class MainManager {
    private static Scanner sc = new Scanner(System.in);
    private static Connection con = DBConnect.connectDatabase();
    public static void main(String[] args) {
        int n;
        do {
            n = menu(sc);
            productManagement(n, sc);

        } while (!"0".equalsIgnoreCase(String.valueOf(n)));

        sc.close();

    }

    public static int menu(Scanner sc) {
        int n;
        do {
            System.out.println("----------------MANAGEMENT----------------");
            System.out.println();
            System.out.println();
            System.out.println("            0. Exit");
            System.out.println("            1. Quản lý hộ dân");
            System.out.println("            2. Quản lý cán bộ");
            System.out.println("            3. Quản lý người đại diện");
            System.out.println("            4. Quản lý công ty ủng hộ");
            System.out.println("            5. Quản lý Ủy Ban");
            System.out.println("            6. Quản lý phân phối");
            System.out.println("            7. Quản lý cán bộ phân phối");
            System.out.println("            8. Quản lý đối tượng ưu tiên");
            System.out.println("            9. Quản lý công dân");
            System.out.println("            10. Quản lý đối tượng công dân");
            System.out.println("            11. Thống kê");
            System.out.println();
            System.out.println("            What do you want to choose?");


            int m = -1;

            do {
                try {
                    System.out.print("Please choose....");
                    m = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mKý tự nhập vào không hợp lệ!\nVui lòng nhập lại (0-11)!\u001B[0m");
                }
            } while (m == -1);

            n = m;
        } while (!(n >= 0 && n <= 11));
        return n;
    }

    public static int productManagement(int n, Scanner sc) {
        try {
            switch (n) {
                case 1:
                   break;
                case 2:
                    System.out.println("Quản lý cán bộ");
                    handleOfficerManage();
                    break;
                case 3:
                    System.out.println("Quản lý người đại diện");
                    break;
                case 4:
                    System.out.println("Quản lý công ty ủng hộ");
                    break;
                case 5:
                    System.out.println("Quản lý Ủy Ban");
                    break;
                case 6:
                    System.out.println("Quản lý phân phối");
                    break;
                case 7:
                    System.out.println("Quản lý cán bộ phân phối");
                    handleOfficerDistribution();
                    break;
                case 8:
                    break;
                case 9:
                    System.out.println("Quản lý công dân");
                    break;
                case 10:
                    System.out.println("\t\t\tCitizen Object Manager ");
                    break;
                case 11:
                    System.out.println("Thống kê");
                    break;
                case 0:
                    System.out.println("Close program.....");
                    DBConnect.disconnectDatabase();
                    sc.close();
                    System.exit(0);
                    break;
            }
        } catch (Exception e) {
            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
        }
        return n;
    }

    private static void handleOfficerManage() throws SQLException {
        displayMenuOfficerManage();
        int choice = 0;
        do {
            System.out.println("Enter your choice...");
            choice = OfficerManage.checkInputInt();

            switch (choice) {
                case 1:
                    OfficerManage.addNewOfficer();
                    displayMenuOfficerManage();
                    break;
                case 2:
                    OfficerManage.updateOfficerTable();
                    displayMenuOfficerManage();
                    break;
                case 3:
                    OfficerManage.deleteOfficer();
                    displayMenuOfficerManage();
                    break;
                case 4:
                    OfficerManage.displayOfficerTable();
                    displayMenuOfficerManage();
                    break;
                case 0:
                    System.out.println("\u001B[32m* Notification: You returned main menu\u001B[0m");
                    break;
                default:
                    System.out.println("\u001B[31m* Warning: Input is invalid. Please try again!\u001B[0m");
                    break;
            }
        } while (choice != 0);
    }

    private static void displayMenuOfficerManage() {
        System.out.println(String.format("| %-25s |", "= Officer Manage Menu ="));
        System.out.println(String.format("| %-25s |", "-------------------------"));
        System.out.println(String.format("| %-25s |", "0. Return main menu"));
        System.out.println(String.format("| %-25s |", "1. Add new Officer"));
        System.out.println(String.format("| %-25s |", "2. Update Officer table"));
        System.out.println(String.format("| %-25s |", "3. Delete Officer by ID"));
        System.out.println(String.format("| %-25s |", "4. Display Officer table"));
        System.out.println(String.format("| %-25s |", "========================="));
    }

    private static void handleOfficerDistribution() throws SQLException {
        displayMenuOfficerDistributionManage();
        int choice = 0;
        do {
            System.out.println("Enter your choice...");
            choice = OfficerDistributionManage.checkInputInt();

            switch (choice) {
                case 1:
                    OfficerDistributionManage.addNewData();
                    displayMenuOfficerDistributionManage();
                    break;
                case 2:
                    OfficerDistributionManage.updateOfficerDistributionTable();
                    displayMenuOfficerDistributionManage();
                    break;
                case 3:
                    OfficerDistributionManage.deleteData();
                    displayMenuOfficerDistributionManage();
                    break;
                case 4:
                    OfficerDistributionManage.displayOfficerDistributionTable();
                    displayMenuOfficerDistributionManage();
                    break;
                case 0:
                    System.out.println("\u001B[32m* Notification: You returned main menu\u001B[0m");
                    break;
                default:
                    System.out.println("\u001B[31m* Warning: Input is invalid. Please try again!\u001B[0m");
                    break;
            }
        } while (choice != 0);
    }

    private static void displayMenuOfficerDistributionManage() {
        System.out.println(String.format("| %-40s |", "=== Officer Distribution Manage Menu ==="));
        System.out.println(String.format("| %-40s |", "----------------------------------------"));
        System.out.println(String.format("| %-40s |", "0. Return main menu"));
        System.out.println(String.format("| %-40s |", "1. Add new data"));
        System.out.println(String.format("| %-40s |", "2. Update Officer Distribution table"));
        System.out.println(String.format("| %-40s |", "3. Delete data by ID"));
        System.out.println(String.format("| %-40s |", "4. Display Officer Distribution table"));
        System.out.println(String.format("| %-40s |", "========================================"));
    }
}
