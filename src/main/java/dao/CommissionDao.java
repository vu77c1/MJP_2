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
		 
		 // Lay du lieu tu database
		 public List<Commission> selectAllCommissions() {
				  List<Commission> result = new ArrayList<>();
				  try {
						   // b1: Tao ket noi
						   Connection connection = JdbcUtil.getConnection();
						   // b2: tao doi tuong statement
						   Statement statement = connection.createStatement();
						   // b3: thuc hien cau lenh sql
						   String sqlString = "  select * from Commission left join Officer on Commission.officer_id= Officer.id";   
//						   System.out.println("Ban da thuc thi: " + sqlString);
						   ResultSet rs = statement.executeQuery(sqlString);
						   // lay tung thuoc tinh cua doi tuong
						   while (rs.next()) {
								    int id = rs.getInt("id");
								    System.out.print(id+"\t");
								    String precint_name = rs.getString("precint_name");
								    System.out.print(precint_name+"\t");
								    String city_name = rs.getString("city_name");
								    System.out.print(city_name+"\t\t");
								    String province_name = rs.getString("province_name");
								    System.out.print(province_name+"\t");
								    int officer_id = rs.getInt("officer_id");
								    System.out.print(officer_id+"\t\t");
								    String name_officer = rs.getString("name");
								    System.out.print(name_officer+"\t");
								    System.out.println();
								    // tao doi tuong tu cac thuoc tinh o tren
//								    Commission commission = new Commission(id, precint_name, city_name,
//								                          province_name, officer_id);
//								    result.add(commission);
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
						   String sqlString = "INSERT INTO commission (precint_name, city_name, province_name,officer_id) VALUES ('"
						                         + com.getPrecintName() + "', '" + com.getCityName() + "', '"
						                         + com.getProvinceName() +"',"+ com.getOfficerId()+");";
//						   System.out.println("Ban da thuc thi: " + sqlString);
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
						                         + "province_name = '" + com.getProvinceName() + "',"
						                         + "officer_id =" + com.getOfficerId()+" where id="+com.getId();
//						   System.out.println("Ban da thuc thi: " + sqlString);
						   result = statement.executeUpdate(sqlString);
				  } catch (SQLException e) {
						   e.printStackTrace();
				  }
				  return result;
		 }
		 
		 
}
