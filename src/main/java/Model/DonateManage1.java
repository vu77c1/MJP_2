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
    private static Scanner sc = new Scanner(System.in);
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");


    public void TopVlaue(Connection con) {
        try {
            System.out.println("\t\t\t********************* TOP 3 VALUE DONATE *************************");
            System.out.println("\t\t\t._______.______________________________________._________________");
            System.out.println("\t\t\t│   ID  │         Company Name         │      Total Amount      │  ");
            System.out.println("\t\t\t│_______│______________________________│________________________│  ");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select Company.id, Company.company_name, CompanyA.total from \n" +
                    "\t\t(select TOP 3 Company.id as id, SUM(DonateDetail.amount) as total from Company \n" +
                    "\t\tinner join Representative on Representative.company_id = Company.id \n" +
                    "\t\tinner join DonateDetail on DonateDetail.representative_id = Representative.id\n" +
                    "\t\tgroup by Company.id\n" +
                    "\t\tORDER BY SUM(DonateDetail.amount) DESC) CompanyA\n" +
                    "\t\tinner join Company on CompanyA.id = Company.id\n" +
                    "\t\t ");
            while (resultSet.next()) {
                String ID = resultSet.getString("id");
                String company_name = resultSet.getString("company_name");
                Float amount = resultSet.getFloat("total");
                System.out.printf("\t\t\t│ %-5S │ %-28s │ %-22s │ \n", ID, company_name, amount);
                System.out.println("\t\t\t│_______│______________________________│________________________││");
            }
            System.out.println();
            System.out.println("\t\t\t*************************** LIST END ***************************");
            waitForEnter();
        } catch (SQLException e) {

            System.out.println("\u001B[31mThere was an error during the Database connection process\u001B[0m");
            System.out.println(e.getMessage());
        }
    }


    public void showList(Connection con) {
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
        } while (dateTime == null);
        do {
            System.out.println("Enter the end date (yyyy/MM/dd): ");
            endday = sc.nextLine();
            try {
                dateTime = (Date) formatter.parse(endday);

            } catch (ParseException e) {
                System.out.println("Invalid expiration format.");
            }


        } while (dateTime == null);


        try {
            System.out.println("\t\t\t********************* List of individual supporters *************************");
            System.out.println("\t\t\t._______.______________________________________._________________");
            System.out.println("\t\t\t│   ID  │     Representative Name      │      Total Amount      │  ");
            System.out.println("\t\t\t│_______│______________________________│________________________│  ");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select Representative.id, Representative.representative_name, re.total from \n" +
                    "\t\t(select Representative.id as id, SUM(DonateDetail.amount) as total from Representative \n" +
                    "\t\tjoin DonateDetail on DonateDetail.representative_id = Representative.id \n" +
                    "\t\tgroup by Representative.id) re \n" +
                    "\t\tinner join Representative on re.id = Representative.id\n" +
                    "\t\tleft join DonateDetail d on d.representative_id = Representative.id\n" +
                    "\t\tWHERE d.donate_date BETWEEN '" + startday + "' AND '" + endday + "' and Representative.company_id is null\n" +
                    "\t\tORDER BY total DESC" +
                    "\t\t ");
            while (resultSet.next()) {
                String ID = resultSet.getString("id");
                String representative_name = resultSet.getString("representative_name");
                Float amount = resultSet.getFloat("total");
                System.out.printf("\t\t\t│ %-5S │ %-28s │ %-22s │ \n", ID, representative_name, amount);
                System.out.println("\t\t\t│_______│______________________________│________________________││");
            }
            System.out.println();
            System.out.println("\t\t\t*************************** LIST END ***************************");
            waitForEnter();
        } catch (SQLException e) {

            System.out.println("\u001B[31mThere was an error during the Database connection process\u001B[0m");
            System.out.println(e.getMessage());
        }

    }
}
