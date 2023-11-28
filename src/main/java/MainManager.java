import Common.DBConnect;
import Model.*;

import static Model.DonateDetailManager.*;
import static Model.DistributionManager.*;
import static Model.Processing.*;
import Common.InputValidator;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class MainManager {
    private static final Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int n;
        try{
            do {
                n = menu(sc);
                productManagement(n, sc);
            } while (!"0".equalsIgnoreCase(String.valueOf(n)));
        }catch (Exception e){
            System.out.println("\t\t\t Có lỗi " + e.getMessage());
        }
    }
    public static int menu(Scanner sc) {
        int n;
        do {
            // Print the menu only once
            System.out.println("----------------MANAGEMENT----------------");
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
                    System.out.print("\t\tPlease choose....");
                    m = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("\t\t\t\u001B[31mKý tự nhập vào không hợp lệ!\n\t\t\tVui lòng nhập lại (0-11)!\u001B[0m");
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
                    HouseManager houseManager = new HouseManager(DBConnect.connectDatabase());
                    System.out.println("\t\tQuản lý hộ dân");
                    houseManager.handleHouseManagement(houseManager, sc);
                   break;
                case 2:
                    System.out.println("\t\tQuản lý cán bộ");
                    break;
                case 3:
                    RepresentativeManager representativeManager =new RepresentativeManager(DBConnect.connectDatabase());
                    System.out.println("\t\t\tQuản lý người đại diện");
                    representativeManager.handleRepresentative(representativeManager,sc);
                    break;
                case 4:
                    CompanyManager companyManager = new CompanyManager(DBConnect.connectDatabase());
                    System.out.println("\t\t\tQuản lý đơn vị ủng hộ");
                    companyManager.handleCompany(companyManager,sc);
                    break;
                case 5:
                    System.out.println("\t\tQuản lý Ủy Ban");
                    break;

                case 6:
                    handleDistributionManager();
                    break;
                case 7:
                    System.out.println("\t\tQuản lý chi tiết phân phối");
                    handleDonateDetailManager(con);
                    break;
                case 8:
                    System.out.println("\t\t\tPriority Object Manager ");
                    PriorityObjectManager po = new PriorityObjectManager();
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("\t\t\t0. Exit");
                    System.out.println("\t\t\t1. Add");
                    System.out.println("\t\t\t2. Update");
                    System.out.println("\t\t\t3. Delete");
                    System.out.println("\t\t\t4. List Priority Object");
                    int choose = -1;
                    while (choose != 0) {
                        choose = InputValidator.validateIntInput("\t\t\tEnter your choice 1-4 (0 to exit):  ");
                        switch (choose) {
                            case 1:
                                po.addPriorityObject();
                                break;
                            case 2:
                                po.updatePriorityObject();
                                break;
                            case 3:
                                po.deletePriorityObject();
                                break;
                            case 4:
                                po.displayPriorityObjects(po.getPriorityObject());
                                break;
                            case 0:
                                System.out.println("\t\t\tExiting Priority Object Management...");
                                break;
                            default:
                                System.out.println("\t\t\tInvalid choice. Please enter a valid option.");
                                break;
                        }
                    }
                    break;
                case 9:
                    CitizenManager citizenManager = new CitizenManager(DBConnect.connectDatabase());
                    System.out.println("\t\tQuản lý công dân");
                    citizenManager.handleCitizenManagement(citizenManager, sc);
                    break;
                case 10:
                    System.out.println("\t\t\tCitizen Object Manager ");
                    CitizenObjectManager co = new CitizenObjectManager();
                    System.out.println("\t\t\t0. Exit");
                    System.out.println("\t\t\t1. Add");
                    System.out.println("\t\t\t2. Update");
                    System.out.println("\t\t\t3. Delete");
                    System.out.println("\t\t\t4. List Citizen Object");
                    int num = -1;
                    while (num != 0) {
                        num = InputValidator.validateIntInput("\t\t\tEnter your choice 1-4 (0 to exit):  ");
                        switch (num) {
                            case 1:
                                co.addCitizenObject();
                                break;
                            case 2:
                                co.updateCitizenObject();
                                break;
                            case 3:
                                co.deletePriorityObject();
                                break;
                            case 4:
                                co.displayCitizenObjects(co.getCitizenObject());
                                break;
                            case 0:
                                System.out.println("\t\t\tExiting Citizen Object Management...");
                                break;
                            default:
                                System.out.println("\t\t\tInvalid choice. Please enter a valid option.");
                                break;
                        }
                    }

                    break;
                case 11:
                    System.out.println("\t\tThống kê");
                    Statistics(con);
                    break;
                case 0:
                    System.out.println("\t\tClose program.....");
                    DBConnect.disconnectDatabase();
                    System.exit(0);
                    break;
            }
        } catch (Exception e) {
            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
        }
        return n;
    }
    public static void handleDonateDetailManager(Connection con) {
        int choice = -1;
        do{
            System.out.println();
            System.out.println("\t\tQuản lý danh sách chi tiết ủng hộ");
            System.out.println("\t\t\t1. Hiển thị danh sách chi tiết ủng hộ");
            System.out.println("\t\t\t2. Thêm thông tin chi tiết ủng hộ");
            System.out.println("\t\t\t3. Sửa thông tin chi tiết ủng hộ");
            System.out.println("\t\t\t4. Xóa thông tin ủng hộ");
            System.out.println("\t\t\t0. Trở về menu chính");
            System.out.println();
            System.out.println("\t\tWhat do you want to choose?");


            do {
                try
                {
                    System.out.print("\t\t\tNhập vào số của chương trình: (0-4): ");
                    choice = Integer.parseInt(sc.nextLine());
                }
                catch (NumberFormatException input)
                {
                    System.out.println("\t\t\t\u001B[31mKý tự nhập vào không hợp lệ!\n\t\t\tVui lòng nhập lại (0-4)!\u001B[0m");
                }
            }
            while (choice == -1);
            switch (choice) {
                case 0:
                    System.out.println("\t\t\tTrở về màn hình chính");
                    waitForEnter();
                    MainManager.main(new String[]{});
                    break;
                case 1:
                    printDonateDetail(con);
                    break;
                case 2:
                    addDonateDetail(con);
                    break;
                case 3:
                    updateDonateDetail(con);
                    break;
                case 4:
                    deleteDonateDetail(con);
                    break;
                default:
                    System.out.println("\t\t\t\u001B[31mChức năng không hợp lệ. Vui lòng chọn lại.\u001B[0m");
                    waitForEnter();
            }
        }
        while (choice >=0 && choice <=4);
    }

    public static void handleDistributionManager() {
        DistributionManager distributionManager=new DistributionManager();
        int choice = -1;
        do{

            System.out.println("\t\t\tManage distribution lists");
            System.out.println("\t\t\t1. Show distribution list");
            System.out.println("\t\t\t2. Add distribution information");
            System.out.println("\t\t\t3. Edit distribution information");
            System.out.println("\t\t\t4. Delete distribution information");
            System.out.println("\t\t\t0. Return to menu main");
            System.out.println();
            System.out.println("\t\t\tWhat do you want to choose?");


            do {
                try
                {
                    System.out.print("\t\t\tEnter the program number: (0-4): ");
                    choice = Integer.parseInt(sc.nextLine());
                }
                catch (NumberFormatException input)
                {
                    System.out.println("\u001B[31mInvalid character entered!\nPlease re-enter (0-4)!\u001B[0m");
                }
            }
            while (choice == -1);
            switch (choice) {
                case 0:
                    System.out.println("\t\t\tReturn to the main screen");
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
                    System.out.println("\u001B[31mInvalid function. Please re-enter.\u001B[0m");
                    waitForEnter();
            }
        }
        while (choice >=0 && choice <=4);
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
                               "\t\t\t13. Hệ thống tự động xác định số hộ dân chưa được nhận cứu trợ của một đợt cũng như giá trị còn lại của đợt đó,\n sau đó tự động phân bổ giá trị cho từng hộ và hiển thị kết quả phân chia dưới dạng bảng,\n nếu người dùng đồng ý với kết quả tính toán thì chọn \"\"Lưu\"\" và hệ thống sẽ lưu vào bảng \"\"PHANPHOI\"\".\n(Đợt ủng hộ được nhập từ bàn phím, hiển thị danh sách hộ sẽ được nhận + giá trị được nhận tương ứng) \n" +
                               "\t\t\t14. Tìm kiếm các đơn vị ủng hộ theo ngày nhận và hiển thị chi tiết ủng hộ tương ứng của đơn vị đó \n" +
                               "\t\t\t15. Liệt kê các đợt ủng hộ và giá trị tương ứng của từng cán bộ, cán bộ nào chưa có đợt ủng hộ cũng liệt kê\n" +
                               "\t\t\t16. Hiển thị thông tin chi tiết  số tiền còn lại (chưa phân phối) của 1 đợt ủng hộ (Đợt ủng hộ X nhập từ bàn phím)\n" +
                               "\t\t\t17. + Khi nhập mới một xét duyệt ưu tiên là có trẻ em thì hệ thống sẽ hỏi là có muốn xoá 1 xét duyệt ưu tiên là phụ nữ mang thai và tăng nhân khẩu của hộ đó lên hay không.\nBấm yes hệ thống sẽ tự động cập nhật. \n" +
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
                    break;
                case 8:
                    break;
                case 9:
                    break;
                case 10:
                    break;
                case 11:
                    break;
                case 12:
                    break;
                case 13:
                    break;
                case 14:
                    break;
                case 15:
                    break;
                case 16:
                    break;
                case 17:
                    break;
                case 18:
                    break;
                case 0:
                    System.out.println("\t\t\tTrở về màn hình chính");
                    waitForEnter();
                    MainManager.main(new String[]{});
                    break;
                default:
                    System.out.println("\t\t\t\u001B[31mChức năng không hợp lệ. Vui lòng chọn lại.\u001B[0m");
                    waitForEnter();
            }
        }
        while (choice >=0 && choice <=9);
    }
}
