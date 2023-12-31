package Model;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static Model.Processing.*;

public class DonateDetailManager {
    private final static Scanner sc = new Scanner(System.in);
    public static void printDonateDetail(Connection con){
        try {
            System.out.println();
            System.out.println("========================================================== DANH SÁCH ỦNG HỘ =========================================================");
            System.out.println("._______.__________________________.____________________._________________._________________._________________._____________________.");
            System.out.println("│   ID  │          Số tiền         │    Ngày ủng hộ     │   Xã/Phường     │  Người đại diện │   Tên Công ty   │   Cán bộ tiếp nhận  │");
            System.out.println("│_______│__________________________│____________________│_________________│_________________│_________________│_____________________│");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT DonateDetail.id, amount, donate_date, precint_name, representative_name, company_name, name FROM DonateDetail Join dbo.Commission C on DonateDetail.commission_id = C.id JOIN dbo.Representative R on R.id = DonateDetail.representative_id join dbo.Company C2 on C2.id = R.company_id join dbo.Officer O on O.id = C.officer_id");
            while (resultSet.next())
            {
                String ID = resultSet.getString("DonateDetail.id");
                double amount = resultSet.getDouble("amount");
                LocalDate donate_date = resultSet.getDate("donate_date").toLocalDate();
                String precint_name = resultSet.getString("precint_name");
                String representative_name = resultSet.getString("representative_name");
                String company_name = resultSet.getString("company_name");
                String name = resultSet.getString("name");
                System.out.printf("│ %-5S │ %-12s │ %-24s │ %-18s │ %-15s │ %-15s │ %-15s │\n", ID, amount, dateFormat.format(donate_date), precint_name,representative_name ,company_name, name);
                System.out.println("│_______│______________│__________________________│____________________│_________________│_________________│_________________│_____________│");
            }
            System.out.println("========================================================= DANH SÁCH KẾT THÚC ========================================================");
            waitForEnter();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }
    }
    public static void addDonateDetail(Connection con){
        try{
            System.out.println();
            System.out.println("=== Thêm thông tin nhà ủng hộ ===");
            String insertQuery = "INSERT INTO DonateDetail (representative_id, commission_id, donate_date, amount) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = con.prepareStatement(insertQuery);
            System.out.print("ID của người ủng hộ đại diện (cá nhân hoặc đại diện công ty) (Tham khảo menu \"Quản lý người đại diện\"): ");
            int representativeId = Processing.inputID(sc, "Representative", "id");
            System.out.println("ID của xã/phường được nhận hỗ trợ (Tham khảo menu \"Quản lý Ủy ban\"): ");
            int commissionId = Processing.inputID(sc, "Commission", "id");
            sc.nextLine(); // Consume the newline character

            LocalDate donateDate;
            String donateDateStr;
            do {
                System.out.print("Nhập vào ngày nhận hỗ trợ (theo định dạng dd/MM/yyyy): ");
                donateDateStr = sc.nextLine();

                try {
                    donateDate = LocalDate.parse(donateDateStr, dateFormat);
                    if (donateDate.isAfter(LocalDate.now())) {
                        System.out.println("\u001B[31mNgày nhận hỗ trợ phải trước hơn ngày hiện tại!\n Bạn không thể du hành thời gian đúng chứ!\u001B[0m");
                        donateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                    }
                } catch (DateTimeParseException ex) {
                    System.out.println("\u001B[31m Ngày tháng nhập vào \"" + donateDateStr + "\" không hợp lệ.\u001B[0m");
                    donateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                }
            } while (donateDate == null);
            double amount = 0;
            String input;

            do {
                System.out.print("Nhập vào số tiền được ủng hộ: ");
                input = sc.next();

                if (!isFloatNumber(input)) {
                    System.out.println("\u001B[31mSố tiền không hợp lệ. Vui lòng nhập lại.\u001B[0m");
                } else {
                    amount = Double.parseDouble(input);
                }
            } while (!isFloatNumber(input));
            insertStatement.setInt(1, representativeId);
            insertStatement.setInt(2, commissionId);
            insertStatement.setDate(3, Date.valueOf(donateDate));
            insertStatement.setDouble(4, amount);
            System.out.println("\u001B[32mNhà hỗ trợ đã được thêm vào cơ sở dữ liệu.\u001B[0m");
            waitForEnter();
        }
        catch (SQLException e)
        {
            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
        }
    }

    //	Xoa Thong Tin Khach Hang
    public static void deleteDonateDetail(Connection con) {
        System.out.println();
        System.out.print("Bạn sắp thao tác xóa một record của 1 lần ủng hộ của nhà tài trợ.\nVui lòng tham khảo các bảng khác trong co sở dữ liệu để tránh sai xót!\n(Các bảng liên quan xin vui lòng tham khảo menu \"Quản lý người đại diện\" và \" Quản lý Ủy ban\")  ");
        sc.nextLine();
        System.out.print("Vui lòng nhập vào ID của record muốn xóa: ");
        int identity = inputID(sc, "DonateDetail", "id");// Tạm thời xóa record theo ID
        System.out.println();
        PreparedStatement pstmt = null;
        String tableName = "DonateDetail";
        try {
//			Kiem Tra Su Ton Ton Tai Cua record, Neu Ton Tai Thi Tien Hanh Xoa
            if (Processing.isIDAlreadyExists(con, identity, tableName)){
            String chose = null;
                    do {
                        System.out.print("Bạn có chắc chắn muốn xóa (Y/N)?");
                        chose = sc.nextLine().trim();
                    } while (!("Y".equalsIgnoreCase(chose) || "N".equalsIgnoreCase(chose)));

                    if ("Y".equalsIgnoreCase(chose)) {
                        //Xóa
                        String sqlDelete = "DELETE FROM DonateDetail WHERE ID=?";
                        pstmt = con.prepareStatement(sqlDelete);
                        pstmt.setInt(1, identity);
                        if (pstmt.executeUpdate() != 0) {
                            System.out.println("Xóa thành công!!!");
                            waitForEnter();
                        }

                    } else {
                        System.out.println(" \u001B[31mINFOR:Không có kết quả nào bị xóa!!!\u001B[0m");
                        waitForEnter();
                }
            } else {
                System.out.println("\u001B[31mKết quả không tồn tại trong Database!!!\u001B[0m");
                waitForEnter();
            }
        } catch (Exception e) {
            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se2) {
                System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + se2.getMessage() + ".\u001B[0m");
            }
        }
    }

    //	Chuc Nang Cap Nhat Thong Tin Khach Hang
    public static void updateDonateDetail(Connection con) {
        System.out.println();
        System.out.print("Bạn sắp thao tác cập nhật một record của 1 lần ủng hộ của nhà tài trợ.\nVui lòng tham khảo các bảng khác trong cơ sở dữ liệu để tránh sai xót!\n(Các bảng liên quan xin vui lòng tham khảo menu \"Quản lý người đại diện\" và \" Quản lý ủy ban\")  ");
        sc.nextLine();
        System.out.print("Vui lòng nhập vào ID của record muốn update: ");
        int identity = inputID(sc, "DonateDetail", "id");// Tạm thời xóa record theo ID
        System.out.println();
        PreparedStatement pstmt = null;
//		Kiem Tra id Da Co Trong Bang Khang Hang Hay Chua? Neu Co Thi Tien Hanh Update
        if (Processing.isIDAlreadyExists(con, identity, "DonateDetail" )) {
            String chose =null;
            boolean check;
//			Hien Thi Menu Update
            do {
                check = true;
                System.out.println("+-------------------------------+");
                System.out.println("|   Bạn muốn update mục nào?    |");
                System.out.println("+-------------------------------+");
                System.out.printf("| %-30s|\n| %-30s| \n| %-30s| \n| %-30s| \n| %-30s| \n", "1. Số tiền",
                        "2. Ngày ủng hộ", "3. Ủy ban nhận", "4. Người đại diện", "5. Tất cả");
                System.out.println("+-------------------------------+");
                System.out.print(" From Update Menu, Your Choice: ");
                sc.nextLine();
                chose = sc.nextLine().trim();
                if (!("1".equals(chose) || "2".equals(chose) || "3".equals(chose) || "4".equals(chose)|| "5".equals(chose))) {
                    check = false;
                    System.out.println("\u001B[31mGiá trị nhập vào không đúng, vui lòng nhập lại!!!\u001B[0m");
                }
            } while (!(Processing.isNumber(chose) && check));

            switch (chose) {
//			Update Số tiền
                case "1":
                    System.out.print("Nhập vào số tiền: ");
                    double newAmount = sc.nextDouble();
                    String sql1 = "UPDATE DonateDetail SET amount =? WHERE id =?";

                    try {
                        pstmt = con.prepareStatement(sql1);
                        pstmt.setDouble(1, newAmount);
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println(" \u001B[32mCập nhật số tiền thành công!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null)
                                pstmt.close();
                        } catch (SQLException se2) {
                            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + se2.getMessage() + ".\u001B[0m");
                        }
                    }
                    break;
//			Update ngày ủng hộ
                case "2":
                    String sql2 = "UPDATE DonateDetail SET donate_date =? WHERE id =?";
                    LocalDate newDonateDate = null;
                    String newDonateDateStr = null;
                    do
                    {
                        System.out.print("Nhập vào ngày nhận hỗ trợ (theo định dạng dd/MM/yyyy): ");
                        newDonateDateStr = sc.nextLine();

                        try
                        {
                            newDonateDate = LocalDate.parse(newDonateDateStr, dateFormat);
                            if (newDonateDate.isAfter(LocalDate.now()))
                            {
                                System.out.println("\u001B[31mNgày nhận hỗ trợ phải trước hơn ngày hiện tại!\n Bạn không thể du hành thời gian đúng chứ!\u001B[0m");
                                newDonateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                            }
                        }
                        catch (DateTimeParseException ex)
                        {
                            System.out.println("\u001B[31m Ngày tháng nhập vào \"" + newDonateDateStr + "\" không hợp lệ.\u001B[0m");
                            newDonateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                        }
                    }
                    while (newDonateDate == null);
                    try {
                        pstmt = con.prepareStatement(sql2);
                        pstmt.setDate(1, Date.valueOf(newDonateDate));
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println(" \u001B[32mCập nhật ngày ủng hộ thành công!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null)
                                pstmt.close();
                        } catch (SQLException e) {
                            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                        }
                    }
                    break;
//			Update ID Ủy ban
                case "3":
                    System.out.print("Nhập vào ID Ủy ban: ");
                    int newCommissionId = inputID(sc, "Commission", "id");
                    String sql3 = "UPDATE DonateDetail SET commission_id =? WHERE id =?";
                    try {
                        pstmt = con.prepareStatement(sql3);
                        pstmt.setInt(1, newCommissionId);
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println("\u001B[32mCập nhật Ủy ban thành công!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null)
                                pstmt.close();
                        } catch (SQLException e) {
                            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                        }
                    }
                    break;

//			Update ID nguoi dai dien
                case "4":
                    System.out.println("Nhập vào ID của người đại diện: ");
                    int newRepresentativeId = inputID(sc,"Representative", "id");
                    String sql4 = "UPDATE DonateDetail SET representative_id =? WHERE id =?";
                    try {
                        pstmt = con.prepareStatement(sql4);
                        pstmt.setInt(1, newRepresentativeId);
                        pstmt.setInt(2, identity);
                        pstmt.executeUpdate();
                        System.out.println("\u001B[32mCập nhật người đại diện thành công!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null)
                                pstmt.close();
                        } catch (SQLException e) {
                            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                        }
                    }
                    break;
                //Update toàn bộ theo ID
                case "5":
                    System.out.print("Nhập vào số tiền: ");
                    double newAmountAll = sc.nextDouble();
                    LocalDate newDonateDateAll = null;
                    String newDonateDateAllStr;
                    do
                    {
                        System.out.print("Nhập vào ngày nhận hỗ trợ (theo định dạng dd/MM/yyyy): ");
                        newDonateDateAllStr = sc.nextLine();

                        try
                        {
                            newDonateDateAll = LocalDate.parse(newDonateDateAllStr, dateFormat);
                            if (newDonateDateAll.isAfter(LocalDate.now()))
                            {
                                System.out.println("\u001B[31mNgày nhận hỗ trợ phải trước hơn ngày hiện tại!\n Bạn không thể du hành thời gian đúng chứ!\u001B[0m");
                                newDonateDateAll = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                            }
                        }
                        catch (DateTimeParseException ex)
                        {
                            System.out.println("\u001B[31m Ngày tháng nhập vào \"" + newDonateDateAllStr + "\" không hợp lệ.\u001B[0m");
                            newDonateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                        }
                    }
                    while (newDonateDateAll == null);
                    System.out.print("Nhập vào ID Ủy ban: ");
                    int newCommissionIdAll = inputID(sc,"Commission", "id");
                    System.out.println("Nhập vào ID của người đại diện: ");
                    int newRepresentativeIdAll = inputID(sc,"Representative", "id");
                    String combinedSql = "UPDATE DonateDetail SET amount =?, donate_date =?, commission_id =?, representative_id =? WHERE id =?";

                    try {
                        pstmt = con.prepareStatement(combinedSql);
                        pstmt.setDouble(1, newAmountAll);
                        pstmt.setDate(2, Date.valueOf(newDonateDateAll));
                        pstmt.setInt(3, newCommissionIdAll);
                        pstmt.setInt(4, newRepresentativeIdAll);
                        pstmt.executeUpdate();
                        System.out.println("\u001B[32mCập nhật theo ID " + identity + " thành công!!!\u001B[0m");
                        waitForEnter();
                    } catch (Exception e) {
                        System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                    } finally {
                        try {
                            if (pstmt != null)
                                pstmt.close();
                        } catch (SQLException e) {
                            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
                        }
                    }
                    break;
            }
        } else {
            System.out.println("\u001B[31mKhông tìm thấy thông tin ủng hộ!!!\u001B[0m");
            waitForEnter();
        }
    }
}
