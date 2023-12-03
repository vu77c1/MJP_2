package Model;

import Common.JDBCQuery1;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

import static Model.Processing.waitForEnter;


public class DonateManage1 {
    public static DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("uuuu/MM/dd").withResolverStyle(ResolverStyle.STRICT);
    public static SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
    private static Scanner sc = new Scanner(System.in);

    public void topValue(Connection con) {
        try {
            System.out.println("\t\t\t********************* TOP 3 VALUE DONATE ***********************");
            System.out.println("\t\t\t._______.______________________________________._________________");
            System.out.println("\t\t\t│   ID  │        Company Name          │      Total Amount      │ ");
            System.out.println("\t\t\t│_______│______________________________│________________________│ ");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("select Company.id, Company.company_name, CompanyA.total from \n" +
                    "\t\t(select TOP 3 Company.id as id, SUM(DonateDetail.amount) as total from Company \n" +
                    "\t\tinner join Representative on Representative.company_id = Company.id \n" +
                    "\t\tinner join DonateDetail on DonateDetail.representative_id = Representative.id\n" +
                    "\t\tgroup by Company.id\n" +
                    "\t\tORDER BY SUM(DonateDetail.amount) DESC) CompanyA\n" +
                    "inner join Company on CompanyA.id = Company.id");
            while (resultSet.next()) {
                String ID = resultSet.getString("id");
                String company_name = resultSet.getString("company_name");
                //Float amount = resultSet.getFloat("total");


                System.out.printf("\t\t\t│ %-5S │ %-28s │ %-22s │\n", ID, company_name, formatFloatingPoint(resultSet.getFloat("total")));
                System.out.println("\t\t\t│_______│______________________________│________________________│");
            }
            System.out.println();
            System.out.println("\t\t\t*************************** LIST END *****************************");
            //waitForEnter();
        } catch (SQLException e) {

            System.out.println("\u001B[31mThere was an error during the Database connection process\u001B[0m");
            System.out.println(e.getMessage());
        }
    }

    private static String formatFloatingPoint(float value) {
        // Format the floating-point number with desired precision
        return String.format("%.2f", value);
    }

    public void showList(Connection con) {
        Date dateTime = null;
        String startdate;
        String enddate;
        formatter.setLenient(false);
        do {
            System.out.println("Enter day start (yyyy/MM/dd): ");
            startdate = sc.nextLine();
            try {

                if (!startdate.equals(formatter)) {
                    dateTime = (Date) formatter.parse(startdate);
                }

            } catch (ParseException e) {
                System.out.println("\u001B[31mInvalid expiration format.\u001B[0m");

            }
        } while (dateTime == null);

        boolean isValid = false;
        do {
            do {
                System.out.println("Enter the end date (yyyy/MM/dd): ");
                enddate = sc.nextLine();
                try {
                    if (!startdate.equals(formatter)) {
                        dateTime = (Date) formatter.parse(enddate);
                    }
                    isValid = checkDateInput(startdate, enddate);
                    if (isValid == false) {
                        System.out.println("\u001B[31mEnd date must be after start date\u001B[0m");
                    }
                } catch (ParseException e) {
                    System.out.println("\u001B[31mInvalid expiration format.\u001B[0m");
                }
            } while (dateTime == null);
        } while (isValid == false);

        try {
            System.out.println("\t\t\t********************* List of individuals donating ***********************");
            System.out.println("\t\t\t._______.______________________________________._________________");
            System.out.println("\t\t\t│   ID  │     Representative Name      │      Total Amount      │ ");
            System.out.println("\t\t\t│_______│______________________________│________________________│ ");
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery("\t\tselect DISTINCT Representative.id, Representative.representative_name, re.total from \n" +
                    "\t\t(select Representative.id as id, SUM(DonateDetail.amount) as total from Representative \n" +
                    "\t\tjoin DonateDetail on DonateDetail.representative_id = Representative.id \n" +
                    "\t\tgroup by Representative.id) re \n" +
                    "\t\tinner join Representative on re.id = Representative.id\n" +
                    "\t\tleft join DonateDetail d on d.representative_id = Representative.id\n" +
                    "\t\tWHERE d.donate_date BETWEEN '" + startdate + "' AND '" + enddate + "' and Representative.company_id is null\n" +
                    "\t\tORDER BY total DESC");
            while (resultSet.next()) {
                String ID = resultSet.getString("id");
                String representative_name = resultSet.getString("representative_name");
                // Float amount = resultSet.getFloat("total");

                System.out.printf("\t\t\t│ %-5S │ %-28s │ %-22s │\n", ID, representative_name, formatFloatingPoint(resultSet.getFloat("total")));
                System.out.println("\t\t\t│_______│______________________________│________________________│");
            }
            System.out.println();
            System.out.println("\t\t\t************************ LIST END *************************************");
            //waitForEnter();
        } catch (SQLException e) {

            System.out.println("\u001B[31mThere was an error during the Database connection process\u001B[0m");
            System.out.println(e.getMessage());
        }


    }

    private static boolean checkDateInput(String startDate, String endDate) {
        boolean isValid = false;
        try {
            Date start = formatter.parse(startDate);
            Date end = formatter.parse(endDate);
            if (start.before(end)) {
                isValid = true;
            }
        } catch (ParseException e) {
            System.out.println(e.toString());
        }
        return isValid;
    }
}