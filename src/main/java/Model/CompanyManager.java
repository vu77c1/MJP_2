package Model;

import Common.DBConnect;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
//        System.out.print("Có đại diện cho công ty nào không (true/false) ?");
//        boolean isCompany = Boolean.parseBoolean(scanner.nextLine());
//        if(isCompany){
            System.out.print("Tên công ty:");
            newCompany.setCompanyName(scanner.nextLine());
            System.out.print("Địa chỉ:");
            newCompany.setCompanyAddrress(scanner.nextLine());
//        }
        return newCompany;
    }
    public void addCompanyInput(Scanner scanner) {
        Company newCompany = inputCompany(scanner);
        addCompany(newCompany);
    }

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
        }
    }


    public void updateCompany(Company company) throws SQLException{
        String query ="UPDATE Company set company_name = ?, company_address = ? WHERE id =?";
        try (PreparedStatement statement =connection.prepareStatement(query)){
            statement.setString(1,company.getCompanyName());
            statement.setString(2,company.getCompanyAddrress());
            statement.setInt(3,company.getId());

            statement.executeUpdate();

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
        }
        return null;
    }

    // Phương thức xóa một Company từ cơ sở dữ liệu

    public void deleteCompany(int companyId) throws SQLException {
        String query = "DELETE FROM Company WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, companyId);
            statement.executeUpdate();
        }
    }

    // Phương thức lấy danh sách tất cả các Company từ cơ sở dữ liệu
    public List<Company> getAllRepresentative() throws SQLException {
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
        return companies;
    }
}
