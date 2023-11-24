package Model;

import Common.JdbcConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CitizenManager {
    Connection connection = null;


    // Constructor nhận connection từ bên ngoài
    public CitizenManager(Connection connection) {
        this.connection = connection;
    }

    // Phương thức thêm một Citizen mới vào cơ sở dữ liệu
    public void addCitizen(Citizen citizen) throws SQLException {
        String query = "INSERT INTO Citizen (name, identity_card, date_of_birth, phone_number, address, house_id, is_household_lord, sex, citizen_object_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, citizen.getName());
            statement.setString(2, citizen.getIdentityCard());
            statement.setDate(3, new java.sql.Date(citizen.getDateOfBirth().getTime()));
            statement.setString(4, citizen.getPhoneNumber());
            statement.setString(5, citizen.getAddress());
            statement.setInt(6, citizen.getHouseId());
            statement.setBoolean(7, citizen.isHouseholdLord());
            statement.setBoolean(8, citizen.getSex());
            statement.setInt(9, citizen.getCitizenObjectId());

            statement.executeUpdate();
        }
    }

    // Phương thức sửa thông tin một Citizen trong cơ sở dữ liệu
    public void updateCitizen(Citizen citizen) throws SQLException {
        String query = "UPDATE Citizen SET name = ?, identity_card = ?, date_of_birth = ?, phone_number = ?, address = ?, house_id = ?, is_household_lord = ?, sex = ?, citizen_object_id = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, citizen.getName());
            statement.setString(2, citizen.getIdentityCard());
            statement.setDate(3, new java.sql.Date(citizen.getDateOfBirth().getTime()));
            statement.setString(4, citizen.getPhoneNumber());
            statement.setString(5, citizen.getAddress());
            statement.setInt(6, citizen.getHouseId());
            statement.setBoolean(7, citizen.isHouseholdLord());
            statement.setBoolean(8, citizen.getSex());
            statement.setInt(9, citizen.getCitizenObjectId());
            statement.setInt(10, citizen.getId());

            statement.executeUpdate();
        }
    }

    // Phương thức xóa một Citizen từ cơ sở dữ liệu
    public void deleteCitizen(int citizenId) throws SQLException {
        String query = "DELETE FROM Citizen WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, citizenId);
            statement.executeUpdate();
        }
    }

    // Phương thức lấy danh sách tất cả các Citizen từ cơ sở dữ liệu
    public List<Citizen> getAllCitizens() throws SQLException {
        List<Citizen> citizens = new ArrayList<>();
        String query = "SELECT * FROM Citizen";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Citizen citizen = new Citizen();
                citizen.setId(resultSet.getInt("id"));
                citizen.setName(resultSet.getString("name"));
                // Thêm các trường thông tin khác tương tự
                citizens.add(citizen);
            }
        }
        return citizens;
    }

}
