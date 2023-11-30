package Common;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;





public class JdbcUtil {
		 private static Connection con = null;
		 static final String connectionUrl = "jdbc:sqlserver://localhost:1433;database=SQLminiversion2;user=sa;password=123456;TrustServerCertificate=true;";
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

