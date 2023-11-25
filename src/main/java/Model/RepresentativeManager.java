package Model;

import Common.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class RepresentativeManager {
    private Connection connection;

    public RepresentativeManager(Connection connection) {
        this.connection = connection;
    }

    public RepresentativeManager() {
        this.connection = DBConnect.connectDatabase();
    }

    public Representative inputRepresentative(Scanner scanner) {
        Representative newRepreesentative = new Representative();
        System.out.println("Nhập thông tin cho người đại diện:");
        System.out.print("Tên người đại diện:");
        newRepreesentative.setRepresentativeName(scanner.nextLine());
        System.out.print("Địa chỉ:");
        newRepreesentative.setRepresentativeAddress(scanner.nextLine());
        System.out.print("Số điện thoại:");
        newRepreesentative.setPhoneNumber(scanner.nextLine());
        System.out.print("ID đơn vị ủng hộ: ");
        int companyId = Integer.parseInt(scanner.nextLine());
        newRepreesentative.setCompanyId(companyId);
        return newRepreesentative;
    }

    ;

    public void addRepresentativeInput(Scanner scanner) {
        Representative newRepresentative = inputRepresentative(scanner);
        addRepresentative(newRepresentative);
    }

    public void addRepresentative(Representative newRepresentative) {
        String query = "INSERT INTO Representative (representative_name, representative_address, phone_number, company_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newRepresentative.getRepresentativeName());
            preparedStatement.setString(2, newRepresentative.getRepresentativeAddress());
            preparedStatement.setString(3, newRepresentative.getPhoneNumber());
            preparedStatement.setInt(4, newRepresentative.getCompanyId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Người đại diện mới đã được thêm vào cơ sở dữ liệu.");
            } else {
                System.out.println("Không thể thêm người đại diện mới.");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm người đại diện mới: " + e.getMessage());
        }
    }

    // Phương thức sửa thông tin Representative trong cơ sở dữ liệu
    public void updateRepresentativeFromConsoleInput(Scanner scanner) {
        try {
            System.out.print("Nhập ID người đại diện cần sửa: ");
            int representativeId = Integer.parseInt(scanner.nextLine());

            Representative existingRepresentative = getSingleRepresentativeById(representativeId);
            if (existingRepresentative != null) {
                Representative updatedRepresentative = inputRepresentative(scanner);
                updatedRepresentative.setId(representativeId);
                updateReprensentative(updatedRepresentative);
                System.out.println("Thông tin người đại diện đã được cập nhật");
            } else {
                System.out.println("Không tìm thấy người đại diện có ID là: " + representativeId);
            }
        } catch (NumberFormatException e){
            System.out.println("ID không hợp lệ");
        } catch (SQLException e){
            System.out.println("Lỗi khi cập nhật người đại diện: " +e.getMessage());
        }
    }


    public void updateReprensentative(Representative representative) throws SQLException{
        String query ="UPDATE Representative set representative_name = ?, representative_address = ?, phone_number = ?, company_id = ? WHERE id =?";
        try (PreparedStatement statement =connection.prepareStatement(query)){
            statement.setString(1,representative.getRepresentativeName());
            statement.setString(2,representative.getRepresentativeAddress());
            statement.setString(3,representative.getPhoneNumber());
            statement.setInt(4,representative.getCompanyId());
            statement.setInt(5,representative.getId());

            statement.executeUpdate();

        }
    }

    public Representative getSingleRepresentativeById(int id) throws SQLException {
        String query = "SELECT * FROM Representative WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Representative representative = new Representative();
                    representative.setId(resultSet.getInt("id"));
                    representative.setRepresentativeName(resultSet.getString("representative_name"));
                    representative.setRepresentativeAddress(resultSet.getString("representative_address"));
                    representative.setPhoneNumber(resultSet.getString("phone_number"));
                    representative.setCompanyId(resultSet.getInt("id_company"));
                    return representative;
                }
            }
        }
        return null;
    }

    // Phương thức xóa một Representative từ cơ sở dữ liệu

    public void deleteRepresentative(int representativeId) throws SQLException {
        String query = "DELETE FROM Representative WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, representativeId);
            statement.executeUpdate();
        }
    }

    // Phương thức lấy danh sách tất cả các Representative từ cơ sở dữ liệu
    public List<Representative> getAllRepresentative() throws SQLException {
        List<Representative> representatives = new ArrayList<>();
        String query = "SELECT * FROM Representative";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Representative representative = new Representative();
                representative.setId(resultSet.getInt("id"));
                representative.setRepresentativeName(resultSet.getString("representative_name"));
                // Thêm các trường thông tin khác tương tự
                representatives.add(representative);
            }
        }
        return representatives;
    }
}
