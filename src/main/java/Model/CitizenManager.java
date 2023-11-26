package Model;

import Common.DBConnect;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class CitizenManager {
    private Connection connection;
    private DateFormat dateFormat;

    // Constructor nhận connection từ bên ngoài
    public CitizenManager(Connection connection) {
        this.connection = connection;
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    // Constructor mặc định, kết nối đến DBConnect và khởi tạo SimpleDateFormat
    public CitizenManager() {
        this.connection = DBConnect.connectDatabase();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    public static boolean isValidBooleanInput(String input) {
        return input.equalsIgnoreCase("true") || input.equalsIgnoreCase("false");
    }

    public static boolean isValidIdentityCard(String cmnd) {
        return cmnd.matches("\\d{12}");
    }

    public static boolean isValidName(String name) {
        return name.matches("[a-zA-Z\\s]+") && name.length() > 0 && name.length() <= 255;
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("\\d{10}");
    }

    public static boolean isValidAddress(String address) {
        return !address.trim().isEmpty() && address.length() <= 255;
    }
    public static boolean isValidHouseId(int houseId) {
        // Kết nối đến cơ sở dữ liệu và thực hiện truy vấn để kiểm tra tồn tại của ID trong bảng House
        try (Connection connection = DBConnect.connectDatabase();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM House WHERE id = ?")) {
            statement.setInt(1, houseId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Trả về true nếu ID tồn tại, ngược lại trả về false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean isValidCitizenObjectId(int citizenObjectId) {
        // Kết nối đến cơ sở dữ liệu và thực hiện truy vấn để kiểm tra tồn tại của ID trong bảng PriorityObject
        try (Connection connection = DBConnect.connectDatabase();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM CitizenObject WHERE id = ?")) {
            statement.setInt(1, citizenObjectId);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next(); // Trả về true nếu ID tồn tại, ngược lại trả về false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Citizen enterCitizenDetailsFromConsole(Scanner scanner) {
        Citizen newCitizen = new Citizen();

        System.out.println("Enter new Citizen:");

        System.out.print("Name: ");
        String name = scanner.nextLine();
        while (!isValidName(name)) {
            System.out.println("Invalid name. Please re-enter your name.");
            System.out.print("Name: ");
            name = scanner.nextLine();
        }
        newCitizen.setName(name);

        System.out.print("IdentityCard  Number : ");
        String cmnd = scanner.nextLine();
        while (!isValidIdentityCard(cmnd)) {
            System.out.println("ID card number is invalid. Please re-enter your ID card number (12 numbers).");
            System.out.print("IdentityCard  Number: ");
            cmnd = scanner.nextLine();
        }
        newCitizen.setIdentityCard(cmnd);

        System.out.print("Date of birth (yyyy-MM-dd): ");
        String dobString = scanner.nextLine();
        try {
            Date dob = java.sql.Date.valueOf(dobString);
            newCitizen.setDateOfBirth(dob);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Use format yyyy-MM-dd.");
        }

        // Yêu cầu người dùng nhập các thông tin khác cho công dân
        String phoneNumber;
        do {
            System.out.print("Phone Number: ");
            phoneNumber = scanner.nextLine();
        } while (!isValidPhoneNumber(phoneNumber));
        newCitizen.setPhoneNumber(phoneNumber);

        String address;
        do {
            System.out.print("Address: ");
            address = scanner.nextLine();
        } while (!isValidAddress(address));
        newCitizen.setAddress(address);

        int houseId = -1;
        do {
            System.out.print("House id : ");
            houseId = Integer.parseInt(scanner.nextLine());
        } while (!isValidHouseId(houseId));
        newCitizen.setHouseId(houseId);

        String isHouseholdLordInput;
        do {
            System.out.print("Is household lord?  (true/false): ");
            isHouseholdLordInput = scanner.nextLine();
        } while (!isValidBooleanInput(isHouseholdLordInput));
        boolean isHouseholdLord = Boolean.parseBoolean(isHouseholdLordInput);
        newCitizen.setHouseholdLord(isHouseholdLord);

        String sexInput;
        do {
            System.out.print("Sex (true-male/false-female): ");
            sexInput = scanner.nextLine();
        } while (!isValidBooleanInput(sexInput));
        boolean sex = Boolean.parseBoolean(sexInput);
        newCitizen.setSex(sex);

        int citizenObjectId = -1;
        do {
            System.out.print("Citizen object id : ");
            citizenObjectId = Integer.parseInt(scanner.nextLine());
        } while (!isValidCitizenObjectId(citizenObjectId));
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
            if (newCitizen.getDateOfBirth() != null) {
                preparedStatement.setDate(3, new java.sql.Date(newCitizen.getDateOfBirth().getTime()));
            } else {
                preparedStatement.setNull(3, Types.DATE);
            }
            preparedStatement.setString(4, newCitizen.getPhoneNumber());
            preparedStatement.setString(5, newCitizen.getAddress());
            preparedStatement.setInt(6, newCitizen.getHouseId());
            preparedStatement.setBoolean(7, newCitizen.isHouseholdLord());
            preparedStatement.setBoolean(8, newCitizen.getSex());
            preparedStatement.setInt(9, newCitizen.getCitizenObjectId());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("New citizens have been added to the database.");
            } else {
                System.out.println("New citizens cannot be added.");
            }
        } catch (SQLException e) {
            System.out.println("Error when adding new citizens: " + e.getMessage());
        }
    }

    // Phương thức sửa thông tin một Citizen trong cơ sở dữ liệu
    public void updateCitizenFromConsoleInput(Scanner scanner) {
        try {
            System.out.print("Enter the ID of the citizen needing update: ");
            int citizenId = Integer.parseInt(scanner.nextLine());

            if (!isValidCitizenId(citizenId)) {
                System.out.println("Invalid ID.");
                return;
            }

            Citizen existingCitizen = getSingleCitizenById(citizenId);
            if (existingCitizen != null) {
                Citizen updatedCitizen = enterCitizenDetailsFromConsole(scanner);
                updateCitizen(updatedCitizen);
                System.out.println("Citizen information has been updated.");
            } else {
                System.out.println("Citizen whose ID was not found " + citizenId);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid ID.");
        } catch (SQLException e) {
            System.out.println("Error updating citizens: " + e.getMessage());
        }
    }
    public boolean isValidCitizenId(int citizenId) {
          return citizenId >= 0 && String.valueOf(citizenId).matches("\\d+");
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
    public boolean isCitizenIdExists(int citizenId) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM Citizen WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, citizenId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }
    public void deleteCitizen(int citizenId) throws SQLException {
        if (citizenId <= 0) {
            System.out.println("Invalid ID.");
            return;
        }

        // Kiểm tra sự tồn tại của ID trong bảng Citizen
        if (!isCitizenIdExists(citizenId)) {
            System.out.println("Citizen with ID " + citizenId + " does not exist.");
            return;
        }

        String query = "DELETE FROM Citizen WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, citizenId);
            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Citizen with ID " + citizenId + " has been deleted.");
            } else {
                System.out.println("No citizen found with ID " + citizenId + ".");
            }
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
    public void displayCitizenTable() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connection.createStatement();
            String sql = "SELECT * FROM Citizen";
            rs = st.executeQuery(sql);

            System.out.println("=================================== Citizen Table ====================================");
            System.out.println(String.format("| %-5s | %-30s | %-15s | %-12s | %-25s | %-7s | %-15s | %-12s | %-18s |",
                    "ID", "Name", "Identity Card", "Date of Birth", "Phone Number", "Address", "House ID", "Is Household Lord", "Sex"));
            System.out.println(String.format("| %-5s | %-30s | %-15s | %-12s | %-25s | %-7s | %-15s | %-12s | %-18s |",
                    "-----", "------------------------------", "---------------", "------------", "-------------------------", "-------", "---------------", "-----------------", "-----------------"));
            while (rs.next()) {
                System.out.println(String.format("| %-5s | %-30s | %-15s | %-12s | %-25s | %-7s | %-15s | %-12s | %-18s |",
                        rs.getInt("id"), rs.getString("name"), rs.getString("identity_card"),
                        dateFormat.format(rs.getDate("date_of_birth")), rs.getString("phone_number"),
                        rs.getString("address"), rs.getInt("house_id"), rs.getBoolean("is_household_lord"),
                        rs.getBoolean("sex")));
                System.out.println(String.format("| %-5s | %-30s | %-15s | %-12s | %-25s | %-7s | %-15s | %-12s | %-18s |",
                        "-----", "------------------------------", "---------------", "------------", "-------------------------", "-------", "---------------", "-----------------", "-----------------"));
            }
            System.out.println();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }
    public static void handleCitizenManagement(CitizenManager citizenManager, Scanner scanner) {
        int choice = -1;

        do {
            try {
                System.out.println("Citizen Management : ");
                System.out.println("\t\t\t0. Exit");
                System.out.println("\t\t\t1. Add");
                System.out.println("\t\t\t2. Delete");
                System.out.println("\t\t\t3. Update");
                System.out.println("\t\t\t4. Display");
                System.out.print("Please choose: ");
                choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        // Thêm công dân
                        citizenManager.addCitizenFromConsoleInput(scanner);
                        break;
                    case 2:
                        // Xóa công dân
                        System.out.print("Input id to delete: ");
                        int citizenIdToDelete = Integer.parseInt(scanner.nextLine());
                        citizenManager.deleteCitizen(citizenIdToDelete);
                        System.out.println("Citizen with ID " + citizenIdToDelete + " deleted.");
                        break;
                    case 3:
                        // Sửa thông tin công dân
                        citizenManager.updateCitizenFromConsoleInput(scanner);
                        break;
                    case 4:
                        // Hiển thị thông tin tất cả công dân
                        citizenManager.displayCitizenTable();
                        break;
                    case 0:
                        // Thoát khỏi chương trình
                        break;
                    default:
                        System.out.println("Invalid selection.");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            } catch (SQLException e) {
                System.out.println("SQL Error: " + e.getMessage());
            }
        } while (choice != 0);
    }



}
