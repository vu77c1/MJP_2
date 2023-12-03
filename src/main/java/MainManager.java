
import Common.DBConnect;
import Model.OfficerDistributionManage;
import Model.OfficerManage;
import Model.ReportLocalDonateInformation;
import Model.DetailedReportsToDistributionOfficer;

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

    public static void Statistics(Connection con) throws SQLException {
        int choice = -1;
        do{
            System.out.println("\t\t Menu thống kê");
            System.out.println("\t\t\t1. Hiển thị top 5 hộ dân có giá trị ủng hộ nhiều nhất trong 1 đợt ủng hộ X (X nhập vào từ bàn phím)\n" +
                    "\t\t\t2. Liệt kê thông tin hộ dân có hệ số ưu tiên cao nhất\n" +
                    "\t\t\t3. Hiển thị thông tin các hộ dân liên quan đến đối tượng ưu tiên X (X nhập từ bàn phím)\n" +
                    "\t\t\t4. Liệt kê top 5 hộ có nhiều đối tượng ưu tiên nhất\n" +
                    "\t\t\t5. Hiển thị top 3 đơn vị có tổng giá trị ủng hộ nhiều nhất\n" +
                    "\t\t\t6. Liệt kê các cá nhân ủng hộ cho 1 đợt từ thiện X (X là MaDotUngHo được nhập từ bàn phím)\n" +
                    "\t\t\t7. Hiển thị top 5 cán bộ tham gia nhiều đợt ủng hộ nhất\n" +
                    "\t\t\t8. Liệt kê tổng giá trị ủng hộ được do mỗi cán bộ phụ trách X tham gia (X nhập từ bàn phím)\n" +
                    "\t\t\t9. Thống kê số hộ khẩu có từ 2 đối tượng ưu tiên trở lên\n" +
                    "\t\t\t10. Thống kê 5 xã/ phường được ủng hộ nhiều nhất (dựa vào số tiền)\n" +
                    "\t\t\t11. Liệt kê các xã phường chưa được ủng hộ\n" +
                    "\t\t\t12. Liệt Kê hộ dân chưa được ủng hộ bằng tiền mặt\n" +
                    "\t\t\t13. Hệ thống tự động xác định số hộ dân chưa được nhận cứu trợ của một đợt cũng như giá trị còn lại của đợt đó,\n\t\t\tsau đó tự động phân bổ giá trị cho từng hộ và hiển thị kết quả phân chia dưới dạng bảng,\n\t\t\tnếu người dùng đồng ý với kết quả tính toán thì chọn \"\"Lưu\"\" và hệ thống sẽ lưu vào bảng \"\"PHANPHOI\"\".\n\t\t\t(Đợt ủng hộ được nhập từ bàn phím, hiển thị danh sách hộ sẽ được nhận + giá trị được nhận tương ứng) \n" +
                    "\t\t\t14. Tìm kiếm các đơn vị ủng hộ theo ngày nhận và hiển thị chi tiết ủng hộ tương ứng của đơn vị đó \n" +
                    "\t\t\t15. Liệt kê các đợt ủng hộ và giá trị tương ứng của từng cán bộ, cán bộ nào chưa có đợt ủng hộ cũng liệt kê\n" +
                    "\t\t\t16. Hiển thị thông tin chi tiết  số tiền còn lại (chưa phân phối) của 1 đợt ủng hộ (Đợt ủng hộ X nhập từ bàn phím)\n" +
                    "\t\t\t17. + Khi nhập mới một xét duyệt ưu tiên là có trẻ em thì hệ thống sẽ hỏi là có muốn xoá 1 xét duyệt ưu tiên là phụ nữ mang thai và tăng nhân khẩu của hộ đó lên hay không.\n\t\t\tBấm yes hệ thống sẽ tự động cập nhật. \n" +
                    "\t\t\t+ Ngược lại khi xoá một xét duyệt ưu tiên là phụ nữ mang thai thì hệ thống tự động hỏi là có thêm 1 ưu tiên trẻ em và thêm nhân khẩu hay không nếu có sẽ tự động cập nhật.\n" +
                    "\t\t\t18. Liệt kê xem hộ dân X (X nhập từ bàn phím) đã nhận bao nhiêu lần quà từ tất cả các đợt ủng hộ và tổng giá trị quà nhận được của các đợt ủng hộ đó.\n" +
                    "\t\t\t0. Trở về menu chính");
            do {
                try
                {
                    System.out.print("\t\tEnter the program number: (0-10): ");
                    choice = Integer.parseInt(sc.nextLine());
                }
                catch (NumberFormatException input)
                {
                    System.out.println("\u001B[31mInvalid character entered!\nPlease re-enter (0-10)!\u001B[0m");
                }
            }
            while (choice == -1);
            switch (choice){
                case 1:
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    break;
                case 5:
                    break;
                case 6:
                    break;
                case 7:
//                    System.out.println("\t\t\tHiển thị top 5 cán bộ tham gia nhiều đợt ủng hộ nhất");
//                    statsTop5Officer(con);
                    break;
                case 8:
//                    System.out.println("\t\t\t8. Liệt kê tổng giá trị ủng hộ được do mỗi cán bộ phụ trách X tham gia (X nhập từ bàn phím)");
//                    statsSumAmountOfficer(con);
                    break;
                case 9:
                    break;
                case 10:
                    ReportLocalDonateInformation.top5Wards();
                    break;
                case 11:
                    ReportLocalDonateInformation.statisticsOfWards();
                    break;
                case 12:
                    break;
                case 13:
//                    System.out.println("\t\t\tHệ thống tự động xác định số hộ dân chưa được nhận cứu trợ của một đợt cũng như giá trị còn lại của đợt đó,\n\t\t\tsau đó tự động phân bổ giá trị cho từng hộ và hiển thị kết quả phân chia dưới dạng bảng,\n\t\t\tnếu người dùng đồng ý với kết quả tính toán thì chọn \"Lưu\" và hệ thống sẽ lưu vào bảng \"PHANPHOI\".\n\t\t\t(Đợt ủng hộ được nhập từ bàn phím, hiển thị danh sách hộ sẽ được nhận + giá trị được nhận tương ứng) \n");
//                    displayAndSaveDistribution(con);
                    break;
                case 14:
                    break;
                case 15:
                    DetailedReportsToDistributionOfficer.listDetailsDistributionOfficer();
                    break;
                case 16:
                    break;
                case 17:
                    break;
                case 18:
//                    System.out.println("\t\t\tLiệt kê xem hộ dân X (X nhập từ bàn phím) đã nhận bao nhiêu lần quà từ tất cả các đợt ủng hộ và tổng giá trị quà nhận được của các đợt ủng hộ đó.\n");
//                    statsCountSumAmount(con);
                    break;
                case 0:
//                    System.out.println("\t\t\tTrở về màn hình chính");
//                    waitForEnter();
//                    MainManager.main(new String[]{});
                    break;
                default:
                    System.out.println("\t\t\t\u001B[31mChức năng không hợp lệ. Vui lòng chọn lại.\u001B[0m");
//                    waitForEnter();
            }
        }
        while (choice >=0 && choice <=9);
    }
}
