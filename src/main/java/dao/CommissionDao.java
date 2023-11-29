package dao;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import Common.JdbcUtil;
import Model.Commission;
import Model.Officer;



public class CommissionDao {
		 public static CommissionDao getInstant() {
				  return new CommissionDao();
		 }

		 // in doi tuong commission
		 public void printlnCommissions(List<Commission> commissions) {
					 System.out.println(String.format(" %-25s  %-25s  %-25s  %-25s", "ID", "precint_name", "city_name ", "province_name" ));
				  Stack<Commission> comStack = new Stack<>();
				  for (Commission com : commissions) {
						   comStack.add(com);
				  }
				  while (!comStack.isEmpty()) {
						   System.out.println(comStack.pop());
				  }
		 }
		 // in doi tuong officer
		 public void printlnOffice(List<Officer> officers) {
				  Stack<Officer> comStack = new Stack<>();
				  for (Officer com : officers) {
						   comStack.add(com);
				  }
				  while (!comStack.isEmpty()) {
						   System.out.println(comStack.pop());
				  }
		 }
//		 lay du lieu Officer
		 public List<Officer> selectAllOfficer() {
				  List<Officer> result = new ArrayList<>();
				  try {
						   // b1: Tao ket noi
						   Connection connection = JdbcUtil.getConnection();
						   // b2: tao doi tuong statement
						   Statement statement = connection.createStatement();
						   // b3: thuc hien cau lenh sql
						   String sqlString = "SELECT * FROM Officer";
//						   System.out.println("Ban da thuc thi: " + sqlString);
						   ResultSet rs = statement.executeQuery(sqlString);
						   // lay tung thuoc tinh cua doi tuong
						   while (rs.next()) {
								    int id = rs.getInt("id");
								    String precint_name = rs.getString("name");
								    String city_name = rs.getString("phone_number");
								    String province_name = rs.getString("address");
								    // tao doi tuong tu cac thuoc tinh o tren
								    Officer officer = new Officer(id, precint_name, city_name,
								                          province_name);
								    result.add(officer);
						   }
				  } catch (SQLException e) {
						   // TODO Auto-generated catch block
						   e.printStackTrace();
				  }
				  return result;
		 }
		 
		 public List<String> fineHouseHoleNotReceived() {
				  List<String> result = new ArrayList<>();
				  try {
						   // b1: Tao ket noi
						   Connection connection = JdbcUtil.getConnection();
						   // b2: tao doi tuong statement
						   Statement statement = connection.createStatement();
						   // b3: thuc hien cau lenh sql
						   String sqlString = " select CONCAT (name,' ', phone_number, ' Dia chi ', address) as Ho_Dan from Citizen\r\n"
						   				  + " left join House on Citizen.house_id=House.id\r\n"
						   				  + "left join Distribution on House.id=Distribution.household_id\r\n"
						   				  + "where Citizen.is_household_lord= 'true' and House.id not IN (select household_id from Distribution )";   
//						   System.out.println("Ban da thuc thi: " + sqlString);
						   ResultSet rs = statement.executeQuery(sqlString);
//					         // lay tung thuoc tinh cua doi tuong
						   while (rs.next()) {
								    String precint_name = rs.getString("Ho_Dan");
								    System.out.println("\n"+precint_name);
//								   
						   }
				  } catch (SQLException e) {
						   // TODO Auto-generated catch block
						   e.printStackTrace();
				  }
				  return result;
		 }
		 
		 // Lay du lieu tu database
		 public List<Commission> selectAllCommissions() {
				  List<Commission> result = new ArrayList<>();
				  try {
						   // b1: Tao ket noi
						   Connection connection = JdbcUtil.getConnection();
						   // b2: tao doi tuong statement
						   Statement statement = connection.createStatement();
						   // b3: thuc hien cau lenh sql
						   String sqlString = "  select * from Commission left join Officer on Commission.id= Officer.commision_id";   
//						   System.out.println("Ban da thuc thi: " + sqlString);
						   ResultSet rs = statement.executeQuery(sqlString);
//					         // lay tung thuoc tinh cua doi tuong
						   while (rs.next()) {
								    int id = rs.getInt("id");
								    String precint_name = rs.getString("precint_name");
								    String city_name = rs.getString("city_name");
								    String province_name = rs.getString("province_name");
								    // tao doi tuong tu cac thuoc tinh o tren
								    Commission commission = new Commission(id, precint_name, city_name,
								                          province_name);
								    result.add(commission);
						   }
				  } catch (SQLException e) {
						   // TODO Auto-generated catch block
						   e.printStackTrace();
				  }
				  return result;
		 }

		 // them giu lieu vao database
		 public int insert(Commission com) {
				  int rs = 0;
				  try {
						   // b1: Tao ket noi
						   Connection connection = JdbcUtil.getConnection();
						   // b2: tao doi tuong statement
						   Statement statement = connection.createStatement();
						   // b3: thuc hien cau lenh sql
						   String sqlString = "INSERT INTO commission (precint_name, city_name, province_name) VALUES ('"
						                         + com.getPrecintName() + "', '" + com.getCityName() + "', '"
						                         + com.getProvinceName() +"');";
						   System.out.println("Ban da thuc thi: " + sqlString);
//						   System.out.println("======================\n");
						   rs = statement.executeUpdate(sqlString);
						   System.out.println("Sussese!");
				  } catch (SQLException e) {
						   e.printStackTrace();
				  }
				  return rs;
		 }

		 // xoa data theo id
		 public int delete(String c) {
				  int id = Integer.parseInt(c);
				  int result = 0;
				  try {
						   // b1: Tao ket noi
						   Connection connection = JdbcUtil.getConnection();
						   // b2: tao doi tuong statement
						   Statement statement = connection.createStatement();
						   // b3: thuc hien cau lenh sql
						   String sqlString = "delete from Commission where id=" + c;
//						   System.out.println("Ban da thuc thi: " + sqlString);
						   result = statement.executeUpdate(sqlString);
				  } catch (SQLException e) {
						   e.printStackTrace();
				  }
				  return result;
		 }

		 // Update theo id
		 public int update(Commission com) {
				  int result = 0;
				  try {
						   // b1: Tao ket noi
						   Connection connection = JdbcUtil.getConnection();
						   // b2: tao doi tuong statement
						   Statement statement = connection.createStatement();
						   // b3: thuc hien cau lenh sql precint_name, city_name, province_name
						   String sqlString = "update  commission set " + "precint_name = '" + com.getPrecintName()
						                         + "'," + "city_name = '" + com.getCityName() + "',"
						                         + "province_name = '" + com.getProvinceName() 
						                         +"' where id="+com.getId();
//						   System.out.println("Ban da thuc thi: " + sqlString);
						   result = statement.executeUpdate(sqlString);
				  } catch (SQLException e) {
						   e.printStackTrace();
				  }
				  return result;
		 }
		 
		 
}
