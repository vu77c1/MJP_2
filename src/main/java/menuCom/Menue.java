package menuCom;



import java.util.ArrayList;
import java.util.DuplicateFormatFlagsException;
import java.util.List;
import java.util.Scanner;

import Model.Commission;
import dao.CommissionDao;



public class Menue {
		 Scanner scanner = new Scanner(System.in);

		 public void checkString(String s) {
				  int numberLeng;
				  do {
						   s = s.trim();
						   numberLeng = s.length();
						   if (numberLeng >= 255) {
								    System.err.println("The name is too long\n ");
								    System.out.println("Input again: ");
								    s = scanner.nextLine();
						   }

				  } while (numberLeng >= 255);
		 }

		 public static String input_int() {
				  String string;
				  do {
						   System.out.println("Input nember:");
						   Scanner sc = new Scanner(System.in);
						   string = sc.nextLine();
						   // kiem tra co phai la chuoi khong
						   for (int i = 0; i < string.length(); i++) {

								    if (Character.isLetter(string.charAt(i))) {
											System.err.println(" err  : " + string + "  \n=> Input again:");
											break;
								    } else if (i + 1 == string.length()) {
											// sc.close();
											return string;
								    }
						   }
				  } while (true);
		 }

//		 kiem tra su ton tai cua ID
		 public int checkExistCommissionID(String str) {
				  String str2 = "UB";
				  String str1 = str.substring(0, 2);
				  if (!str2.equals(str1)) {
						   System.out.println("Commission no exist");
						   return 0;
				  }
				  // cat chuoi tu vi tri thu 3
				  String id = str.substring(str.indexOf("_") + 1);
				  System.out.println("wait...");
				  Integer checkId = 0;
				  List<Commission> CommissionDelete = new ArrayList<>();
				  CommissionDelete = CommissionDao.getInstant().selectAll();
				  // kiem tra ton tai
				  for (Commission com : CommissionDelete) {
						   if (String.valueOf(com.getId()).equalsIgnoreCase(id)) {
								    checkId = 1;
								    break;
						   }
				  }
				  return checkId;
		 }

//		Kiem tra su ton tai cua doi tuong Commission
		 public int checkExistCommission(Commission c) {
				  int check = 0;
				  List<Commission> result = new ArrayList<>();
				  result = CommissionDao.getInstant().selectAll();
				  // kiem tra du lieu nhap co ton tai trong data khong
				  for (Commission commission : result) {
						   if (c.getPrecintName().equalsIgnoreCase(commission.getPrecintName()) == true
						                         && c.getCityName().equalsIgnoreCase(
						                                               commission.getCityName()) == true
						                         && c.getProvinceName().equalsIgnoreCase(
						                                               commission.getProvinceName()) == true &&
						                                               c.getOfficerId()==(commission.getOfficerId())) {
								    check = 1;
								    break;
						   }
				  }
				  return check;
		 }

		 public void menuUpdate(int id) {
				  System.out.println("1: Update commission name");
				  System.out.println("2: Update city name");
				  System.out.println("3: Update province name");
				  System.out.println("4: Update all commission");
				  System.out.println("0: Exit");
				  int n = Integer.parseInt(String.valueOf(input_int()));
				  switch (n) {
				  case 0:
						   
						   break;
				  case 1:
						   
						   System.out.println("Update Success");
						   break;
				  case 2:

						   System.out.println("Update Success");
						   break;
				  case 3:

						   System.out.println("Update Success");
						   break;
				  case 4:
						   
						   System.out.println("Input precint name: ");
						   String precint_name = scanner.nextLine();
						   checkString(precint_name);// ham checkString() kiem tra chuoi ok chua
						   System.out.println("Input city name: ");
						   String city_name = scanner.nextLine();
						   checkString(city_name);
						   System.out.println("Input province name: ");
						   String province_name = scanner.nextLine();
						   checkString(province_name);
						   System.out.println("Input officer id: ");
						   String officerId = scanner.nextLine();
						   String id1 = officerId.substring(officerId.indexOf("_") + 1);
						   Integer officer_ID=Integer.valueOf(id1);
						   System.out.println("Please...");
						   // tao doi tuong moi tu ban phim
						   Commission commissionNew = new Commission(id, precint_name, city_name, province_name,officer_ID);
						   
						   CommissionDao.getInstant().update(commissionNew);
						   System.out.println("Update Success");
						   break;
				  }

		 }

		 public void subMenu() {
				  System.out.println("1: Get all commission");
				  System.out.println("2: Insert a new commission");
				  System.out.println("3: Delete a commission");
				  System.out.println("4: Update a commission");
				  System.out.println("0: Exit");
				  int n = Integer.parseInt(String.valueOf(input_int()));
				  switch (n) {
				  case 0:

						   break;
				  case 1:
						   System.out.println("Connecting...");
						   CommissionDao commissionDao = new CommissionDao();
						   commissionDao.println(commissionDao.selectAll());
						   break;
				  case 2:

						   Integer check = 0;
						   System.out.println("Input precint name: ");
						   String precint_name = scanner.nextLine();
						   checkString(precint_name);// ham checkString() kiem tra chuoi ok chua
						   System.out.println("Input city name: ");
						   String city_name = scanner.nextLine();
						   checkString(city_name);
						   System.out.println("Input province name: ");
						   String province_name = scanner.nextLine();
						   checkString(province_name);
						   System.out.println("Input officer id: ");
						   String officerId = scanner.nextLine();
						   String id = officerId.substring(officerId.indexOf("_") + 1);
						   Integer officer_ID=Integer.valueOf(id);
						   System.out.println("Please...");
						   // tao doi tuong moi tu ban phim
						   Commission commissionNew = new Commission(precint_name, city_name, province_name,officer_ID);
						   Commission commissionCheck = new Commission(precint_name, city_name, province_name,officer_ID);
						   check = checkExistCommission(commissionCheck);// ham checkExistCommission() kiem tra
						                                                 // ton tai cua doi tuoi commission
						   if (check == 1) {
								    System.out.println("Exist data!!");
						   } else {
								    CommissionDao.getInstant().insert(commissionNew);
								    CommissionDao commissionInsert = new CommissionDao();
								    commissionInsert.println(commissionInsert.selectAll());
						   }
						   break;
				  case 3:
						   System.out.println("Input id: ");
						   String idInputString = scanner.nextLine();
//						   Tao chuoi moi o vi tri: "_" + 1
						   String id2 = idInputString.substring(idInputString.indexOf("_") + 1);
						   int checkId = checkExistCommissionID(idInputString);
						   if (checkId == 1) {
								    CommissionDao.getInstant().delete(id2);
								    System.out.println("Delete Success");
						   } else {
								    System.out.println("Commission no exist");
						   }
						   break;
				  case 4:
						   System.out.println("Input id: ");
						   String idUpdate = scanner.nextLine();
//						   Tao chuoi moi o vi tri: "_" + 1
						   String idUp = idUpdate.substring(idUpdate.indexOf("_") + 1);
						   int checkIdUP = checkExistCommissionID(idUpdate);
						   if (checkIdUP == 1) {
								    menuUpdate(Integer.parseInt(idUp));
								    
						   } 
						   break;

				  }

		 }
}