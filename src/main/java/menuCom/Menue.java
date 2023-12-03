package menuCom;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import Model.Commission;
import Model.Officer;
import dao.CommissionDao;



public class Menue {
    Scanner scanner = new Scanner(System.in);
    CommissionDao commissionDao = new CommissionDao();

    public static boolean checkSpecialCharacter(String str) {
        // Kiểm tra xem chuỗi có chứa ký tự đặc biệt hay không
        return str.matches("^[a-zA-Z0-9 ]{0,255}$");
    }

    public static void checkString(String s) {
        Scanner scanner = new Scanner(System.in);
        do {
            s = s.trim();
            if (!checkSpecialCharacter(s)) {
                System.out.println("Input again: ");
                s = scanner.nextLine();
            }
        } while (!checkSpecialCharacter(s));
    }

    //		 -------------------------------------------------------------------------------------------------
    public static boolean isPhase(String str) {
        // Kiểm tra xem chuỗi có chứa ký tự đặc biệt hay không
        return str.matches("([1-9]|[1][0-2])$");
    }

    public static String checkInputPhase(String s) {
        Scanner scanner = new Scanner(System.in);
        do {
            s = s.trim();
            if (!isPhase(s)) {
                System.out.println("Input again: ");
                s = scanner.nextLine();
            }
        } while (!isPhase(s));
        return s;
    }

    public static void inputPhase() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input phase: ");
        String phase = scanner.nextLine();
        phase = Menue.checkInputPhase(phase);
        CommissionDao.getInstant().findHouseHoleNotReceived(phase);
    }

    public static boolean isNumber(String str) {
        // Kiểm tra xem chuỗi có chứa ký tự đặc biệt hay không
        return str.matches("^[1-9][0-9]*$");
    }

    // kiem tra so hang nhap vao
    public static int checkInputRow(int n) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input row number: ");
        String s;
        do {
            s = scanner.nextLine();
            s = s.trim();
            if (!isNumber(s) || (Integer.parseInt(s) > n)) {
                System.out.println("Input again: ");
            }

        } while (!isNumber(s) || (Integer.parseInt(s) > n));
        return Integer.parseInt(s);


    }

    //--------------------------------------------------------------------------------------------------------
    public static boolean isDay(String str) {
        // Kiểm tra xem chuỗi có chứa ký tự đặc biệt hay không
        return str.matches("(\\d{4})[-|/]([0][1-9]|[1][0-2])[-|/]([0|1][1-9]|[12][0-9]|[3][01])$");
    }

    public static void inputDate() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Input start day (yyyy-mm-dd):");
        String startDay = scanner.nextLine();
        startDay = Menue.checkInputDay(startDay);
        System.out.println("Input end day (yyyy-mm-dd):");
        String endDay = scanner.nextLine();
        endDay = Menue.checkInputDay(endDay);
        CommissionDao.getInstant().fineDonateByDate(startDay, endDay);
    }

    public static String checkInputDay(String s) {
        Scanner scanner = new Scanner(System.in);
        do {
            s = s.trim();
            if (!isDay(s)) {
                System.out.println("Input again: ");
                s = scanner.nextLine();
            }
        } while (!isDay(s));
        return s;
    }
//-------------------------------------------------------------------------------------------------------------

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
        CommissionDelete = CommissionDao.getInstant().selectAllCommissions();
        // kiem tra ton tai
        for (Commission com : CommissionDelete) {
            if (String.valueOf(com.getId()).equalsIgnoreCase(id)) {
                checkId = 1;
                break;
            }
        }
        return checkId;
    }

    //		 kiem tra su ton tai cua ID
    public int checkExistOfficerID(String str) {
        String str2 = "CB";
        String str1 = str.substring(0, 2);
        if (!str2.equals(str1)) {
            return 0;
        }
        // cat chuoi tu vi tri thu 3
        String id = str.substring(str.indexOf("_") + 1);
        System.out.println("wait...");
        Integer checkId = 0;
        List<Officer> officerID = new ArrayList<>();
        officerID = CommissionDao.getInstant().selectAllOfficer();
        // kiem tra ton tai
        for (Officer com : officerID) {
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
        result = CommissionDao.getInstant().selectAllCommissions();
        // kiem tra du lieu nhap co ton tai trong data khong
        for (Commission commission : result) {
            if (c.getPrecintName().trim().equalsIgnoreCase(commission.getPrecintName()) == true
                    && c.getCityName().trim().equalsIgnoreCase(
                    commission.getCityName()) == true
                    && c.getProvinceName().trim().equalsIgnoreCase(
                    commission.getProvinceName()) == true) {
                check = 1;
                break;
            }
        }
        return check;
    }

    public int findIdCommission(List<Commission> commissions, int row) {
        int id = 0;
        int numberRow = 0;
        int i = 1;
        Map<Integer, Commission> mapCommission = new HashMap<>();
        for (Commission commission : commissions) {
            commission.setIndex(commission.getIndex() + i);
            i++;
            numberRow = commission.getIndex();
            mapCommission.put(numberRow, commission);
        }
        Commission commission = mapCommission.get(row);
//				  System.out.println("id: " + commission.getId());
        id = commission.getId();
        return id;
    }


    public void menuUpdate(int id) {
//				  System.out.println("1: Update commission name");
//				  System.out.println("2: Update city name");
//				  System.out.println("3: Update province name");
//				  System.out.println("4: Update all commission");
//				  System.out.println("0: Exit");
//				  int n = Integer.parseInt(String.valueOf(input_int()));
        switch (4) {
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
                Commission commissionNew = new Commission(id, precint_name, city_name, province_name);
                CommissionDao.getInstant().update(commissionNew);
                System.out.println("Update Success++++++++++++++");
                Integer sumNumberRecord1 = commissionDao
                        .printlnCommissions(commissionDao.selectAllCommissions());
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
//						   CommissionDao commissionDao1 = new CommissionDao();
//						   commissionDao1.printlnOffice(commissionDao1.selectAllOfficer());
                break;
            case 1:
                System.out.println("Connecting...");

//						   System.out.println("id \t\t\tPrecintName \t\t\tCityName \t\t\tProvinceName \t\t\tOfficerId ");
//						   CommissionDao commissionDao = new CommissionDao();
                commissionDao.printlnCommissions(commissionDao.selectAllCommissions());
                break;
            case 2:
                commissionDao.printlnCommissions(commissionDao.selectAllCommissions());
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

                System.out.println("Please...");
                // tao doi tuong moi tu ban phim
                Commission commissionCheck = new Commission(precint_name, city_name, province_name);
                check = checkExistCommission(commissionCheck);// ham checkExistCommission() kiem tra
                // ton tai cua doi tuoi commission
                if (check == 1) {
                    System.out.println("Exist data!!");
                } else {
                    CommissionDao.getInstant().insert(commissionCheck);
                    CommissionDao commissionInsert = new CommissionDao();
                    commissionInsert.printlnCommissions(
                            commissionInsert.selectAllCommissions());
                }
                break;
            case 3:
                Integer sumNumberRecord = commissionDao
                        .printlnCommissions(commissionDao.selectAllCommissions());
                int inputRow = checkInputRow(sumNumberRecord);
                Integer commissionID = findIdCommission(commissionDao.selectAllCommissions(),
                        inputRow);
                CommissionDao.getInstant().delete(commissionID);
                System.out.println("Delete Success");
                commissionDao.printlnCommissions(commissionDao.selectAllCommissions());
                break;
            case 4:
                Integer sumNumberRecord1 = commissionDao
                        .printlnCommissions(commissionDao.selectAllCommissions());
                int inputRow1 = checkInputRow(sumNumberRecord1);
                Integer commissionID1 = findIdCommission(commissionDao.selectAllCommissions(),
                        inputRow1);
//						   System.out.println("================= ");
                menuUpdate((commissionID1));

                break;

        }

    }
}
