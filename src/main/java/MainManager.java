import java.util.Scanner;

public class MainManager {
    private static Scanner sc = new Scanner(System.in);
    public static void main(String[] args) {
        int n;
        do {
            n = menu(sc);
            productManagement(n, sc);

        } while (!"0".equalsIgnoreCase(String.valueOf(n)));

        sc.close();

    }
    public static int menu(Scanner sc) {
        int n;

        do {
            System.out.println("----------------MANAGEMENT----------------");
            System.out.println();

            System.out.println();
            System.out.println("            0. Exit");
            System.out.println("            1. Quản lý hộ dân");
            System.out.println("            2. Quản lý cán bộ");
            System.out.println("            3. Quản lý người đại diện");
            System.out.println("            4. Quản lý công ty ủng hộ");
            System.out.println("            5. Quản lý Ủy Ban");
            System.out.println("            6. Quản lý phân phối");
            System.out.println("            7. Quản lý chi tiết phân phối");
            System.out.println("            8. Quản lý đối tượng ưu tiên");
            System.out.println("            9. Quản lý công dân");
            System.out.println("            10. Quản lý đối tượng công dân");
            System.out.println("            11. Thống kê");
            System.out.println();
            System.out.println("            What do you want to choose?");


            int m = -1;

            do {
                try {
                    System.out.print("Please choose....");
                    m = Integer.parseInt(sc.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                }
            } while (m == -1);

            n = m;


        } while (!(n >= 0 && n <= 11));
        return n;
    }

    public static int productManagement(int n, Scanner sc) {
        try {

            switch (n) {
                case 1:
                    System.out.println("Quản lý hộ dân");
                    break;
                case 2:
                    System.out.println("Quản lý cán bộ");
                    break;
                case 3:
                    System.out.println("Quản lý người đại diện");

                    break;
                case 4:
                    System.out.println("Quản lý công ty ủng hộ");

                    break;
                case 5:
                    System.out.println("Quản lý Ủy Ban");
                    break;
                case 6:
                    System.out.println("Quản lý phân phối");

                    break;
                case 7:
                    System.out.println("Quản lý chi tiết phân phối");

                    break;
                case 8:
                    System.out.println("Quản lý đối tượng ưu tiên");


                    break;
                case 9:
                    System.out.println("Quản lý công dân");

                    break;
                case 10:
                    System.out.println("Quản lý đối tượng công dân");

                    break;
                case 11:
                    System.out.println("Thống kê");

                    break;
                case 0:
                    System.out.println("Close program.....");
                    break;

            }
        } catch (Exception e) {

            e.printStackTrace();
        }
        return n;

    }
}
