package Model;

import Common.DBConnect;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CitizenManager {
    private Connection connection;


    // Constructor nhận connection từ bên ngoài
    public CitizenManager(Connection connection) {
        this.connection = connection;
    }
    public CitizenManager() {
        this.connection = DBConnect.connectDatabase();
    }
    public Citizen enterCitizenDetailsFromConsole(Scanner scanner) {
        Citizen newCitizen = new Citizen();

        System.out.println("Enter new Citizen:");

        System.out.print("Tên: ");
        newCitizen.setName(scanner.nextLine());

        System.out.print("Số CMND: ");
        newCitizen.setIdentityCard(scanner.nextLine());

        System.out.print("Ngày sinh (yyyy-MM-dd): ");
        String dobString = scanner.nextLine();
        try {
            Date dob = java.sql.Date.valueOf(dobString);
            newCitizen.setDateOfBirth(dob);
        } catch (IllegalArgumentException e) {
            System.out.println("Định dạng ngày không hợp lệ. Sử dụng định dạng yyyy-MM-dd.");
        }

        // Yêu cầu người dùng nhập các thông tin khác cho công dân
        System.out.print("Số điện thoại: ");
        newCitizen.setPhoneNumber(scanner.nextLine());

        System.out.print("Địa chỉ: ");
        newCitizen.setAddress(scanner.nextLine());

        System.out.print("ID hộ: ");
        int houseId = Integer.parseInt(scanner.nextLine());
        newCitizen.setHouseId(houseId);

        System.out.print("Là chủ hộ (true/false): ");
        boolean isHouseholdLord = Boolean.parseBoolean(scanner.nextLine());
        newCitizen.setHouseholdLord(isHouseholdLord);

        System.out.print("Giới tính (true-male/false-female): ");
        boolean sex = Boolean.parseBoolean(scanner.nextLine());
        newCitizen.setSex(sex);

        System.out.print("ID đối tượng công dân: ");
        int citizenObjectId = Integer.parseInt(scanner.nextLine());
        newCitizen.setCitizenObjectId(citizenObjectId);

        return newCitizen;
    }
    public void addCitizenFromConsoleInput(Scanner scanner) {
        Citizen newCitizen = enterCitizenDetailsFromConsole(scanner);

        addCitizen(newCitizen); // Gọi phương thức addCitizen đã viết trước đó
    }


    public void addCitizen(Citizen newCitizen) {
        String query = "INSERT INTO Citizen (name, identity_card, date_of_birth, phone_number, address, house_id, is_household_lord, sex, citizen_object_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newCitizen.getName());
            preparedStatement.setString(2, newCitizen.getIdentityCard());
            preparedStatement.setDate(3, new java.sql.Date(newCitizen.getDateOfBirth().getTime()));
            preparedStatement.setString(4, newCitizen.getPhoneNumber());
            preparedStatement.setString(5, newCitizen.getAddress());
            preparedStatement.setInt(6, newCitizen.getHouseId());
            preparedStatement.setBoolean(7, newCitizen.isHouseholdLord());
            preparedStatement.setBoolean(8, newCitizen.getSex());
            preparedStatement.setInt(9, newCitizen.getCitizenObjectId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Công dân mới đã được thêm vào cơ sở dữ liệu.");
            } else {
                System.out.println("Không thể thêm công dân mới.");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm công dân mới: " + e.getMessage());
        }
    }

    // Phương thức sửa thông tin một Citizen trong cơ sở dữ liệu
    public void updateCitizenFromConsoleInput(Scanner scanner) {
        try {
            System.out.print("Nhập ID của công dân cần sửa: ");
            int citizenId = Integer.parseInt(scanner.nextLine());

            Citizen existingCitizen = getSingleCitizenById(citizenId);
            if (existingCitizen != null) {
                Citizen updatedCitizen = enterCitizenDetailsFromConsole(scanner);
                updatedCitizen.setId(citizenId);
                updateCitizen(updatedCitizen);
                System.out.println("Thông tin công dân đã được cập nhật.");
            } else {
                System.out.println("Không tìm thấy công dân có ID là " + citizenId);
            }
        } catch (NumberFormatException e) {
            System.out.println("ID không hợp lệ.");
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật công dân: " + e.getMessage());
        }
    }

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
    public Citizen getSingleCitizenById(int id) throws SQLException {
        String query = "SELECT * FROM Citizen WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Citizen citizen = new Citizen();
                    citizen.setId(resultSet.getInt("id"));
                    citizen.setName(resultSet.getString("name"));
                    citizen.setIdentityCard(resultSet.getString("identity_card"));
                    citizen.setDateOfBirth(resultSet.getDate("date_of_birth"));
                    citizen.setPhoneNumber(resultSet.getString("phone_number"));
                    citizen.setAddress(resultSet.getString("address"));
                    citizen.setHouseId(resultSet.getInt("house_id"));
                    citizen.setHouseholdLord(resultSet.getBoolean("is_household_lord"));
                    citizen.setSex(resultSet.getBoolean("sex"));
                    citizen.setCitizenObjectId(resultSet.getInt("citizen_object_id"));
                    return citizen;
                }
            }
        }
        return null;
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
