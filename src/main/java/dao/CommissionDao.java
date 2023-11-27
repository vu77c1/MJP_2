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



public class CommissionDao {
		 public static CommissionDao getInstant() {
				  return new CommissionDao();
		 }

		 // in doi tuong commission
		 public void println(List<Commission> commissions) {
				  Stack<Commission> comStack = new Stack<>();
				  for (Commission com : commissions) {
						   comStack.add(com);
				  }
				  while (!comStack.isEmpty()) {
						   System.out.println(comStack.pop());
				  }
		 }

		 // Lay du lieu tu database
		 public List<Commission> selectAll() {
				  List<Commission> result = new ArrayList<>();
				  try {
						   // b1: Tao ket noi
						   Connection connection = JdbcUtil.getConnection();
						   // b2: tao doi tuong statement
						   Statement statement = connection.createStatement();
						   // b3: thuc hien cau lenh sql
						   String sqlString = "SELECT * FROM commission";
//						   System.out.println("Ban da thuc thi: " + sqlString);
						   ResultSet rs = statement.executeQuery(sqlString);
						   // lay tung thuoc tinh cua doi tuong
						   while (rs.next()) {
								    int id = rs.getInt("id");
								    String precint_name = rs.getString("precint_name");
								    String city_name = rs.getString("city_name");
								    String province_name = rs.getString("province_name");
								    int officer_id = rs.getInt("officer_id");
								    // tao doi tuong tu cac thuoc tinh o tren
								    Commission commission = new Commission(id, precint_name, city_name,
								                          province_name, officer_id);
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
						   System.out.println("Ban da thuc thi: " + sqlString);
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
						   System.out.println("Ban da thuc thi: " + sqlString);
						   result = statement.executeUpdate(sqlString);
				  } catch (SQLException e) {
						   e.printStackTrace();
				  }
				  return result;
		 }
		 
		 
}
