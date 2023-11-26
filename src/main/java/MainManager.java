

import java.sql.Connection;
import java.util.Scanner;
import Common.*;
import Model.*;
import static Model.DistributionManager.*;
import static Model.Processing.*;
public class MainManager {


    private  static Connection con=DBConnect.connectDatabase();
    private static final Scanner sc = new Scanner(System.in);
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

    public static int productManagement(int n, Scanner scanner) {

        try {

            switch (n) {
                case 1:
                    System.out.println("Quản lý hộ dân");
                    break;
                case 2:
                    System.out.println("Quản lý cán bộ");
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
                    handleDistributionManager();
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
            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
        }
        return n;
    }





    public static void handleDistributionManager() {
        DistributionManager distributionManager=new DistributionManager();
        int choice = -1;
        do{

            System.out.println("\t\t\tQuản lý danh sách phân phối");
            System.out.println("\t\t\t1. Hiển thị danh sách phân phối");
            System.out.println("\t\t\t2. Thêm thông tin phân phối");
            System.out.println("\t\t\t3. Sửa thông tin phân phối");
            System.out.println("\t\t\t4. Xóa thông tin phân phối");
            System.out.println("\t\t\t0. Trở về menu chính");
            System.out.println();
            System.out.println("\t\t\tWhat do you want to choose?");


            do {
                try
                {
                    System.out.print("\t\t\tNhập vào số của chương trình: (0-4): ");
                    choice = Integer.parseInt(sc.nextLine());
                }
                catch (NumberFormatException input)
                {
                    System.out.println("\u001B[31mKý tự nhập vào không hợp lệ!\nVui lòng nhập lại (0-4)!\u001B[0m");
                }
            }
            while (choice == -1);
            switch (choice) {
                case 0:
                    System.out.println("\t\t\tTrở về màn hình chính");
                    waitForEnter();
                    main(new String[]{});
                    break;
                case 1:
                    printDistribution(con);
                    break;
                case 2:

                    distributionManager.addDistribution();
                    break;
                case 3:
                    distributionManager.updateDistribution();
                    break;
                case 4:
                    distributionManager.deleteDistribution();
                    break;
                default:
                    System.out.println("\u001B[31mChức năng không hợp lệ. Vui lòng chọn lại.\u001B[0m");
                    waitForEnter();
            }
        }
        while (choice >=0 && choice <=4);
    }
}