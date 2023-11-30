package Model;


import java.util.Scanner;


public class Processing1 {
    private static Scanner sc = new Scanner(System.in);
    public static void waitForEnter() {
        while (true) {
            System.out.println("\u001B[32mPress 'Enter' to return to the menu...\u001B[0m");
            String input = sc.nextLine();

            if (input.isEmpty()) {
                // Nếu người dùng nhấn Enter (để trống input), thoát khỏi vòng lặp
                break;
            }
        }
    }
}
