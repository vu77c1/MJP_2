package Common;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;





public class JdbcUtil {
		 private static Connection con = null;
		 static final String connectionUrl = "jdbc:sqlserver://TRAN-XUAN-PHUON\\SQLEXPRESS:1433 ;databaseName=miniproject_english;user=sa;password=123456789;encrypt=true; trustServerCertificate = true";
		 static {
				  
				  try {
						   con = DriverManager.getConnection(connectionUrl);
				  } catch (SQLException e) {
						   e.printStackTrace();
				  }
		 }
		 
public static Connection getConnection() {
				  
				  return con;
		 }
public static void closeConnection() throws SQLException {
		  con.close();
		  
}
		 
	  
}

