package Model;

import Common.InputValidator;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
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
                    choice = Processing.validateIntInput("\t\t\tEnter function number: (0-4): ");
                } catch (NumberFormatException input) {
                    System.out.println("\t\t\t\u001B[31mThe entered character is not valid!\n\t\t\tPlease input again: (0-4)!\u001B[0m");
                    waitForEnter();
                }
            }
            while (choice == -1);
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
        }
        while (choice != 0);
    }

    public static @NotNull Map<Integer, DonateDetail> getDonateDetail(Connection con) {
        Map<Integer, DonateDetail> indexObjectMap = new LinkedHashMap<>();
        try {
            String selectQuery = """
                    SELECT distinct
                        dbo.DonateDetail.id,
                        amount,
                        donate_date,
                        C.precint_name,
                        representative_name,
                        COALESCE(company_name, '(Individual)') AS company_name,
                        O.name,
                        DonateDetail.commission_id,
                        DonateDetail.representative_id
                    FROM
                        DonateDetail
                            LEFT JOIN dbo.Commission C ON DonateDetail.commission_id = C.id
                            LEFT JOIN dbo.Representative R ON R.id = DonateDetail.representative_id
                            LEFT JOIN dbo.Company C2 ON C2.id = R.company_id
                            LEFT JOIN dbo.Distribution D on C.id = D.commission_id
                            Left Join dbo.OfficerDistribution on D.id = OfficerDistribution.distribution_id
                            LEFT JOIN dbo.Officer O ON O.id = OfficerDistribution.officer_id ORDER BY ID DESC""";

            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
                ResultSet rs = preparedStatement.executeQuery();
                if (rs != null) {
                    int index = 1;
                    try {
                        while (rs.next()) {
                            DonateDetail donateDetail = new DonateDetail(
                                    rs.getInt("id"),
                                    rs.getDouble("amount"),
                                    rs.getObject("donate_date", LocalDate.class),
                                    rs.getInt("commission_id"),
                                    rs.getInt("representative_id"),
                                    rs.getString("precint_name"),
                                    rs.getString("representative_name"),
                                    rs.getString("company_name"),
                                    rs.getString("name")
                            );

                            // Check if company name is null
                            if (rs.getString("company_name") == null) {
                                donateDetail.setCompanyName("(Individual)");
                            }

                            indexObjectMap.put(index, donateDetail);
                            index++;
                        }
                    } catch (SQLException ex) {
                        System.out.println("\t\t\t\u001B[31mThere was an error processing the result set: " + ex.getMessage() + ".\u001B[0m");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + ex.getMessage() + ".\u001B[0m");
        }
        return indexObjectMap;
    }

    public static int getCommissionIdByName(Connection con, String commissionName) {
        int commissionId = -1; // Default value if not found
        try {
            String selectQuery = "SELECT id FROM Commission WHERE precint_name = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, commissionName);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    commissionId = rs.getInt("id");
                }
            }
        } catch (SQLException ex) {
            System.out.println("\t\t\t\u001B[31mError retrieving Commission ID: " + ex.getMessage() + ".\u001B[0m");
        }
        return commissionId;
    }

    public static int getRepresentativeIdByName(Connection con, String representativeName) {
        int representativeId = -1; // Default value if not found
        try {
            String selectQuery = "SELECT id FROM Representative WHERE representative_name = ?";
            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
                preparedStatement.setString(1, representativeName);
                ResultSet rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    representativeId = rs.getInt("id");
                }
            }
        } catch (SQLException ex) {
            System.out.println("\t\t\t\u001B[31mError retrieving Representative Id: " + ex.getMessage() + ".\u001B[0m");
        }
        return representativeId;
    }

    public static int getIdFromIndex(Connection con, int targetIndex) {
        Map<Integer, DonateDetail> indexObjectMap = getDonateDetail(con);

        if (indexObjectMap.containsKey(targetIndex)) {
            DonateDetail donateDetail = indexObjectMap.get(targetIndex);
            if (donateDetail != null) {
                return donateDetail.getId();
            } else {
                System.out.println("\t\t\t\u001B[31mError: DonateDetail object is null for index " + targetIndex + "\u001B[0m");
            }
        } else {
            System.out.println("\t\t\t\u001B[31mError: Index " + targetIndex + " is out of bounds.\u001B[0m");
        }
        return -1;
    }

    public static void printDonateDetail(Connection con) {
        try {
            Map<Integer, DonateDetail> donateDetailMap = getDonateDetail(con);
            if (!donateDetailMap.isEmpty()) {
                System.out.println();
                System.out.println("============================================================ \u001B[1mDONATION LIST\u001B[0m ============================================================");
                System.out.println("┌────────┬────────────────────┬────────────────────┬────────────────────┬────────────────────────┬──────────────────────┬─────────────┐");
                System.out.println("│   \u001B[1mID\u001B[0m   │   \u001B[1mAmount of money\u001B[0m  │   \u001B[1mDonation Date\u001B[0m    │    \u001B[1mCommune/Ward\u001B[0m    │     \u001B[1mRepresentative\u001B[0m     │     \u001B[1mCompany name\u001B[0m     │   \u001B[1mOfficer\u001B[0m   │");
                System.out.println("├────────┼────────────────────┼────────────────────┼────────────────────┼────────────────────────┼──────────────────────┼─────────────┤");
                int rowCount = 0;
                int totalRecords = countRecords(con, "DonateDetail");
                for (Map.Entry<Integer, DonateDetail> entry : donateDetailMap.entrySet()) {
                    Integer index = entry.getKey();
                    DonateDetail donateDetail = entry.getValue();
                    String amount = String.format("%.0f", donateDetail.getAmount());
                    LocalDate donateDate = donateDetail.getDonateDate();
                    String precintName = donateDetail.getPrecintName();
                    String representativeName = donateDetail.getRepresentativeName();
                    String companyName = donateDetail.getCompanyName();
                    String officerName = donateDetail.getOfficerName();

                    System.out.printf("│ %-6S │ %-18s │ %-18s │ %-18s │ %-22s │ %-20s │ %-11s │\n", index, amount, dateFormat.format(donateDate), precintName, representativeName, companyName, officerName);
                    rowCount++;
                    // Print separator line after each record (except the last one)
                    if (rowCount < totalRecords) {
                        System.out.println("├────────┼────────────────────┼────────────────────┼────────────────────┼────────────────────────┼──────────────────────┼─────────────┤");
                    }
                }
                System.out.println("└────────┴────────────────────┴────────────────────┴────────────────────┴────────────────────────┴──────────────────────┴─────────────┘");
                System.out.println("============================================================   \u001B[1mLIST ENDED\u001B[0m  ============================================================");
            } else {
                System.out.println("\t\t\t\u001B[31mThere have been no donations yet.\u001B[0m");
            }
            waitForEnter();
        } catch (Exception e) {
            System.out.println("\t\t\t\u001B[31mThere was an error: " + e.getMessage() + "\u001B[0m");
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
                WHERE DonateDetail.id = ?
                GROUP BY C.precint_name, dbo.DonateDetail.id,amount,donate_date,representative_name,company_name,O.name""";
        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery)) {
            selectStatement.setInt(1, ID);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (countRecords(con, "DonateDetail") > 0) {
                    boolean hasNext = resultSet.next();
                    while (hasNext) {
                        System.out.println();
                        System.out.println("============================================================ \u001B[1mDONATION LIST\u001B[0m ============================================================");
                        System.out.println("┌────────┬────────────────────┬────────────────────┬────────────────────┬────────────────────────┬──────────────────────┬─────────────┐");
                        System.out.println("│   \u001B[1mID\u001B[0m   │   \u001B[1mAmount of money\u001B[0m  │   \u001B[1mDonation Date\u001B[0m    │    \u001B[1mCommune/Ward\u001B[0m    │     \u001B[1mRepresentative\u001B[0m     │     \u001B[1mCompany name\u001B[0m     │   \u001B[1mOfficer\u001B[0m   │");
                        System.out.println("├────────┼────────────────────┼────────────────────┼────────────────────┼────────────────────────┼──────────────────────┼─────────────┤");
                        String amount = String.format("%.0f", resultSet.getDouble("amount"));
                        LocalDate donate_date = resultSet.getObject("donate_date", LocalDate.class);
                        String precint_name = resultSet.getString("precint_name");
                        String representative_name = resultSet.getString("representative_name");
                        String company_name;
                        if (resultSet.getString("company_name") == null) {
                            company_name = "(Individual)";
                        } else {
                            company_name = resultSet.getString("company_name");
                        }
                        String name = resultSet.getString("name");
                        System.out.printf("│ %-6S │ %-18s │ %-18s │ %-18s │ %-22s │ %-20s │ %-11s │\n", ID, amount, dateFormat.format(donate_date), precint_name, representative_name, company_name, name);
                        hasNext = resultSet.next();
                    }
                    System.out.println("└────────┴────────────────────┴────────────────────┴────────────────────┴────────────────────────┴──────────────────────┴─────────────┘");
                    System.out.println("============================================================   \u001B[1mLIST ENDED\u001B[0m  ============================================================");
                } else {
                    System.out.println("\t\t\t\u001B[31mThere have been no donations yet.\u001B[0m");
                }
            }
        } catch (SQLException e) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + "\u001B[0m");
        }
    }

    public static void addDonateDetail(Connection con) {
        try {
            System.out.println("\t\t\t=== Add Donationer Information ===");
            System.out.print("\t\t\t\u001B[31mYou are about to add a record for a sponsor's donation.\n\t\t\tPlease refer to other tables in the database to avoid mistakes!\n\t\t\t(For related tables please refer to the menu \"Representative manage\" và \"Commission manage\")\n  \u001B[0m");
            waitForEnter();
            String checkQuery = "SELECT * FROM DonateDetail WHERE representative_id = ? AND commission_id = ? AND donate_date = ?";
            String insertQuery = "INSERT INTO DonateDetail (representative_id, commission_id, donate_date, amount) VALUES (?, ?, ?, ?)";
            String updateQuery = "UPDATE DonateDetail SET amount = amount + ? WHERE representative_id =? AND commission_id = ? and donate_date = ?";
            String representativeName;
            int representativeId;
            do {
                System.out.print("\t\t\tPlease provide the name of the Sponsor representative (individual or corporate representative) \n\t\t\t(Refer to menu \"Representative manage\"): ");
                representativeName = sc.nextLine();
                representativeId = getRepresentativeIdByName(con, representativeName);
                if (representativeId == -1) {
                    System.out.println("\t\t\t\u001B[31mRepresentative's name not found. Please enter a valid representative name.\u001B[0m");
                }
            }
            while (representativeId == -1);
            String precintName;
            int commissionId;
            do {
                System.out.print("\t\t\tPlease provide the name of the Commune/Ward receiving the donation \n\t\t\t(Refer to the menu \"Commission manage\"): ");
                precintName = sc.nextLine();
                commissionId = getCommissionIdByName(con, precintName);
                if (commissionId == -1) {
                    System.out.println("\t\t\t\u001B[31mCommune/Ward name not found. Please enter a valid Commune/Ward name.\u001B[0m");
                }
            }
            while (commissionId == -1);
            LocalDate donateDate = Processing.validateDateInput("\t\t\tPlease enter the date of receipt of donation (in dd/MM/yyyy format): ");
            double amount = Processing.validateDoubleInput("\t\t\tPlease enter the donation amount (VND): ");
            // Check if a record with the specified representative_id, commission_id, and donate_date already exists
            PreparedStatement checkStatement = con.prepareStatement(checkQuery);
            checkStatement.setInt(1, representativeId);
            checkStatement.setInt(2, commissionId);
            checkStatement.setDate(3, Date.valueOf(donateDate));
            ResultSet resultSet = checkStatement.executeQuery();
            if (resultSet.next()) {
                // A record already exists, so update it
                PreparedStatement updateStatement = con.prepareStatement(updateQuery);
                updateStatement.setDouble(1, amount);
                updateStatement.setInt(2, representativeId);
                updateStatement.setInt(3, commissionId);
                updateStatement.setDate(4, Date.valueOf(donateDate));

                // Execute the update query
                updateStatement.executeUpdate();
                System.out.println("\t\t\t\u001B[31mA Record of this donor has already exists in the database. Updating in progress...\u001B[0m");
                System.out.println("\t\t\t\u001B[32mDonor's has been updated to the database.\u001B[0m");
            } else {
                // No record found, so insert a new record
                PreparedStatement insertStatement = con.prepareStatement(insertQuery);
                insertStatement.setInt(1, representativeId);
                insertStatement.setInt(2, commissionId);
                insertStatement.setDate(3, Date.valueOf(donateDate));
                insertStatement.setDouble(4, amount);
                // Execute the insert query
                insertStatement.executeUpdate();
                System.out.println("\t\t\t\u001B[32mNew Donor has been added to the database.\u001B[0m");
            }
            waitForEnter();
        } catch (SQLException e) {
            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
        }
    }

    //Xoa Thong Tin Donate
    public static void deleteDonateDetail(Connection con) {
        System.out.println();
        System.out.print("\t\t\t\u001B[31mYou are about to delete a record for a sponsor's donation.\n\t\t\tPlease refer to other tables in the database to avoid mistakes!\n\t\t\t(For related tables please refer to the menu \"Representative manage\" và \"Commission manage\")\n\u001B[0m");
        waitForEnter();
        printDonateDetail(con);
        int targetIndex;
        int identity = -1;

        do {
            System.out.print("\t\t\tPlease input the sequence number of the record you want to delete: ");
            try {
                targetIndex = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid integer
                System.out.println("\t\t\t\u001B[31mInvalid input. Please enter a valid integer.\u001B[0m");
                continue;  // Continue the loop if the input is not a valid integer
            }
            identity = getIdFromIndex(con, targetIndex);
            if (identity != -1) {
                System.out.println("\t\t\t\u001B[32mFound ID at index " + targetIndex + ": " + identity + "\u001B[0m");
                waitForEnter();
                printDonateDetailByID(con, identity);
                System.out.println();
                PreparedStatement pstmt = null;

                try {
                    //Kiem Tra Su Ton Tai Cua record, Neu Ton Tai Thi Tien Hanh Xoa
                    if (isIDAlreadyExists(con, identity, "DonateDetail")) {
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
                        }
                        while (!"y".equalsIgnoreCase(choice) && !"n".equalsIgnoreCase(choice));
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
            } else {
                System.out.println("\t\t\t\u001B[31mInvalid index or ID not found.\u001B[0m");
            }
        }
        while (identity == -1);
    }

    //	Chuc Nang Cap Nhat Thong Tin Donate
    public static void updateDonateDetail(Connection con) {
        System.out.println();
        System.out.print("\t\t\t\u001B[31mYou are about to edit a record for a sponsor's donation.\n\t\t\tPlease refer to other tables in the database to avoid mistakes!\n\t\t\t(For related tables please refer to the menu \"Representative manage\" và \"Commission manage\")\n\u001B[0m");
        waitForEnter();
        printDonateDetail(con);
        int targetIndex;
        int identity = -1;

        do {
            System.out.print("\t\t\tPlease input the sequence number of the record you want to update: ");
            try {
                targetIndex = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                // Handle the case where the input is not a valid integer
                System.out.println("\t\t\t\u001B[31mInvalid input. Please enter a valid integer.\u001B[0m");
                continue;  // Continue the loop if the input is not a valid integer
            }
            identity = getIdFromIndex(con, targetIndex);
            if (identity != -1) {
                System.out.println("\t\t\t\u001B[32mFound ID at index " + targetIndex + ": " + identity + "\u001B[0m");
                waitForEnter();
                printDonateDetailByID(con, identity);
                waitForEnter();
                System.out.println();
                PreparedStatement pstmt = null;
                //Kiem Tra id Da Co Trong Bang DonateDetail Hay Chua? Neu Co Thi Tien Hanh Update
                if (isIDAlreadyExists(con, identity, "DonateDetail")) {
                    int choice;
                    //Hien Thi Menu Update
                    do {
                        System.out.println("\t\t\t┌───────────────────────────────────┐");
                        System.out.println("\t\t\t│ \u001B[1mWhich item do you want to update?\u001B[0m │");
                        System.out.println("\t\t\t├───────────────────────────────────┤");
                        System.out.printf("\t\t\t│ %-34s│\n\t\t\t│ %-34s│\n\t\t\t│ %-34s│\n", "1. Amount of money", "2. Donation Date", "0. Return to the Management menu");
                        System.out.println("\t\t\t└───────────────────────────────────┘");
                        System.out.println("\t\t\tFrom Update Menu, Your Choice: ");
                        choice = InputValidator.validateIntInput("\t\t\tPlease Choice: ");
                        if (choice < 0 || choice > 2) {
                            System.out.println("\t\t\t\u001B[31mInvalid choice! Please enter a number between 0 and 4.\u001B[0m");
                            choice = -1; // Reset choice to -1 to repeat the loop
                        }
                    }
                    while (choice == -1);

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
                                }
                                while (true);
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
                                System.out.println("\t\t\t\u001B[32mDonation date successfully updated.!!!\u001B[0m");
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

                    }
                } else {
                    System.out.println("\t\t\t\u001B[31mNo donation information found!!!\u001B[0m");
                    waitForEnter();
                }
            } else {
                System.out.println("\t\t\t\u001B[31mInvalid index or ID not found.\u001B[0m");
            }
        }
        while (identity == -1);
    }

    public static void statsTop5Officer(Connection con) {
        try {
            if (countRecords(con, "DonateDetail") > 0) {
                System.out.println();
                System.out.println("=================================== \u001B[1mDONATION LIST\u001B[0m ==================================");
                System.out.println("┌───────┬────────────────────────┬────────────────────────┬────────────────────────┐");
                System.out.println("│   \u001B[1mID\u001B[0m  │         \u001B[1mOfficer\u001B[0m        │       \u001B[1mCommission\u001B[0m       │  \u001B[1mParticipations Times\u001B[0m  │");
                System.out.println("├───────┼────────────────────────┼────────────────────────┼────────────────────────┤");
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
                boolean hasNext = resultSet.next();
                while (hasNext) {
                    int ID = resultSet.getInt("id");
                    String companyName = resultSet.getString("name");
                    String precinctName = resultSet.getString("precint_name");
                    int Stats = resultSet.getInt("Stats");

                    System.out.printf("│ %-5S │ %-22s │ %-22s │ %-22s │%n", ID, companyName, precinctName, Stats);

                    hasNext = resultSet.next();

                    // Print separator line after each record (except the last one)
                    if (hasNext) {
                        System.out.println("├───────┼────────────────────────┼────────────────────────┼────────────────────────┤");
                    }
                }
                // Print the bottom border after the last record
                System.out.println("└───────┴────────────────────────┴────────────────────────┴────────────────────────┘");
                System.out.println("==================================   \u001B[1mLIST ENDED\u001B[0m   ==================================");
            } else {
                System.out.println("\t\t\t\u001B[31mThere have been no donations yet.\u001B[0m");
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
            int id = Processing.inputID("Officer", "id");
            selectStatement.setInt(1, id);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (countRecords(con, "OfficerDistribution") > 0) {
                    System.out.println();
                    System.out.println("=================================== \u001B[1mDONATION LIST\u001B[0m ==================================");
                    System.out.println("┌───────┬────────────────────────┬────────────────────────┬────────────────────────┐");
                    System.out.println("│   \u001B[1mID\u001B[0m  │         \u001B[1mOfficer\u001B[0m        │       \u001B[1mCommission\u001B[0m       │  \u001B[1mParticipations Times\u001B[0m  │");
                    System.out.println("├───────┼────────────────────────┼────────────────────────┼────────────────────────┤");
                    int rowCount = 1;
                    int totalRecords = countRecords(con, "OfficerDistribution");
                    while (resultSet.next()) {
                        String companyName = resultSet.getString("name");
                        String precintName = resultSet.getString("precint_name");
                        int Stats = resultSet.getInt("Stats");
                        System.out.printf("│ %-5S │ %-22s │ %-22s │ %-22s │\n", id, companyName, precintName, Stats);
                        rowCount++;
                        // Print separator line after each record (except the last one)
                        if (rowCount < totalRecords) {
                            System.out.println("├───────┼────────────────────────┼────────────────────────┼────────────────────────┤");
                        }

                    }
                    System.out.println("└───────┴────────────────────────┴────────────────────────┴────────────────────────┘");
                    System.out.println("==================================   \u001B[1mLIST ENDED\u001B[0m   ==================================");
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
                boolean hasNext = resultSet.next();
                System.out.println();
                System.out.println("============================================ \u001B[1mLIST OF HOUSEHOLDS\u001B[0m ============================================");
                System.out.println("┌───────┬─────────────────────────────────────┬─────────────────────────────────────┬──────────────────────┐");
                System.out.println("│  \u001B[1mID\u001B[0m   │        \u001B[1mHousehold Lord's Name\u001B[0m        │             \u001B[1mCommune/Ward\u001B[0m            │    \u001B[1mAmount of money\u001B[0m   │");
                System.out.println("├───────┼─────────────────────────────────────┼─────────────────────────────────────┼──────────────────────┤");
                while (hasNext) {
                    int householdId = resultSet.getInt("household_id");
                    String householdLordName = resultSet.getString("household_lord_name");
                    String precintName = resultSet.getString("precint_name");
                    String allocatedAmount = String.format("%.0f", resultSet.getDouble("allocated_amount"));
                    System.out.printf("│ %-5S │ %-35s │ %-35s │ %-20s │%n", householdId, householdLordName, precintName, allocatedAmount);
                    hasNext = resultSet.next();
                    // Print separator line after each record (except the last one)
                    if (hasNext) {
                        System.out.println("├───────┼─────────────────────────────────────┼─────────────────────────────────────┼──────────────────────┤");
                    }
                }
                System.out.println("└───────┴─────────────────────────────────────┴─────────────────────────────────────┴──────────────────────┘");
                System.out.println("============================================     \u001B[1mLIST ENDED\u001B[0m     ============================================");
            } else {
                System.out.println("\t\t\t\u001B[31m No data available yet!!!\u001B[0m");
            }
            waitForEnter();
            // Hỏi người dùng có muốn thực hiện INSERT không
            System.out.print("\t\t\t\u001B[32mDo you want to do an INSERT statement? (Y/N): \u001B[0m");
            String input = sc.next();
            sc.nextLine();

            // Nếu người dùng nhập Y, thực hiện lệnh INSERT
            if ("Y".equalsIgnoreCase(input)) {
                insertIntoDistribution(connection, preparedStatement);
                System.out.println("\t\t\t\u001B[32mInsert successfully!!!\u001B[0m");
                System.out.print("\t\t\tWould you like to review the 'PHANPHOI' table (Y/N): ");
                String print = sc.next();
                sc.next();
                if ("Y".equalsIgnoreCase(print)) {
                    printDistribution(con);
                    waitForEnter();
                }
            } else {
                System.out.println("\t\t\t\u001B[31mThere have been no donations yet.\u001B[0m");
            }
            waitForEnter();
        } catch (SQLException e) {
            System.out.println("\t\t\t\u001B[31m\t\t\tThere was an error connecting to the Database: " + e.getMessage() + ".\u001B[0m");
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
                    System.out.println("\u001B[31m\t\t\tAn error occurred while executing the INSERT statement.\u001B[0m");
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
