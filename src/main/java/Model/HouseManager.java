package Model;
import Common.DBConnect;

import java.sql.*;
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

    public static void handleHouseManagement(HouseManager houseManager, Scanner scanner) {
        int choice;

        do {
            System.out.println("House Management :");
            System.out.println("1. Add");
            System.out.println("2. Delete");
            System.out.println("3. Update");
            System.out.println("4. Display");
            System.out.println("0. Exit ");

            try {
                System.out.print("Please choose: ");
                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        // Thêm hộ dân
                        System.out.print("Input commission ID  which can be referenced in the commission table: ");
                        int commissionId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Input priority object ID which can be referenced in the priority object table: ");
                        int priorityObjectId = Integer.parseInt(scanner.nextLine());

                        if (!HouseManager.isValidCommissionId(connection, commissionId)) {
                            System.out.println("Commission ID is invalid.");
                            break;
                        }

                        if (!HouseManager.isValidPriorityObjectId(connection, priorityObjectId)) {
                            System.out.println("Priority Object ID is invalid.");
                            break;
                        }

                        houseManager.addHouse(commissionId, priorityObjectId);
                        break;

                    case 2:
                        // Xóa hộ dân
                        System.out.print("Input house ID: ");
                        int houseIdToDelete = Integer.parseInt(scanner.nextLine());

                        if (!HouseManager.isValidHouseId(connection, houseIdToDelete)) {
                            System.out.println("Invalid house ID.");
                            break;
                        }

                        houseManager.deleteHouse(houseIdToDelete);
                        System.out.println("Household has ID " + houseIdToDelete + " has been deleted successfully.");
                        break;
                    case 3:
                        // Sửa thông tin hộ dân
                        System.out.print("Nhập ID hộ dân cần sửa: ");
                        int houseIdToUpdate = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nhập commission ID mới: ");
                        int newCommissionId = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nhập priority object ID mới: ");
                        int newPriorityObjectId = Integer.parseInt(scanner.nextLine());

                        if (!HouseManager.isValidHouseId(connection, houseIdToUpdate)) {
                            System.out.println("Invalid house ID.");
                            break;
                        }

                        if (!HouseManager.isValidCommissionId(connection, newCommissionId)) {
                            System.out.println("The new Commission ID is invalid.");
                            break;
                        }

                        if (!HouseManager.isValidPriorityObjectId(connection, newPriorityObjectId)) {
                            System.out.println("The new Priority Object ID is invalid.");
                            break;
                        }

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
