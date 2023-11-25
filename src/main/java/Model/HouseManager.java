package Model;
import Common.DBConnect;
import org.w3c.dom.ls.LSOutput;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class HouseManager {
    private Connection connection;

    // Constructor
    public HouseManager(Connection connection) {
        this.connection = connection;
    }
    public HouseManager() {
        this.connection = DBConnect.connectDatabase();
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
    public void displayAllHouses() throws SQLException {
        String query = "SELECT * FROM House";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            System.out.println("List of houses:");
            while (resultSet.next()) {
                int houseId = resultSet.getInt("id");
                int commissionId = resultSet.getInt("commission_id");
                int priorityObjectId = resultSet.getInt("priority_object_id");
                System.out.println("House ID: " + houseId + ", Commission ID: " + commissionId +
                        ", Priority Object ID: " + priorityObjectId);
            }
        }
    }
}
