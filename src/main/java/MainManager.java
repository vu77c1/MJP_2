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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dao.CommissionDao;
import menuCom.Menue;

public class MainManager {
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        int n;
        do {
            n = menu(sc);
            productManagement(n, sc);

        } while (!"0".equalsIgnoreCase(String.valueOf(n)));

//        sc.close();

    }

    public static int menu(Scanner sc) {
        int n;

        do {
            System.out.println("----------------MANAGEMENT----------------");
            System.out.println();
            System.out.println();
            System.out.println("            0. Exit");
            System.out.println("            1. House manage");
            System.out.println("            2. Officer manage");
            System.out.println("            3. Representative manage ");
            System.out.println("            4. Company manage");
            System.out.println("            5. Commission manage");
            System.out.println("            6. Distribution manage");
            System.out.println("            7. Donate detail manage");
            System.out.println("            8. Piority object manage");
            System.out.println("            9. Citizen manage");
            System.out.println("            10. Citizen object manage");
            System.out.println("            11. Officer distribution manage ");
            System.out.println("            12. Statistics");
            System.out.println();
            System.out.println("            What do you want to choose?");


            int m = -1;

            do {
                try {
                    System.out.print("\t\t\tPlease choose....");
                    m = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("\t\t\tInvalid input. Please enter a valid integer.");
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
                    houseManager.handleHouseManagement(houseManager, sc);
                    break;
                case 2:

                    OfficerManage.getOfficerManage();

                    break;
                case 3:
                    RepresentativeManager representativeManager = new RepresentativeManager(DBConnect.connectDatabase());

                    representativeManager.handleRepresentative(representativeManager, sc);

                    break;
                case 4:
                    CompanyManager companyManager = new CompanyManager(DBConnect.connectDatabase());
                    companyManager.handleCompany(companyManager, sc);

                    break;
                case 5:
                    Menue menue = new Menue();
                    menue.subMenu();
                    break;

                case 6:
                    handleDistributionManager();
                    break;
                case 7:

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
                                // choose=0;
                                break;
                            default:
                                System.out.println("\t\t\tInvalid choice. Please enter a valid option.");
                                break;
                        }
                    }


                    break;


                case 9:
                    CitizenManager citizenManager = new CitizenManager(DBConnect.connectDatabase());
                    System.out.println("\t\t\tCitizen Manage");
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

                    OfficerDistributionManage.getOfficerDistributionManage();
                case 12:
                    Statistics(DBConnect.connectDatabase());
                    break;
                case 0:
                    System.out.println("\t\t\tClose program.....");
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
            System.out.println("\t\t\t3. Update distribution information");
            System.out.println("\t\t\t4. Delete distribution information");
            System.out.println("\t\t\t0. Return to menu main");
            System.out.println();
            System.out.println("\t\t\tWhat do you want to choose?");


            do {
                try {
                    System.out.print("\t\t\tEnter the program number: (0-4): ");
                    choice = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException input) {
                    System.out.println("\t\t\t\u001B[31mInvalid character entered!\n\t\t\tPlease re-enter (0-4)!\u001B[0m");
                }
            }
            while (choice == -1);
            switch (choice) {
                case 0:
                    System.out.println("\t\t\tReturn to the main screen");
                    // waitForEnter();
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
                    System.out.println("\t\t\t\u001B[31mInvalid function. Please re-enter.\u001B[0m\n");

            }
        }
        while (choice != 0);
    }

    public static void Statistics(Connection connection) throws SQLException {
        int choice = -1;
        do {
            System.out.println("\t\t Statistics Menu");
            System.out.println("\t\t\t1. Displays the top 5 households with the highest donation value in one donation period \n" +
                    "\t\t\t2. List household information with the highest priority coefficient\n" +
                    "\t\t\t3. Displays household information related to priority object \n" +
                    "\t\t\t4. List the top 5 households with the most priority objects\n" +
                    "\t\t\t5. Displays the top 3 units with the highest total support value\n" +
                    "\t\t\t6. List individuals who support a charity event\n" +
                    "\t\t\t7. Displays the top 5 officers who participated in the most support rounds\n" +
                    "\t\t\t8. List the total value donated by each officer in charge\n" +
                    "\t\t\t9. Statistics on the number of households with 2 or more priority objects\n" +
                    "\t\t\t10. Statistics of 5 communes/wards with the most support (based on amount of money)\n" +
                    "\t\t\t11. List communes and wards that have not been supported\n" +
                    "\t\t\t12. List households that have not been supported in cash\n" +
                    "\t\t\t13. Display and save distribution\n" +
                    "\t\t\t14. Search for donors by date received and display their respective donation details\n" +
                    "\t\t\t15. List the support rounds and the corresponding value of each officer. Also list any officers who do not have a support round.\n" +
                    "\t\t\t16. Displays detailed information about the remaining (undistributed) amount of a donation\n" +
                    "\t\t\t17. Process Priority Decision\n" +
                    "\t\t\t18. List how many gifts the household has received from all support rounds and the total value of gifts received from those support rounds.\n" +
                    "\t\t\t0. Return to main menu");
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

                    System.out.print("\t\t\tInput the donation phase(MM/yyyy): ");
                    String donationRound = "";

                    HouseManager houseManager = new HouseManager();

                    while (true) {
                        donationRound = sc.nextLine();

                        // Regular expression pattern for MM/yyyy format
                        String regex = "^(0[1-9]|1[0-2])/[0-9]{4}$";
                        Pattern pattern = Pattern.compile(regex);
                        Matcher matcher = pattern.matcher(donationRound);

                        if (!matcher.matches()) {
                            System.out.println("Invalid format! Please enter the donation phase in MM/yyyy format.");
                        } else {
                            // Perform additional validation (e.g., negative numbers, zero)
                            String[] parts = donationRound.split("/");
                            int month = Integer.parseInt(parts[0]);
                            int year = Integer.parseInt(parts[1]);

                            if (month <= 0 || year <= 0) {
                                System.out.println("Please enter a valid month and year (greater than zero).");
                            } else {
                                // Input is valid, proceed to call displayTop5HouseholdsByDonation
                                houseManager.displayTop5HouseholdsByDonation(connection, donationRound);
                                break;
                            }
                        }
                    }
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
                    donateManage1.topValue(connection);
                    break;
                case 6:
                    DonateManage1 donateManage2 = new DonateManage1();
                    donateManage2.showList(connection);
                    break;
                case 7:

                    statsTop5Officer(con);
                    break;
                case 8:

                    statsSumAmountOfficer(con);
                    break;
                case 9:
                    ListOfStaticTwoPiority listOfStaticTwoPiority = new ListOfStaticTwoPiority();
                    listOfStaticTwoPiority.statisticsOfTwoPriorityObjects();
                    break;
                case 10:
                    System.out.println("\t\t\tTop 5 wards with the most donate");
                    top5Wards();
                    break;
                case 11:
                    System.out.println("\t\t\tStatistics of wards that have not been donated");
                    statisticsOfWards();
                    break;
                case 12:
                    System.out.println("\t\t\tList households that have not been supported in cash");
                    Menue.inputPhase();

                    break;
                case 13:
                    displayAndSaveDistribution(con);
                    break;
                case 14:
                    Menue.inputDate();
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
                    ListOfHouseHolds listOfHouseHolds = new ListOfHouseHolds();
                    listOfHouseHolds.numDonateAndSumAmoutReceived(sc);
                    break;
                case 0:
                    System.out.println("\t\t\tReturn main menuh");
                    waitForEnter();
                    MainManager.main(new String[]{});
                    break;
                default:
                    System.out.println("\t\t\t\u001B[31mInvalid function. Please select again.\u001B[0m");
                    waitForEnter();
            }
        }
        while (choice >= 0 && choice <= 9);
    }
}

