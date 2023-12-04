package Model;

import Common.DBConnect;
import Common.InputValidatorKhue;
import Common.JDBCQuery;

import java.sql.*;
import java.util.*;

public class CompanyManager {
    private final Connection connection;
    public CompanyManager(Connection connection){
        this.connection =connection;
    }

    public CompanyManager(){
        this.connection= DBConnect.connectDatabase();
    }

    // Trình quản lý bảng Company
    public void handleCompany(CompanyManager companyManager, Scanner scanner) {
        System.out.println("Company Management:");
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
        String companyName = InputValidatorKhue.validateString("Name:");
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
    }

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
                int id = InputValidatorKhue.validateIntInput("Input company ID you need to fix: ");
                int companyId = getCompanyObject().get(id).getId();
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

//        } catch (NumberFormatException e){
//            System.out.println("ID invalid");
        } catch (SQLException e){
            System.out.println("Error update: " +e.getMessage());
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

    public void updatecompanyidForRepresentative(int companyId) throws SQLException{
        int id = getCompanyObject().get(companyId).getId();
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
        int id = getCompanyObject().get(companyId).getId();
        String query = "DELETE FROM Company WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int row = statement.executeUpdate();
            if(row>0){
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
    public Map<Integer, Company> getCompanyObject() {
        Map<Integer, Company> indexObjectMap = new LinkedHashMap<>();
        try {
            JDBCQuery.openConnection();
            String sql = "SELECT * FROM Company ORDER BY id desc";
            ResultSet rs = JDBCQuery.executeSelectQuery(sql);
            if (rs != null) {
                int index = 1;
                try {
                    while (rs.next()) {
                        Company company = new Company(
                                rs.getInt("id"),
                                rs.getString("company_name"),
                                rs.getString("company_address")
                        );
                        indexObjectMap.put(index, company);
                        index++;
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        } catch (Exception ex2) {
            ex2.printStackTrace();

        } finally {
            JDBCQuery.closeConnection();

        }
        return indexObjectMap;
    }

    //Hiển thị bảng Company
    public void displayCompany() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st =connection.createStatement();
            System.out.println();
            System.out.println("._______._________________________.______________________________.");
            System.out.println("│   No  │         Company         │            Address           │");
            System.out.println("│_______│_________________________│______________________________│");
            rs = st.executeQuery("SELECT * FROM Company ORDER BY id desc");
            int index = 1;
            while (rs.next())
            {
                String id = rs.getString("id");
                String companyName = rs.getString("company_name");
                String companyAddress = rs.getString("company_address");
                System.out.printf("│ %-5S │ %-23s │ %-28s │\n", index, companyName, companyAddress );
                System.out.println("│_______│_________________________│______________________________│");
                index ++;
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