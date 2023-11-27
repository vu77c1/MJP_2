package Model;

import Common.InputValidator1;
import Common.JDBCQuery1;

import java.sql.*;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Scanner;


import static Model.Processing.*;

public class DistributionManager {
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu/MM/dd").withResolverStyle(ResolverStyle.STRICT);
    public static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private   static Scanner sc=new Scanner(System.in);



    public static void printDistribution(Connection con){
        try {
            System.out.println("\t\t\t********************************************* DISTRIBUTION LIST *****************************************");
            System.out.println("\t\t\t._______.______________________________________.____________________._________________._________________.__");
            System.out.println("\t\t\t│   ID  │         ID Commission            │    ID Household    │ Amount received    │  date_received  │ ");
            System.out.println("\t\t\t│_______│__________________________________│____________________│____________________│_________________│  ");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT Distribution.id,commission_id, precint_name, city_name,province_name, household_id, amount_received, date_received  FROM Distribution Join dbo.Commission C on C.id= Distribution.commission_id");
            while (resultSet.next())
            {
                String ID = resultSet.getString("id");
                String commission_id = resultSet.getString("commission_id");
                String precint_name = resultSet.getString("precint_name");
                String city_name = resultSet.getString("city_name");
                String province_name = resultSet.getString("province_name");
                String household_id = resultSet.getString("household_id");
                double amount_received = resultSet.getFloat("amount_received");
                LocalDate date_received = resultSet.getDate("date_received").toLocalDate();
                System.out.printf("\t\t\t│ %-5S │ %-32s │ %-18s │ %-18s │%-16s │ \n", ID,precint_name+"- "+city_name+"- "+province_name, household_id, amount_received,dateFormat.format(date_received));
                System.out.println("\t\t\t│_______│__________________________________│____________________│____________________│_________________│");
            }
            System.out.println();
            System.out.println("\t\t\t******************************************** LIST END ********************************************************");
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
            JDBCQuery1.openConnection();
            String sql = "select  * from Distribution";
            ResultSet rs = JDBCQuery1.executeSelectQuery(sql);
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
            JDBCQuery1.closeConnection();

        }
        return infoList;
    }

    //Lay tat ca thong tin Distribution trong database co tham so
    public ArrayList<Distribution> getDistribution(String sql) {
        ArrayList<Distribution> infoList = new ArrayList<>();
        try {
            JDBCQuery1.openConnection();
            ResultSet rs = JDBCQuery1.executeSelectQuery(sql);
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
            JDBCQuery1.closeConnection();

        }
        return infoList;
    }
    //Enter data
    public Distribution inputDistribution() {
        Integer comid = InputValidator1.validateIntInput("\t\t\tEnter New Commission ID:  ");
        Integer houseid= InputValidator1.validateIntInput("\t\t\tEnter New HouseHold  ID:  ");
        Double amount= InputValidator1.validateDoubleInput("\t\t\tEnter New Amount Received:  ");
        java.util.Date datereceived= InputValidator1.validateDateInput("\t\t\tEnter New Date Received with format dd/MM/yyyy:  ");
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
            Distribution di = inputDistribution();
            JDBCQuery1.openConnection();
            String sql = "insert into Distribution (commission_id, household_id, amount_received, date_received) values (?,?,?,?)";

            Object[] prams = {di.getCommissionId(), di.getHouseholdID(),di.getAmountReceived(),di.getDateReceived()};
            int rs = JDBCQuery1.executeUpdateQuery(sql, prams);
            if (rs > 0) {
                System.out.println("\t\t\tAdd success!!");
            } else {
                System.out.println("\t\t\tAdd failed!!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            JDBCQuery1.closeConnection();
        }

    }
    //hàm cập nhật lại thông tin
    public void updateDistribution() {
        int flag=0;
        do {
            int id;
            id = InputValidator1.validateIntInput("\t\t\tEnter ID to update: ");

            if (isIdExists(id)) {
                displayDistribution(getDistribution("select * from Distribution where id=" + id));


                Integer comid = InputValidator1.validateIntInput("\t\t\tEnter New Commission ID:  ");
                Integer houseid= InputValidator1.validateIntInput("\t\t\tEnter New HouseHold  ID:  ");
                Double amount= InputValidator1.validateDoubleInput("\t\t\tEnter New Amount Received:  ");
                java.util.Date datereceived= InputValidator1.validateDateInput("\t\t\tEnter New Date Received with format dd/MM/yyyy:  ");


                try {

                    JDBCQuery1.openConnection();

                    String sql = "UPDATE Distribution SET commission_id = ?, household_id = ?,amount_received = ?,date_received = ? WHERE id =?;";
                    Object[] prams = {comid, houseid, amount, datereceived,id};

                    int rowsAffected = JDBCQuery1.executeUpdateQuery(sql, prams);

                    if (rowsAffected > 0) {
                        System.out.println("\t\t\tUpdate success!!");
                        flag=1;
                    } else {
                        System.out.println("\t\t\tUpdate failed. No Distribution found with the specified ID.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    JDBCQuery1.closeConnection();
                }

            } else {
                System.out.println("\t\t\t ID does not exist. Please enter ID again!");

            }

        }while (flag==0);


    }

    //hàm xóa phân phối theo id
    public void deleteDistribution() {
        int flag=0;
        do {
            int id;
            id = InputValidator1.validateIntInput("\t\t\tEnter ID to delete: ");
            if (isIdExists(id)) {
                try {
                    JDBCQuery1.openConnection();

                    String sql = "DELETE FROM Distribution WHERE id = ?";
                    Object[] params = {id};
                    int rowsAffected = JDBCQuery1.executeUpdateQuery(sql, params);

                    if (rowsAffected > 0) {
                        System.out.println("\t\t\tDelete success!!");
                    } else {
                        System.out.println("\t\t\tDelete failed. No Distribution found with the specified ID.");
                    }
                    flag=1;
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    JDBCQuery1.closeConnection();
                }

            } else {
                System.out.println("\t\t\tID does not exist. Please enter ID again!");
            }
        }while(flag==0);



    }



    //hiển thị danh sách Phân phối
    public void displayDistribution(ArrayList<Distribution> distributions) {
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
            JDBCQuery1.openConnection();

            String sql = "SELECT COUNT(*) FROM Distribution WHERE id = ?";
            Object[] params = {id};

            ResultSet rs = JDBCQuery1.executeSelectQuery(sql, params);

            if (rs.next()) {
                int count = rs.getInt(1);
                exists = (count > 0);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            JDBCQuery1.closeConnection();
        }
        return exists;
    }



}


