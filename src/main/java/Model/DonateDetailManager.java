package Model;

import Common.InputValidator;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static Model.Processing.*;

public class DonateDetailManager {
    private final static Scanner sc = new Scanner(System.in);
    public static void printDonateDetail(Connection con){
        try {
            if (countRecords(con, "DonateDetail")>0){
                System.out.println();
                System.out.println("============================================================= DANH SÁCH ỦNG HỘ ============================================================");
                System.out.println("._______.____________________.____________________.____________________.____________________.______________________.______________________.");
                System.out.println("│   ID  │       Số tiền      │    Ngày ủng hộ     │      Xã/Phường     │   Người đại diện   │      Tên Công ty     │   Cán bộ tiếp nhận   │");
                System.out.println("│_______│____________________│____________________│____________________│____________________│______________________│______________________│");
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
                                LEFT JOIN dbo.Officer O ON O.id = C.officer_id""");
                while (resultSet.next())
                {
                    String ID = resultSet.getString("id");
                    String amount = String.format("%.0f", resultSet.getDouble("amount"));
                    LocalDate donate_date = resultSet.getDate("donate_date").toLocalDate();
                    String precint_name = resultSet.getString("precint_name");
                    String representative_name = resultSet.getString("representative_name");
                    String company_name = resultSet.getString("company_name");
                    String name = resultSet.getString("name");
                    System.out.printf("│ %-5S │ %-18s │ %-18s │ %-18s │ %-18s │ %-20s │ %-20s │\n", ID, amount, dateFormat.format(donate_date), precint_name,representative_name ,company_name, name);
                    System.out.println("│_______│____________________│____________________│____________________│____________________│______________________│______________________│");
                }
                System.out.println("============================================================ DANH SÁCH KẾT THÚC ===========================================================");
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
                        LEFT JOIN dbo.Officer O ON O.id = C.officer_id WHERE DonateDetail.id = ?""";
        try (PreparedStatement selectStatement = con.prepareStatement(selectQuery)){
            selectStatement.setInt(1, ID);
            try (ResultSet resultSet = selectStatement.executeQuery()) {
                if (countRecords(con, "DonateDetail")>0){
                    while (resultSet.next())
                    {
                        System.out.println();
                        System.out.println("============================================================= DANH SÁCH ỦNG HỘ ============================================================");
                        System.out.println("._______.____________________.____________________.____________________.____________________.______________________.______________________.");
                        System.out.println("│   ID  │       Số tiền      │    Ngày ủng hộ     │      Xã/Phường     │   Người đại diện   │      Tên Công ty     │   Cán bộ tiếp nhận   │");
                        System.out.println("│_______│____________________│____________________│____________________│____________________│______________________│______________________│");
                        String amount = String.format("%.0f", resultSet.getDouble("amount"));
                        LocalDate donate_date = resultSet.getDate("donate_date").toLocalDate();
                        String precint_name = resultSet.getString("precint_name");
                        String representative_name = resultSet.getString("representative_name");
                        String company_name = resultSet.getString("company_name");
                        String name = resultSet.getString("name");
                        System.out.printf("│ %-5S │ %-18s │ %-18s │ %-18s │ %-18s │ %-20s │ %-20s │\n", ID, amount, dateFormat.format(donate_date), precint_name,representative_name ,company_name, name);
                        System.out.println("│_______│____________________│____________________│____________________│____________________│______________________│______________________│");
                    }
                    System.out.println("============================================================ DANH SÁCH KẾT THÚC ===========================================================");
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
                    LocalDate newDonateDate;
                    String newDonateDateStr;
                    do
                    {
                        System.out.print("\t\t\tNhập vào ngày nhận hỗ trợ (theo định dạng dd/MM/yyyy): ");
                        newDonateDateStr = sc.nextLine();

                        try
                        {
                            newDonateDate = LocalDate.parse(newDonateDateStr, dateFormat);
                            if (newDonateDate.isAfter(LocalDate.now()))
                            {
                                System.out.println("\t\t\t\u001B[31mNgày nhận hỗ trợ phải trước hơn ngày hiện tại!\n Bạn không thể du hành thời gian đúng chứ!\u001B[0m");
                                newDonateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                            }
                        }
                        catch (DateTimeParseException ex)
                        {
                            System.out.println("\t\t\t\u001B[31m Ngày tháng nhập vào \"" + newDonateDateStr + "\" không hợp lệ.\u001B[0m");
                            newDonateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                        }
                    }
                    while (newDonateDate == null);
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
                    LocalDate newDonateDateAll = null;
                    String newDonateDateAllStr;
                    do
                    {
                        System.out.print("\t\t\tNhập vào ngày nhận hỗ trợ (theo định dạng dd/MM/yyyy): ");
                        newDonateDateAllStr = sc.nextLine();

                        try
                        {
                            newDonateDateAll = LocalDate.parse(newDonateDateAllStr, dateFormat);
                            if (newDonateDateAll.isAfter(LocalDate.now()))
                            {
                                System.out.println("\t\t\t\u001B[31mNgày nhận hỗ trợ phải trước hơn ngày hiện tại!\n Bạn không thể du hành thời gian đúng chứ!\u001B[0m");
                                newDonateDateAll = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                            }
                        }
                        catch (DateTimeParseException ex)
                        {
                            System.out.println("\t\t\t\u001B[31m Ngày tháng nhập vào \"" + newDonateDateAllStr + "\" không hợp lệ.\u001B[0m");
                        }
                    }
                    while (newDonateDateAll == null);
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
    public static void statsTop3Amount(Connection con) {
        try {
            if (countRecords(con, "DonateDetail") > 0) {
                System.out.println("\t\t\t - Hiển thị top 5 cán bộ tham gia nhiều đợt ủng hộ nhất\n");
                System.out.println();
                System.out.println("================================= DANH SÁCH ỦNG HỘ =================================");
                System.out.println("._______.________________________.________________________.________________________.");
                System.out.println("│  STT  │       Tên Công ty      │     Người đại diện     │      Tổng số tiền      │");
                System.out.println("│_______│________________________│________________________│________________________│");
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("""
                        SELECT TOP 3 Company.company_name, Representative.representative_name, SUM(amount) AS Stats FROM DonateDetail
                            LEFT JOIN dbo.Representative ON DonateDetail.representative_id = Representative.id
                            LEFT JOIN dbo.Company on Representative.company_id = Company.id
                            GROUP BY Company.company_name, Representative.representative_name ORDER BY Stats DESC""");
                int i = 1;
                while (resultSet.next()) {
                    String companyName = resultSet.getString("company_name");
                    String representative_name = resultSet.getString("representative_name");
                    String Stats = String.format("%.0f", resultSet.getDouble("Stats"));
                    System.out.printf("│ %-5S │ %-22s │ %-22s │ %-22s │\n", i, companyName, representative_name, Stats);
                    System.out.println("│_______│________________________│________________________│________________________│");
                    i++;
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
                           dbo.Commission ON Officer.id = Commission.officer_id
                               LEFT JOIN
                           dbo.Distribution ON Commission.id = Distribution.commission_id
                               LEFT JOIN
                           dbo.OfficerDistribution ON Distribution.id = OfficerDistribution.distribution_id
                       GROUP BY
                           Officer.id, Officer.name, Commission.precint_name
                       ORDER BY
                           Stats DESC;
                        """);
                int i = 1;
                while (resultSet.next()) {
                    int ID = resultSet.getInt("id");
                    String companyName = resultSet.getString("name");
                    String precintName = resultSet.getString("precint_name");
                    int Stats = resultSet.getInt("Stats");
                    System.out.printf("│ %-5S │ %-22s │ %-22s │ %-22s │\n", ID, companyName, precintName, Stats);
                    System.out.println("│_______│________________________│________________________│________________________│");
                    i++;
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
                   SUM(Distribution.amount_received) as Stats
               FROM
                   Officer
                       LEFT JOIN
                   dbo.Commission ON Officer.id = Commission.officer_id
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
                    System.out.println("\t\t\t\u001B[31mChưa có đợt ủng hộ nào.\u001B[31");
                }
                waitForEnter();
            }
            }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\t\t\t\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }
    }
}
