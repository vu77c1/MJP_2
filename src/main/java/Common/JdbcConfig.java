package Common;

import java.sql.Connection;

public class JdbcConfig {
    public static Connection connection;
    public static String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String JDBC_URL = "jdbc:sqlserver://localhost:1433;database=MiniProject;TrustServerCertificate=true;";
    public static String USERNAME = "sa";
    public static String PASSWORD = "123456789";

}
