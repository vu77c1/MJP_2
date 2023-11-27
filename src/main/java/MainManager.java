import Common.DBConnect;
import Model.*;

import static Model.DonateDetailManager.*;
import static Model.DistributionManager.*;
import static Model.Processing.*;
import Common.InputValidator;
import java.sql.Connection;
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
                    HouseManager houseManager = new HouseManager(DBConnect.connectDatabase());
                    System.out.println("\t\tQuản lý hộ dân");
                    houseManager.handleHouseManagement(houseManager, sc);
                   break;
                case 2:
                    System.out.println("\t\tQuản lý cán bộ");
                    break;
                case 3:
                    System.out.println("\t\tQuản lý người đại diện");

                    break;
                case 4:
                    System.out.println("\t\tQuản lý công ty ủng hộ");

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
    public static void Statistics(Connection con){
        int choice = -1;
        do{
            System.out.println("\t\t Menu thống kê");
            System.out.println("\t\t\t1. Hiển thị top 5 hộ dân có giá trị ủng hộ nhiều nhất trong 1 đợt ủng hộ X (X nhập vào từ bàn phím)\n" +
                               "\t\t\t2. Liệt kê thông tin hộ dân có hệ số ưu tiên cao nhất\n" +
                               "\t\t\t3. Hiển thị thông tin các hộ dân liên quan đến đối tượng ưu tiên X (X nhập từ bàn phím)\n" +
                               "\t\t\t4. Liệt kê top 5 hộ có nhiều đối tượng ưu tiên nhất\n" +
                               "\t\t\t5. Hiển thị top 3 đơn vị có tổng giá trị ủng hộ nhiều nhất \n" +
                               "\t\t\t6. Liệt kê các cá nhân ủng hộ cho 1 đợt từ thiện X (X là MaDotUngHo được nhập từ bàn phím)\n" +
                               "\t\t\t7. Hiển thị top 5 cán bộ tham gia nhiều đợt ủng hộ nhất\n" +
                               "\t\t\t8. Liệt kê tổng giá trị ủng hộ được do mỗi cán bộ phụ trách X tham gia (X nhập từ bàn phím)\n" +
                               "\t\t\t0. Trở về menu chính");
            do {
                try
                {
                    System.out.print("\t\t\tEnter the program number: (0-8): ");
                    choice = Integer.parseInt(sc.nextLine());
                }
                catch (NumberFormatException input)
                {
                    System.out.println("\u001B[31mInvalid character entered!\nPlease re-enter (0-8)!\u001B[0m");
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
                    statsTop5Officer(con);
                    break;
                case 8:
                    statsSumAmountOfficer(con);
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
        while (choice >=0 && choice <=8);
    }
}
