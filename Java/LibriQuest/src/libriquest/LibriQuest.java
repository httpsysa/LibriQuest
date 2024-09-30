 package libriquest;

import java.util.ArrayList;
import java.util.Scanner;

public class LibriQuest {

    static Admin admin = new Admin();
    static Student student = new Student();

    // Data structures to store inventory and borrowed books
    static ArrayList<String> inventoryList = new ArrayList<>();
    static ArrayList<String> borrowedList = new ArrayList<>();

    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        showMainMenu();
    }

    // Main Menu: Student or Admin
    public static void showMainMenu() {
        String[] options = {"Student", "Admin"};
        while (true) {
            System.out.println("-----------------------------------------------");
            System.out.println(" ");
            System.out.println("Select a User:\n1. Student\n2. Admin\n3. Exit");
            System.out.println(" ");
            System.out.println("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    studentLogin();
                    break;
                case 2:
                    adminLogin();
                    break;
                case 3:
                    System.exit(0); // Exit the program
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Student login
    public static void studentLogin() {
        if (student.login()) {
            student.showStudentMenu();
        } else {
            System.out.println("Invalid login. Please try again.");
        }
    }

    // Admin login
    public static void adminLogin() {
        if (admin.login()) {
            admin.showAdminMenu();
        } else {
            System.out.println("Invalid login. Please try again.");
        }
    }
}
