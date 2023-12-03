package Model;

import Common.DBConnect;
import Common.InputValidatorKhue;
import Common.JDBCQuery1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ListOfHouseHolds {
    private Connection connection;

    public ListOfHouseHolds(Connection connection) {
        this.connection = connection;
    }

    public ListOfHouseHolds() {
        this.connection = DBConnect.connectDatabase();
    }

    public void numDonateAndSumAmoutReceived(Scanner scanner) throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st =connection.createStatement();
            int id = InputValidatorKhue.validateIntInput("Input ID");
            rs = st.executeQuery("SELECT dbo.Citizen.id, name, is_household_lord, house_id, amount_distribution From \n" +
                    "House Left Join dbo.Distribution DB on House.id = DB.household_id\n" +
                    "\t  Right Join dbo.Citizen on House.id = Citizen.house_id Where Citizen.id =" + id);
            if(rs!=null){
                int sum = 0, num = 0;
                while (rs.next())
                {   num +=1;
                    int amountReceived = rs.getInt("amount_distribution");
                    sum += amountReceived;
                };
                System.out.println("Number donated: " + num);
                System.out.println("Sum money donated "+ id +":" + sum);
            }else {
                System.out.println("Not Donate");
            }

            InputValidatorKhue.waitForEnter();
        }catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println("Error in the database connection process");
        }finally {
            if(rs!=null){
                rs.close();
            }
            if(st!=null){
                st.close();
            }
        }
    }


}