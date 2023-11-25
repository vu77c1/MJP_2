package Model;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static Model.Processing.*;

public class DonateDetailManager {
    private final static Scanner scanner = new Scanner(System.in);
    public static void printDonateDetail(Connection con){
        try {
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
            System.out.println("=== Thêm thông tin nhà ủng hộ ===");
            String insertQuery = "INSERT INTO DonateDetail (representative_id, commission_id, donate_date, amount) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = con.prepareStatement(insertQuery);
            System.out.print("ID của người ủng hộ đại diện (cá nhân hoặc đại diện công ty) (Tham khảo menu \"Quản lý người đại diện\"): ");
            int representative_id = scanner.nextInt();
            System.out.print("Nhập vào ID của xã/phường được nhận hỗ trợ (Tham khảo menu \"Quản lý Ủy ban\"): ");
            int commission_id = scanner.nextInt();
            LocalDate donateDate = null;
            String donateDateStr = null;
            do
            {
                System.out.print("Nhập vào ngày nhận hỗ trợ (theo định dạng MM/dd/yyyy): ");
                donateDateStr = scanner.nextLine();

                try
                {
                    donateDate = LocalDate.parse(donateDateStr, dateFormat);
                    if (donateDate.isAfter(LocalDate.now()))
                    {
                        System.out.println("\u001B[31mNgày nhận hỗ trợ phải trước hơn ngày hiện tại!\n Bạn không thể du hành thời gian đúng chứ!\u001B[0m");
                        donateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                    }
                }
                catch (DateTimeParseException ex)
                {
                    System.out.println("\u001B[31m Ngày tháng nhập vào \"" + donateDateStr + "\" không hợp lệ.\u001B[0m");
                    donateDate = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                }
            }
            while (donateDate == null);

            System.out.print("Nhập vào số tiền được ủng hộ: ");
            double amount = scanner.nextDouble();
            insertStatement.setInt(1, representative_id);
            insertStatement.setInt(2, commission_id);
            insertStatement.setDate(3, Date.valueOf(donateDate));
            insertStatement.setDouble(4, amount);
            System.out.println("\u001B[32mNhà hỗ trợ đã được thêm vào cơ sở dữ liệu.\u001B[0m");
            waitForEnter();
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
        }
    }

    //	Xoa Thong Tin Khach Hang
    public static void deleteDonateDetail(Connection con) {
            System.out.print("Bạn sắp thao tác xóa một record của 1 lần ủng hộ của nhà tài trợ.\nVui lòng tham khảo các bảng khác trong co sở dữ liệu để tránh sai xót!\n((Các bảng liên quan xin vui lòng tham khảo menu \"Quản lý người đại diện\" và \" Quản lý Ủy ban\")  ");
            waitForEnter();
            System.out.print("Vui lòng nhập vào ID của record muốn xóa: ");
        String identity = scanner.nextLine();// Tạm thời xóa record theo ID
        System.out.println();
        PreparedStatement pstmt = null;
        String tableName = "DonateDetail";
        try {
//			Kiem Tra Su Ton Ton Tai Cua Khach Hang, Neu Ton Tai Thi Tien Hanh Xoa
            if (Processing.isIDAlreadyExists(con, identity, tableName)){
            String chose = null;
                    do {
                        System.out.print("Bạn có chắc chắn muốn xóa (Y/N)?");
                        chose = scanner.nextLine().trim();
                    } while (!("Y".equalsIgnoreCase(chose) || "N".equalsIgnoreCase(chose)));

                    if ("Y".equalsIgnoreCase(chose)) {
                        //Xóa
                        String sqlDelete = "DELETE FROM DonateDetail WHERE ID=?";
                        pstmt = con.prepareStatement(sqlDelete);
                        pstmt.setString(1, identity);
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
        System.out.print("Bạn sắp thao tác cập nhật một record của 1 lần ủng hộ của nhà tài trợ.\nVui lòng tham khảo các bảng khác trong co sở dữ liệu để tránh sai xót!\n((Các bảng liên quan xin vui lòng tham khảo menu \"Quản lý người đại diện\" và \" Quản lý ủy ban\")  ");
        waitForEnter();
        System.out.print("Vui lòng nhập vào ID của record muốn update: ");
        String identity = scanner.nextLine();// Tạm thời xóa record theo ID
        System.out.println();
        PreparedStatement pstmt = null;
        String tableName = "DonateDetail";

//		Kiem Tra CMND Da Co Trong Bang Khang Hang Hay Chua? Neu Co Thi Tien Hanh Update
        if (Processing.isIDAlreadyExists(con, identity, tableName )) {
            String chose =null;
            boolean check;
//			Hien Thi Menu Update
            do {
                check = true;
                System.out.println("+-------------------------------+");
                System.out.println("|   Bạn muốn update mục nào?    |");
                System.out.println("+-------------------------------+");
                System.out.printf("| %-30s|\n| %-30s| \n| %-30s| \n| %-30s| \n", "1. Số tiền",
                        "2. Ngày ủng hộ", "3. Ủy ban nhận", "4. Người đại diện");
                System.out.println("+-------------------------------+");
                System.out.print(" From Update Menu, Your Choice: ");
                chose = scanner.nextLine().trim();
                if (!("1".equals(chose) || "2".equals(chose) || "3".equals(chose) || "4".equals(chose))) {
                    check = false;
                    System.out.println("\u001B[31mGiá trị nhập vào không đúng, vui lòng nhập lại!!!\u001B[0m");
                }
            } while (!(Processing.isNumber(chose) && check));

            switch (chose) {
//			Update Số tiền
                case "1":
                    System.out.print("Nhập vào số tiền: ");
                    double newAmount = scanner.nextDouble();
                    String sql1 = "UPDATE DonateDetail SET amount =? WHERE id =?";

                    try {
                        pstmt = con.prepareStatement(sql1);
                        pstmt.setDouble(1, newAmount);
                        pstmt.setString(2, identity);
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
                        System.out.print("Nhập vào ngày nhận hỗ trợ (theo định dạng MM/dd/yyyy): ");
                        newDonateDateStr = scanner.nextLine();

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
                        pstmt.setString(2, identity);
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
                    String newCommissionId = scanner.nextLine();
                    String sql3 = "UPDATE DonateDetail SET commission_id =? WHERE id =?";
                    try {
                        pstmt = con.prepareStatement(sql3);
                        pstmt.setString(1, newCommissionId);
                        pstmt.setString(2, identity);
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
                    System.out.print("Nhập vào ID của người đại diện: ");
                    String newRepresentativeId = scanner.nextLine();
                    String sql4 = "UPDATE DonateDetail SET representative_id =? WHERE id =?";
                    try {
                        pstmt = con.prepareStatement(sql4);
                        pstmt.setString(1, newRepresentativeId);
                        pstmt.setString(2, identity);
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
            }
        } else {
            System.out.println("\u001B[31mKhông tìm thấy thông tin ủng hộ!!!\u001B[0m");
            waitForEnter();
        }
    }
}
