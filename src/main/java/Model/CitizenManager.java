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
    private boolean isValidDateFormat(String dobString) {
        try {
            java.sql.Date.valueOf(dobString);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
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
        String dobString;
        do {
            System.out.print("Date of birth (yyyy-MM-dd): ");
            dobString = scanner.nextLine();

            if (isValidDateFormat(dobString)) {
                try {
                    Date dob = java.sql.Date.valueOf(dobString);
                    newCitizen.setDateOfBirth(dob);
                } catch (IllegalArgumentException e) {
                    System.out.println("Invalid date format. Use format yyyy-MM-dd.");
                }
            } else {
                System.out.println("Invalid date format. Please use format yyyy-MM-dd.");
            }
        } while (!isValidDateFormat(dobString));

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
    public void displayCitizenTable() {
        Statement st = null;
        ResultSet rs = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            st = connection.createStatement();
            String sql = "SELECT * FROM Citizen";
            rs = st.executeQuery(sql);

            System.out.println("=================================== Citizen Table ====================================");
            System.out.println("| ID    | Name                           | Identity Card   | Date of Birth | Phone Number         | Address                | House ID        | Is Household Lord | Gender |");
            System.out.println("| ----- | ------------------------------ | --------------- | --------------| --------------------- | ---------------------- | --------------- | ----------------- | ------ |");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String identityCard = rs.getString("identity_card");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");
                int houseId = rs.getInt("house_id");
                boolean isHouseholdLord = rs.getBoolean("is_household_lord");
                boolean gender = rs.getBoolean("sex");

                // Định dạng lại ngày tháng từ kiểu Date sang chuỗi theo định dạng dd/MM/yyyy
                String dateOfBirthString = "";
                Date dateOfBirth = rs.getDate("date_of_birth");
                if (dateOfBirth != null) {
                    dateOfBirthString = dateFormat.format(dateOfBirth);
                }

                System.out.println(String.format("| %-5s | %-30s | %-15s | %-13s | %-21s | %-24s | %-15s | %-17s | %-6s |",
                        id, name, identityCard, dateOfBirthString, phoneNumber, address, houseId, isHouseholdLord, gender ? "Male" : "Female"));
                System.out.println("| ----- | ------------------------------ | --------------- | --------------| --------------------- | ---------------------- | --------------- | ----------------- | ------ |");
            }
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } finally {
            // Đóng ResultSet và Statement sau khi sử dụng xong
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }
    public void fetchCitizenInfoWithPriorityFactor() {
        String query = "SELECT h.id AS house_id, hh.name AS household_head_name, " +
                "SUM(" +
                "    CASE " +
                "        WHEN DATEDIFF(YEAR, c.date_of_birth, GETDATE()) <= 8 THEN 1 " +
                "        WHEN DATEDIFF(YEAR, c.date_of_birth, GETDATE()) >= 60 THEN 1 " +
                "        ELSE " +
                "            CASE " +
                "                WHEN co.type_name_object IN ('Phu nu mang thai', 'Nguoi tan tat','Tre em') THEN 1 " +
                "                ELSE 0 " +
                "            END " +
                "    END" +
                ") AS total_priority_factor " +
                "FROM " +
                "    House h " +
                "LEFT JOIN " +
                "    Citizen c ON h.id = c.house_id " +
                "LEFT JOIN " +
                "    CitizenObject co ON c.citizen_object_id = co.id " +
                "LEFT JOIN " +
                "    Citizen hh ON h.id = hh.house_id AND hh.is_household_lord = 1 " +
                "WHERE " +
                "    hh.name IS NOT NULL " +
                "GROUP BY " +
                "    h.id, hh.name " +
                "ORDER BY " +
                "    total_priority_factor DESC";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("================================== House Priority Factor ==================================");
            System.out.println("| House ID | Household Head Name     | Total Priority Factor |");
            System.out.println("|----------|--------------------------|-----------------------|");

            while (resultSet.next()) {
                int houseId = resultSet.getInt("house_id");
                String householdHeadName = resultSet.getString("household_head_name");
                int totalPriorityFactor = resultSet.getInt("total_priority_factor");

                System.out.printf("| %-9s| %-25s| %-22s|%n", houseId, householdHeadName, totalPriorityFactor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

//    public void processPriorityDecision(Scanner scanner, Connection connection) {
//        System.out.println("Bạn muốn xét duyệt ưu tiên là có trẻ em? (yes/no)");
//        String decision = scanner.nextLine().toLowerCase();
//
//        if (decision.equals("yes")) {
//            processPregnantWomanDecision(scanner, connection);
//        } else {
//            System.out.println("Không cần thực hiện xét duyệt ưu tiên là có trẻ em.");
//        }
//    }
//
//    public void processPregnantWomanDecision(Scanner scanner, Connection connection) {
//        System.out.println("Xoá xét duyệt ưu tiên là phụ nữ mang thai? (yes/no)");
//        String input = scanner.nextLine().toLowerCase();
//
//        if (input.equals("yes")) {
//            System.out.println("Bạn muốn thêm 1 ưu tiên trẻ em và thêm nhân khẩu không? (yes/no)");
//            String confirmationInput = scanner.nextLine().toLowerCase();
//
//            if (confirmationInput.equals("yes")) {
//                int houseId = getHouseIdOfPregnantWoman(connection);
//                int childCitizenObjectId = getCitizenObjectIdForChild(connection);
//
//                if (houseId != -1 && childCitizenObjectId != -1) {
//                    updateCitizenObject(connection);
//                    String insertQuery = "INSERT INTO Citizen (house_id, citizen_object_id) VALUES (?, ?)";
//                    performInsert(connection, houseId, childCitizenObjectId);
//                } else {
//                    System.out.println("Không thể lấy thông tin cần thiết.");
//                }
//            } else {
//                System.out.println("Không cập nhật.");
//            }
//        } else {
//            System.out.println("Không cần thực hiện xoá.");
//        }
//    }
//
//    // Các hàm xử lý truy vấn và thao tác với cơ sở dữ liệu
//    public int getHouseIdOfPregnantWoman(Connection connection) {
//        // Thực hiện truy vấn để lấy house_id của người phụ nữ mang thai từ CSDL
//        String query = "SELECT house_id FROM Citizen WHERE citizen_object_id = (SELECT id FROM CitizenObject WHERE type_name_object = 'Phu nu mang thai')";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getInt("house_id");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//    public int getCitizenObjectIdForChild(Connection connection) {
//        // Thực hiện truy vấn để lấy citizen_object_id của trẻ em từ CSDL
//        String query = "SELECT id FROM CitizenObject WHERE type_name_object = 'Tre em'";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            ResultSet resultSet = statement.executeQuery();
//            if (resultSet.next()) {
//                return resultSet.getInt("id");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return -1;
//    }
//
//    public void updateCitizenObject(Connection connection) {
//        // Thực hiện cập nhật citizen_object_id từ 'phu nu mang thai' sang 'nguoi binh thuong'
//        String updateQuery = "UPDATE Citizen SET citizen_object_id = (SELECT id FROM CitizenObject WHERE type_name_object = 'Binh Thuong') WHERE citizen_object_id = (SELECT id FROM CitizenObject WHERE type_name_object = 'Phu nu mang thai')";
//        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
//            statement.executeUpdate();
//            System.out.println("Đã cập nhật xét duyệt ưu tiên phụ nữ mang thai.");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void performInsert(Connection connection, int houseId, int childCitizenObjectId) {
//        String insertQuery = "INSERT INTO Citizen (house_id, citizen_object_id) VALUES (?, ?)";
//
//        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
//            insertStatement.setInt(1, houseId);
//            insertStatement.setInt(2, childCitizenObjectId);
//            // Đặt các giá trị cần thiết vào các cột khác trong câu lệnh INSERT
//            // Ví dụ:
//            // insertStatement.setString(3, "Tên giá trị");
//            // insertStatement.setInt(4, 123);
//
//            int rowsInserted = insertStatement.executeUpdate();
//            System.out.println("Đã thêm ưu tiên trẻ em và thêm nhân khẩu.");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

    public void processPriorityDecision(Scanner scanner, Connection connection) {
        System.out.println("Bạn muốn xét duyệt ưu tiên trong một house_id cụ thể? (yes/no)");
        String decision = scanner.nextLine().toLowerCase();

        if (decision.equals("yes")) {
            System.out.println("Nhập house_id:");
            int houseId = scanner.nextInt();
            scanner.nextLine(); // Đọc bỏ dòng trống sau khi nhập số

            processPregnantWomanDecision(scanner, connection, houseId);
            checkHouseBefore(connection, houseId);
            checkHouseAfter(connection,houseId);
        } else {
            // Xử lý logic khi không muốn xét duyệt theo house_id cụ thể
            System.out.println("Không cần thực hiện xét duyệt ưu tiên trong một house_id cụ thể.");
        }
    }

    // Hàm xử lý quyết định về phụ nữ mang thai trong house_id
    public void processPregnantWomanDecision(Scanner scanner, Connection connection, int houseId) {
        System.out.println("Xoá xét duyệt ưu tiên là phụ nữ mang thai? (yes/no)");
        String input = scanner.nextLine().toLowerCase();

        if (input.equals("yes")) {
            // Thêm đối tượng 'Trẻ em' trước khi xét duyệt
            int childCitizenObjectId = insertChildCitizenObjectIfNotExists(connection);
            if (childCitizenObjectId != -1) {
                System.out.println("Đã thêm đối tượng 'Trẻ em'.");

                // Tiếp tục xét duyệt
                System.out.println("Bạn muốn thêm 1 ưu tiên trẻ em và thêm nhân khẩu không? (yes/no)");
                String confirmationInput = scanner.nextLine().toLowerCase();

                if (confirmationInput.equals("yes")) {
                    updateCitizenObject(connection, houseId);
                    performInsert(connection, houseId, childCitizenObjectId);
                } else {
                    System.out.println("Không cập nhật.");
                }
            } else {
                System.out.println("Không thể thêm đối tượng 'Trẻ em'.");
                System.out.println("Không thể thực hiện xét duyệt.");
            }
        } else {
            System.out.println("Không cần thực hiện xoá.");
        }
    }

    public int insertChildCitizenObjectIfNotExists(Connection connection) {
        // Kiểm tra xem bản ghi 'Tre em' đã tồn tại trong bảng CitizenObject chưa
        if (!checkChildCitizenObjectExists(connection)) {
            // Nếu không tồn tại, thêm mới
            String insertQuery = "INSERT INTO CitizenObject (type_name_object) VALUES ('Tre em')";
            try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
                int rowsInserted = insertStatement.executeUpdate();
                if (rowsInserted > 0) {
                    ResultSet generatedKeys = insertStatement.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the ID of the newly inserted record
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            // Nếu đã tồn tại, trả về ID của 'Tre em' đã có sẵn
            return getChildCitizenObjectId(connection);
        }
        return -1; // Trả về -1 nếu có lỗi hoặc không thể thêm
    }

    public boolean checkChildCitizenObjectExists(Connection connection) {
        String query = "SELECT COUNT(*) AS count FROM CitizenObject WHERE type_name_object = 'Tre em'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0; // Trả về true nếu 'Tre em' đã tồn tại
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getChildCitizenObjectId(Connection connection) {
        String query = "SELECT id FROM CitizenObject WHERE type_name_object = 'Tre em'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    public int getHouseIdOfPregnantWoman(Connection connection, int houseId) {
        String query = "SELECT house_id FROM Citizen WHERE citizen_object_id = (SELECT id FROM CitizenObject WHERE type_name_object = 'Phu nu mang thai') AND house_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, houseId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("house_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int getCitizenObjectIdForChild(Connection connection, int houseId) {
        String query = "SELECT id FROM CitizenObject WHERE type_name_object = 'Tre em'";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void updateCitizenObject(Connection connection, int houseId) {
        String updateQuery = "UPDATE Citizen SET citizen_object_id = (SELECT id FROM CitizenObject WHERE type_name_object = 'Binh Thuong') WHERE citizen_object_id = (SELECT id FROM CitizenObject WHERE type_name_object = 'Phu nu mang thai') AND house_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
            statement.setInt(1, houseId);
            statement.executeUpdate();
            System.out.println("Đã cập nhật xét duyệt ưu tiên phụ nữ mang thai.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void performInsert(Connection connection, int houseId, int childCitizenObjectId) {
        String insertQuery = "INSERT INTO Citizen (house_id, citizen_object_id) VALUES (?, ?)";

        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            insertStatement.setInt(1, houseId);
            insertStatement.setInt(2, childCitizenObjectId);

            int rowsInserted = insertStatement.executeUpdate();
            System.out.println("Đã thêm ưu tiên trẻ em và thêm nhân khẩu.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void checkHouseBefore(Connection con, int houseId) {
        System.out.println("\t\t\tBefore ");
        Integer id=houseId;
        try {
            System.out.println("\t\t\t.___________________________________________________________________");
            System.out.println("\t\t\t│           Name HouseHold          │          Total people        │ ");
            System.out.println("\t\t\t│___________________________________│______________________________│ ");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("(select name, (select  count(Citizen.house_id) as SL from Citizen  where  Citizen.house_id="+id+") as SL from Citizen where is_household_lord=1 and Citizen.house_id="+id+")");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                Integer SL = resultSet.getInt("SL");

                System.out.printf("\t\t\t│\t\t\t%-14s\t\t\t│  \t\t\t\t%-14s │  \n",name,  SL-1);
                System.out.println("\t\t\t│___________________________________│______________________________│");
            } } catch (SQLException e) {

            System.out.println("\u001B[31mThere was an error during the Database connection process\u001B[0m");


        }
    }
    public void checkHouseAfter(Connection con, int houseId) {
        System.out.println("\t\t\tBefore ");
        Integer id=houseId;
        try {
            System.out.println("\t\t\t.___________________________________________________________________");
            System.out.println("\t\t\t│           Name HouseHold          │          Total people        │ ");
            System.out.println("\t\t\t│___________________________________│______________________________│ ");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("(select name, (select  count(Citizen.house_id) as SL from Citizen  where  Citizen.house_id="+id+") as SL from Citizen where is_household_lord=1 and Citizen.house_id="+id+")");
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                Integer SL = resultSet.getInt("SL");

                System.out.printf("\t\t\t│\t\t\t%-14s\t\t\t│  \t\t\t\t%-14s │  \n",name,  SL);
                System.out.println("\t\t\t│___________________________________│______________________________│");
            } } catch (SQLException e) {

            System.out.println("\u001B[31mThere was an error during the Database connection process\u001B[0m");


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
                System.out.print("\t\t\tPlease choose: ");
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
