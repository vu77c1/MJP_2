package Model;

import Common.DBConnect;
import Common.InputValidatorKhue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class ListOfStaticTwoPiority {
    private Connection connection;

    public ListOfStaticTwoPiority(Connection connection) {
        this.connection = connection;
    }

    public ListOfStaticTwoPiority() {
        this.connection = DBConnect.connectDatabase();
    }

    // Thống kê số hộ khẩu có từ 2 đối tượng ưu tiên trở lên
    public void statisticsOfTwoPriorityObjects() throws SQLException {
        Statement st = null;
        ResultSet rs = null;
        try {
            st =connection.createStatement();
            System.out.println();
            System.out.println("Household is donate two:");
            rs = st.executeQuery("""
                    SELECT  name, is_household_lord FROM (SELECT dbo.Citizen.name, is_household_lord FROM (SELECT Count(dbo.Citizen.id) AS total, house_id FROM\s
                    Citizen Join dbo.CitizenObject CO on Citizen.citizen_object_id = CO.id\s
                    		Join dbo.House H on Citizen.house_id = H.id
                    		Where citizen_object_id <>3 or DATEDIFF(YEAR, date_of_birth, GETDATE()) <= 8 or DATEDIFF(YEAR, date_of_birth, GETDATE()) >= 60
                    		group by house_id) AS A  inner join dbo.Citizen on dbo.Citizen.house_id = A.house_id where A.total >= 2) AS B Where is_household_lord =1""");
            while (rs.next())
            {
                String citizenName = rs.getString("name");
                System.out.println(citizenName);
            };
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
