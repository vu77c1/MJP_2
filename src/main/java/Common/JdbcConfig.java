package Common;

import java.sql.Connection;

public class JdbcConfig {
    public static Connection connection;
    public static String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    public static String JDBC_URL = "jdbc:sqlserver://DESKTOP-TESBAVI:1433;databaseName=Datamini_v01";
    public static String USERNAME = "sa";
    public static String PASSWORD = "123456";

}
