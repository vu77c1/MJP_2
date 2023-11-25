import Common.DBConnect;
import Model.Company;
import Model.CompanyManager;
import Model.Representative;
import Model.RepresentativeManager;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MainManager {
    private static Scanner sc = new Scanner(System.in);
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
            System.out.println("            7. Quản lý chi tiết phân phối");
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
                    System.out.println("Invalid input. Please enter a valid integer.");
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
                    System.out.println("Quản lý hộ dân");
                    break;
                case 2:
                    System.out.println("Quản lý cán bộ");
                    break;
                case 3:
                    RepresentativeManager representativeManager =new RepresentativeManager(DBConnect.connectDatabase());
                    System.out.println("Quản lý người đại diện");
                    handleRepresentative(representativeManager,sc);

                    break;
                case 4:
                    CompanyManager companyManager = new CompanyManager(DBConnect.connectDatabase());
                    System.out.println("Quản lý đơn vị ủng hộ");
                    handleCompany(companyManager,sc);
                    break;
                case 5:
                    System.out.println("Quản lý Ủy Ban");
                    break;
                case 6:
                    System.out.println("Quản lý phân phối");

                    break;
                case 7:
                    System.out.println("Quản lý chi tiết phân phối");

                    break;
                case 8:
                    System.out.println("Quản lý đối tượng ưu tiên");


                    break;
                case 9:
                    System.out.println("Quản lý công dân");

                    break;
                case 10:
                    System.out.println("Quản lý đối tượng công dân");

                    break;
                case 11:
                    System.out.println("Thống kê");

                    break;
                case 0:
                    System.out.println("Close program.....");
                    break;

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return n;

    }

    private static void handleRepresentative(RepresentativeManager representativeManager, Scanner scanner) {
        System.out.println("Quản lý người đại diện - Chọn chức năng:");
        System.out.println("1. Thêm người đại diện");
        System.out.println("2. Xóa người đại diện");
        System.out.println("3. Sửa thông tin người đại diện");
        System.out.println("0. Quay lại menu chính");

        int choice = -1;

        do {
            try {
                System.out.print("Vui lòng chọn: ");
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        // Thêm người đại diện
                        representativeManager.addRepresentativeInput(scanner);
                        break;
                    case 2:
                        // Xóa người đại diện
                        System.out.print("Nhập ID người đại diện cần xóa: ");
                        int representativeIdToDelete = Integer.parseInt(scanner.nextLine());
                        representativeManager.deleteRepresentative(representativeIdToDelete);
                        System.out.println("Người đại diện có ID " + representativeIdToDelete + " đã được xóa thành công.");
                        break;
                    case 3:
                        // Sửa thông tin người đại diện
                        representativeManager.updateRepresentativeFromConsoleInput(scanner);
                        break;
                    case 0:
                        // Quay lại menu chính
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số.");
            } catch (SQLException e) {
                System.out.println("Lỗi SQL: " + e.getMessage());
            }
        } while (choice != 0);
    }

    private static void handleCompany(CompanyManager companyManager, Scanner scanner) {
        System.out.println("Quản lý đơn vị ủng hộ - Chọn chức năng:");
        System.out.println("1. Thêm đơn vị ủng hộ");
        System.out.println("2. Xóa đơn vị ủng hộ");
        System.out.println("3. Sửa thông tin đơn vị ủng hộ");
        System.out.println("0. Quay lại menu chính");

        int choice = -1;

        do {
            try {
                System.out.print("Vui lòng chọn: ");
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        // Thêm đơn vị ủng hộ
                        companyManager.addCompanyInput(scanner);
                        break;
                    case 2:
                        // Xóa đơn vị ủng hộ
                        System.out.print("Nhập ID đơn vị ủng hộ cần xóa: ");
                        int representativeIdToDelete = Integer.parseInt(scanner.nextLine());
                        companyManager.deleteCompany(representativeIdToDelete);
                        System.out.println("Đơn vị ủng hộ có ID " + representativeIdToDelete + " đã được xóa thành công.");
                        break;
                    case 3:
                        // Sửa đơn vị ủng hộ
                        companyManager.updateCompanyFromConsoleInput(scanner);
                        break;
                    case 0:
                        // Quay lại menu chính
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số.");
            } catch (SQLException e) {
                System.out.println("Lỗi SQL: " + e.getMessage());
            }
        } while (choice != 0);
    }

}
