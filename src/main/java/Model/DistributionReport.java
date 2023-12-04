package Model;

import Common.InputValidator;
import Common.JDBCQuery;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

public class DistributionReport {
    public ResultSet getListDistributionReport(int month, int year) {
        ResultSet rs = null;
        try {
            JDBCQuery.openConnection();
            String sql = """
                    WITH B AS (
                                            SELECT commission_id, SUM(amount_distribution) as total_amount_received  FROM Distribution st
                                            WHERE st.household_id is not null AND MONTH(st.date_distribution)=? AND YEAR(st.date_distribution)=?
                                            GROUP BY commission_id
                                                          
                                        ),
                                         A AS(
                                            SELECT commission_id,  SUM(amount) as total_amount  FROM DonateDetail dn LEFT JOIN Commission c on dn.commission_id=c.id GROUP BY commission_id
                                        )
                                        SELECT
                                            A.commission_id,
                                            C.precint_name,
                                            C.city_name,
                                            C.province_name,
                                            A.total_amount,
                                            ISNULL(B.total_amount_received, 0) AS total_amount_received,
                                            A.total_amount - ISNULL(B.total_amount_received, 0) AS remaining_amount
                                        FROM
                                            A
                                        LEFT JOIN
                                            B ON A.commission_id = B.commission_id
                                        LEFT JOIN Commission C ON C.id=A.commission_id                            """;
            Object[] params = {month, year};
            rs = JDBCQuery.executeSelectQuery(sql, params);


        } catch (Exception exception) {
            exception.printStackTrace();

        }
        return rs;
    }

    public void printDistributionReport() {
        System.out.println("To display detailed information about the remaining amount (not yet distributed) for a specific donation campaign");
        try {

            int month = -1;
            do {
                try {
                    month  = InputValidator.validateIntInput("Enter Month (1-12): ");
                }
                catch(Exception e)
                {
                    System.out.print("Error");
                }

            }
            while (!(month>=1&&month<=12));
            Boolean check =false;
            int year;
            do {
                year=  InputValidator.validateIntInput("Enter Year: ");
                if (year>0&&Integer.toString(year).length()==4)
                {
                    check=true;
                }
                else {
                    System.out.println("Error year not <=0 and length=4 char....");
                    check=false;
                }


            }
            while (!check);
            ResultSet rs = getListDistributionReport(month, year);
            // Check if the ResultSet is not null
            if (rs != null) {
                // Get metadata to retrieve column names
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Print column headers
                for (int i = 2; i <= columnCount; i++) {
                    System.out.printf("\u001B[1m%-25s\u001B[0m", metaData.getColumnLabel(i).toUpperCase());
                }
                System.out.println();

                // Print data
                while (rs.next()) {
                    // Check if the object_type contains the specified substring
                    for (int i = 2; i <= columnCount; i++) {
                        // Check if the column is a floating-point number and format accordingly
                        if (rs.getMetaData().getColumnType(i) == java.sql.Types.FLOAT ||
                                rs.getMetaData().getColumnType(i) == java.sql.Types.DOUBLE) {
                            System.out.printf("%-25s", formatFloatingPoint(rs.getDouble(i)));
                        } else {
                            System.out.printf("%-25s", rs.getString(i));
                        }

                    }
                    System.out.println();
                }

            } else {
                System.out.print("Data Empty");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String formatFloatingPoint(double value) {
        // Format the floating-point number with desired precision
        return String.format("%.2f", value);
    }

    public void printResultSet(ResultSet rs) {
        try {
            // Check if the ResultSet is not null
            if (rs != null) {
                // Get metadata to retrieve column names
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();

                // Print column headers
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("\u001B[1m%-25s\u001B[0m", metaData.getColumnLabel(i).toUpperCase());
                }
                System.out.println();

                // Print data
                while (rs.next()) {
                    for (int i = 1; i <= columnCount; i++) {
                        System.out.printf("%-25s", rs.getString(i));

                    }
                    System.out.println();
                }
            } else {
                System.out.println("Data Empty");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
