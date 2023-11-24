package Model;

import Common.JdbcConnect;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static Model.Processing.*;

public class DonateDetailManager {
    private static Scanner scanner = new Scanner(System.in);
    public static void printDonateDetail(Connection con){
        try {
                System.out.println("========================================================== DANH SÁCH ỦNG HỘ =========================================================");
                System.out.println("._______.__________________________.____________________._________________._________________._________________._____________________.");
                System.out.println("│  STT  │          Số tiền         │    Ngày ủng hộ     │   Xã/Phường     │  Người đại diện │   Tên Công ty   │   Cán bộ tiếp nhận  │");
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
                    System.out.printf("│ %-5S │ %-12s │ %-24s │ %-18s │ %-15s │ %-15s │ %-15s │ %-11s │\n", ID, amount, dateFormat.format(donate_date), precint_name,representative_name ,company_name, name);
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
        try{System.out.println("=== Thêm thông tin nhà ủng hộ ===");
            String insertQuery = "INSERT INTO DonateDetail (representative_id, commission_id, donate_date, amount) VALUES (?, ?, ?, ?)";
            PreparedStatement insertStatement = con.prepareStatement(insertQuery);
            System.out.print("ID của người ủng hộ đại diện (cá nhân hoặc đại diện công ty) (Tham khảo menu \"Quản lý người đại diện\"): ");
            int representative_id = scanner.nextInt();
            System.out.print("Nhập vào ID của xã/phường được nhận hỗ trợ (Tham khảo menu \"Quản lý Ủy ban\"): ");
            int commission_id = scanner.nextInt();
            LocalDate donate_date = null;
            String donate_dateStr = null;
            do
            {
                System.out.print("Nhập vào ngày nhận hỗ trợ (theo định dạng MM/dd/yyyy): ");
                donate_dateStr = scanner.nextLine();

                try
                {
                    donate_date = LocalDate.parse(donate_dateStr, dateFormat);
                    if (donate_date.isAfter(LocalDate.now()))
                    {
                        System.out.println("\u001B[31mNgày nhận hỗ trợ phải trước hơn ngày hiện tại!\n Bạn không thể du hành thời gian đúng chứ!\u001B[0m");
                        donate_date = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                    }
                }
                catch (DateTimeParseException ex)
                {
                    System.out.println("\u001B[31m Ngày tháng nhập vào \"" + donate_date + "\" không hợp lệ.\u001B[0m");
                    donate_date = null; // Cập nhật giá trị donate_date để vòng lặp tiếp tục
                }
            }
            while (donate_date == null);

            System.out.print("Nhập vào số tiền được ủng hộ: ");
            double amount = scanner.nextDouble();
            insertStatement.setInt(1, representative_id);
            insertStatement.setInt(2, commission_id);
            insertStatement.setDate(3, Date.valueOf(donate_date));
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
    public void deleteDonateDetail(Connection con) {
            System.out.print("Bạn sắp thao tác xóa một record của 1 lần ủng hộ của nhà tài trợ.\nVui lòng tham khảo các bảng khác trong co sở dữ liệu để tránh sai xót!\n((Các bảng liên quan xin vui lòng tham khảo menu \"Quản lý người đại diện\" và \" Quản lý ủy ban\")  ");
            waitForEnter();
            System.out.print("Vui lòng nhập vào ID của record muốn xóa: ");
        String identity = scanner.nextLine();// Tạm thời xóa record theo ID
        System.out.println();
        PreparedStatement pstmt = null;
        String tableName = "DonateDetail";
        try {
//			Kiem Tra Su Ton Ton Tai Cua Khach Hang, Neu Ton Tai Thi Tien Hanh Xoa
            if (Processing.isIDAlreadyExists(con, identity, tableName) == true){
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
                        }

                    } else {
                        System.out.println(" \u001B[31mINFOR:Không có kết quả nào bị xóa!!!\u001B[0m");
                }
            } else {
                System.out.println("\u001B[31mKết quả không tồn tại trong Database!!!\u001B[0m");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
            } catch (SQLException se2) {
                se2.printStackTrace();
            }
        }
    }
}
