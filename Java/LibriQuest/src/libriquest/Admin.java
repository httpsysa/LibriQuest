package libriquest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Admin extends LibriQuest {

    private static final HashMap<String, String> adminUsers = new HashMap<>();

    InventoryManagement inventoryManagement = new InventoryManagement();
    BorrowedBook borrowedBook = new BorrowedBook(); 
    BookRequest bookRequest = new BookRequest();          

    private static int inventoryIdCounter = 1; // To generate unique Inventory IDs
    private static int borrowedIdCounter = 1; // To generate unique Borrowed IDs
    private static int requestIdCounter = 1; // To generate unique Request IDs
    private static ArrayList<Object[]> inventoryData = new ArrayList<>();
    private static ArrayList<Object[]> borrowedData = new ArrayList<>();
    private static ArrayList<Object[]> requestData = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    // Constructor to add admin credentials
    public Admin() {
        adminUsers.put("admin1", "admin123");
        adminUsers.put("admin2", "admin456");
    }

    public boolean login() {
        System.out.println("-----------------------------------------------");
        System.out.println("\tADMIN LOGIN");
        System.out.println("");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        return adminUsers.containsKey(username) && adminUsers.get(username).equals(password);
    }

    // Show Admin Menu
    public void showAdminMenu() {
        while (true) {
            System.out.println("---------------------------------------------");
            System.out.println("\tADMIN MENU");
            System.out.println("");
            System.out.println("1. Add Inventory");
            System.out.println("2. View Inventory List");
            System.out.println("3. View Borrowed List");
            System.out.println("4. View Request List"); 
            System.out.println("5. Exit");

            System.out.println("");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    inventoryManagement.addInventory();
                    break;
                case 2:
                    inventoryManagement.viewInventory();
                    break;
                case 3:
                    borrowedBook.viewBorrowed();
                    break;
                case 4:
                    bookRequest.viewRequests(); 
                case 5:
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
