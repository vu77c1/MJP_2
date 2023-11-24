package Common;

import java.sql.Connection;

public class JdbcConfig {
    public static Connection connection;
    public static String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String JDBC_URL = "jdbc:sqlserver://localhost:1433;database=IJP;TrustServerCertificate=true;";
    public static String USERNAME = "STM";
    public static String PASSWORD = "C0991ryk";

}
