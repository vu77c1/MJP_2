package Model;

import Common.InputValidator;
import Common.JDBCQuery;

import java.sql.*;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


import static Model.Processing.*;

public class DistributionManager {
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/uuuu").withResolverStyle(ResolverStyle.STRICT);
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private   static Scanner sc=new Scanner(System.in);


    public static void printDistribution(Connection con){
        try {
            System.out.println("************************************ DANH SÁCH PHÂN PHỐI ************************************");
            System.out.println("._______._____________________.____________________._________________._________________._________________._____________________.");
            System.out.println("│   ID  │     ID Xã/Phường   │    ID Chủ Hộ       │ Số tiền được nhận  │     Ngày nhận   │ ");
            System.out.println("│_______│____________________│____________________│____________________│_________________│  ");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Distribution.id, commission_id, household_id, amount_received, date_received  FROM Distribution Join dbo.Commission C on C.id= Distribution.commission_id");
            while (resultSet.next())
            {
                String ID = resultSet.getString("id");
                String commission_id = resultSet.getString("commission_id");
                String household_id = resultSet.getString("household_id");
                double amount_received = resultSet.getFloat("amount_received");
                LocalDate date_received = resultSet.getDate("date_received").toLocalDate();

                System.out.printf("│ %-5S │ %-18s │ %-18s │ %-18s │%-16s │ \n", ID, commission_id,household_id, amount_received,dateFormat.format(date_received));
                System.out.println("│_______│____________________│____________________│____________________│_________________│");
            }
            System.out.println("************************************ DANH SÁCH KẾT THÚC ************************************");
            waitForEnter();
        }catch (SQLException e) {

            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database\u001B[0m");
            System.out.println(e.getMessage());
        }
    }
    //Lay tat ca thong tin Distribution trong databse khong truyen tham so
    public ArrayList<Distribution> getDistribution() {
        ArrayList<Distribution> infoList = new ArrayList<>();
        try {
            JDBCQuery.openConnection();
            String sql = "select  * from Distribution";
            ResultSet rs = JDBCQuery.executeSelectQuery(sql);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        infoList.add(new Distribution(
                                rs.getInt("id"),
                                rs.getInt("commission_id"),rs.getInt("household_id"),
                                rs.getFloat("amount_received"),rs.getDate("date_received")
                        ));
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
        return infoList;
    }

    //Lay tat ca thong tin Distribution trong database co tham so
    public ArrayList<Distribution> getDistribution(String sql) {
        ArrayList<Distribution> infoList = new ArrayList<>();
        try {
            JDBCQuery.openConnection();
            ResultSet rs = JDBCQuery.executeSelectQuery(sql);
            if (rs != null) {
                try {
                    while (rs.next()) {
                        infoList.add(new Distribution(
                                rs.getInt("id"),
                                rs.getInt("commission_id"),rs.getInt("household_id"),
                                rs.getFloat("amount_received"),rs.getDate("date_received")
                        ));
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
        return infoList;
    }
    //Enter data
    public Distribution inputPriorityObject() {
        Integer comid = InputValidator.validateIntInput("\t\t\tEnter New Commission ID:  ");
        Integer houseid=InputValidator.validateIntInput("\t\t\tEnter New HouseHold  ID:  ");
        Double amount=InputValidator.validateDoubleInput("\t\t\tEnter New Amount Received:  ");
        java.util.Date datereceived=InputValidator.validateDateInput("\t\t\tEnter New Date Received with format dd/MM/yyyy:  ");
        Distribution di = new Distribution();
        di.setCommissionId(comid);
        di.setHouseholdID(houseid);
        di.setAmountReceived(amount);
        di.setDateReceived(datereceived);
        return di;
    }

    //Thêm dữ liêu vào Distribution
    public void addDistribution() {


        try {
            Distribution di = inputPriorityObject();
            JDBCQuery.openConnection();
            String sql = "insert into Distribution (commission_id, household_id, amount_received, date_received) values (?,?,?,?)";

            Object[] prams = {di.getCommissionId(), di.getHouseholdID(),di.getAmountReceived(),di.getDateReceived()};
            int rs = JDBCQuery.executeUpdateQuery(sql, prams);
            if (rs > 0) {
                System.out.println("\t\t\tAdd success!!");
            } else {
                System.out.println("\t\t\tAdd failed!!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            JDBCQuery.closeConnection();
        }

    }
    //hàm cập nhật lại thông tin
    public void updatePriorityObject() {
        int id;
        id = InputValidator.validateIntInput("\t\t\tEnter ID to update: ");
        if (isIdExists(id)) {
            displayPriorityObjects(getDistribution("select * from Distribution where id=" + id));


            Integer comid = InputValidator.validateIntInput("\t\t\tEnter New Commission ID:  ");
            Integer houseid=InputValidator.validateIntInput("\t\t\tEnter New HouseHold  ID:  ");
            Double amount=InputValidator.validateDoubleInput("\t\t\tEnter New Amount Received:  ");
            java.util.Date datereceived=InputValidator.validateDateInput("\t\t\tEnter New Date Received with format dd/MM/yyyy:  ");


            try {

                JDBCQuery.openConnection();

                String sql = "UPDATE Distribution SET commission_id = ?, household_id = ?,amount_received = ?,date_received = ? WHERE id =?;";
                Object[] prams = {comid, houseid, amount, datereceived,id};

                int rowsAffected = JDBCQuery.executeUpdateQuery(sql, prams);

                if (rowsAffected > 0) {
                    System.out.println("\t\t\tUpdate success!!");
                } else {
                    System.out.println("\t\t\tUpdate failed. No Distribution found with the specified ID.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                JDBCQuery.closeConnection();
            }

        } else {
            System.out.println("\t\t\tUpdate failed. The list of Distribution is empty.");
        }
    }

    //hàm xóa phân phối theo id
    public void deleteDistribution() {
        int id;
        id = InputValidator.validateIntInput("\t\t\tEnter ID to delete: ");
        if (isIdExists(id)) {
            try {
                JDBCQuery.openConnection();

                String sql = "DELETE FROM Distribution WHERE id = ?";
                Object[] params = {id};
                int rowsAffected = JDBCQuery.executeUpdateQuery(sql, params);

                if (rowsAffected > 0) {
                    System.out.println("\t\t\tDelete success!!");
                } else {
                    System.out.println("\t\t\tDelete failed. No Distribution found with the specified ID.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                JDBCQuery.closeConnection();
            }

        } else {
            System.out.println("\t\t\tDelete failed. The list of Distribution is empty.");
        }

    }


    //hiển thị danh sách Phân phối
    public void displayPriorityObjects(ArrayList<Distribution> distributions) {
        System.out.println("\t\t\tID\t|\tCommissionID|\tHouseHoldID\t|\tAmountReceived\t|\tDateReceived");

        for (Distribution di : distributions) {
            System.out.println("\t\t\t" + di.getId() + "\t|\t\t" + di.getCommissionId() +"\t\t|\t\t"+
                    di.getHouseholdID() +"\t\t|\t\t"+ di.getAmountReceived()+"\t|\t" + di.getDateReceived());
        }
    }



    //Kiểm tra Distribution có tồn tại không

    public boolean isIdExists(int id) {
        boolean exists = false;
        try {
            JDBCQuery.openConnection();

            String sql = "SELECT COUNT(*) FROM Distribution WHERE id = ?";
            Object[] params = {id};

            ResultSet rs = JDBCQuery.executeSelectQuery(sql, params);

            if (rs.next()) {
                int count = rs.getInt(1);
                exists = (count > 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBCQuery.closeConnection();
        }
        return exists;
    }

}


