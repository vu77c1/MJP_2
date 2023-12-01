package Model;

import Common.InputValidator;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;
import java.util.Scanner;

import static Model.DistributionManager.printDistribution;
import static Model.Processing.*;

public class DonateDetailManager {
    private static final Scanner sc = new Scanner(System.in);

    public static void handleDonateDetailManager(Connection con) {
        int choice = -1;
        do {
            System.out.println();
            System.out.println("\t\tManage your donation details list");
            System.out.println("\t\t\t1. Displays a detailed list of donations");
            System.out.println("\t\t\t2. Add Donation Details");
            System.out.println("\t\t\t3. Edit Donation details");
            System.out.println("\t\t\t4. Delete Donation details");
            System.out.println("\t\t\t0. Return to main menu");
            System.out.println();
            System.out.println("\t\tWhat do you want to choose?");
            do {
                try {
                    //System.out.print("\t\t\tEnter function number: (0-4): ");
                    choice = Processing.validateIntInput("\t\t\tEnter function number: (0-4): ");
                } catch (NumberFormatException input) {
                    System.out.println("\t\t\t\u001B[31mThe entered character is not valid!\n\t\t\tPlease input again: (0-4)!\u001B[0m");
                    waitForEnter();
                }
            } while (choice == -1);
            switch (choice) {
                case 0:
                    System.out.println("\t\t\tReturn to main menu");
                    waitForEnter();
                    try {
                        Class<?> mainManagerClass = Class.forName("MainManager");
                        Method mainMethod = mainManagerClass.getMethod("main", String[].class);
                        mainMethod.invoke(null, (Object) new String[]{});
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                        System.out.println("\t\t\t\u001B[31mAn error occurred during processing!\u001B[0m");
                    }
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
                    System.out.println("\t\t\t\u001B[31mFunction is invalid. Please select again.\u001B[0m");
                    waitForEnter();
                    choice = -1;
            }
        } while (choice != 0);
    }

    public static void printDonateDetail(Connection con) {
        try {
            if (countRecords(con, "DonateDetail") > 0) {
                System.out.println();
                System.out.println("=========================================================== DONATION LIST ===========================================================");
                System.out.println("._______.____________________.____________________.____________________.________________________.______________________.____________.");
                System.out.println("│   ID  │   Amount of money  │   Donation Date    │    Commune/Ward    │      Representative    │      Company name    │   Officer  │");
                System.out.println("│_______│____________________│____________________│____________________│________________________│______________________│____________│");
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("""
                        SELECT distinct
                            dbo.DonateDetail.id,
                            amount,
                            donate_date,
                            C.precint_name,
                            representative_name,
                            company_name,
                            O.name
                        FROM
                            DonateDetail
                                LEFT JOIN dbo.Commission C ON DonateDetail.commission_id = C.id
                                LEFT JOIN dbo.Representative R ON R.id = DonateDetail.representative_id
                                LEFT JOIN dbo.Company C2 ON C2.id = R.company_id
                                LEFT JOIN dbo.Distribution D on C.id = D.commission_id
                                LEFT JOIN dbo.OfficerDistribution on D.id = OfficerDistribution.distribution_id
                                LEFT JOIN dbo.Officer O ON O.id = OfficerDistribution.officer_id""");
                while (resultSet.next()) {
                    String ID = resultSet.getString("id");
                    String amount = String.format("%.0f", resultSet.getDouble("amount"));
                    LocalDate donate_date = resultSet.getDate("donate_date").toLocalDate();
                    String precint_name = resultSet.getString("precint_name");
                    String representative_name = resultSet.getString("representative_name");
                    String company_name;
                    if (resultSet.getString("company_name") == null){
                        company_name = "(Individual)";
                    } else {
                        company_name = resultSet.getString("company_name");
                    }
                    String name = resultSet.getString("name");
                    System.out.printf("│ %-5S │ %-18s │ %-18s │ %-18s │ %-22s │ %-20s │ %-10s │\n", ID, amount, dateFormat.format(donate_date), precint_name, representative_name, company_name, name);
                    System.out.println("│_______│____________________│____________________│____________________│________________________│______________________│____________│");
                }
                System.out.println("===========================================================   LIST ENDED  ===========================================================");
            } else {
                System.out.println("\t\t\t\u001B[31mThere have been no donations yet.\u001B[31");
            }
            waitForEnter();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database!\u001B[0m");
        }
    }

    public static void printDonateDetailByID(Connection con, int ID) {
        String selectQuery = """
                SELECT distinct
                    dbo.DonateDetail.id,
                    amount,
                    donate_date,
                    C.precint_name,
                    representative_name,
                    company_name,
                    O.name
                FROM
                    DonateDetail
                        LEFT JOIN dbo.Commission C ON DonateDetail.commission_id = C.id
                        LEFT JOIN dbo.Representative R ON R.id = DonateDetail.representative_id
                        LEFT JOIN dbo.Company C2 ON C2.id = R.company_id
                        LEFT JOIN dbo.Distribution D on C.id = D.commission_id
                        Left Join dbo.OfficerDistribution on D.id = OfficerDistribution.distribution_id
                        LEFT JOIN dbo.Officer O ON O.id = OfficerDistribution.officer_id
                WHERE DonateDetail.id = ?""";
        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery)) {
            selectStatement.setInt(1, ID);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (countRecords(con, "DonateDetail") > 0) {
                    while (resultSet.next()) {
                        System.out.println();
                        System.out.println("=========================================================== DONATION LIST ===========================================================");
                        System.out.println("._______.____________________.____________________.____________________.________________________.______________________.____________.");
                        System.out.println("│   ID  │   Amount of money  │   Donation Date    │    Commune/Ward    │      Representative    │      Company name    │   Officer  │");
                        System.out.println("│_______│____________________│____________________│____________________│________________________│______________________│____________│");
                        String amount = String.format("%.0f", resultSet.getDouble("amount"));
                        LocalDate donate_date = resultSet.getDate("donate_date").toLocalDate();
                        String precint_name = resultSet.getString("precint_name");
                        String representative_name = resultSet.getString("representative_name");
                        String company_name ;
                        if (resultSet.getString("company_name") == null){
                            company_name = "(Individual)";
                        } else {
                            company_name = resultSet.getString("company_name");
                        }
                        String name = resultSet.getString("name");
                        System.out.printf("│ %-5S │ %-18s │ %-18s │ %-18s │ %-22s │ %-20s │ %-10s │\n", ID, amount, dateFormat.format(donate_date), precint_name, representative_name, company_name, name);
                        System.out.println("│_______│____________________│____________________│____________________│________________________│______________________│____________│");
                    }
                    System.out.println("===========================================================   LIST ENDED  ===========================================================");
                } else {
                    System.out.println("\t\t\t\u001B[31mThere have been no donations yet.\u001B[31");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database!\u001B[0m");
        }
    }

    public static void addDonateDetail(Connection con) {
        try {
            System.out.println("\t\t\t=== Add Donationer Information ===");
            System.out.print("\t\t\t\u001B[31mYou are about to add a record for a sponsor's donation.\n\t\t\tPlease refer to other tables in the database to avoid mistakes!\n\t\t\t(For related tables please refer to the menu \"Representative Management\" và \"Commission Management\")\n  \u001B[0m");
            waitForEnter();
            String insertQuery = "INSERT INTO DonateDetail (representative_id, commission_id, donate_date, amount) VALUES (?, ?, ?, ?)";
            System.out.println("\t\t\tID of the representative supporter (individual or corporate representative) (Refer to menu\"Representative Management\")");
            int representativeId = Processing.inputID(sc, "Representative", "id");
            System.out.println("\t\t\tID of the commune/ward receiving donation (Refer to the menu \"Commission Management\")");
            int commissionId = Processing.inputID(sc, "Commission", "id");
            //sc.nextLine(); // Consume the newline character
            LocalDate donateDate = Processing.validateDateInput("\t\t\tEnter the date of receipt of support (in dd/MM/yyyy format): ");
            double amount = 0;
            String input;

            do {
                System.out.print("\t\t\tEnter the donation amount: ");
                input = sc.next();

                if (!isFloatNumber(input)) {
                    System.out.println("\t\t\t\u001B[31mInvalid amount. Please re-enter.\u001B[0m");
                } else {
                    amount = Double.parseDouble(input);
                }
            } while (!isFloatNumber(input));
            PreparedStatement insertStatement = con.prepareStatement(insertQuery);
            insertStatement.setInt(1, representativeId);
            insertStatement.setInt(2, commissionId);
            insertStatement.setDate(3, Date.valueOf(donateDate));
            insertStatement.setDouble(4, amount);
            // Execute the query
            insertStatement.executeUpdate();
            System.out.println("\t\t\t\u001B[32mDonationer has been added to the database.\u001B[0m");
            waitForEnter();
        } catch (SQLException e) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
        }
    }

    //	Xoa Thong Tin Khach Hang
    public static void deleteDonateDetail(Connection con) {
        System.out.println();
        System.out.print("\t\t\t\u001B[31mYou are about to delete a record for a sponsor's donation.\n\t\t\tPlease refer to other tables in the database to avoid mistakes!\n\t\t\t(For related tables please refer to the menu \"Representative Management\" và \"Commission Management\")\n\u001B[0m");
        waitForEnter();
        printDonateDetail(con);
        System.out.println("\t\t\tPlease input the ID of the record you want to delete: ");
        int identity = inputID(sc, "DonateDetail", "id");// Tạm thời xóa record theo ID
        printDonateDetailByID(con, identity);
        System.out.println();
        PreparedStatement pstmt = null;
        String tableName = "DonateDetail";
        try {
//			Kiem Tra Su Ton Tai Cua record, Neu Ton Tai Thi Tien Hanh Xoa
            if (Processing.isIDAlreadyExists(con, identity, tableName)) {
                String choice;
                System.out.print("\t\t\tAre you sure you want to delete (Y/N)? ");
                do {
                    choice = sc.nextLine().trim().toLowerCase();  // Convert to lowercase

                    if ("y".equalsIgnoreCase(choice)) {
                        // Delete the record
                        String sqlDelete = "DELETE FROM DonateDetail WHERE ID=?";
                        pstmt = con.prepareStatement(sqlDelete);
                        pstmt.setInt(1, identity);
                        if (pstmt.executeUpdate() != 0) {
                            System.out.println("\t\t\t\u001B[32mDeleted successfully!!!\u001B[0m");
                            waitForEnter();
                        }
                    } else if ("n".equalsIgnoreCase(choice)) {
                        System.out.println("\t\t\t\u001B[31mINFO: No results were deleted!!!\u001B[0m");
                        waitForEnter();
                    } else {
                        System.out.print("\t\t\tInvalid choice. Please enter (Y/N): ");
                    }
                } while (!"y".equalsIgnoreCase(choice) && !"n".equalsIgnoreCase(choice));
            } else {
                System.out.println("\t\t\t\u001B[31mThe result does not exist in the Database!!!\u001B[0m");
                waitForEnter();
            }
        } catch (Exception e) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
            } catch (SQLException se2) {
                System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + se2.getMessage() + ".\u001B[0m");
            }
        }
    }

    //	Chuc Nang Cap Nhat Thong Tin Khach Hang
    public static void updateDonateDetail(Connection con) {

        System.out.println();
        System.out.print("\t\t\t\u001B[31mYou are about to edit a record for a sponsor's donation.\n\t\t\tPlease refer to other tables in the database to avoid mistakes!\n\t\t\t(For related tables please refer to the menu \"Representative Management\" và \" Commission Management\")\n\u001B[0m");
        waitForEnter();
        printDonateDetail(con);
        System.out.println("\t\t\tPlease input the ID of the record you want to update: ");
        int identity = inputID(sc, "DonateDetail", "id");// Tạm thời xóa record theo ID
        printDonateDetailByID(con, identity);
        waitForEnter();
        System.out.println();
        PreparedStatement pstmt = null;
        //Kiem Tra id Da Co Trong Bang Khang Hang Hay Chua? Neu Co Thi Tien Hanh Update
        if (Processing.isIDAlreadyExists(con, identity, "DonateDetail")) {
            int choice;
            //Hien Thi Menu Update
            do {
                System.out.println("\t\t\t+-----------------------------------+");
                System.out.println("\t\t\t│ Which item do you want to update? │");
                System.out.println("\t\t\t+-----------------------------------+");
                System.out.printf("\t\t\t│ %-34s|\n\t\t\t│ %-34s│ \n\t\t\t│ %-34s│ \n\t\t\t│ %-34s│\n\t\t\t│ %-34s│ \n\t\t\t│ %-34s│\n", "1. Amount of money", "2. Donation Date", "3. Commission", "4. Representative", "5. All", "0. Return to the Management menu");
                System.out.println("\t\t\t+-----------------------------------+");
                System.out.println("\t\t\tFrom Update Menu, Your Choice: ");
                choice = InputValidator.validateIntInput("\t\t\tPlease Choice: ");
                if (choice < 0 || choice > 5) {
                    System.out.println("\t\t\t\u001B[31mInvalid choice! Please enter a number between 0 and 4.\u001B[0m");
                    choice = -1; // Reset choice to -1 to repeat the loop
                }
            } while (choice == -1);

            switch (choice) {
                case 0:
                    System.out.println("\t\t\tBack to menu");
                    handleDonateDetailManager(con);
                    break;
                //Update Số tiền
                case 1:
                    double newAmount = InputValidator.validateDoubleInput("\t\t\tEnter amount: ");
                    String sql1 = "UPDATE DonateDetail SET amount =? WHERE id =?";
                    try {
                        pstmt = con.prepareStatement(sql1);
                        pstmt.setDouble(1, newAmount);
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println("\t\t\t\u001B[32mUpdated the amount successfully!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null) pstmt.close();
                        } catch (SQLException se2) {
                            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + se2.getMessage() + ".\u001B[0m");
                        }
                        do {
                            System.out.print("\t\t\tDo you want to continue editing (Y/N)? ");
                            String c = sc.next();
                            sc.nextLine(); // Consume the newline character

                            if ("Y".equalsIgnoreCase(c) || "y".equalsIgnoreCase(c)) {
                                updateDonateDetail(con);
                            } else if ("N".equalsIgnoreCase(c) || "n".equalsIgnoreCase(c)) {
                                break;
                            } else {
                                System.out.println("\t\t\t\u001B[31mThe input selection is incorrect, please re-enter!!!\u001B[0m");
                            }
                        } while (true);
                        waitForEnter();
                    }
                    break;
                //Update ngày ủng hộ
                case 2:
                    String sql2 = "UPDATE DonateDetail SET donate_date =? WHERE id =?";
                    LocalDate newDonateDate = Processing.validateDateInput("\t\t\tEnter a new receipt date (in dd/MM/yyyy format): ");
                    try {
                        pstmt = con.prepareStatement(sql2);
                        pstmt.setDate(1, Date.valueOf(newDonateDate));
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println("\t\t\t\u001B[32mUpdated Donatation date successful!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null) pstmt.close();
                        } catch (SQLException se2) {
                            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + se2.getMessage() + ".\u001B[0m");
                        }
                        do {
                            System.out.print("\t\t\tDo you want to continue editing (Y/N)? ");
                            String c = sc.next();
                            sc.nextLine(); // Consume the newline character

                            if ("Y".equalsIgnoreCase(c)) {
                                updateDonateDetail(con);
                            } else if ("N".equalsIgnoreCase(c)) {
                                break;
                            } else {
                                System.out.println("\t\t\t\u001B[31mThe input selection is incorrect, please re-enter!!!\u001B[0m");
                            }
                        } while (true);
                        waitForEnter();
                    }
                    break;
                //Update ID Ủy ban
                case 3:
                    System.out.println("\t\t\tEnter the Commission ID: ");
                    int newCommissionId = inputID(sc, "Commission", "id");
                    String sql3 = "UPDATE DonateDetail SET commission_id =? WHERE id =?";
                    try {
                        pstmt = con.prepareStatement(sql3);
                        pstmt.setInt(1, newCommissionId);
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println("\t\t\t\u001B[32mUpdated Commission successful!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null) pstmt.close();
                        } catch (SQLException se2) {
                            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + se2.getMessage() + ".\u001B[0m");
                        }
                        do {
                            System.out.print("\t\t\tDo you want to continue editing (Y/N)? ");
                            String c = sc.next();
                            sc.nextLine(); // Consume the newline character

                            if ("Y".equalsIgnoreCase(c)) {
                                updateDonateDetail(con);
                            } else if ("N".equalsIgnoreCase(c)) {
                                break;
                            } else {
                                System.out.println("\t\t\t\u001B[31mThe input selection is incorrect, please re-enter!!!\u001B[0m");
                            }
                        } while (true);
                        waitForEnter();
                    }
                    break;
                //Update ID nguoi dai dien
                case 4:
                    System.out.println("\t\t\tEnter the representative's ID: ");
                    int newRepresentativeId = inputID(sc, "Representative", "id");
                    String sql4 = "UPDATE DonateDetail SET representative_id =? WHERE id =?";
                    try {
                        pstmt = con.prepareStatement(sql4);
                        pstmt.setInt(1, newRepresentativeId);
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println("\t\t\t\u001B[32mSuccessfully updated the representative!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null) pstmt.close();
                        } catch (SQLException se2) {
                            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + se2.getMessage() + ".\u001B[0m");
                        }
                        do {
                            System.out.print("\t\t\tDo you want to continue editing (Y/N)? ");
                            String c = sc.next();
                            sc.nextLine(); // Consume the newline character

                            if ("Y".equalsIgnoreCase(c)) {
                                updateDonateDetail(con);
                            } else if ("N".equalsIgnoreCase(c)) {
                                break;
                            } else {
                                System.out.println("\t\t\t\u001B[31mThe input selection is incorrect, please re-enter!!!\u001B[0m");
                            }
                        } while (true);
                        waitForEnter();
                    }
                    break;
                //Update toàn bộ theo ID
                case 5:
                    System.out.print("\t\t\tEnter amount: ");
                    double newAmountAll = sc.nextDouble();
                    sc.nextLine(); // Consume the newline character
                    LocalDate newDonateDateAll = Processing.validateDateInput("\t\t\tEnter a new receipt date (in dd/MM/yyyy format): ");
                    System.out.println("\t\t\tEnter the Commission ID: ");
                    int newCommissionIdAll = inputID(sc, "Commission", "id");
                    System.out.println("\t\t\tEnter the representative's ID: ");
                    int newRepresentativeIdAll = inputID(sc, "Representative", "id");
                    String combinedSql = "UPDATE DonateDetail SET amount =?, donate_date =?, commission_id =?, representative_id =? WHERE id =?";

                    try {
                        pstmt = con.prepareStatement(combinedSql);
                        pstmt.setDouble(1, newAmountAll);
                        pstmt.setDate(2, Date.valueOf(newDonateDateAll));
                        pstmt.setInt(3, newCommissionIdAll);
                        pstmt.setInt(4, newRepresentativeIdAll);
                        pstmt.setInt(5, identity);
                        pstmt.executeUpdate();
                        System.out.println("\t\t\t\u001B[32mUpdated by ID " + identity + " successfully!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null) pstmt.close();
                        } catch (SQLException se2) {
                            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + se2.getMessage() + ".\u001B[0m");
                        }
                    }
                    do {
                        System.out.print("\t\t\tDo you want to continue editing (Y/N)? ");
                        String c = sc.next();
                        sc.nextLine(); // Consume the newline character

                        if ("Y".equalsIgnoreCase(c)) {
                            updateDonateDetail(con);
                        } else if ("N".equalsIgnoreCase(c)) {
                            break;
                        } else {
                            System.out.println("\t\t\t\u001B[31mThe input selection is incorrect, please re-enter!!!\u001B[0m");
                        }
                    } while (true);
                    waitForEnter();
                    break;
            }
        } else {
            System.out.println("\t\t\t\u001B[31mNo donation infor found!!!\u001B[0m");
            waitForEnter();
        }
    }

    public static void statsTop5Officer(Connection con) {
        try {
            if (countRecords(con, "DonateDetail") > 0) {
                System.out.println();
                System.out.println("================================== DONATION LIST ==================================");
                System.out.println("._______.________________________.________________________.________________________.");
                System.out.println("│   ID  │         Officer        │       Commission       │  Participations Times  │");
                System.out.println("│_______│________________________│________________________│________________________│");
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("""
                        SELECT DISTINCT TOP 5
                            Officer.id,
                            Officer.name,
                            Commission.precint_name,
                            COUNT(OfficerDistribution.officer_id) as Stats
                        FROM
                            Officer
                                LEFT JOIN
                            dbo.Commission ON Officer.commision_id = Commission.id
                                LEFT JOIN
                            dbo.Distribution ON Commission.id = Distribution.commission_id
                                LEFT JOIN
                            dbo.OfficerDistribution ON Distribution.id = OfficerDistribution.distribution_id
                        GROUP BY
                            Officer.id, Officer.name, Commission.precint_name
                        ORDER BY
                            Stats DESC;
                         """);
                while (resultSet.next()) {
                    int ID = resultSet.getInt("id");
                    String companyName = resultSet.getString("name");
                    String precintName = resultSet.getString("precint_name");
                    int Stats = resultSet.getInt("Stats");
                    System.out.printf("│ %-5S │ %-22s │ %-22s │ %-22s │\n", ID, companyName, precintName, Stats);
                    System.out.println("│_______│________________________│________________________│________________________│");
                }
                System.out.println("==================================  LIST ENDED   ==================================");
            } else {
                System.out.println("\t\t\t\u001B[31mThere have been no donations yet.\u001B[31");
            }
            waitForEnter();
        } catch (SQLException e) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
        }
    }

    public static void statsSumAmountOfficer(Connection con) {
        String selectQuery = """
                SELECT DISTINCT
                    Officer.id,
                    Officer.name,
                    Commission.precint_name,
                    SUM(Distribution.amount_distribution) as Stats
                FROM
                    Officer
                        LEFT JOIN
                            dbo.Commission ON Officer.commision_id = Commission.id
                        LEFT JOIN
                    dbo.Distribution ON Commission.id = Distribution.commission_id
                        LEFT JOIN
                    dbo.OfficerDistribution ON Distribution.id = OfficerDistribution.distribution_id
                WHERE Officer.id = ?
                GROUP BY
                    Officer.id, Officer.name, Commission.precint_name
                ORDER BY
                    Stats DESC""";
        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery)) {
            System.out.println();
            OfficerManager.displayOfficerTable();
            waitForEnter();
            System.out.println("\t\t\tEnter the officer's ID: ");
            int id = Processing.inputID(sc, "Officer", "id");
            selectStatement.setInt(1, id);
            int i = 1;
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (countRecords(con, "OfficerDistribution") > 0) {
                    System.out.println();
                    System.out.println("================================== DONATION LIST ==================================");
                    System.out.println("._______.________________________.________________________.________________________.");
                    System.out.println("│   ID  │         Officer        │       Commission       │  Participations Times  │");
                    System.out.println("│_______│________________________│________________________│________________________│");
                    while (resultSet.next()) {
                        String companyName = resultSet.getString("name");
                        String precintName = resultSet.getString("precint_name");
                        int Stats = resultSet.getInt("Stats");
                        System.out.printf("│ %-5S │ %-22s │ %-22s │ %-22s │\n", i, companyName, precintName, Stats);
                        System.out.println("│_______│________________________│________________________│________________________│");
                        i++;
                    }
                    System.out.println("==================================   LIST ENDED   =================================");
                } else {
                    System.out.println("\t\t\t\u001B[31mThere have been no donations yet\u001B[0m");
                }
                waitForEnter();
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
        }
    }

    public static void statsCountSumAmount(Connection con) {
        System.out.println("\t\t\tNhập ID của hộ dân (X): ");
        int householdId = Processing.inputID(sc, "House", "id");
        String SQL_QUERY_STATS_BY_HOUSEHOLD = """
                    SELECT
                        House.id,
                        Citizen.name,
                        Citizen.address,
                        CO.type_name_object,
                        PriorityObject.object_type,
                        COUNT(Distribution.amount_distribution) AS SL,
                        SUM(Distribution.amount_distribution) AS TS
                    FROM
                        House
                        LEFT JOIN Citizen ON House.id = Citizen.house_id
                        LEFT JOIN dbo.CitizenObject CO ON CO.id = Citizen.citizen_object_id
                        LEFT JOIN PriorityObject ON House.priority_object_id = PriorityObject.id
                        LEFT JOIN Distribution ON House.id = Distribution.household_id
                    WHERE
                        House.id = ? AND Citizen.is_household_lord = 1
                    GROUP BY
                        House.id, Citizen.name, Citizen.address, CO.type_name_object, PriorityObject.object_type
                    ORDER BY
                        TS DESC;
                """;
        try (PreparedStatement preparedStatement = con.prepareStatement(SQL_QUERY_STATS_BY_HOUSEHOLD)) {
            preparedStatement.setInt(1, householdId);

            if (countRecords(con, "DonateDetail") > 0) {
                System.out.println();
                System.out.println("================================================================ LIST OF HOUSEHOLDS =========================================================");
                System.out.println("._______.________________________.________________________.________________________.___________________________.____________._______________.");
                System.out.println("│   ID  │  Household Lord's Name │         Address        │     Citizen Objects    │     Household Objects     │    COUNT   │      SUM      │");
                System.out.println("│_______│________________________│________________________│________________________│___________________________│____________│_______________│");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int houseID = resultSet.getInt("id");
                        String citizenName = resultSet.getString("name");
                        String citizenAddress = resultSet.getString("address");
                        String typeNameObject = resultSet.getString("type_name_object");
                        String objectType = resultSet.getString("object_type");
                        int SL = resultSet.getInt("SL"); // Corrected column name
                        String TS = String.format("%.0f", resultSet.getDouble("TS"));
                        System.out.printf("│ %-5S │ %-22s │ %-22s │ %-22s │ %-25s │ %-10s │ %-13s │%n", houseID, citizenName, citizenAddress, typeNameObject, objectType, SL, TS);
                        System.out.println("│_______│________________________│________________________│________________________│___________________________│____________│_______________│");
                    }
                    System.out.println("============================================================     LIST ENDED     ============================================================");
                }
            } else {
                System.out.println("\t\t\t\u001B[31mThere have been no donations yet.\u001B[31");
            }
            waitForEnter();
        } catch (SQLException e) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
        }
    }


    public static void displayAndSaveDistribution(Connection connection) {
        //LocalDate userInputDate = Processing.validateDateInput("Nhập vào đợt ủng hộ: ");
        String userInputMonthYear = Processing.validateMonthYearInput("\t\t\tEnter the donation phase (MM/yyyy): ");

        // Assuming userInputMonthYear is a String in the format "MM/yyyy"
        // Split the input to extract month and year separately
        String[] parts = userInputMonthYear.split("/");
        int inputMonth = Integer.parseInt(parts[0]);
        int inputYear = Integer.parseInt(parts[1]);
        // Thực hiện truy vấn SQL để lấy dữ liệu
        String sqlQuery = """
                WITH UnaidedHouseholds AS (
                     SELECT
                         h.id AS household_id,
                         d.id AS distribution_id,
                         dd.amount - COALESCE(SUM(d.amount_distribution), 0) AS remaining_amount
                     FROM
                         House h
                             INNER JOIN
                         Distribution d ON h.id = d.household_id
                             LEFT JOIN
                         DonateDetail dd ON d.id = dd.id
                     WHERE
                         d.date_distribution BETWEEN DATEADD(MONTH, DATEDIFF(MONTH, 0, ?), 0) AND DATEADD(MONTH, DATEDIFF(MONTH, 0, ?) + 1, -1)
                     GROUP BY
                         h.id, d.id, dd.amount
                 ),
                      TotalRemainingAmount AS (
                          SELECT
                              distribution_id,
                              SUM(remaining_amount) AS total_remaining_amount
                          FROM
                              UnaidedHouseholds
                          GROUP BY
                              distribution_id
                      )
                 SELECT
                     c.id AS commission_id,
                     uhh.household_id,
                     IIF(tra.total_remaining_amount = 0, 0, uhh.remaining_amount / NULLIF(tra.total_remaining_amount, 0) * d.amount_distribution) AS allocated_amount,
                     c.precint_name as precint_name,
                     citizen.name as household_lord_name
                 FROM
                     UnaidedHouseholds uhh
                         JOIN
                     TotalRemainingAmount tra ON uhh.distribution_id = tra.distribution_id
                         JOIN
                     Distribution d ON uhh.distribution_id = d.id
                         JOIN
                     Commission c ON d.commission_id = c.id
                         JOIN
                     House h ON uhh.household_id = h.id
                         JOIN
                     Citizen citizen ON h.id = citizen.house_id AND citizen.is_household_lord = 1
                     where h.priority_object_id != 5 or citizen.citizen_object_id != 6
                 ORDER BY allocated_amount DESC;
                        """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {

            LocalDate userInputDate = LocalDate.of(inputYear, inputMonth, 1);
            LocalDate thirtyDaysAgo = userInputDate.plusDays(30);

            // Convert LocalDate to java.sql.Date
            Date userInputSqlDate = Date.valueOf(userInputDate);
            Date thirtyDaysAgoSqlDate = Date.valueOf(thirtyDaysAgo);

            // Use in preparedStatement
            preparedStatement.setDate(1, userInputSqlDate);
            preparedStatement.setDate(2, thirtyDaysAgoSqlDate);

            // Thực hiện truy vấn và hiển thị kết quả
            ResultSet resultSet = preparedStatement.executeQuery();
            if (countRecords(con, "Distribution") > 0) {
                System.out.println();
                System.out.println("================================================== LIST OF HOUSEHOLDS ==================================================");
                System.out.println("._______._____________________________________.___________._____________________________________.______________________.");
                System.out.println("│  ID   │        Household Lord's Name        │Commission │             Commune/Ward            │    Amount of money   │");
                System.out.println("│_______│_____________________________________│___________│_____________________________________│______________________│");
                while (resultSet.next()) {
                    int householdId = resultSet.getInt("household_id");
                    int commissionId = resultSet.getInt("commission_id");
                    String householdLordName = resultSet.getString("household_lord_name");
                    String precintName = resultSet.getString("precint_name");
                    String allocatedAmount = String.format("%.0f", resultSet.getDouble("allocated_amount"));
                    System.out.printf("│ %-5S │ %-35s │ %-9s │ %-35s │ %-20s │%n", householdId, householdLordName, commissionId, precintName, allocatedAmount);
                    System.out.println("│_______│_____________________________________│___________│_____________________________________│______________________│");
                }
                System.out.println("==================================================     LIST ENDED     ==================================================");
            } else {
                System.out.println("\t\t\t\u001B[31m No data available yet!!!\u001B[0m");
            }
            waitForEnter();
            // Hỏi người dùng có muốn thực hiện INSERT không
            System.out.print("\t\t\t\u001B[32mDo you want to do an INSERT statement? (Y/N): \u001B[0m");
            String input = sc.nextLine();

            // Nếu người dùng nhập Y, thực hiện lệnh INSERT
            if ("Y".equalsIgnoreCase(input)) {
                insertIntoDistribution(connection, preparedStatement);
                System.out.println("\t\t\t\u001B[32mInsert successfully!!!\u001B[0m");
                System.out.print("\t\t\tWould you like to review the 'PHANPHOI' table (Y/N): ");
                String print = sc.nextLine();
                if ("Y".equalsIgnoreCase(print)) {
                    printDistribution(con);
                    waitForEnter();
                }
            } else {
                System.out.println("\t\t\t\u001B[31mThere have been no donations yet.\u001B[31");
            }
            waitForEnter();
        } catch (SQLException e) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
        }
    }

    private static void insertIntoDistribution(Connection connection, PreparedStatement preparedStatement) {
        // Thực hiện lệnh INSERT
        String insertQuery = "INSERT INTO dbo.Distribution (commission_id, household_id, amount_distribution, date_distribution) " + "VALUES (?, ?, ?, GETDATE())";

        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int commissionId = resultSet.getInt("commission_id");
                int householdId = resultSet.getInt("household_id");
                double allocatedAmount = resultSet.getDouble("allocated_amount");

                // Thiết lập giá trị cho lệnh INSERT
                insertStatement.setInt(1, commissionId);
                insertStatement.setInt(2, householdId);
                insertStatement.setDouble(3, allocatedAmount);

                // Thực hiện lệnh INSERT
                int affectedRows = insertStatement.executeUpdate();
                if (affectedRows == 0) {
                    System.out.println("\u001B[31mAn error occurred while executing the INSERT statement.\u001B[0m");
                    waitForEnter();
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
            waitForEnter();
        }
    }
}
