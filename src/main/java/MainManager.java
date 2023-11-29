import Common.DBConnect;
import Common.JdbcConfig;
import Model.*;

import static Model.Task1002OfDung.*;
import static Model.DonateDetailManager.*;
import static Model.DistributionManager.*;
import static Model.Processing.*;
import static Model.Task1003OfDung.*;
import Common.InputValidator;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
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
            System.out.println("            11. Quản lý Cán bộ phân phôi ");
            System.out.println("            12. Thống kê");
            System.out.println();
            System.out.println("            What do you want to choose?");


            int m = -1;

            do {
                try {
                    System.out.print("\t\t\tPlease choose....");
                    m = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                }
            } while (m == -1);

            n = m;


        } while (!(n >= 0 && n <= 12));
        return n;
    }

    public static int productManagement(int n, Scanner sc) {
        try {

            switch (n) {
                case 1:
                    HouseManager houseManager = new HouseManager(DBConnect.connectDatabase());
                    System.out.println("Quản lý hộ dân");
                    houseManager.handleHouseManagement(houseManager, sc);
                    break;
                case 2:
                    System.out.println("Quản lý cán bộ");
                    OfficerManage.getOfficerManage();

                    break;
                case 3:
                    RepresentativeManager representativeManager = new RepresentativeManager(DBConnect.connectDatabase());
                    System.out.println("Quản lý người đại diện");
                    representativeManager.handleRepresentative(representativeManager, sc);

                    break;
                case 4:
                    CompanyManager companyManager = new CompanyManager(DBConnect.connectDatabase());
                    System.out.println("Quản lý đơn vị ủng hộ");
                    companyManager.handleCompany(companyManager, sc);

                    break;
                case 5:
                    System.out.println("Quản lý Ủy Ban");
                    break;

                case 6:
                    handleDistributionManager();
                    break;
                case 7:
                    System.out.println("Quản lý chi tiết phân phối");
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
                    System.out.println("Quản lý công dân");
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
                    System.out.println("Quản lý cán bộ phân phôi ");
                    OfficerDistributionManage.getOfficerDistributionManage();
                case 12:
                    System.out.println("Thống kê");
                    Statistics(DBConnect.connectDatabase());
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

    public static void handleDistributionManager() {
        DistributionManager distributionManager = new DistributionManager();
        int choice = -1;
        do {

            System.out.println("\t\t\tManage distribution lists");
            System.out.println("\t\t\t1. Show distribution list");
            System.out.println("\t\t\t2. Add distribution information");
            System.out.println("\t\t\t3. Edit distribution information");
            System.out.println("\t\t\t4. Delete distribution information");
            System.out.println("\t\t\t0. Return to menu main");
            System.out.println();
            System.out.println("\t\t\tWhat do you want to choose?");


            do {
                try {
                    System.out.print("\t\t\tEnter the program number: (0-4): ");
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException input) {
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
        while (choice >= 0 && choice <= 4);
    }

    public static void Statistics(Connection connection) throws SQLException {
        int choice = -1;
        do {
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
                try {
                    System.out.print("\t\tEnter the program number: (0-18): ");
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException input) {
                    System.out.println("\u001B[31mInvalid character entered!\nPlease re-enter (0-10)!\u001B[0m");
                }
            }
            while (choice == -1);
            switch (choice) {
                case 1:
                    System.out.println("\t\t\tHi?n th? top 5 h? dân có giá tr? ?ng h? nhi?u nh?t trong 1 d?t ?ng h? ");
                    System.out.print("\t\t\tNh?p d?t ?ng h? (MM/yyyy): ");
                    String donationRound = sc.nextLine();

                    HouseManager houseManager = new HouseManager();
                    houseManager.displayTop5HouseholdsByDonation(connection, donationRound);
                    break;
                case 2:
                    CitizenManager citizenManager = new CitizenManager();
                    citizenManager.fetchCitizenInfoWithPriorityFactor();
                    break;
                case 3:
                    CitizenReport citizenReport = new CitizenReport();
                    citizenReport.printSearchByCitizenObject();
                    break;
                case 4:
                    System.out.println("\t\t\tist the top 5 households with the most priority beneficiaries");
                    CitizenReport citizenReport1 = new CitizenReport();
                    citizenReport1.printCitizenObjectTop5();
                    break;
                case 5:
                    DonateManage1 donateManage1 = new DonateManage1();
                    donateManage1.TopVlaue(connection);
                    break;
                case 6:
                    DonateManage1 donateManage2 = new DonateManage1();
                    donateManage2.showList(connection);
                    break;
                case 7:
                    System.out.println("\t\t\tHi?n th? top 5 cán b? tham gia nhi?u d?t ?ng h? nh?t");
                    statsTop5Officer(con);
                    break;
                case 8:
                    System.out.println("\t\t\t8. Li?t kê t?ng giá tr? ?ng h? du?c do m?i cán b? ph? trách X tham gia (X nh?p t? bàn phím)");
                    statsSumAmountOfficer(con);
                    break;
                case 9:
                    ListOfStaticTwoPiority listOfStaticTwoPiority =  new ListOfStaticTwoPiority();
                    listOfStaticTwoPiority.statisticsOfTwoPriorityObjects();
                    break;
                case 10:
                    System.out.println("\t\t\tTop 5 wards with the most donate");
                    top5Wards();
                    break;
                case 11:
                    System.out.println("Statistics of wards that have not been donated");
                    statisticsOfWards();
                    break;
                case 12:
                    break;
                case 13:
                    System.out.println("\t\t\tH? th?ng t? d?ng xác d?nh s? h? dân chua du?c nh?n c?u tr? c?a m?t d?t cung nhu giá tr? còn l?i c?a d?t dó,\n\t\t\tsau dó t? d?ng phân b? giá tr? cho t?ng h? và hi?n th? k?t qu? phân chia du?i d?ng b?ng,\n\t\t\tn?u ngu?i dùng d?ng ý v?i k?t qu? tính toán thì ch?n \"Luu\" và h? th?ng s? luu vào b?ng \"PHANPHOI\".\n\t\t\t(Ð?t ?ng h? du?c nh?p t? bàn phím, hi?n th? danh sách h? s? du?c nh?n + giá tr? du?c nh?n tuong ?ng) \n");
                    displayAndSaveDistribution(con);
                    break;
                case 14:
                    break;
                case 15:
                    getTask1003OfDung();
                    break;
                case 16:
                    DistributionReport distributionReport = new DistributionReport();
                    distributionReport.printDistributionReport();
                    break;
                case 17:
                    CitizenManager citizenManager1 = new CitizenManager();
                    citizenManager1.processPriorityDecision(sc, connection);
                    break;
                case 18:
                    System.out.println("\t\t\tLi?t kê xem h? dân X (X nh?p t? bàn phím) dã nh?n bao nhiêu l?n quà t? t?t c? các d?t ?ng h? và t?ng giá tr? quà nh?n du?c c?a các d?t ?ng h? dó.\n");
                    ListOfHouseHolds listOfHouseHolds = new ListOfHouseHolds();
                    listOfHouseHolds.numDonateAndSumAmoutReceived(sc);
                    break;
                case 0:
                    System.out.println("\t\t\tTr? v? màn hình chính");
                    waitForEnter();
                    MainManager.main(new String[]{});
                    break;
                default:
                    System.out.println("\t\t\t\u001B[31mCh?c nang không h?p l?. Vui lòng ch?n l?i.\u001B[0m");
                    waitForEnter();
            }
        }
        while (choice >= 0 && choice <= 9);
    }
}

