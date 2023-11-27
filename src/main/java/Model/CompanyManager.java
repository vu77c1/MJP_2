package Model;

import Common.DBConnect;
import Common.InputValidatorKhue;
import Common.JDBCQuery;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CompanyManager {
    private Connection connection;
    public CompanyManager(Connection connection){
        this.connection =connection;
    };
    public CompanyManager(){
        this.connection= DBConnect.connectDatabase();
    }
    public Company inputCompany(Scanner scanner){
        Company newCompany = new Company();
//      System.out.print("Tên công ty:");
        String companyName = InputValidatorKhue.validateStringCompany("Tên công ty:");
        newCompany.setCompanyName(companyName);
//        System.out.print("Địa chỉ:");
        String companyAddrress = InputValidatorKhue.validateStringCompany("Địa chỉ:");
        newCompany.setCompanyAddrress(companyAddrress);
        return newCompany;
    }
    public String addCompanyInput(Scanner scanner) {
        Company newCompany = inputCompany(scanner);
        addCompany(newCompany);
        return newCompany.getCompanyName();
    };

    public void addCompany(Company newCompany) {
        String query = "INSERT INTO Company (company_name, company_address) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newCompany.getCompanyName());
            preparedStatement.setString(2, newCompany.getCompanyAddrress());


            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Đơn vị ủng hộ đã được thêm vào cơ sở dữ liệu.");
            } else {
                System.out.println("Không thể thêm đơn vị ủng hộ mới.");
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi thêm đơn vị ủng hộ: " + e.getMessage());
        }
        finally {
            JDBCQuery.closeConnection();
        }
    }

    // Phương thức sửa thông tin Company trong cơ sở dữ liệu
    public void updateCompanyFromConsoleInput(Scanner scanner) {
        try {
            System.out.print("Nhập ID đơn vị ủng hộ cần sửa: ");
            int companyId = Integer.parseInt(scanner.nextLine());

            Company existingCompany = getSingleCompanyById(companyId);
            if (existingCompany != null) {
                Company updatedCompany = inputCompany(scanner);
                updatedCompany.setId(companyId);
                updateCompany(updatedCompany);
                System.out.println("Thông tin đơn vị ủng hộ đã được cập nhật");
            } else {
                System.out.println("Không tìm thấy đơn vị ủng hộ có ID là: " + companyId);
            }
        } catch (NumberFormatException e){
            System.out.println("ID không hợp lệ");
        } catch (SQLException e){
            System.out.println("Lỗi khi cập nhật đơn vị ủng hộ: " +e.getMessage());
        }finally {
            JDBCQuery.closeConnection();
        }
    }


    public void updateCompany(Company company) throws SQLException{
        String query ="UPDATE Company set company_name = ?, company_address = ? WHERE id =?";
        try (PreparedStatement statement =connection.prepareStatement(query)){
            statement.setString(1,company.getCompanyName());
            statement.setString(2,company.getCompanyAddrress());
            statement.setInt(3,company.getId());

            statement.executeUpdate();

        } finally {
            JDBCQuery.closeConnection();
        }

    }

    public Company getSingleCompanyById(int id) throws SQLException {
        String query = "SELECT * FROM Company WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Company company = new Company();
                    company.setId(resultSet.getInt("id"));
                    company.setCompanyName(resultSet.getString("company_name"));
                    company.setCompanyAddrress(resultSet.getString("company_address"));
                    return company;
                }
            }
        }finally {
            JDBCQuery.closeConnection();
        }
        return null;
    }

    public int getSingleCompanyByName(String name) throws SQLException {
        String query = "SELECT * FROM Company WHERE company_name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                }
            }
        } finally {
            JDBCQuery.closeConnection();
        }
        return -1;
    }

    // Phương thức xóa một Company từ cơ sở dữ liệu

    public void deleteCompany(int companyId) throws SQLException {
        String query = "DELETE FROM Company WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, companyId);
            statement.executeUpdate();
        }
        finally {
            JDBCQuery.closeConnection();
        }
    }

    // Phương thức lấy danh sách tất cả các Company từ cơ sở dữ liệu
    public List<Company> getAllCompany() throws SQLException {
        List<Company> companies = new ArrayList<>();
        String query = "SELECT * FROM Company";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Company company = new Company();
                company.setId(resultSet.getInt("id"));
                company.setCompanyName(resultSet.getString("company_name"));
                // Thêm các trường thông tin khác tương tự
                companies.add(company);
            }
        }
        finally {
            JDBCQuery.closeConnection();
        }
        return companies;
    }

    public void displayCompany() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st =connection.createStatement();
            System.out.println();
            System.out.println("============== DANH SÁCH ĐƠN VỊ ỦNG HỘ ==============");
            System.out.println("================= DANH SÁCH KẾT THÚC ================");
            System.out.println("._______.____________________.______________________.");
            System.out.println("│   ID  │    Tên Đơn vị      │    Địa chỉ đơn vị    │");
            System.out.println("│_______│____________________│____________________│");
            rs = st.executeQuery("SELECT * from Company");
            while (rs.next())
            {
                String id = rs.getString("id");
                String companyName = rs.getString("company_name");
                String companyAddress = rs.getString("company_address");
                System.out.printf("│ %-5S │ %-18s │ %-18s │\n", id, companyName, companyAddress );
                System.out.println("│_______│____________________│____________________│");
            }
            System.out.println("================= DANH SÁCH KẾT THÚC ================");
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

    public void handleCompany(CompanyManager companyManager, Scanner scanner) {
        System.out.println("Quản lý đơn vị ủng hộ - Chọn chức năng:");
        System.out.println("1. Hiển thị đơn vị ủng hộ");
        System.out.println("2. Thêm đơn vị ủng hộ");
        System.out.println("3. Xóa đơn vị ủng hộ");
        System.out.println("4. Sửa thông tin đơn vị ủng hộ");
        System.out.println("0. Quay lại menu chính");

        int choice = -1;

        do {
            try {
                System.out.print("Vui lòng chọn: ");
                choice = Integer.parseInt(scanner.nextLine());
                switch (choice) {
                    case 1:
                        companyManager.displayCompany();
                        break;
                    case 2:
                        // Thêm đơn vị ủng hộ
                        companyManager.addCompanyInput(scanner);
                        break;
                    case 3:
                        // Xóa đơn vị ủng hộ
                        System.out.print("Nhập ID đơn vị ủng hộ cần xóa: ");
                        int representativeIdToDelete = Integer.parseInt(scanner.nextLine());
                        companyManager.deleteCompany(representativeIdToDelete);
                        System.out.println("Đơn vị ủng hộ có ID " + representativeIdToDelete + " đã được xóa thành công.");
                        break;
                    case 4:
                        // Sửa đơn vị ủng hộ
                        companyManager.updateCompanyFromConsoleInput(scanner);
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
