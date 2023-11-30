package Model;

import Common.DBConnect;
import Common.InputValidatorKhue;
import Common.JDBCQuery;

import java.sql.*;
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

    // Trình quản lý bảng Representative
    public void handleRepresentative(RepresentativeManager representativeManager, Scanner scanner) {
        System.out.println("Representative Management:");
        System.out.println("1. Display");
        System.out.println("2. Add");
        System.out.println("3. Delete");
        System.out.println("4. Update");
        System.out.println("0. Exit");

        int choice = -1;

        do {
            try {
                System.out.print("Please choose: ");
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        representativeManager.displayRepresentative();
                        break;
                    case 2:
                        // Thêm người đại diện
                        representativeManager.addRepresentativeInput(scanner);
                        break;
                    case 3:
                        // Xóa người đại diện
                        int representativeIdToDelete = InputValidatorKhue.validateIntInput("Input representative ID you need to delete: ");
                        representativeManager.updateRepresentativeidForDonateDetail(representativeIdToDelete);
                        representativeManager.deleteRepresentative(representativeIdToDelete);
                        break;
                    case 4:
                        // Sửa thông tin người đại diện
                        representativeManager.updateRepresentativeFromConsoleInput(scanner);
                        break;
                    case 0:
                        // Quay lại menu chính
                        break;
                    default:
                        System.out.println("Not choose.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please input number.");
            } catch (SQLException e) {
                System.out.println("SQL error: " + e.getMessage());
            }
        } while (choice != 0);
    }

    public Representative inputRepresentative(Scanner scanner) throws SQLException {
        Representative newRepresentative = new Representative();
        String representativeName = InputValidatorKhue.validateString("Name:");
        newRepresentative.setRepresentativeName(representativeName);
        String representativeAddress = InputValidatorKhue.validateString("Address:");
        newRepresentative.setRepresentativeAddress(representativeAddress);
        String phoneNumber = InputValidatorKhue.validateStringNumber("Phone:");
        newRepresentative.setPhoneNumber(phoneNumber);
        System.out.println("Is these any company representaitive ? (Y/N)");
        String isCompany = scanner.nextLine();
        int companyId = 0;
        //Tạo đơn vị ủng hộ
        if (isCompany.equalsIgnoreCase("Y")) {
            CompanyManager companyManager = new CompanyManager();
            String companyName = companyManager.addCompanyInput(scanner);
            // add Id lại cho companyId
            companyId = companyManager.getSingleCompanyByName(companyName);
            newRepresentative.setCompanyId(companyId);
        } else {
            System.out.println("Representative isn't company.");
        }
        return newRepresentative;
    }

    ;

    public void addRepresentativeInput(Scanner scanner) throws SQLException {
        Representative newRepresentative = inputRepresentative(scanner);
        // add dữ liệu không có id hoặc có id
        if (newRepresentative.getCompanyId() != 0) {
            addRepresentative(newRepresentative);
        } else {
            addRepresentativeNoCompany(newRepresentative);
        }

    }

    //Thêm người đại diện có đơn vị ủng hộ
    public void addRepresentative(Representative newRepresentative) {
        String query = "INSERT INTO Representative (representative_name, representative_address, phone_number, company_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newRepresentative.getRepresentativeName());
            preparedStatement.setString(2, newRepresentative.getRepresentativeAddress());
            preparedStatement.setString(3, newRepresentative.getPhoneNumber());
            preparedStatement.setInt(4, newRepresentative.getCompanyId());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Add success!!");
            } else {
                System.out.println("Add unsuccessful!!");
            }

        } catch (SQLException e) {
            System.out.println("Error when adding a new representative: " + e.getMessage());
        } finally {
            JDBCQuery.closeConnection();
        }
    }

    //Thêm người đại diện không có đơn vị ủng hộ
    public void addRepresentativeNoCompany(Representative newRepresentative) {
        String query = "INSERT INTO Representative (representative_name, representative_address, phone_number) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newRepresentative.getRepresentativeName());
            preparedStatement.setString(2, newRepresentative.getRepresentativeAddress());
            preparedStatement.setString(3, newRepresentative.getPhoneNumber());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Add success!!");
            } else {
                System.out.println("Add unsuccessful!!");
            }

        } catch (SQLException e) {
            System.out.println("Error when adding a new representative:  " + e.getMessage());
        } finally {
            JDBCQuery.closeConnection();
        }
    }

    // Phương thức sửa thông tin Representative trong cơ sở dữ liệu
    public void updateRepresentativeFromConsoleInput(Scanner scanner) {
        try {
            boolean checkIdRepresentative =false;
            do{
                int representativeId = InputValidatorKhue.validateIntInput(" Input ID you want to fix:   ");
                Representative existingRepresentative = getSingleRepresentativeById(representativeId);
                if (existingRepresentative != null) {
                    Representative updatedRepresentative = inputRepresentative(scanner);
                    updatedRepresentative.setId(representativeId);
                    updateReprensentative(updatedRepresentative);
                    System.out.println("Update success!!");
                } else {
                    System.out.println("ID not found ");
                }
            } while (!checkIdRepresentative);

        } catch (NumberFormatException e) {
            System.out.println("ID invalid ");
        } catch (SQLException e) {
            System.out.println("Error updating: " + e.getMessage());
        }
    }


    public void updateReprensentative(Representative representative) throws SQLException {
        String query = "UPDATE Representative set representative_name = ?, representative_address = ?, phone_number = ?, company_id = ? WHERE id =?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, representative.getRepresentativeName());
            statement.setString(2, representative.getRepresentativeAddress());
            statement.setString(3, representative.getPhoneNumber());
            statement.setInt(4, representative.getCompanyId());
            statement.setInt(5, representative.getId());

            statement.executeUpdate();
        } finally {
            JDBCQuery.closeConnection();
        }
    }

    //Tìm kiếm Representative theo ID
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
        } finally {
            JDBCQuery.closeConnection();
        }
        return null;
    }

    // Phương thức xóa một Representative từ cơ sở dữ liệu
    public void deleteRepresentative(int representativeId) throws SQLException {
        String query = "DELETE FROM Representative WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, representativeId);
            int i = statement.executeUpdate();
            if (i > 0) {
                System.out.println("\t\tDelete success!!");
            } else {
                System.out.println("\t\t\tDelete failed. No Representative found with the specified ID.");
            }
        } finally {
            JDBCQuery.closeConnection();
        }
    }

    // Xét giá trị representative_id về null trong bảng DonateDetail
    public void updateRepresentativeidForDonateDetail(int id) throws SQLException{
        String query ="UPDATE DonateDetail set representative_id = Null WHERE representative_id ="+ id;
        try (PreparedStatement statement =connection.prepareStatement(query)){
            statement.executeUpdate();
        } finally {
            JDBCQuery.closeConnection();
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
        } finally {
            JDBCQuery.closeConnection();
        }
        return representatives;
    }

    //Hiển thị bảng Representative
    public void displayRepresentative() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connection.createStatement();
            System.out.println();
            System.out.println("._______.______________________.___________________________________.____________________.____________.");
            System.out.println("│   ID  │    Representative    │                Address            │         Phone      │ ID Company │");
            System.out.println("│_______│______________________│___________________________________│____________________│____________│");
            rs = st.executeQuery("SELECT dbo.Representative.id, representative_name, representative_address, phone_number, company_id FROM Representative;");
            while (rs.next()) {
                String ID = rs.getString("id");
                String representativeName = rs.getString("representative_name");
                String representativeAddress = rs.getString("representative_address");
                String phoneNumber = rs.getString("phone_number");
                String companyId = rs.getString("company_id");
                System.out.printf("│ %-5S │ %-20s │ %-33s │ %-18s │ %-10s │\n", ID, representativeName, representativeAddress, phoneNumber, companyId);
                System.out.println("│_______│______________________│___________________________________│____________________│____________│");
            }

            InputValidatorKhue.waitForEnter();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error in the database connection process");
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }

}
