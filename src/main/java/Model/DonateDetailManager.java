package Model;

import Common.InputValidator;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static Model.DistributionManager.printDistribution;
import static Model.Processing.*;

public class DonateDetailManager {
    private final static Scanner sc = new Scanner(System.in);
    public static void printDonateDetail(Connection con){
        try {
            if (countRecords(con, "DonateDetail")>0){
                System.out.println();
                System.out.println("========================================================== DANH SÁCH ỦNG HỘ =========================================================");
                System.out.println("._______.____________________.____________________.____________________.________________________.______________________.____________.");
                System.out.println("│   ID  │       Số tiền      │    Ngày ủng hộ     │      Xã/Phường     │      Người đại diện    │      Tên Công ty     │   Cán bộ   │");
                System.out.println("│_______│____________________│____________________│____________________│________________________│______________________│____________│");
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("""
                        SELECT
                            dbo.DonateDetail.id,
                            amount,
                            donate_date,
                            C.precint_name,
                            representative_name,
                            company_name,
                            O.name
                        FROM
                            DonateDetail
                                LEFT JOIN dbo.Commission C ON DonateDetail.commission_id = C.id
                                LEFT JOIN dbo.Representative R ON R.id = DonateDetail.representative_id
                                LEFT JOIN dbo.Company C2 ON C2.id = R.company_id
                                LEFT JOIN dbo.Distribution D on C.id = D.commission_id
                                LEFT JOIN dbo.OfficerDistribution on D.id = OfficerDistribution.distribution_id
                                LEFT JOIN dbo.Officer O ON O.id = OfficerDistribution.officer_id""");
                while (resultSet.next())
                {
                    String ID = resultSet.getString("id");
                    String amount = String.format("%.0f", resultSet.getDouble("amount"));
                    LocalDate donate_date = resultSet.getDate("donate_date").toLocalDate();
                    String precint_name = resultSet.getString("precint_name");
                    String representative_name = resultSet.getString("representative_name");
                    String company_name = resultSet.getString("company_name");
                    String name = resultSet.getString("name");
                    System.out.printf("│ %-5S │ %-18s │ %-18s │ %-18s │ %-22s │ %-20s │ %-10s │\n", ID, amount, dateFormat.format(donate_date), precint_name,representative_name ,company_name, name);
                    System.out.println("│_______│____________________│____________________│____________________│________________________│______________________│____________│");
                }
                System.out.println("=========================================================== DANH SÁCH KẾT THÚC ======================================================");
            }else{
                System.out.println("\t\t\t\u001B[31mChưa có đợt ủng hộ nào.\u001B[31");
            }
            waitForEnter();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }
    }
    public static void printDonateDetailByID(Connection con, int ID){
        String selectQuery = """
                SELECT
                    dbo.DonateDetail.id,
                    amount,
                    donate_date,
                    C.precint_name,
                    representative_name,
                    company_name,
                    O.name
                FROM
                    DonateDetail
                        LEFT JOIN dbo.Commission C ON DonateDetail.commission_id = C.id
                        LEFT JOIN dbo.Representative R ON R.id = DonateDetail.representative_id
                        LEFT JOIN dbo.Company C2 ON C2.id = R.company_id
                        LEFT JOIN dbo.Distribution D on C.id = D.commission_id
                        Left Join dbo.OfficerDistribution on D.id = OfficerDistribution.distribution_id
                        LEFT JOIN dbo.Officer O ON O.id = OfficerDistribution.officer_id
                WHERE DonateDetail.id = ?""";
        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery)){
            selectStatement.setInt(1, ID);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (countRecords(con, "DonateDetail")>0){
                    while (resultSet.next())
                    {
                        System.out.println();
                        System.out.println("========================================================== DANH SÁCH ỦNG HỘ =========================================================");
                        System.out.println("._______.____________________.____________________.____________________.________________________.______________________.____________.");
                        System.out.println("│   ID  │       Số tiền      │    Ngày ủng hộ     │      Xã/Phường     │      Người đại diện    │      Tên Công ty     │   Cán bộ   │");
                        System.out.println("│_______│____________________│____________________│____________________│________________________│______________________│____________│");
                        String amount = String.format("%.0f", resultSet.getDouble("amount"));
                        LocalDate donate_date = resultSet.getDate("donate_date").toLocalDate();
                        String precint_name = resultSet.getString("precint_name");
                        String representative_name = resultSet.getString("representative_name");
                        String company_name = resultSet.getString("company_name");
                        String name = resultSet.getString("name");
                        System.out.printf("│ %-5S │ %-18s │ %-18s │ %-18s │ %-22s │ %-20s │ %-10s │\n", ID, amount, dateFormat.format(donate_date), precint_name,representative_name ,company_name, name);
                        System.out.println("│_______│____________________│____________________│____________________│________________________│______________________│____________│");
                    }
                    System.out.println("=========================================================== DANH SÁCH KẾT THÚC ======================================================");
                }else{
                    System.out.println("\t\t\t\u001B[31mChưa có đợt ủng hộ nào.\u001B[31");
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }
    }
    public static void addDonateDetail(Connection con){
        try{
            System.out.println();
            System.out.println("\t\t\t=== Thêm thông tin nhà ủng hộ ===");
            System.out.print("\t\t\t\u001B[31mBạn sắp thao tác thêm một record cho 1 lần ủng hộ của nhà tài trợ.\n\t\t\tVui lòng tham khảo các bảng khác trong co sở dữ liệu để tránh sai xót!\n\t\t\t(Các bảng liên quan xin vui lòng tham khảo menu \"Quản lý người đại diện\" và \" Quản lý Ủy ban\")\n \t\t\t(Nhấn Enter để tiếp tục)  \u001B[0m");
            sc.nextLine();
            String insertQuery = "INSERT INTO DonateDetail (representative_id, commission_id, donate_date, amount) VALUES (?, ?, ?, ?)";
            System.out.println("\t\t\tID của người ủng hộ đại diện (cá nhân hoặc đại diện công ty) (Tham khảo menu \"Quản lý người đại diện\")");
            int representativeId = Processing.inputID(sc, "Representative", "id");
            System.out.println("\t\t\tID của xã/phường được nhận hỗ trợ (Tham khảo menu \"Quản lý Ủy ban\")");
            int commissionId = Processing.inputID(sc, "Commission", "id");
            //sc.nextLine(); // Consume the newline character
            LocalDate donateDate;
            String donateDateStr;
            do {
                System.out.print("\t\t\tNhập vào ngày nhận hỗ trợ (theo định dạng dd/MM/yyyy): ");
                donateDateStr = sc.nextLine();

                try {
                    donateDate = LocalDate.parse(donateDateStr, dateFormat);
                    if (donateDate.isAfter(LocalDate.now())) {
                        System.out.println("\t\t\t\u001B[31mNgày nhận hỗ trợ phải trước hơn ngày hiện tại!\n Bạn không thể du hành thời gian đúng chứ!\u001B[0m");
                        donateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                    }
                } catch (DateTimeParseException ex) {
                    System.out.println("\t\t\t\u001B[31m Ngày tháng nhập vào \"" + donateDateStr + "\" không hợp lệ.\u001B[0m");
                    donateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                }
            } while (donateDate == null);
            double amount = 0;
            String input;

            do {
                System.out.print("\t\t\tNhập vào số tiền được ủng hộ: ");
                input = sc.next();

                if (!isFloatNumber(input)) {
                    System.out.println("\t\t\t\u001B[31mSố tiền không hợp lệ. Vui lòng nhập lại.\u001B[0m");
                } else {
                    amount = Double.parseDouble(input);
                }
            } while (!isFloatNumber(input));
            PreparedStatement insertStatement = con.prepareStatement(insertQuery);
            insertStatement.setInt(1, representativeId);
            insertStatement.setInt(2, commissionId);
            insertStatement.setDate(3, Date.valueOf(donateDate));
            insertStatement.setDouble(4, amount);
            // Execute the query
            insertStatement.executeUpdate();
            System.out.println("\t\t\t\u001B[32mNhà hỗ trợ đã được thêm vào cơ sở dữ liệu.\u001B[0m");
            waitForEnter();
        }
        catch (SQLException e)
        {
            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
        }
    }

    //	Xoa Thong Tin Khach Hang
    public static void deleteDonateDetail(Connection con) {
        System.out.println();
        System.out.print("\t\t\t\u001B[31mBạn sắp thao tác xóa một record của 1 lần ủng hộ của nhà tài trợ.\n\t\t\tVui lòng tham khảo các bảng khác trong co sở dữ liệu để tránh sai xót!\n\t\t\t(Các bảng liên quan xin vui lòng tham khảo menu \"Quản lý người đại diện\" và \" Quản lý Ủy ban\")\n \t\t\t(Nhấn Enter để tiếp tục)\u001B[0m  ");
        sc.nextLine();
        printDonateDetail(con);
        System.out.print("\t\t\tVui lòng nhập vào ID của record muốn xóa: ");
        int identity = inputID(sc, "DonateDetail", "id");// Tạm thời xóa record theo ID
        printDonateDetailByID(con, identity);
        System.out.println();
        PreparedStatement pstmt = null;
        String tableName = "DonateDetail";
        try {
//			Kiem Tra Su Ton Tai Cua record, Neu Ton Tai Thi Tien Hanh Xoa
            if (Processing.isIDAlreadyExists(con, identity, tableName)){
            String chose;
                    do {
                        System.out.print("\t\t\tBạn có chắc chắn muốn xóa (Y/N)?");
                        chose = sc.nextLine().trim();
                    } while (!("Y".equalsIgnoreCase(chose) || "N".equalsIgnoreCase(chose)));

                    if ("Y".equalsIgnoreCase(chose)) {
                        //Xóa
                        String sqlDelete = "DELETE FROM DonateDetail WHERE ID=?";
                        pstmt = con.prepareStatement(sqlDelete);
                        pstmt.setInt(1, identity);
                        if (pstmt.executeUpdate() != 0) {
                            System.out.println("\t\t\t\u001B[32mXóa thành công!!!\u001B[0m");
                            waitForEnter();
                        }
                    } else {
                        System.out.println(" \t\t\t\u001B[31mINFOR:Không có kết quả nào bị xóa!!!\u001B[0m");
                        waitForEnter();
                }
            } else {
                System.out.println("\t\t\t\u001B[31mKết quả không tồn tại trong Database!!!\u001B[0m");
                waitForEnter();
            }
        } catch (Exception e) {
            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se2) {
                System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + se2.getMessage() + ".\u001B[0m");
            }
        }
    }

    //	Chuc Nang Cap Nhat Thong Tin Khach Hang
    public static void updateDonateDetail(Connection con) {
        System.out.println();
        System.out.print("\t\t\t\u001B[31mBạn sắp thao tác cập nhật một record của 1 lần ủng hộ của nhà tài trợ.\n\t\t\tVui lòng tham khảo các bảng khác trong cơ sở dữ liệu để tránh sai xót!\n\t\t\t(Các bảng liên quan xin vui lòng tham khảo menu \"Quản lý người đại diện\" và \" Quản lý ủy ban\")\n\t\t\t(Nhấn Enter để tiếp tục) \u001B[0m");
        sc.nextLine();
        printDonateDetail(con);
        System.out.println("\t\t\tVui lòng nhập vào ID của record muốn update: ");
        int identity = inputID(sc, "DonateDetail", "id");// Tạm thời xóa record theo ID
        printDonateDetailByID(con, identity);
        System.out.println();
        PreparedStatement pstmt = null;
//		Kiem Tra id Da Co Trong Bang Khang Hang Hay Chua? Neu Co Thi Tien Hanh Update
        if (Processing.isIDAlreadyExists(con, identity, "DonateDetail" )) {
            String chose;
            boolean check;
//			Hien Thi Menu Update
            do {
                check = true;
                System.out.println("\t\t\t+-------------------------------+");
                System.out.println("\t\t\t|   Bạn muốn update mục nào?    |");
                System.out.println("\t\t\t+-------------------------------+");
                System.out.printf("\t\t\t| %-30s|\n\t\t\t| %-30s| \n\t\t\t| %-30s| \n\t\t\t| %-30s| \n\t\t\t| %-30s| \n\t\t\t| %-30s| \n", "1. Số tiền",
                        "2. Ngày ủng hộ", "3. Ủy ban nhận", "4. Người đại diện", "5. Tất cả", "0. Trở về menu Quản lý");
                System.out.println("\t\t\t+-------------------------------+");
                System.out.print("\t\t\tFrom Update Menu, Your Choice: ");
                chose = sc.nextLine().trim();
                if (!("0".equals(chose) ||"1".equals(chose) || "2".equals(chose) || "3".equals(chose) || "4".equals(chose)|| "5".equals(chose))) {
                    check = false;
                    System.out.println("\t\t\t\u001B[31mGiá trị nhập vào không đúng, vui lòng nhập lại!!!\u001B[0m");
                }
            } while (!(Processing.isNumber(chose) && check));

            switch (chose) {
//			Update Số tiền
                case "0":

                    break;
                case "1":
                    double newAmount = InputValidator.validateDoubleInput("\t\t\tNhập vào số tiền: ");
                    String sql1 = "UPDATE DonateDetail SET amount =? WHERE id =?";

                    try {
                        pstmt = con.prepareStatement(sql1);
                        pstmt.setDouble(1, newAmount);
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println("\t\t\t\u001B[32mCập nhật số tiền thành công!!!\u001B[0m");
                    } catch (Exception e) {
                        System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null)
                                pstmt.close();
                        } catch (SQLException se2) {
                            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + se2.getMessage() + ".\u001B[0m");
                        }
                        do {
                            System.out.print("\t\t\tBạn có muốn tiếp tục sửa không (Y/N)?");
                            String c = sc.nextLine();

                            if ("Y".equalsIgnoreCase(c)) {
                                updateDonateDetail(con);
                            } else if ("N".equalsIgnoreCase(c)) {
                                break;
                            } else {
                                System.out.println("\t\t\t\u001B[31mLựa chọn nhập vào không đúng, vui lòng nhập lại!!!\u001B[0m");
                            }
                        } while (true);
                        waitForEnter();
                    }
                    break;
//			Update ngày ủng hộ
                case "2":
                    String sql2 = "UPDATE DonateDetail SET donate_date =? WHERE id =?";
                    LocalDate newDonateDate = Processing.validateDateInput("\t\t\tNhập vào ngày nhận mới: ");
                    try {
                        pstmt = con.prepareStatement(sql2);
                        pstmt.setDate(1, Date.valueOf(newDonateDate));
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println("\t\t\t\u001B[32mCập nhật ngày ủng hộ thành công!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null)
                                pstmt.close();
                        } catch (SQLException se2) {
                            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + se2.getMessage() + ".\u001B[0m");
                        }
                        do {
                            System.out.print("\t\t\tBạn có muốn tiếp tục sửa không (Y/N)?");
                            String c = sc.nextLine();

                            if ("Y".equalsIgnoreCase(c)) {
                                updateDonateDetail(con);
                            } else if ("N".equalsIgnoreCase(c)) {
                                break;
                            } else {
                                System.out.println("\t\t\t\u001B[31mLựa chọn nhập vào không đúng, vui lòng nhập lại!!!\u001B[0m");
                            }
                        } while (true);
                        waitForEnter();
                    }
                    break;
//			Update ID Ủy ban
                case "3":
                    System.out.println("\t\t\tNhập vào ID Ủy ban: ");
                    int newCommissionId = inputID(sc, "Commission", "id");
                    String sql3 = "UPDATE DonateDetail SET commission_id =? WHERE id =?";
                    try {
                        pstmt = con.prepareStatement(sql3);
                        pstmt.setInt(1, newCommissionId);
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println("\t\t\t\u001B[32mCập nhật Ủy ban thành công!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null)
                                pstmt.close();
                        } catch (SQLException se2) {
                            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + se2.getMessage() + ".\u001B[0m");
                        }
                        do {
                            System.out.print("\t\t\tBạn có muốn tiếp tục sửa không (Y/N)?");
                            String c = sc.nextLine();

                            if ("Y".equalsIgnoreCase(c)) {
                                updateDonateDetail(con);
                            } else if ("N".equalsIgnoreCase(c)) {
                                break;
                            } else {
                                System.out.println("\t\t\t\u001B[31mLựa chọn nhập vào không đúng, vui lòng nhập lại!!!\u001B[0m");
                            }
                        } while (true);
                        waitForEnter();
                    }
                    break;
//			Update ID nguoi dai dien
                case "4":
                    System.out.println("\t\t\tNhập vào ID của người đại diện: ");
                    int newRepresentativeId = inputID(sc,"Representative", "id");
                    String sql4 = "UPDATE DonateDetail SET representative_id =? WHERE id =?";
                    try {
                        pstmt = con.prepareStatement(sql4);
                        pstmt.setInt(1, newRepresentativeId);
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println("\t\t\t\u001B[32mCập nhật người đại diện thành công!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null)
                                pstmt.close();
                        } catch (SQLException se2) {
                            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + se2.getMessage() + ".\u001B[0m");
                        }
                        do {
                            System.out.print("\t\t\tBạn có muốn tiếp tục sửa không (Y/N)?");
                            String c = sc.nextLine();

                            if ("Y".equalsIgnoreCase(c)) {
                                updateDonateDetail(con);
                            } else if ("N".equalsIgnoreCase(c)) {
                                break;
                            } else {
                                System.out.println("\t\t\t\u001B[31mLựa chọn nhập vào không đúng, vui lòng nhập lại!!!\u001B[0m");
                            }
                        } while (true);
                        waitForEnter();
                    }
                    break;
                //Update toàn bộ theo ID
                case "5":
                    System.out.print("\t\t\tNhập vào số tiền: ");
                    double newAmountAll = sc.nextDouble();
                    sc.nextLine(); // Consume the newline character
                    LocalDate newDonateDateAll = Processing.validateDateInput("\t\t\tNhập vào ngày nhận mới: ");
                    System.out.println("\t\t\tNhập vào ID Ủy ban: ");
                    int newCommissionIdAll = inputID(sc,"Commission", "id");
                    System.out.println("\t\t\tNhập vào ID của người đại diện: ");
                    int newRepresentativeIdAll = inputID(sc,"Representative", "id");
                    String combinedSql = "UPDATE DonateDetail SET amount =?, donate_date =?, commission_id =?, representative_id =? WHERE id =?";

                    try {
                        pstmt = con.prepareStatement(combinedSql);
                        pstmt.setDouble(1, newAmountAll);
                        pstmt.setDate(2, Date.valueOf(newDonateDateAll));
                        pstmt.setInt(3, newCommissionIdAll);
                        pstmt.setInt(4, newRepresentativeIdAll);
                        pstmt.setInt(5, identity);
                        pstmt.executeUpdate();
                        System.out.println("\t\t\t\u001B[32mCập nhật theo ID " + identity + " thành công!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null)
                                pstmt.close();
                        } catch (SQLException se2) {
                            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database: " + se2.getMessage() + ".\u001B[0m");
                        }
                        do {
                            System.out.print("\t\t\tBạn có muốn tiếp tục sửa không (Y/N)?");
                            String c = sc.nextLine();

                            if ("Y".equalsIgnoreCase(c)) {
                                updateDonateDetail(con);
                            } else if ("N".equalsIgnoreCase(c)) {
                                break;
                            } else {
                                System.out.println("\t\t\t\u001B[31mLựa chọn nhập vào không đúng, vui lòng nhập lại!!!\u001B[0m");
                            }
                        } while (true);
                        waitForEnter();
                    }
                    break;
            }
        } else {
            System.out.println("\t\t\t\u001B[31mKhông tìm thấy thông tin ủng hộ!!!\u001B[0m");
            waitForEnter();
        }
    }
    public static void statsTop5Officer(Connection con) {
        try {
            if (countRecords(con, "DonateDetail") > 0) {
                System.out.println();
                System.out.println("================================= DANH SÁCH ỦNG HỘ =================================");
                System.out.println("._______.________________________.________________________.________________________.");
                System.out.println("│   ID  │       Tên Cán bộ       │         Ủy ban         │  Tổng số lần tham gia  │");
                System.out.println("│_______│________________________│________________________│________________________│");
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("""
                       SELECT DISTINCT TOP 5
                           Officer.id,
                           Officer.name,
                           Commission.precint_name,
                           COUNT(OfficerDistribution.officer_id) as Stats
                       FROM
                           Officer
                               LEFT JOIN
                           dbo.Commission ON Officer.commision_id = Commission.id
                               LEFT JOIN
                           dbo.Distribution ON Commission.id = Distribution.commission_id
                               LEFT JOIN
                           dbo.OfficerDistribution ON Distribution.id = OfficerDistribution.distribution_id
                       GROUP BY
                           Officer.id, Officer.name, Commission.precint_name
                       ORDER BY
                           Stats DESC;
                        """);
                while (resultSet.next()) {
                    int ID = resultSet.getInt("id");
                    String companyName = resultSet.getString("name");
                    String precintName = resultSet.getString("precint_name");
                    int Stats = resultSet.getInt("Stats");
                    System.out.printf("│ %-5S │ %-22s │ %-22s │ %-22s │\n", ID, companyName, precintName, Stats);
                    System.out.println("│_______│________________________│________________________│________________________│");
                }
                System.out.println("================================ DANH SÁCH KẾT THÚC ================================");
            } else {
                System.out.println("\t\t\t\u001B[31mChưa có đợt ủng hộ nào.\u001B[31");
            }
            waitForEnter();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }
    }

    public static void statsSumAmountOfficer(Connection con){
        String selectQuery = """
               SELECT DISTINCT
                   Officer.id,
                   Officer.name,
                   Commission.precint_name,
                   SUM(Distribution.amount_distribution) as Stats
               FROM
                   Officer
                       LEFT JOIN
                           dbo.Commission ON Officer.commision_id = Commission.id
                       LEFT JOIN
                   dbo.Distribution ON Commission.id = Distribution.commission_id
                       LEFT JOIN
                   dbo.OfficerDistribution ON Distribution.id = OfficerDistribution.distribution_id
               WHERE Officer.id = ?
               GROUP BY
                   Officer.id, Officer.name, Commission.precint_name
               ORDER BY
                   Stats DESC""";
        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery)){
            System.out.println();
            OfficerManager.displayOfficerTable();
            waitForEnter();
            System.out.println("\t\t\tNhập vào ID của cán bộ: ");
            int id = Processing.inputID(sc, "Officer", "id");
            selectStatement.setInt(1, id);
            int i = 1;
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (countRecords(con, "OfficerDistribution") > 0) {
                    System.out.println();
                    System.out.println("================================= DANH SÁCH ỦNG HỘ =================================");
                    System.out.println("._______.________________________.________________________.________________________.");
                    System.out.println("│  STT  │       Tên Cán bộ       │         Ủy ban         │  Tổng giá trị ủng hộ   │");
                    System.out.println("│_______│________________________│________________________│________________________│");
                    while (resultSet.next()) {
                        String companyName = resultSet.getString("name");
                        String precintName = resultSet.getString("precint_name");
                        int Stats = resultSet.getInt("Stats");
                        System.out.printf("│ %-5S │ %-22s │ %-22s │ %-22s │\n", i, companyName, precintName, Stats);
                        System.out.println("│_______│________________________│________________________│________________________│");
                        i++;
                    }
                    System.out.println("================================ DANH SÁCH KẾT THÚC ================================");
                } else {
                    System.out.println("\t\t\t\u001B[31mChưa có đợt ủng hộ nào.\u001B[0m");
                }
                waitForEnter();
            }
            }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }
    }
    public static void statsCountSumAmount(Connection con) {
        System.out.println("\t\t\tNhập ID của hộ dân (X): ");
        int householdId = Processing.inputID(sc, "House", "id");
        String SQL_QUERY_STATS_BY_HOUSEHOLD = """
        SELECT
            House.id,
            Citizen.name,
            Citizen.address,
            CO.type_name_object,
            PriorityObject.object_type,
            COUNT(Distribution.amount_distribution) AS SL,
            SUM(Distribution.amount_distribution) AS TS
        FROM
            House
            LEFT JOIN Citizen ON House.id = Citizen.house_id
            LEFT JOIN dbo.CitizenObject CO ON CO.id = Citizen.citizen_object_id
            LEFT JOIN PriorityObject ON House.priority_object_id = PriorityObject.id
            LEFT JOIN Distribution ON House.id = Distribution.household_id
        WHERE
            House.id = ? AND Citizen.is_household_lord = 1
        GROUP BY
            House.id, Citizen.name, Citizen.address, CO.type_name_object, PriorityObject.object_type
        ORDER BY
            TS DESC;
    """;
        try (PreparedStatement preparedStatement = con.prepareStatement(SQL_QUERY_STATS_BY_HOUSEHOLD)) {
            preparedStatement.setInt(1, householdId);

            if (countRecords(con, "DonateDetail") > 0) {
                System.out.println();
                System.out.println("================================================================ DANH SÁCH HỘ DÂN ===========================================================");
                System.out.println("._______.________________________.________________________.________________________.___________________________.____________._______________.");
                System.out.println("│   ID  │      Họ tên chủ hộ     │        Địa chỉ         │   Đối tượng công dân   │     Đối tượng hộ dân      │    COUNT   │      SUM      │");
                System.out.println("│_______│________________________│________________________│________________________│___________________________│____________│_______________│");

                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        int houseID = resultSet.getInt("id");
                        String citizenName = resultSet.getString("name");
                        String citizenAddress = resultSet.getString("address");
                        String typeNameObject = resultSet.getString("type_name_object");
                        String objectType = resultSet.getString("object_type");
                        int SL = resultSet.getInt("SL"); // Corrected column name
                        String TS = String.format("%.0f", resultSet.getDouble("TS"));
                        System.out.printf("│ %-5S │ %-22s │ %-22s │ %-22s │ %-25s │ %-10s │ %-13s │%n", houseID, citizenName, citizenAddress, typeNameObject, objectType, SL, TS);
                        System.out.println("│_______│________________________│________________________│________________________│___________________________│____________│_______________│");
                    }
                    System.out.println("============================================================ DANH SÁCH KẾT THÚC =============================================================");
                }
            } else {
                System.out.println("\t\t\t\u001B[31mChưa có đợt ủng hộ nào.\u001B[31");
            }
            waitForEnter();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }
    }


    public static void displayAndSaveDistribution(Connection connection){
        LocalDate userInputDate = Processing.validateDateInput("Nhập vào đợt ủng hộ: ");
        // Thực hiện truy vấn SQL để lấy dữ liệu
        String sqlQuery = """
            
                WITH UnaidedHouseholds AS (
                SELECT
                    h.id AS household_id,
                    d.id AS distribution_id,
                    d.amount_distribution - COALESCE(SUM(dd.amount), 0) AS remaining_amount
                FROM
                    House h
                        INNER JOIN
                    Distribution d ON h.id = d.household_id
                        LEFT JOIN
                    DonateDetail dd ON d.id = dd.id
                WHERE
                    d.date_distribution BETWEEN DATEADD(MONTH, DATEDIFF(MONTH, 0, ?), 0) AND DATEADD(MONTH, DATEDIFF(MONTH, 0, ?) + 1, -1)
                GROUP BY
                    h.id, d.id, d.amount_distribution
            ),
                 TotalRemainingAmount AS (
                     SELECT
                         distribution_id,
                         SUM(remaining_amount) AS total_remaining_amount
                     FROM
                         UnaidedHouseholds
                     GROUP BY
                         distribution_id
                 )
            SELECT
                c.id AS commission_id,
                uhh.household_id,
                IIF(tra.total_remaining_amount = 0, 0, uhh.remaining_amount / NULLIF(tra.total_remaining_amount, 0) * d.amount_distribution) AS allocated_amount,
                c.precint_name as precint_name,
                citizen.name as household_lord_name
            FROM
                UnaidedHouseholds uhh
                    JOIN
                TotalRemainingAmount tra ON uhh.distribution_id = tra.distribution_id
                    JOIN
                Distribution d ON uhh.distribution_id = d.id
                    JOIN
                Commission c ON d.commission_id = c.id
                    JOIN
                House h ON uhh.household_id = h.id
                    JOIN
                Citizen citizen ON h.id = citizen.house_id AND citizen.is_household_lord = 1
                         ORDER BY allocated_amount DESC;
                        """;
        try (PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery)) {
            // Assuming userInputDate is a LocalDate
            LocalDate thirtyDaysAgo = userInputDate.minusDays(30);

            // Convert LocalDate to java.sql.Date
            Date userInputSqlDate = Date.valueOf(userInputDate);
            Date thirtyDaysAgoSqlDate = Date.valueOf(thirtyDaysAgo);

            // Use in preparedStatement
            preparedStatement.setDate(2, userInputSqlDate);
            preparedStatement.setDate(1, thirtyDaysAgoSqlDate);

            // Thực hiện truy vấn và hiển thị kết quả
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int householdId = resultSet.getInt("household_id");
                int commissionId = resultSet.getInt("commission_id");
                String householdLordName = resultSet.getString("household_lord_name");
                String precintName = resultSet.getString("precint_name");
                String allocatedAmount = String.format("%.0f",resultSet.getDouble("allocated_amount"));
                System.out.println();
                System.out.println("\t\t\tHousehold ID: " + householdId);
                System.out.println("\t\t\tHousehold Lord Name: " + householdLordName);
                System.out.println("\t\t\tCommission ID: " + commissionId);
                System.out.println("\t\t\tCommission Name: " + precintName);
                System.out.println("\t\t\tAllocated Amount per Household: " + allocatedAmount);
                System.out.println("\t\t\t----------------------------");
            }
            waitForEnter();
            // Hỏi người dùng có muốn thực hiện INSERT không
            System.out.print("\t\t\t\u001B[32mBạn có muốn thực hiện INSERT không? (Y/N): \u001B[0m");
            String input = sc.nextLine();

            // Nếu người dùng nhập Y, thực hiện lệnh INSERT
            if ("Y".equalsIgnoreCase(input)) {
                insertIntoDistribution(connection, preparedStatement);
                System.out.println("\t\t\t\u001B[32mInstert thành công!!!\u001B[0m");
                System.out.print("\t\t\tBạn có muốn xem lại bảng 'PHANPHOI' không? (Y/N): ");
                String print = sc.nextLine();
                if ("Y".equalsIgnoreCase(print)) {
                    printDistribution(con);
                    waitForEnter();
                }
            } else {
                System.out.println("\t\t\t\u001B[31mNgười dùng đã chọn không thực hiện INSERT.\u001B[0m");
                waitForEnter();
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }
    }
    private static void insertIntoDistribution(Connection connection, PreparedStatement preparedStatement){
        // Thực hiện lệnh INSERT
        String insertQuery = "INSERT INTO dbo.Distribution (commission_id, household_id, amount_distribution, date_distribution) "
                + "VALUES (?, ?, ?, GETDATE())";

        try (PreparedStatement insertStatement = connection.prepareStatement(insertQuery)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int commissionId = resultSet.getInt("commission_id");
                int householdId = resultSet.getInt("household_id");
                double allocatedAmount = resultSet.getDouble("allocated_amount");

                // Thiết lập giá trị cho lệnh INSERT
                insertStatement.setInt(1, commissionId);
                insertStatement.setInt(2, householdId);
                insertStatement.setDouble(3, allocatedAmount);

                // Thực hiện lệnh INSERT
                int affectedRows = insertStatement.executeUpdate();
                if (affectedRows == 0) {
                    System.out.println("\u001B[31mĐã có lỗi xảy ra khi thực hiện lệnh INSERT.\u001B[0m");
                }
            }
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }
    }
}
