package Model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Date;
import java.util.Scanner;

import static Model.Processing1.waitForEnter;


public class DonateManage1 {
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu/MM/dd").withResolverStyle(ResolverStyle.STRICT);
    private static Scanner sc= new Scanner(System.in);
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");


    public void TopVlaue(Connection con){
        try {
            System.out.println("\t\t\t********************************************* TOP 3 VALUE DONATE *****************************************");
            System.out.println("\t\t\t._______.______________________________________.____________________._________________._________________.__");
            System.out.println("\t\t\t│   ID  │            Amount            │      Donate Date       │ Representative Name │  Company Name  │ ");
            System.out.println("\t\t\t│_______│______________________________│________________________│_____________________│________________│  ");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("Select top 3 dbo.DonateDetail.id, amount, donate_date, representative_name, company_name from DonateDetail join Representative R on DonateDetail.representative_id=R.id join Company C on R.company_id= C.id order by  amount Desc ");
            while (resultSet.next())
            {
                String ID = resultSet.getString("id");
                Float amount = resultSet.getFloat("amount");
                LocalDate donate_date = resultSet.getDate("donate_date").toLocalDate();
                String representative_name = resultSet.getString("representative_name");
                String company_name = resultSet.getString("company_name");



                System.out.printf("\t\t\t│ %-5S │ %-28s │ %-22s │ %-19s │ %-14s │\n", ID,amount,dateFormat.format(donate_date),representative_name, company_name);
                System.out.println("\t\t\t│_______│______________________________│________________________│_____________________│________________│");
            }
            System.out.println();
            System.out.println("\t\t\t******************************************** LIST END ********************************************************");
            waitForEnter();
        }catch (SQLException e) {

            System.out.println("\u001B[31mThere was an error during the Database connection process\u001B[0m");
            System.out.println(e.getMessage());
        }
    }



    public void printDistribution(Connection con){
        Date dateTime = null;
        String startday;
        String endday;
            do {
                System.out.println("Enter day start (yyyy/MM/dd): ");
                startday = sc.nextLine();
                try {
                    dateTime = (Date) formatter.parse(startday);

                } catch (ParseException e) {
                    System.out.println("Invalid expiration format.");
                }
            }while (dateTime==null);
              do {
              System.out.println("Enter the end date (yyyy/MM/dd): ");
              endday = sc.nextLine();
              try {
                  dateTime = (Date) formatter.parse(endday);

              } catch (ParseException e) {
                  System.out.println("Invalid expiration format.");
              }


            }while (dateTime==null);


        try {
            System.out.println("\t\t\t********************************************* TOP 3 VALUE DONATE *****************************************");
            System.out.println("\t\t\t._______.______________________________________.____________________._________________._________________.__");
            System.out.println("\t\t\t│   ID  │            Amount            │      Donate Date       │ Representative Name │  Company Name  │ ");
            System.out.println("\t\t\t│_______│______________________________│________________________│_____________________│________________│  ");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("Select dbo.DonateDetail.id,amount,donate_date,commission_id, representative_id, representative_name, company_name from DonateDetail left join Representative R on DonateDetail.representative_id=R.id left  join Company C on R.company_id= C.id WHERE DonateDetail.donate_date BETWEEN  '"+startday+"' AND '"+endday+"' and R.company_id is null ");
            while (resultSet.next())
            {
                String ID = resultSet.getString("id");
                Float amount = resultSet.getFloat("amount");
                LocalDate donate_date = resultSet.getDate("donate_date").toLocalDate();
                String representative_name = resultSet.getString("representative_name");
                String company_name = resultSet.getString("company_name");



                System.out.printf("\t\t\t│ %-5S │ %-28s │ %-22s │ %-19s │ %-14s │\n", ID,amount,dateFormat.format(donate_date),representative_name, company_name);
                System.out.println("\t\t\t│_______│______________________________│________________________│_____________________│________________│");
            }
            System.out.println();
            System.out.println("\t\t\t******************************************** LIST END ********************************************************");
            waitForEnter();
        }catch (SQLException e) {

            System.out.println("\u001B[31mThere was an error during the Database connection process\u001B[0m");
            System.out.println(e.getMessage());
        }
    }

}
