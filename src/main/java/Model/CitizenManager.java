package Model;

import Common.DBConnect;

import java.sql.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
//import java.util.Date;

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

    public boolean isValidIdentityCard(String cmnd) {
        return cmnd.matches("\\d{12}") && !cmnd.matches("0{12}");
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
        return phoneNumber.matches("\\d{10}") && !phoneNumber.matches("0{10}");
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
    public Map<Integer, String> getHouseList() {
        Map<Integer, String> indexHouseMap = new LinkedHashMap<>();

        try (Connection connection = DBConnect.connectDatabase();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT H.id, CONCAT(C.precint_name, ', ', C.city_name, ', ', C.province_name, ', ', P.object_type) AS house_info, ROW_NUMBER() OVER (ORDER BY H.id) AS row_index " +
                             "FROM House H " +
                             "JOIN Commission C ON H.commission_id = C.id " +
                             "JOIN PriorityObject P ON H.priority_object_id = P.id");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                indexHouseMap.put(resultSet.getInt("row_index"), resultSet.getString("house_info"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return indexHouseMap;
    }



    public Map<Integer, String> getCitizenObjectList() {
        Map<Integer, String> indexCitizenObjectMap = new LinkedHashMap<>();

        try (Connection connection = DBConnect.connectDatabase();
             PreparedStatement statement = connection.prepareStatement("SELECT id, type_name_object, coefficient FROM CitizenObject");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String typeNameObject = resultSet.getString("type_name_object");
                int coefficient = resultSet.getInt("coefficient");

                String displayInfo = typeNameObject + " (Coefficient: " + coefficient + ")";
                indexCitizenObjectMap.put(id, displayInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return indexCitizenObjectMap;
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

        System.out.print("IdentityCard Number : ");
        String cmnd = scanner.nextLine();
        while (!isValidIdentityCard(cmnd)) {
            if (cmnd.matches("0{12}")) {
                System.out.println("ID card number cannot consist of 12 zeros. Please re-enter your ID card number.");
            } else {
                System.out.println("ID card number is invalid. Please re-enter your ID card number (12 numbers).");
            }
            System.out.print("IdentityCard Number: ");
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

        Map<Integer, String> houseMap = getHouseList();
        System.out.println("Select House:");
        for (Map.Entry<Integer, String> entry : houseMap.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }

        // Nhập index của House từ người dùng
        int houseIndex = -1;
        do {
            System.out.print("House Index: ");
            houseIndex = Integer.parseInt(scanner.nextLine());
        } while (!houseMap.containsKey(houseIndex));

        // Lấy ID của House từ danh sách ánh xạ
        int houseID = houseIndex; // Đây là ID của House được chọn

        // Lưu ID của House cho Citizen
        newCitizen.setHouseId(houseID);
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

        Map<Integer, String> citizenObjectMap = getCitizenObjectList();
        System.out.println("Select Citizen Object:");
        for (Map.Entry<Integer, String> entry : citizenObjectMap.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue());
        }

        // Nhập index của Citizen Object từ người dùng
        int citizenObjectIndex = -1;
        do {
            System.out.print("Citizen Object Index: ");
            citizenObjectIndex = Integer.parseInt(scanner.nextLine());
        } while (!citizenObjectMap.containsKey(citizenObjectIndex));

        // Lấy ID của Citizen Object từ danh sách ánh xạ
        int citizenObjectID = citizenObjectIndex; // Đây là ID của Citizen Object được chọn

        // Lưu ID của Citizen Object cho Citizen
        newCitizen.setCitizenObjectId(citizenObjectID);




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
    public void deleteCitizen(Connection connection) throws SQLException {


        LinkedHashMap<Integer, Integer> indexToIdMap = new LinkedHashMap<>();
        TreeMap<Integer, String> citizenData = new TreeMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String queryAllCitizens = "SELECT c.id, c.name, c.identity_card, c.date_of_birth, c.phone_number, c.address, c.is_household_lord, c.sex, co.type_name_object FROM Citizen c JOIN CitizenObject co ON c.citizen_object_id = co.id ORDER BY c.id DESC";
        try (PreparedStatement statementAllCitizens = connection.prepareStatement(queryAllCitizens)) {
            ResultSet rsAllCitizens = statementAllCitizens.executeQuery();

            int index = 1;
            while (rsAllCitizens.next()) {
                int citizenId = rsAllCitizens.getInt("id");

                String citizenInfo = String.format("Index: %d, Name: %s, Identity Card: %s, Date of Birth: %s, Phone Number: %s, Address: %s, Household Lord: %s, Sex: %s, Type: %s",
                        index,
                        rsAllCitizens.getString("name"),
                        rsAllCitizens.getString("identity_card"),
                        dateFormat.format(rsAllCitizens.getDate("date_of_birth")),
                        rsAllCitizens.getString("phone_number"),
                        rsAllCitizens.getString("address"),
                        rsAllCitizens.getBoolean("is_household_lord"),
                        rsAllCitizens.getBoolean("sex") ? "Male" : "Female",
                        rsAllCitizens.getString("type_name_object"));

                citizenData.put(index, citizenInfo);
                indexToIdMap.put(index, citizenId); // Associate the index with the corresponding citizen ID
                index++;
            }

            System.out.println("| Index | Name                   | Identity Card | Date of Birth | Phone Number | Address                | Household Lord | Sex    | Type                  |");
            System.out.println("| ----- | ---------------------- | ------------- | ------------- | ------------ | ---------------------- | -------------- | ------ | --------------------- |");

            for (Map.Entry<Integer, String> entry : citizenData.entrySet()) {
                String citizenInfo = entry.getValue();
                String[] infoParts = citizenInfo.split(", ");

                System.out.printf("| %-6s| %-22s| %-14s| %-14s| %-13s| %-22s| %-15s| %-7s| %-21s|%n",
                        infoParts[0].split(": ")[1],
                        infoParts[1].split(": ")[1],
                        infoParts[2].split(": ")[1],
                        infoParts[3].split(": ")[1],
                        infoParts[4].split(": ")[1],
                        infoParts[5].split(": ")[1],
                        infoParts[6].split(": ")[1],
                        infoParts[7].split(": ")[1],
                        infoParts[8].split(": ")[1]
                );
            }
            Scanner scanner = new Scanner(System.in);
            int chosenIndex;

            while (true) {
                System.out.println("Enter the index to delete the citizen: ");
                String input = scanner.nextLine();

                // Kiểm tra nếu chuỗi nhập vào không phải là số hoặc số âm
                if (!input.matches("\\d+") || Integer.parseInt(input) <= 0) {
                    System.out.println("Vui lòng chỉ nhập số dương. Vui lòng nhập lại.");
                    continue;
                }

                chosenIndex = Integer.parseInt(input);

                // Kiểm tra xem index có trong danh sách index hiển thị không
                if (!indexToIdMap.containsKey(chosenIndex)) {
                    System.out.println("Lựa chọn index không tồn tại. Vui lòng nhập lại.");
                    continue;
                }

                break; // Nếu index hợp lệ, thoát khỏi vòng lặp
            }

            int chosenId = indexToIdMap.getOrDefault(chosenIndex, -1);
            if (chosenId != -1) {
                // Perform deletion using chosenId
                String deleteQuery = "DELETE FROM Citizen WHERE id = ?";
                try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
                    deleteStatement.setInt(1, chosenId);
                    int rowsAffected = deleteStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("Citizen with ID " + chosenId + " has been deleted.");
                    } else {
                        System.out.println("No citizen found with ID " + chosenId + ".");
                    }
                }
            } else {
                System.out.println("Invalid index. Please enter a valid index.");
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
        LinkedHashMap<Integer, String> citizenData = new LinkedHashMap<>();
        Statement st = null;
        ResultSet rs = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try {
            st = connection.createStatement();
            String sql = "SELECT c.*, co.type_name_object, cm.precint_name " +
                    "FROM Citizen c " +
                    "JOIN CitizenObject co ON c.citizen_object_id = co.id " +
                    "JOIN House h ON c.house_id = h.id " +
                    "JOIN Commission cm ON h.commission_id = cm.id " +
                    "ORDER BY c.id DESC"; // Sắp xếp giảm dần theo ID
            rs = st.executeQuery(sql);

            System.out.println("=================================== Citizen Table ====================================");
            System.out.println("| Index | Name                           | Identity Card   | Date of Birth | Phone Number         | Address                | Precint Name    | Is Household Lord | Gender |   Name Object |");
            System.out.println("| ----- | ------------------------------ | --------------- | --------------| --------------------- | ---------------------- | --------------- | ----------------- | ------ | ---------------- |");

            int index = 1;
            while (rs.next()) {
                String name = rs.getString("name");
                String identityCard = rs.getString("identity_card");
                String phoneNumber = rs.getString("phone_number");
                String address = rs.getString("address");
                boolean isHouseholdLord = rs.getBoolean("is_household_lord");
                boolean gender = rs.getBoolean("sex");
                String typeNameObject = rs.getString("type_name_object");
                String precintName = rs.getString("precint_name");

                // Định dạng lại ngày tháng từ kiểu Date sang chuỗi theo định dạng dd/MM/yyyy
                String dateOfBirthString = "";
                Date dateOfBirth = rs.getDate("date_of_birth");
                if (dateOfBirth != null) {
                    dateOfBirthString = dateFormat.format(dateOfBirth);
                }

                // Kiểm tra tuổi để xác định loại đối tượng công dân
                int age = calculateAge(dateOfBirth);
                if (age <= 8) {
                    typeNameObject = "Tre em";
                } else if (age >= 60) {
                    typeNameObject = "Nguoi gia";
                }

                String citizenInfo = String.format("| %-5s | %-30s | %-15s | %-13s | %-21s | %-24s | %-15s | %-17s | %-6s | %-16s |",
                        index, name, identityCard, dateOfBirthString, phoneNumber, address, precintName, isHouseholdLord, gender ? "Male" : "Female", typeNameObject);

                citizenData.put(index, citizenInfo);
                index++;
            }

            // Hiển thị dữ liệu từ LinkedHashMap theo thứ tự giảm dần và cập nhật lại index
            LinkedHashMap<Integer, String> reversedMap = new LinkedHashMap<>();
            int reversedIndex = 1;

            for (Map.Entry<Integer, String> entry : citizenData.entrySet()) {
                reversedMap.put(reversedIndex, entry.getValue());
                reversedIndex++;
            }

            for (String citizenInfo : reversedMap.values()) {
                System.out.println(citizenInfo);
                System.out.println("| ----- | ------------------------------ | --------------- | --------------| --------------------- | ---------------------- | --------------- | ----------------- | ------ | ---------------- |");
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



    // Phương thức tính tuổi từ ngày sinh
    public int calculateAge(Date dateOfBirth) {
        if (dateOfBirth == null) {
            // Handle null dateOfBirth here
            return 0; // Or any appropriate default value
        }

        Calendar birthCalendar = new GregorianCalendar();
        birthCalendar.setTime(dateOfBirth);
        Calendar today = Calendar.getInstance();
        int age = today.get(Calendar.YEAR) - birthCalendar.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < birthCalendar.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }
        return age;
    }

    public void fetchCitizenInfoWithPriorityFactor() {
        String query = "SELECT com.precint_name AS precinct_name, hh.name AS household_head_name, " +
                "SUM(" +
                "    CASE " +
                "        WHEN co.type_name_object = 'Nguoi khuyet tat' THEN 4 " + // Người khuyết tật có ưu tiên cao nhất
                "        WHEN co.type_name_object = 'Phu nu mang thai' THEN 3 " + // Phụ nữ mang thai có ưu tiên cao hơn
                "        WHEN co.type_name_object = 'Tre em' THEN 2 " + // Trẻ em có ưu tiên tiếp theo
                "        WHEN co.type_name_object = 'Nguoi gia' THEN 1 " + // Người già có ưu tiên cuối cùng trước trường hợp bình thường
                "        ELSE 0 " + // Trường hợp còn lại (Bình thường)
                "    END" +
                ") AS total_priority_factor " +
                "FROM " +
                "    House h " +
                "LEFT JOIN " +
                "    Commission com ON h.commission_id = com.id " +
                "LEFT JOIN " +
                "    Citizen ci ON h.id = ci.house_id " +
                "LEFT JOIN " +
                "    CitizenObject co ON ci.citizen_object_id = co.id " +
                "LEFT JOIN " +
                "    Citizen hh ON h.id = hh.house_id AND hh.is_household_lord = 1 " +
                "WHERE " +
                "    hh.name IS NOT NULL " +
                "GROUP BY " +
                "    com.precint_name, hh.name " +
                "ORDER BY " +
                "    CASE " +
                "        WHEN MAX(CASE WHEN co.type_name_object = 'Nguoi khuyet tat' THEN 4 ELSE 0 END) > 0 THEN 4 " +
                "        WHEN MAX(CASE WHEN co.type_name_object = 'Tre em' THEN 2 ELSE 0 END) > 0 THEN 3 " +
                "        WHEN MAX(CASE WHEN co.type_name_object = 'Phu nu mang thai' THEN 3 ELSE 0 END) > 0 THEN 2 " +
                "        WHEN MAX(CASE WHEN co.type_name_object = 'Nguoi gia' THEN 1 ELSE 0 END) > 0 THEN 1 " +
                "        ELSE 0 " +
                "    END DESC";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            System.out.println("================================== Precinct Priority Factor ==================================");
            System.out.println("| Precinct Name | Household Head Name     | Total Priority Factor |");
            System.out.println("|---------------|--------------------------|-----------------------|");

            while (resultSet.next()) {
                String precinctName = resultSet.getString("precinct_name");
                String householdHeadName = resultSet.getString("household_head_name");
                int totalPriorityFactor = resultSet.getInt("total_priority_factor");

                System.out.printf("| %-14s| %-25s| %-22s|%n", precinctName, householdHeadName, totalPriorityFactor);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void displayHouseTable() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st = connection.createStatement();
            String sql = "SELECT H.id, CONCAT(C.precint_name, ', ', C.city_name, ', ', C.province_name) AS commission_info, P.object_type " +
                    "FROM House H " +
                    "INNER JOIN Commission C ON H.commission_id = C.id " +
                    "INNER JOIN PriorityObject P ON H.priority_object_id = P.id " +
                    "ORDER BY H.id DESC"; // Sắp xếp theo ID giảm dần
            rs = st.executeQuery(sql);

            System.out.println("=================================== House Table ====================================");
            System.out.println(String.format("| %-5s | %-35s | %-20s |", "ID", "Commission Information", "Priority Object Type"));
            System.out.println(String.format("| %-5s | %-35s | %-20s |", "-----", "-----------------------------------", "---------------------"));
            int index = 1; // Index bắt đầu từ 1
            while (rs.next()) {
                System.out.println(String.format("| %-5s | %-35s | %-20s |",
                        index, rs.getString("commission_info"), rs.getString("object_type")));
                System.out.println(String.format("| %-5s | %-35s | %-20s |", "-----", "-----------------------------------", "---------------------"));
                index++;
            }
            System.out.println();
        } finally {
            // Đóng ResultSet và Statement sau khi sử dụng xong
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public void processPriorityDecision(Scanner scanner, Connection connection) {
        int rowCount = 0;
        try {
            // Hiển thị bảng House trước khi người dùng nhập lựa chọn
            displayHouseTable();

            System.out.println("Do you want to prioritize approval? (yes/no)");
            String decision = scanner.nextLine().toLowerCase();

            if (decision.equals("yes")) {
                int selectedIndex;

                do {
                    System.out.println("Input the index of the house to approve:");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Please enter a valid index number.");
                        scanner.next(); // Đọc và loại bỏ dữ liệu không hợp lệ
                    }
                    selectedIndex = scanner.nextInt();
                    scanner.nextLine(); // Đọc bỏ dòng trống sau khi nhập số

                    if (selectedIndex <= 0 || selectedIndex > rowCount) {
                        System.out.println("Please enter a valid index within the displayed range.");
                    }
                } while (selectedIndex <= 0 || selectedIndex > rowCount);

                int houseId = mapSelectedIndexToHouseId(selectedIndex, connection);
                if (houseId != -1) {
                    processPregnantWomanDecision(scanner, connection, houseId);
                    checkHouseBefore(connection, houseId);
                    checkHouseAfter(connection, houseId);
                } else {
                    System.out.println("Invalid house index selected. Please select a valid index.");
                }
            } else {
                System.out.println("There is no need to perform priority review within a specific house_id.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int mapSelectedIndexToHouseId(int selectedIndex, Connection connection) throws SQLException {
        PreparedStatement statement = null;
        ResultSet rs = null;
        int houseId = -1;

        try {
            String sql = "SELECT H.id " +
                    "FROM House H " +
                    "INNER JOIN Commission C ON H.commission_id = C.id " +
                    "INNER JOIN PriorityObject P ON H.priority_object_id = P.id " +
                    "ORDER BY H.id DESC";

            statement = connection.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            rs = statement.executeQuery();
            int rowCount = 0;

            if (rs.last()) {
                rowCount = rs.getRow();
                rs.beforeFirst(); // Di chuyển con trỏ về trước hàng đầu tiên
            }

            if (selectedIndex <= 0 || selectedIndex > rowCount) {
                return -1; // Trả về -1 nếu index không hợp lệ
            }

            if (rs.absolute(selectedIndex)) {
                houseId = rs.getInt("id");
            }
        } finally {
            // Đóng ResultSet và PreparedStatement sau khi sử dụng xong
            if (rs != null) {
                rs.close();
            }
            if (statement != null) {
                statement.close();
            }
        }

        return houseId;
    }
    // Hàm xử lý quyết định về phụ nữ mang thai trong house_id
    public void processPregnantWomanDecision(Scanner scanner, Connection connection, int houseId) {
        System.out.println("Remove priority review for pregnant women? (yes/no)");
        String input = scanner.nextLine().toLowerCase();

        if (input.equals("yes")) {
            // Thêm đối tượng 'Trẻ em' trước khi xét duyệt
            int childCitizenObjectId = insertChildCitizenObjectIfNotExists(connection);
            if (childCitizenObjectId != -1) {
                System.out.println("Added 'Children' object.");

                // Tiếp tục xét duyệt
                System.out.println("Do you want to add a child priority and additional demographics? (yes/no)");
                String confirmationInput = scanner.nextLine().toLowerCase();

                if (confirmationInput.equals("yes")) {
                    updateCitizenObject(connection, houseId);
                    performInsert(connection, houseId, childCitizenObjectId);
                } else {
                    System.out.println("Not updated.");
                }
            } else {
                System.out.println("Cannot add object 'Children'.");
                System.out.println("Unable to perform review.");
            }
        } else {
            System.out.println("No need to perform deletion.");
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
            System.out.println("Updated priority review for pregnant women.");
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
            System.out.println("Added child priority and added demographics.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void checkHouseBefore(Connection con, int houseId) {
        System.out.println("\t\t\tAfter ");
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
        System.out.println("\t\t\t ");
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
    public static void displayCitizenInfo( Connection connection) {
        LinkedHashMap<Integer, Integer> indexToIdMap = new LinkedHashMap<>();
        TreeMap<Integer, String> citizenData = new TreeMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        String queryAllCitizens = "SELECT c.id, c.name, c.identity_card, c.date_of_birth, c.phone_number, c.address, c.is_household_lord, c.sex, co.type_name_object FROM Citizen c JOIN CitizenObject co ON c.citizen_object_id = co.id ORDER BY c.id DESC";
        try (PreparedStatement statementAllCitizens = connection.prepareStatement(queryAllCitizens)) {
            ResultSet rsAllCitizens = statementAllCitizens.executeQuery();

            int index = 1;
            while (rsAllCitizens.next()) {
                int citizenId = rsAllCitizens.getInt("id");

                String citizenInfo = String.format("Index: %d, Name: %s, Identity Card: %s, Date of Birth: %s, Phone Number: %s, Address: %s, Household Lord: %s, Sex: %s, Type: %s",
                        index,
                        rsAllCitizens.getString("name"),
                        rsAllCitizens.getString("identity_card"),
                        dateFormat.format(rsAllCitizens.getDate("date_of_birth")),
                        rsAllCitizens.getString("phone_number"),
                        rsAllCitizens.getString("address"),
                        rsAllCitizens.getBoolean("is_household_lord"),
                        rsAllCitizens.getBoolean("sex") ? "Male" : "Female",
                        rsAllCitizens.getString("type_name_object"));

                citizenData.put(index, citizenInfo);
                indexToIdMap.put(index, citizenId); // Associate the index with the corresponding citizen ID
                index++;
            }

            System.out.println("| Index | Name                   | Identity Card | Date of Birth | Phone Number | Address                | Household Lord | Sex    | Type                  |");
            System.out.println("| ----- | ---------------------- | ------------- | ------------- | ------------ | ---------------------- | -------------- | ------ | --------------------- |");

            for (Map.Entry<Integer, String> entry : citizenData.entrySet()) {
                String citizenInfo = entry.getValue();
                String[] infoParts = citizenInfo.split(", ");

                System.out.printf("| %-6s| %-22s| %-14s| %-14s| %-13s| %-22s| %-15s| %-7s| %-21s|%n",
                        infoParts[0].split(": ")[1],
                        infoParts[1].split(": ")[1],
                        infoParts[2].split(": ")[1],
                        infoParts[3].split(": ")[1],
                        infoParts[4].split(": ")[1],
                        infoParts[5].split(": ")[1],
                        infoParts[6].split(": ")[1],
                        infoParts[7].split(": ")[1],
                        infoParts[8].split(": ")[1]
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }



        // Cho phép người dùng chọn cách cập nhật
        Scanner scanner = new Scanner(System.in);
        int chosenId = -1;

        while (true) {
            System.out.println("Select index to update information: ");
            String input = scanner.nextLine();

            // Kiểm tra xem chuỗi nhập vào có chứa ký tự đặc biệt hay không
            if (!input.matches("[a-zA-Z0-9]+")) {
                System.out.println("Do not enter special characters or spaces. Please re-enter.");
                continue;
            }

            // Kiểm tra xem chuỗi nhập vào có phải là số không
            if (!input.matches("\\d+")) {
                System.out.println("Please enter numbers only. Please re-enter.");
                continue;
            }

            int chosenIndex = Integer.parseInt(input);

            // Kiểm tra xem index có tồn tại trong map không
            if (indexToIdMap.containsKey(chosenIndex)) {
                chosenId = indexToIdMap.get(chosenIndex);
                break; // Nếu có, thoát khỏi vòng lặp
            } else {
                System.out.println("Index option does not exist. Please re-enter.");
            }
        }

        if (chosenId != -1) {
            System.out.println("Select update method (1 - phoneNumber, 2 - typeNameObject):");
            int choice;

            while (true) {
                String input = scanner.nextLine();

                // Kiểm tra xem chuỗi nhập vào có phải là số không và không âm
                if (!input.matches("\\d+") || Integer.parseInt(input) <= 0) {
                    System.out.println("Incorrect input. Please re-enter.");
                    continue;
                }

                choice = Integer.parseInt(input);
                break;
            }

            if (choice == 1) {
                updatePhoneNumber(chosenId, connection);
            } else if (choice == 2) {
                updateTypeNameObject(chosenId, connection);
            } else {
                System.out.println("Invalid selection.");
            }
        }
    }


    public static void updatePhoneNumber(int citizenId, Connection connection) {
        Scanner scanner = new Scanner(System.in);

        String newPhoneNumber;
        boolean isValid = false;

        do {
            System.out.println("Enter the new phone number:");
            newPhoneNumber = scanner.nextLine();

            // Kiểm tra tính hợp lệ của số điện thoại
            isValid = isValidPhoneNumber(newPhoneNumber);
        } while (!isValid);

        try {
            String updateQuery = "UPDATE Citizen SET phone_number = ? WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateQuery)) {
                statement.setString(1, newPhoneNumber);
                statement.setInt(2, citizenId);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Phone number updated successfully.");
                } else {
                    System.out.println("Unable to update the phone number.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void updateTypeNameObject(int citizenId, Connection connection) {
        try {
            // Display information from the CitizenObject table
            String query = "SELECT * FROM CitizenObject";
            try (Statement statement = connection.createStatement()) {
                ResultSet rs = statement.executeQuery(query);

                // Display information from the CitizenObject table
                System.out.println("CitizenObject Information:");
                System.out.println("| Index | Type Name Object |");
                System.out.println("--------------------------");
                while (rs.next()) {
                    int index = rs.getInt("id");
                    String typeNameObject = rs.getString("type_name_object");
                    System.out.println("|   " + index + "   |   " + typeNameObject + "   |");
                }

                Scanner scanner = new Scanner(System.in);
                System.out.println("Choose an index from the CitizenObject table:");
                int index = scanner.nextInt();

                // Perform the update on typeNameObject based on the chosen index from the CitizenObject table
                String updateQuery = "UPDATE Citizen SET citizen_object_id = ? WHERE id = ?";
                try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                    updateStatement.setInt(1, index);
                    updateStatement.setInt(2, citizenId);
                    int rowsAffected = updateStatement.executeUpdate();

                    if (rowsAffected > 0) {
                        System.out.println("typeNameObject updated successfully.");
                    } else {
                        System.out.println("Unable to update typeNameObject.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isCitizenExists(int citizenId) {
        boolean exists = false;
        String query = "SELECT COUNT(*) AS count FROM Citizen WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, citizenId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int count = rs.getInt("count");
                exists = (count > 0);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exists;
    }
    public void handleCitizenManagement(CitizenManager citizenManager, Scanner scanner) {
        int choice = -1;

        do {
            try {
                System.out.println("\t\tCitizen Management : ");
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
                        citizenManager.deleteCitizen(connection);
                        break;
                    case 3:
                        // Sửa thông tin công dân

                       displayCitizenInfo(connection);
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

