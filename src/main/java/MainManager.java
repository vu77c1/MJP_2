
import static Model.DonateDetailManager.addDonateDetail;
import static Model.DonateDetailManager.deleteDonateDetail;
import static Model.DonateDetailManager.printDonateDetail;
import static Model.DonateDetailManager.updateDonateDetail;
import static Model.Processing.closeScanner;
import static Model.Processing.waitForEnter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

import Common.DBConnect;
import Common.InputValidator;
import Model.CitizenManager;
import Model.CitizenObjectManager;
import Model.HouseManager;
import Model.PriorityObjectManager;
import menuCom.Menue;

public class MainManager {
    private static Scanner sc = new Scanner(System.in);
//    private static Connection con = DBConnect.connectDatabase();
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
                    System.out.println("Quản lý hộ dân");
                    handleHouseManagement(houseManager, sc);
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
                    Menue menue = new Menue();
				  menue.subMenu();
                    break;
                case 6:
                    System.out.println("Quản lý phân phối");
                    break;
                case 7:
                    System.out.println("Quản lý chi tiết phân phối");
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
                    System.out.println("Thống kê");
                    break;
                case 0:
                    System.out.println("Close program.....");
                    DBConnect.disconnectDatabase();
                    closeScanner();
                    System.exit(0);
                    break;
            }
        } catch (Exception e) {
            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
        }
        return n;
    }
    private static void handleHouseManagement(HouseManager houseManager, Scanner scanner) {
        System.out.println("Quản lý hộ dân - Chọn chức năng:");
        System.out.println("1. Thêm hộ dân");
        System.out.println("2. Xóa hộ dân");
        System.out.println("3. Sửa thông tin hộ dân");
        System.out.println("0. Quay lại menu chính");

        int choice = -1;

        do {
            try {
                System.out.print("Vui lòng chọn: ");
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        // Thêm hộ dân
                        System.out.print("Nhập commission ID: ");
                        int commissionId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nhập priority object ID: ");
                        int priorityObjectId = Integer.parseInt(scanner.nextLine());
                        houseManager.addHouse(commissionId, priorityObjectId);
                        break;
                    case 2:
                        // Xóa hộ dân
                        System.out.print("Nhập ID hộ dân cần xóa: ");
                        int houseIdToDelete = Integer.parseInt(scanner.nextLine());
                        houseManager.deleteHouse(houseIdToDelete);
                        System.out.println("\u001B[32mHộ dân có ID " + houseIdToDelete + " đã được xóa thành công.\u001B[0m");
                        break;
                    case 3:
                        // Sửa thông tin hộ dân
                        System.out.print("Nhập ID hộ dân cần sửa: ");
                        int houseIdToUpdate = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nhập commission ID mới: ");
                        int newCommissionId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nhập priority object ID mới: ");
                        int newPriorityObjectId = Integer.parseInt(scanner.nextLine());
                        houseManager.updateHouse(houseIdToUpdate, newCommissionId, newPriorityObjectId);
                        break;
                    case 0:
                        System.out.println("Trở về màn hình chính");
                        waitForEnter();
                        main(new String[]{});
                        break;
                    default:
                        System.out.println("\u001B[31mLựa chọn không hợp lệ.\u001B[0m");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số.");
            } catch (SQLException e) {
                System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
            }
        } while (choice != 0);
    }
    private static void handleCitizenManagement(CitizenManager citizenManager, Scanner scanner) {
        System.out.println("Quản lý công dân - Chọn chức năng:");
        System.out.println("1. Thêm công dân");
        System.out.println("2. Xóa công dân");
        System.out.println("3. Sửa thông tin công dân");
        System.out.println("0. Quay lại menu chính");

        int choice = -1;

        do {
            try {
                System.out.print("Vui lòng chọn: ");
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        // Thêm công dân
                        citizenManager.addCitizenFromConsoleInput(scanner);
                        break;
                    case 2:
                        // Xóa công dân
                        System.out.print("Nhập ID công dân cần xóa: ");
                        int citizenIdToDelete = Integer.parseInt(scanner.nextLine());
                        citizenManager.deleteCitizen(citizenIdToDelete);
                        System.out.println("\u001B[32mCông dân có ID " + citizenIdToDelete + " đã được xóa thành công.\u001B[0m");
                        break;
                    case 3:
                        // Sửa thông tin công dân
                        citizenManager.updateCitizenFromConsoleInput(scanner);
                        break;
                    case 0:
                        System.out.println("Trở về màn hình chính");
                        waitForEnter();
                        main(new String[]{});
                        break;
                    default:
                        System.out.println("\u001B[31mLựa chọn không hợp lệ.\u001B[0m");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số.");
            } catch (SQLException e) {
                System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
            }
        } while (choice != 0);
    }
    public static void handleDonateDetailManager(Connection con) {
        int choice = -1;
        do{
            System.out.println();
            System.out.println("Quản lý danh sách chi tiết ủng hộ");
            System.out.println("1. Hiển thị danh sách chi tiết ủng hộ");
            System.out.println("2. Thêm thông tin chi tiết ủng hộ");
            System.out.println("3. Sửa thông tin chi tiết ủng hộ");
            System.out.println("4. Xóa thông tin ủng hộ");
            System.out.println("0. Trở về menu chính");
            System.out.println();
            System.out.println("What do you want to choose?");


            do {
                try
                {
                    System.out.print("Nhập vào số của chương trình: (0-4): ");
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
                    System.out.println("Trở về màn hình chính");
                    waitForEnter();
                    main(new String[]{});
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
                    System.out.println("\u001B[31mChức năng không hợp lệ. Vui lòng chọn lại.\u001B[0m");
                    waitForEnter();
            }
        }
        while (choice >=0 && choice <=4);
    }
}
