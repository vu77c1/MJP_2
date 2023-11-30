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

    // Trình quản lý bảng Company
    public void handleCompany(CompanyManager companyManager, Scanner scanner) {
        System.out.println("Company Management:");
        System.out.println("1. Display");
        System.out.println("2. Add");
        System.out.println("3. Delete");
        System.out.println("4. Display");
        System.out.println("0. Exit");

        int choice = -1;

        do {
            try {
                System.out.print("Please choose: ");
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
                        int companyIdToDelete = InputValidatorKhue.validateIntInput("Input company ID you need to delete: ");
                        companyManager.updatecompanyidForRepresentative(companyIdToDelete);
                        companyManager.deleteCompany(companyIdToDelete);

                        break;
                    case 4:
                        // Sửa đơn vị ủng hộ
                        companyManager.updateCompanyFromConsoleInput(scanner);
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

    public Company inputCompany(Scanner scanner){
        Company newCompany = new Company();
        String companyName = InputValidatorKhue.validateString("Copany name:");
        newCompany.setCompanyName(companyName);
        String companyAddrress = InputValidatorKhue.validateString("Address:");
        newCompany.setCompanyAddrress(companyAddrress);
        return newCompany;
    }

    //Thêm Company vào cơ sở dữ liệu và trả về companyName
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
                System.out.println("Add success!!.");
            } else {
                System.out.println("Add unsuccess!!");
            }

        } catch (SQLException e) {
            System.out.println("Error while adding company: " + e.getMessage());
        }
        finally {
            JDBCQuery.closeConnection();
        }
    }

    // Phương thức sửa thông tin Company trong cơ sở dữ liệu
    public void updateCompanyFromConsoleInput(Scanner scanner) {
        try {
            boolean checkCompanyId =false;
            do {
                int companyId = InputValidatorKhue.validateIntInput("Input company ID you need to fix: ");
                Company existingCompany = getSingleCompanyById(companyId);
                if (existingCompany != null) {
                    Company updatedCompany = inputCompany(scanner);
                    updatedCompany.setId(companyId);
                    updateCompany(updatedCompany);
                    System.out.println("Update success!!");
                    checkCompanyId =true;
                } else {
                    System.out.println("ID not found: " + companyId);
                }
            } while (!checkCompanyId);

        } catch (NumberFormatException e){
            System.out.println("ID invalid");
        } catch (SQLException e){
            System.out.println("Error update: " +e.getMessage());
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

    public void updatecompanyidForRepresentative(int id) throws SQLException{
        String query ="UPDATE Representative set company_id = Null WHERE company_id ="+ id;
        try (PreparedStatement statement =connection.prepareStatement(query)){
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
            if(statement.executeUpdate()>0){
                System.out.println("\t\tDelete success!!");
            } else {
                System.out.println("\t\t\tDelete failed. No company found with the specified ID.");
            }

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

    //Hiển thị bảng Company
    public void displayCompany() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st =connection.createStatement();
            System.out.println();
            System.out.println("._______._________________________.______________________________.");
            System.out.println("│   ID  │         Company         │            Address           │");
            System.out.println("│_______│_________________________│______________________________│");
            rs = st.executeQuery("SELECT * FROM Company");
            while (rs.next())
            {
                String id = rs.getString("id");
                String companyName = rs.getString("company_name");
                String companyAddress = rs.getString("company_address");
                System.out.printf("│ %-5S │ %-23s │ %-28s │\n", id, companyName, companyAddress );
                System.out.println("│_______│_________________________│______________________________│");
            }
            InputValidatorKhue.waitForEnter();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error in the database connection process");
        }finally {
            if(rs!=null){
                rs.close();
            }
            if(st!=null){
                st.close();
            }
        }
    }

}