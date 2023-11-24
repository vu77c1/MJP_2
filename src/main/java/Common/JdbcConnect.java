package Common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class JdbcConnect {
    private static Scanner scanner = new Scanner(System.in);
    public static Connection connectDatabase() {
        final String JDBC_DRIVER = JdbcConfig.JDBC_DRIVER;
        final String DB_URL = JdbcConfig.JDBC_URL;
        final String USERNAME = JdbcConfig.USERNAME;
        final String PASSWORD = JdbcConfig.PASSWORD;
        Connection con = null;
        // Load the JDBC driver
        try {
            Class.forName(JDBC_DRIVER);
            con = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            System.out.println("\u001B[32mDatabase Connected\u001B[0m");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("\u001B[31mCó lỗi trong quá trình kết nối Database: " + e.getMessage() + ".\u001B[0m");
        }
        return con;
    }
    public static Connection disconnectDatabase(Connection con) {
        if (con != null) {
            try {
                scanner.close();
                con.close();
                System.out.println("\u001B[32mDatabase connection closed.\u001B[0m");
            } catch (SQLException e) {
                System.err.println("\u001B[31mError closing the database connection: " + e.getMessage() + ".\u001B[0m");
                // Handle the exception as needed
            }
        }
        return con;
    }
}
