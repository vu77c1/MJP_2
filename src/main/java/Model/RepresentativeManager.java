package Model;

import Common.DBConnect;
import Common.InputValidatorKhue;
import Common.JDBCQuery;

import java.sql.*;
import java.time.LocalDate;
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


    public Representative inputRepresentative(Scanner scanner) throws SQLException {
        Representative newRepreesentative = new Representative();
        System.out.println("Nhập thông tin cho người đại diện:");
//        System.out.print("Tên người đại diện:");
        String representativeName = InputValidatorKhue.validateStringRepresentative("Tên người đại diện:");
        newRepreesentative.setRepresentativeName(representativeName);
//        System.out.print("Địa chỉ:");
        String representativeAddress = InputValidatorKhue.validateStringRepresentative("Địa chỉ:");
        newRepreesentative.setRepresentativeAddress(representativeAddress);
//        System.out.print("Số điện thoại:");
        String phoneNumber =InputValidatorKhue.validateStringRepresentative("Số điện thoại:");
        newRepreesentative.setPhoneNumber(phoneNumber);
        System.out.println("Có đại diện cho đơn vị nào không (Y/N)");
        String isCompany = scanner.nextLine();
        int companyId = 0;
        if(isCompany.equalsIgnoreCase("Y")){
            CompanyManager companyManager =new CompanyManager();
            String companyName = companyManager.addCompanyInput(scanner);
            companyId = companyManager.getSingleCompanyByName(companyName);
            newRepreesentative.setCompanyId(companyId);
        }else {
            System.out.println("Người đại diện không muốn đại diện cho đơn vị mới.");
        }

//        System.out.print("ID đơn vị ủng hộ: ");

        return newRepreesentative;
    }

    ;

    public void addRepresentativeInput(Scanner scanner) throws SQLException {
        Representative newRepresentative = inputRepresentative(scanner);
        if(newRepresentative.getCompanyId()!=0){
            addRepresentative(newRepresentative);
        }else {
            addRepresentativeNoCompany(newRepresentative);
        }

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
        }finally {
            JDBCQuery.closeConnection();
        }
    }

    public void addRepresentativeNoCompany(Representative newRepresentative) {
        String query = "INSERT INTO Representative (representative_name, representative_address, phone_number) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newRepresentative.getRepresentativeName());
            preparedStatement.setString(2, newRepresentative.getRepresentativeAddress());
            preparedStatement.setString(3, newRepresentative.getPhoneNumber());

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Người đại diện mới đã được thêm vào cơ sở dữ liệu.");
            } else {
                System.out.println("Không thể thêm người đại diện mới.");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm người đại diện mới: " + e.getMessage());
        }finally {
            JDBCQuery.closeConnection();
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
        }finally {
            JDBCQuery.closeConnection();
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
        }finally {
            JDBCQuery.closeConnection();
        }
        return null;
    }

    // Phương thức xóa một Representative từ cơ sở dữ liệu

    public void deleteRepresentative(int representativeId) throws SQLException {
        String query = "DELETE FROM Representative WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, representativeId);
            statement.executeUpdate();
        }finally {
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
        }
        finally {
            JDBCQuery.closeConnection();
        }
        return representatives;
    }

    public void displayRepresentative() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st =connection.createStatement();
            System.out.println();
            System.out.println("================================= DANH SÁCH NGƯỜI ĐẠI DIỆN =================================");
            System.out.println("._______.____________________.____________________.____________________.____________________.");
            System.out.println("│   ID  │   Người đại diện   │       Địa chỉ      │     Số điện thoại  │    Tên Đơn vị      │");
            System.out.println("│_______│____________________│____________________│____________________│____________________│");
            rs = st.executeQuery("SELECT dbo.Representative.id, representative_name, representative_address, phone_number, company_name FROM Representative Join dbo.Company C on Representative.company_id = C.id ;");
            while (rs.next())
            {
                String ID = rs.getString("id");
                String representativeName = rs.getString("representative_name");
                String representativeAddress = rs.getString("representative_address");
                String phoneNumber = rs.getString("phone_number");
                String companyName = rs.getString("company_name");
                System.out.printf("│ %-5S │ %-18s │ %-18s │ %-18s │ %-18s │\n", ID, representativeName, representativeAddress ,phoneNumber ,companyName);
                System.out.println("│_______│____________________│____________________│____________________│____________________│");
            }
            System.out.println("====================================  DANH SÁCH KẾT THÚC =================================== ");
            InputValidatorKhue.waitForEnter();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }finally {
            if(rs!=null){
                rs.close();
            }
            if(st!=null){
                st.close();
            }
        }
    }

    public void handleRepresentative(RepresentativeManager representativeManager, Scanner scanner) {
        System.out.println("Quản lý người đại diện - Chọn chức năng:");
        System.out.println("1. Hiển thị danh sách người đại diện");
        System.out.println("2. Thêm người đại diện");
        System.out.println("3. Xóa người đại diện");
        System.out.println("4. Sửa thông tin người đại diện");
        System.out.println("0. Quay lại menu chính");

        int choice = -1;

        do {
            try {
                System.out.print("Vui lòng chọn: ");
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
                        System.out.print("Nhập ID người đại diện cần xóa: ");
                        int representativeIdToDelete = Integer.parseInt(scanner.nextLine());
                        representativeManager.deleteRepresentative(representativeIdToDelete);
                        System.out.println("Người đại diện có ID " + representativeIdToDelete + " đã được xóa thành công.");
                        break;
                    case 4:
                        // Sửa thông tin người đại diện
                        representativeManager.updateRepresentativeFromConsoleInput(scanner);
                        break;
                    case 0:
                        // Quay lại menu chính
                        break;
                    default:
                        System.out.println("Lựa chọn không hợp lệ.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Vui lòng nhập số.");
            } catch (SQLException e) {
                System.out.println("Lỗi SQL: " + e.getMessage());
            }
        } while (choice != 0);
    }

}
