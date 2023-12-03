//package Model;
//import Common.DBConnect;
//import org.jetbrains.annotations.NotNull;
//
//import java.sql.*;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
//public class HouseManager {
//    private static Connection connection;
//
//    // Constructor
//    public HouseManager(Connection connection) {
//        this.connection = connection;
//    }
//    public HouseManager() {
//        this.connection = DBConnect.connectDatabase();
//    }
//    public static boolean isValidIdInTable(Connection connection, String tableName, int id) throws SQLException {
//        String query = "SELECT COUNT(*) FROM " + tableName + " WHERE id = ?";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, id);
//            try (ResultSet resultSet = statement.executeQuery()) {
//                if (resultSet.next()) {
//                    int count = resultSet.getInt(1);
//                    return count > 0;
//                }
//            }
//        }
//        return false;
//    }
//
//    public static boolean isValidHouseId(Connection connection, int houseId) throws SQLException {
//        return isValidIdInTable(connection, "House", houseId);
//    }
//
//    //    public static boolean isValidCommissionId(Connection connection, int commissionId) throws SQLException {
////        return isValidIdInTable(connection, "Commission", commissionId);
////    }
////
////    public static boolean isValidPriorityObjectId(Connection connection, int priorityObjectId) throws SQLException {
////        return isValidIdInTable(connection, "PriorityObject", priorityObjectId);
////    }
//    public static @NotNull Map<Integer, House> getHouseDetails(Connection con) {
//        Map<Integer, House> indexObjectMap = new LinkedHashMap<>();
//        try {
//            String selectQuery = """
//                SELECT
//                    H.id,
//                    H.commission_id,
//                    H.priority_object_id,
//                    C.precint_name,
//                    PO.object_type
//                FROM
//                    House H
//                    LEFT JOIN Commission C ON H.commission_id = C.id
//                    LEFT JOIN PriorityObject PO ON H.priority_object_id = PO.id
//                ORDER BY H.id DESC""";
//
//            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
//                ResultSet rs = preparedStatement.executeQuery();
//                if (rs != null) {
//                    int index = 1;
//                    try {
//                        while (rs.next()) {
//                            House house = new House(
//                                    rs.getInt("id"),
//                                    rs.getInt("commission_id"),
//                                    rs.getInt("priority_object_id"),
//                                    rs.getString("precint_name"),
//                                    rs.getString("object_type")
//                            );
//                            indexObjectMap.put(index, house);
//                            index++;
//                        }
//                    } catch (SQLException ex) {
//                        System.out.println("\t\t\t\u001B[31mThere was an error processing the result set: " + ex.getMessage() + ".\u001B[0m");
//                    }
//                }
//            }
//        } catch (Exception ex) {
//            System.out.println("\t\t\t\u001B[31mThere was an error connecting to the Database: " + ex.getMessage() + ".\u001B[0m");
//        }
//        return indexObjectMap;
//    }
////    public static int getCommissionIdByName(Connection con, String commissionName) {
////        int commissionId = -1; // Default value if not found
////        try {
////            String selectQuery = "SELECT id FROM Commission WHERE precint_name = ?";
////            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
////                preparedStatement.setString(1, commissionName);
////                ResultSet rs = preparedStatement.executeQuery();
////                if (rs.next()) {
////                    commissionId = rs.getInt("id");
////                }
////            }
////        } catch (SQLException ex) {
////            System.out.println("\t\t\t\u001B[31mError retrieving Commission ID: " + ex.getMessage() + ".\u001B[0m");
////        }
////        return commissionId;
////    }
////    public static int getPiorityObjectIdByName(Connection con, String representativeName) {
////        int representativeId = -1; // Default value if not found
////        try {
////            String selectQuery = "SELECT id FROM PiorityObject WHERE object_type = ?";
////            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
////                preparedStatement.setString(1, representativeName);
////                ResultSet rs = preparedStatement.executeQuery();
////                if (rs.next()) {
////                    representativeId = rs.getInt("id");
////                }
////            }
////        } catch (SQLException ex) {
////            System.out.println("\t\t\t\u001B[31mError retrieving Representative Id: " + ex.getMessage() + ".\u001B[0m");
////        }
////        return representativeId;
////    }
////    public static int getIdFromIndex(Connection con, int targetIndex) {
////        Map<Integer, House> indexObjectMap = getHouseDetails(con);
////
////        if (indexObjectMap.containsKey(targetIndex)) {
////            House house = indexObjectMap.get(targetIndex);
////            if (house != null) {
////                return house.getId();
////            } else {
////                System.out.println("\t\t\t\u001B[31mError: DonateDetail object is null for index " + targetIndex + "\u001B[0m");
////            }
////        } else {
////            System.out.println("\t\t\t\u001B[31mError: Index " + targetIndex + " is out of bounds.\u001B[0m");
////        }
////        return -1;
////    }
//
//    // Add a new house to the database
////    public void addHouse(int commissionId, int priorityObjectId) throws SQLException {
////        String query = "INSERT INTO House (commission_id, priority_object_id) VALUES (?, ?)";
////        try (PreparedStatement statement = connection.prepareStatement(query)) {
////            statement.setInt(1, commissionId);
////            statement.setInt(2, priorityObjectId);
////            int rowsInserted = statement.executeUpdate();
////            if (rowsInserted > 0) {
////                System.out.println("House added successfully.");
////            }
////        }
////    }
//    public static int getCommissionIdByIndex(Connection con, int index) {
//        int commissionId = -1; // Giá trị mặc định nếu không tìm thấy
//
//        try {
//            String selectQuery = "SELECT TOP 1 id FROM (SELECT TOP " + index + " id FROM Commission ORDER BY id) AS sub ORDER BY id DESC";
//
//            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
//                ResultSet rs = preparedStatement.executeQuery();
//                if (rs.next()) {
//                    commissionId = rs.getInt("id");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return commissionId;
//    }
//    public static int getPriorityObjectIdByIndex(Connection con, int index) {
//        int priorityObjectId = -1; // Giá trị mặc định nếu không tìm thấy
//
//        try {
//            String selectQuery = "SELECT TOP 1 id FROM (SELECT TOP " + index + " id FROM PriorityObject ORDER BY id) AS sub ORDER BY id DESC";
//
//            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
//                ResultSet rs = preparedStatement.executeQuery();
//                if (rs.next()) {
//                    priorityObjectId = rs.getInt("id");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        return priorityObjectId;
//    }
//    public static int getIntInputFromUser(Scanner scanner, String message) {
//        int userInput = 0;
//        boolean isValid = false;
//
//        while (!isValid) {
//            System.out.print(message);
//            while (!scanner.hasNextInt()) {
//                System.out.println("Please enter a valid integer!");
//                scanner.next(); // Đọc và bỏ qua đầu vào không phải số nguyên
//            }
//            userInput = scanner.nextInt();
//
//            if (userInput <= 0) {
//                System.out.println("Number incorrect. Please enter a different number.");
//            } else {
//                isValid = true;
//            }
//        }
//
//        return userInput;
//    }
//    public static boolean isValidHouseData(Connection con, int commissionId, int priorityObjectId) {
//        // Kiểm tra tính hợp lệ của dữ liệu hộ dân dựa trên commissionId và priorityObjectId
//        if (!isValidCommissionId(con, commissionId)) {
//            System.out.println("Invalid Commission ID.");
//            return false;
//        }
//
//        if (!isValidPriorityObjectId(con, priorityObjectId)) {
//            System.out.println("Invalid Priority Object ID.");
//            return false;
//        }
//
//        return true;
//    }
//
//    private static boolean isValidCommissionId(Connection con, int commissionId) {
//        // Kiểm tra tính hợp lệ của commissionId trong cơ sở dữ liệu
//        String query = "SELECT COUNT(*) FROM Commission WHERE id = ?";
//        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
//            preparedStatement.setInt(1, commissionId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                int count = resultSet.getInt(1);
//                return count > 0; // Trả về true nếu commissionId tồn tại trong bảng Commission
//            }
//        } catch (SQLException ex) {
//            System.out.println("SQL error: " + ex.getMessage());
//        }
//        return false;
//    }
//
//    private static boolean isValidPriorityObjectId(Connection con, int priorityObjectId) {
//        // Kiểm tra tính hợp lệ của priorityObjectId trong cơ sở dữ liệu
//        String query = "SELECT COUNT(*) FROM PriorityObject WHERE id = ?";
//        try (PreparedStatement preparedStatement = con.prepareStatement(query)) {
//            preparedStatement.setInt(1, priorityObjectId);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            if (resultSet.next()) {
//                int count = resultSet.getInt(1);
//                return count > 0; // Trả về true nếu priorityObjectId tồn tại trong bảng PriorityObject
//            }
//        } catch (SQLException ex) {
//            System.out.println("SQL error: " + ex.getMessage());
//        }
//        return false;
//    }
//    public static void addHouse(Connection con) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("\t\t\t=== Add House Information ===");
//        System.out.print("\t\t\t\u001B[31mYou are about to add a record for a household.\n\t\t\tPlease refer to other tables in the database to avoid mistakes!\u001B[0m\n");
//        // Hiển thị thông tin Commission để người dùng chọn
//        displayCommissionList(con);
//        int commissionIndex =  getIntInputFromUser(scanner, "Enter the index of the Commission: "); // Nhận đầu vào từ người dùng
//        int commissionId = getCommissionIdByIndex(con, commissionIndex); // Lấy commission_id từ index
//
//        // Hiển thị thông tin Priority Object để người dùng chọn
//        displayPriorityObjectList(con);
//        int priorityObjectIndex = getIntInputFromUser(scanner, "Enter the index of the Priority Object: "); // Nhận đầu vào từ người dùng
//        int priorityObjectId = getPriorityObjectIdByIndex(con, priorityObjectIndex); // Lấy priority_object_id từ index
//
//        // Thực hiện kiểm tra và thêm dữ liệu
//        if (commissionId != -1 && priorityObjectId != -1) {
//            // Kiểm tra nếu đã tồn tại dữ liệu với commission_id và priority_object_id tương ứng
//
//            // Thêm dữ liệu mới vào cơ sở dữ liệu
//            insertHouseData(con, commissionId, priorityObjectId);
//            System.out.println("\t\t\t\u001B[32mNew house information has been added to the database.\u001B[0m");
//
//        } else {
//            System.out.println("\t\t\t\u001B[31mInvalid Commission ID or Priority Object ID.\u001B[0m");
//        }
//
//    }
//
//    public static void insertHouseData(Connection con, int commissionId, int priorityObjectId) {
//        try {
//            String insertQuery = "INSERT INTO House (commission_id, priority_object_id) VALUES (?, ?)";
//
//            try (PreparedStatement preparedStatement = con.prepareStatement(insertQuery)) {
//                preparedStatement.setInt(1, commissionId);
//                preparedStatement.setInt(2, priorityObjectId);
//
//                int rowsAffected = preparedStatement.executeUpdate();
//                if (rowsAffected > 0) {
//                    System.out.println("House data inserted successfully.");
//                } else {
//                    System.out.println("Failed to insert house data.");
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    public static void displayCommissionList(Connection con) {
//        try {
//            String selectQuery = "SELECT id, precint_name, city_name FROM Commission";
//            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
//                ResultSet resultSet = preparedStatement.executeQuery();
//                int index = 1;
//                while (resultSet.next()) {
//                    String precintName = resultSet.getString("precint_name");
//
//                    String cityName = resultSet.getString("city_name");
//                    System.out.println(index + ". Precint: " + precintName +  ", City: " + cityName);
//                    index++;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void displayPriorityObjectList(Connection con) {
//        try {
//            String selectQuery = "SELECT id, object_type FROM PriorityObject";
//            try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
//                ResultSet resultSet = preparedStatement.executeQuery();
//                int index = 1;
//                while (resultSet.next()) {
//                    String objectType = resultSet.getString("object_type");
//                    System.out.println(index + ". Object Type: " + objectType);
//                    index++;
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
//    // Delete a house from the database by ID
//    public void deleteHouse(Connection con) throws SQLException {
//        displayHouseTable();
//
//        int houseIndex = getIntInputFromUser("Enter the index of the house to delete: ");
//        int houseId = getHouseIdByIndex(con, houseIndex);
//
//        if (houseId != -1) {
//            try {
//                con.setAutoCommit(false); // Tắt chế độ tự động commit
//
//                // Cập nhật house_id thành NULL trong bảng Citizen trước khi xóa dòng từ bảng House
//                String updateCitizenQuery = "UPDATE Citizen SET house_id = NULL WHERE house_id = ?";
//                try (PreparedStatement updateStatement = con.prepareStatement(updateCitizenQuery)) {
//                    updateStatement.setInt(1, houseId);
//                    updateStatement.executeUpdate();
//                }
//
//                // Cập nhật house_id thành NULL trong bảng Distribution trước khi xóa dòng từ bảng House
//                String updateDistributionQuery = "UPDATE Distribution SET household_id = NULL WHERE household_id = ?";
//                try (PreparedStatement updateDistributionStatement = con.prepareStatement(updateDistributionQuery)) {
//                    updateDistributionStatement.setInt(1, houseId);
//                    updateDistributionStatement.executeUpdate();
//                }
//
//                // Xóa dòng từ bảng House
//                String deleteHouseQuery = "DELETE FROM House WHERE id = ?";
//                try (PreparedStatement deleteStatement = con.prepareStatement(deleteHouseQuery)) {
//                    deleteStatement.setInt(1, houseId);
//                    int rowsDeleted = deleteStatement.executeUpdate();
//                    if (rowsDeleted > 0) {
//                        System.out.println("House with ID " + houseId + " has been deleted.");
//                    } else {
//                        System.out.println("House with ID " + houseId + " not found.");
//                    }
//                }
//
//                con.commit(); // Commit các thay đổi vào cơ sở dữ liệu
//            } catch (SQLException e) {
//                con.rollback(); // Rollback nếu có lỗi xảy ra
//                System.out.println("Error occurred, transaction rolled back.");
//                e.printStackTrace();
//            } finally {
//                con.setAutoCommit(true); // Bật lại chế độ tự động commit sau khi hoàn thành
//            }
//        } else {
//            System.out.println("Invalid House index.");
//        }
//    }
//
//    // Update house information in the database by ID
//    public void updateHouseInfo(Connection con) throws SQLException {
//        displayHouseTable();
//        int houseIndex = getIntInputFromUser("Enter the index of the house to update: ");
//        int houseId = getHouseIdByIndex(con, houseIndex);
//        if (houseId != -1) {
//            displayPriorityObjectList(con);
//            int priorityObjectIndex = getIntInputFromUser("Enter the index of the Priority Object: ");
//            int priorityObjectId = getPriorityObjectIdByIndex(con, priorityObjectIndex);
//            if (priorityObjectId != -1) {
//                updateHouse(houseId, priorityObjectId);
//            } else {
//                System.out.println("Invalid Priority Object index.");
//            }
//        } else {
//            System.out.println("Invalid House index.");
//        }
//    }
//    public int getIntInputFromUser(String message) {
//        Scanner scanner = new Scanner(System.in);
//        int userInput;
//        do {
//            try {
//                System.out.print(message);
//                userInput = scanner.nextInt();
//                if (userInput <= 0) {
//                    throw new IllegalArgumentException("Please enter a non-negative integer.");
//                }
//                break;
//            } catch (InputMismatchException e) {
//                System.out.println("Invalid input. Please enter an integer.");
//                scanner.nextLine(); // Đọc và loại bỏ input không hợp lệ để tránh lặp vô hạn
//            } catch (IllegalArgumentException e) {
//                System.out.println(e.getMessage());
//            }
//        } while (true);
//        return userInput;
//    }
//    public int getHouseIdByIndex(Connection con, int index) throws SQLException {
//        int houseId = -1; // Giá trị mặc định nếu không tìm thấy
//
//        String selectQuery = "SELECT id FROM (SELECT id, ROW_NUMBER() OVER (ORDER BY id DESC) AS row_num FROM House) AS numberedRows WHERE row_num = ?";
//        try (PreparedStatement preparedStatement = con.prepareStatement(selectQuery)) {
//            preparedStatement.setInt(1, index);
//            ResultSet rs = preparedStatement.executeQuery();
//            if (rs.next()) {
//                houseId = rs.getInt("id");
//            }
//        }
//
//        return houseId;
//    }
//    public void updateHouse(int houseId, int priorityObjectId) throws SQLException {
//        String query = "UPDATE House SET priority_object_id = ? WHERE id = ?";
//        try (PreparedStatement statement = connection.prepareStatement(query)) {
//            statement.setInt(1, priorityObjectId);
//            statement.setInt(2, houseId);
//            int rowsUpdated = statement.executeUpdate();
//            if (rowsUpdated > 0) {
//                System.out.println("House information updated for ID " + houseId);
//            } else {
//                System.out.println("House with ID " + houseId + " not found.");
//            }
//        }
//    }
//
//    // Display all houses from the database
//    public void displayHouseTable() throws SQLException {
//        Statement st = null;
//        ResultSet rs = null;
//        try {
//            st = connection.createStatement();
//            String sql = "SELECT H.id, CONCAT(C.precint_name, ', ', C.city_name, ', ', C.province_name) AS commission_info, P.object_type " +
//                    "FROM House H " +
//                    "INNER JOIN Commission C ON H.commission_id = C.id " +
//                    "INNER JOIN PriorityObject P ON H.priority_object_id = P.id " +
//                    "ORDER BY H.id DESC"; // Sắp xếp theo ID giảm dần
//            rs = st.executeQuery(sql);
//
//            System.out.println("=================================== House Table ====================================");
//            System.out.println(String.format("| %-5s | %-35s | %-20s |", "ID", "Commission Information", "Priority Object Type"));
//            System.out.println(String.format("| %-5s | %-35s | %-20s |", "-----", "-----------------------------------", "---------------------"));
//            int index = 1; // Index bắt đầu từ 1
//            while (rs.next()) {
//                System.out.println(String.format("| %-5s | %-35s | %-20s |",
//                        index, rs.getString("commission_info"), rs.getString("object_type")));
//                System.out.println(String.format("| %-5s | %-35s | %-20s |", "-----", "-----------------------------------", "---------------------"));
//                index++;
//            }
//            System.out.println();
//        } finally {
//            // Đóng ResultSet và Statement sau khi sử dụng xong
//            if (rs != null) {
//                rs.close();
//            }
//            if (st != null) {
//                st.close();
//            }
//        }
//    }
//
//    //- Hiển thị top 5 hộ dân có giá trị ủng hộ nhiều nhất trong 1 đợt ủng hộ X (X nhập vào từ bàn phím)
//    public static void displayTop5HouseholdsByDonation(Connection connection, String donationRound) {
//        try {
//            PreparedStatement statement = connection.prepareStatement(
//                    "SELECT TOP 5 CO.precint_name, C2.name AS household_owner, SUM(D.amount_distribution) AS total_donation " +
//                            "FROM Distribution D " +
//                            "INNER JOIN House H ON D.household_id = H.id " +
//                            "INNER JOIN Commission CO ON H.commission_id = CO.id " +
//                            "INNER JOIN Citizen C2 ON H.id = C2.house_id AND C2.is_household_lord = 1 " +
//                            "WHERE MONTH(D.date_distribution) = ? " +
//                            "AND YEAR(D.date_distribution) = ? " +
//                            "GROUP BY CO.precint_name, C2.name " +
//                            "ORDER BY total_donation DESC"
//            );
//
//            // Parsing the input donationRound to get month and year separately
//            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
//            java.util.Date parsedDate = dateFormat.parse(donationRound);
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(parsedDate);
//            int month = calendar.get(Calendar.MONTH) + 1; // Adding 1 as Calendar.MONTH starts from 0
//            int year = calendar.get(Calendar.YEAR);
//
//            statement.setInt(1, month);
//            statement.setInt(2, year);
//
//            ResultSet resultSet = statement.executeQuery();
//
//            System.out.println("Top 5 household with the highest donations in round " + donationRound + ":");
//            System.out.println("Precinct Name\t\t\t\t\tHousehold Owner\t\t\t\tTotal Donation");
//
//            while (resultSet.next()) {
//                String precinctName = resultSet.getString("precint_name");
//                String householdOwner = resultSet.getString("household_owner");
//                double totalDonation = resultSet.getDouble("total_donation");
//
//                System.out.printf("%-32s\t%-32s\t\t%.2f\n", precinctName, householdOwner, totalDonation);
//            }
//        } catch (SQLException | ParseException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    public static void handleHouseManagement(HouseManager houseManager, Scanner scanner) {
//        int choice;
//
//        do {
//            System.out.println("\t\tHouse Management :");
//            System.out.println("\t\t\t0. Exit");
//            System.out.println("\t\t\t1. Add");
//            System.out.println("\t\t\t2. Delete");
//            System.out.println("\t\t\t3. Update");
//            System.out.println("\t\t\t4. Display");
//            System.out.print("\t\t\tPlease choose: ");
//
//            try {
//                System.out.print("Please choose: ");
//                choice = Integer.parseInt(scanner.nextLine());
//
//                switch (choice) {
//                    case 1:
//                        // Thêm hộ dân
//                        addHouse(connection);
//                        break;
//
//                    case 2:
//                        // Xóa hộ dân
//                        houseManager.deleteHouse(connection);
//                        break;
//                    case 3:
//                        // Sửa thông tin hộ dân
//                        try {
//                            houseManager.updateHouseInfo(connection);
//                            System.out.println("House information updated successfully.");
//                        } catch (SQLException e) {
//                            System.out.println("SQL error: " + e.getMessage());
//                        }
//                        break;
//
//                    case 4:
//                        // Hiển thị thông tin tất cả các hộ dân
//                        try {
//                            houseManager.displayHouseTable();
//                        } catch (SQLException ex) {
//                            System.out.println("SQL error: " + ex.getMessage());
//                        }
//                        break;
//
//                    case 0:
//                        // Thoát khỏi menu
//                        System.out.println("Exiting...");
//                        return;
//
//                    default:
//                        System.out.println("Invalid selection.");
//                        break;
//
//                }
//            } catch (NumberFormatException e) {
//                System.out.println("Please enter a number.");
//                choice = -1;
//            } catch (Exception e) {
//                System.out.println("Error: " + e.getMessage());
//                choice = -1;
//            }
//        } while (true);
//    }
//
//
//}