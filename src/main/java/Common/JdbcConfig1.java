package Common;

import java.sql.Connection;

public class JdbcConfig1 {
    public static Connection connection;
    public static String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String JDBC_URL = "jdbc:sqlserver://DESKTOP-3UKTJM9\\SQLEXPRESS01:1433;database=miniproject_english;TrustServerCertificate=true;";
    public static String USERNAME = "sa";
    public static String PASSWORD = "123456";


}
