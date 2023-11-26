

import java.sql.Connection;
import java.util.Scanner;
import Common.*;
import Model.*;
import static Model.DistributionManager.*;
import static Model.Processing.*;
public class MainManager {


    private  static Connection con=JDBCQuery1.connectDatabase();
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
            System.out.println("            1. Manage HoseHold");
            System.out.println("            2. Manage Officer");
            System.out.println("            3. Manage Representative");
            System.out.println("            4. Company management is donate");
            System.out.println("            5. Commission Management");
            System.out.println("            6. Distribution management");
            System.out.println("            7. Manage distribution details");
            System.out.println("            8. Manage priority objects");
            System.out.println("            9. Citizen management");
            System.out.println("            10. Managing citizen objects");
            System.out.println("            11. Statistical");
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
                    System.out.println("Manage HoseHold");
                    break;
                case 2:
                    System.out.println("Manage Officer");
                    break;
                case 3:
                    System.out.println("Manage Representative");

                    break;
                case 4:
                    System.out.println("Company management is donate");

                    break;
                case 5:
                    System.out.println("Commission Management");
                    break;
                case 6:
                    handleDistributionManager();
                    break;
                case 7:
                    System.out.println("Manage distribution details");

                    break;
                case 8:
                    System.out.println("Manage priority objects");

                    break;
                case 9:
                    System.out.println("Citizen management");

                    break;
                case 10:
                    System.out.println("Managing citizen objects");

                    break;
                case 11:
                    System.out.println("Statistical");
                    break;

                case 0:
                    System.out.println("Close program.....");
                    break;


            }
        } catch (Exception e) {
            System.out.println("\u001B[31mThere was an error during connection Database: " + e.getMessage() + ".\u001B[0m");
        }
        return n;
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
}