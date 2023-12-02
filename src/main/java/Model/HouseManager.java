package Model;
import Common.DBConnect;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class HouseManager {
    private static Connection connection;

    // Constructor
    public HouseManager(Connection connection) {
        this.connection = connection;
    }
    public HouseManager() {
        this.connection = DBConnect.connectDatabase();
    }
    public static boolean isValidIdInTable(Connection connection, String tableName, int id) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        }
        return false;
    }

    public static boolean isValidHouseId(Connection connection, int houseId) throws SQLException {
        return isValidIdInTable(connection, "House", houseId);
    }

    public static boolean isValidCommissionId(Connection connection, int commissionId) throws SQLException {
        return isValidIdInTable(connection, "Commission", commissionId);
    }

    public static boolean isValidPriorityObjectId(Connection connection, int priorityObjectId) throws SQLException {
        return isValidIdInTable(connection, "PriorityObject", priorityObjectId);
    }
    // Add a new house to the database
    public void addHouse(int commissionId, int priorityObjectId) throws SQLException {
        String query = "INSERT INTO House (commission_id, priority_object_id) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, commissionId);
            statement.setInt(2, priorityObjectId);
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("House added successfully.");
            }
        }
    }

    // Delete a house from the database by ID
    public void deleteHouse(int houseId) throws SQLException {
        String query = "DELETE FROM House WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, houseId);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("House with ID " + houseId + " has been deleted.");
            } else {
                System.out.println("House with ID " + houseId + " not found.");
            }
        }
    }

    // Update house information in the database by ID
    public void updateHouse(int houseId, int commissionId, int priorityObjectId) throws SQLException {
        String query = "UPDATE House SET commission_id = ?, priority_object_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, commissionId);
            statement.setInt(2, priorityObjectId);
            statement.setInt(3, houseId);
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("House information updated for ID " + houseId);
            } else {
                System.out.println("House with ID " + houseId + " not found.");
            }
        }
    }

    // Display all houses from the database
    public void displayHouseTable() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connection.createStatement();
            String sql = "SELECT H.id, CONCAT(C.precint_name, ', ', C.city_name, ', ', C.province_name) AS commission_info, P.object_type " +
                    "FROM House H " +
                    "INNER JOIN Commission C ON H.commission_id = C.id " +
                    "INNER JOIN PriorityObject P ON H.priority_object_id = P.id";
            rs = st.executeQuery(sql);

            System.out.println("=================================== House Table ====================================");
            System.out.println(String.format("| %-5s | %-35s | %-20s |", "ID", "Commission Information", "Priority Object Type"));
            System.out.println(String.format("| %-5s | %-35s | %-20s |", "-----", "-----------------------------------", "---------------------"));
            while (rs.next()) {
                System.out.println(String.format("| %-5s | %-35s | %-20s |",
                        rs.getInt("id"), rs.getString("commission_info"), rs.getString("object_type")));
                System.out.println(String.format("| %-5s | %-35s | %-20s |", "-----", "-----------------------------------", "---------------------"));
            }
            System.out.println();
        } finally {
            // Đóng ResultSet và Statement sau khi sử dụng xong
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
    //- Hiển thị top 5 hộ dân có giá trị ủng hộ nhiều nhất trong 1 đợt ủng hộ X (X nhập vào từ bàn phím)
    public static void displayTop5HouseholdsByDonation(Connection connection, String donationRound) {
        try {
            PreparedStatement statement = connection.prepareStatement(
                    "SELECT TOP 5 D.household_id, SUM(D.amount_distribution) AS total_donation, " +
                            "C.name AS household_name " +
                            "FROM Distribution D " +
                            "INNER JOIN House H ON D.household_id = H.id " +
                            "INNER JOIN Citizen C ON H.id = C.house_id " +
                            "WHERE DATEPART(MONTH, D.date_distribution) = MONTH(?) " +
                            "AND DATEPART(YEAR, D.date_distribution) = YEAR(?) " +
                            "AND C.is_household_lord = 1 " +
                            "GROUP BY D.household_id, C.name " +
                            "ORDER BY total_donation DESC"
            );

            // Parsing the input donationRound to create a date in 'MM/yyyy' format
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
            java.util.Date parsedDate = dateFormat.parse(donationRound);
            java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());

            statement.setDate(1, sqlDate);
            statement.setDate(2, sqlDate);

            ResultSet resultSet = statement.executeQuery();

            System.out.println("Top 5 households with the highest donations in round " + donationRound + ":");
            System.out.println("Household ID\tHousehold Name\t\t\t\t\tTotal Donation");

            while (resultSet.next()) {
                int householdId = resultSet.getInt("household_id");
                String householdName = resultSet.getString("household_name");
                double totalDonation = resultSet.getDouble("total_donation");

                System.out.printf("%-13d\t%-24s\t\t%.2f\n", householdId, householdName, totalDonation);
            }
        } catch (SQLException | ParseException e) {
            e.printStackTrace();
        }
    }






    public static void handleHouseManagement(HouseManager houseManager, Scanner scanner) {
        int choice;

        do {
            System.out.println("\t\tHouse Management :");
            System.out.println("\t\t\t0. Exit");
            System.out.println("\t\t\t1. Add");
            System.out.println("\t\t\t2. Delete");
            System.out.println("\t\t\t3. Update");
            System.out.println("\t\t\t4. Display");
            System.out.print("\t\t\tPlease choose: ");

            try {
                System.out.print("Please choose: ");
                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        // Thêm hộ dân
                        boolean validAddInput = false;
                        int commissionId = 0;
                        int priorityObjectId = 0;

                        do {
                            try {
                                System.out.print("Input commission ID which can be referenced in the commission table: ");
                                commissionId = Integer.parseInt(scanner.nextLine());

                                System.out.print("Input priority object ID which can be referenced in the priority object table: ");
                                priorityObjectId = Integer.parseInt(scanner.nextLine());

                                if (!HouseManager.isValidCommissionId(connection, commissionId)) {
                                    System.out.println("Commission ID is invalid.");
                                } else if (!HouseManager.isValidPriorityObjectId(connection, priorityObjectId)) {
                                    System.out.println("Priority Object ID is invalid.");
                                } else {
                                    validAddInput = true;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Please enter a valid number.");
                            } catch (SQLException ex) {
                                System.out.println("SQL error: " + ex.getMessage());
                            }
                        } while (!validAddInput);

                        houseManager.addHouse(commissionId, priorityObjectId);
                        break;

                    case 2:
                        // Xóa hộ dân
                        boolean validDeleteInput = false;
                        int houseIdToDelete = 0;

                        do {
                            try {
                                System.out.print("Input house ID: ");
                                houseIdToDelete = Integer.parseInt(scanner.nextLine());

                                if (!HouseManager.isValidHouseId(connection, houseIdToDelete)) {
                                    System.out.println("Invalid house ID.");
                                } else {
                                    validDeleteInput = true;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Please enter a valid number.");
                            } catch (SQLException ex) {
                                System.out.println("SQL error: " + ex.getMessage());
                            }
                        } while (!validDeleteInput);

                        houseManager.deleteHouse(houseIdToDelete);
                        System.out.println("Household with ID " + houseIdToDelete + " has been deleted successfully.");
                        break;

                    case 3:
                        // Sửa thông tin hộ dân
                        boolean validUpdateInput = false;
                        int houseIdToUpdate = 0;
                        int newCommissionId = 0;
                        int newPriorityObjectId = 0;

                        do {
                            try {
                                System.out.print("Input house ID to update: ");
                                houseIdToUpdate = Integer.parseInt(scanner.nextLine());

                                System.out.print("Input new commission ID: ");
                                newCommissionId = Integer.parseInt(scanner.nextLine());

                                System.out.print("Input new priority object ID: ");
                                newPriorityObjectId = Integer.parseInt(scanner.nextLine());

                                if (!HouseManager.isValidHouseId(connection, houseIdToUpdate)) {
                                    System.out.println("Invalid house ID.");
                                } else if (!HouseManager.isValidCommissionId(connection, newCommissionId)) {
                                    System.out.println("The new Commission ID is invalid.");
                                } else if (!HouseManager.isValidPriorityObjectId(connection, newPriorityObjectId)) {
                                    System.out.println("The new Priority Object ID is invalid.");
                                } else {
                                    validUpdateInput = true;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println("Please enter a valid number.");
                            } catch (SQLException ex) {
                                System.out.println("SQL error: " + ex.getMessage());
                            }
                        } while (!validUpdateInput);

                        houseManager.updateHouse(houseIdToUpdate, newCommissionId, newPriorityObjectId);
                        break;

                    case 4:
                        // Hiển thị thông tin tất cả các hộ dân
                        try {
                            houseManager.displayHouseTable();
                        } catch (SQLException ex) {
                            System.out.println("SQL error: " + ex.getMessage());
                        }
                        break;

                    case 0:
                        // Thoát khỏi menu
                        System.out.println("Exiting...");
                        return;

                    default:
                        System.out.println("Invalid selection.");
                        break;

                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
                choice = -1;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
                choice = -1;
            }
        } while (true);
    }


}
