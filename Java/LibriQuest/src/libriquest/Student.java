package libriquest;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Student {
    BorrowedBook borrowBook = new BorrowedBook();
    BookRequest bookRequest = new BookRequest();       

    private static final HashMap<String, String> studentUsers = new HashMap<>();
    static ArrayList<BorrowedBook> borrowedBooks = new ArrayList<>();
    static ArrayList<BookRequest> bookRequests = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    public Student() {
        // Adding 5 student users
        studentUsers.put("student1", "pass1");
        studentUsers.put("student2", "pass2");
        studentUsers.put("student3", "pass3");
        studentUsers.put("student4", "pass4");
        studentUsers.put("student5", "pass5");
    }

    // Login method using Scanner
    public boolean login() {
        System.out.println("-----------------------------------------------");
        System.out.println("\tSTUDENT LOGIN");
        System.out.println("");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        return studentUsers.containsKey(username) && studentUsers.get(username).equals(password);
    }

    // Show Student Menu
    public void showStudentMenu() {
        while (true) {
            System.out.println("-----------------------------------------------");
            System.out.println("\tSTUDENT MENU");
            System.out.println("");
            System.out.println("1. View Catalog");
            System.out.println("2. Exit");

            System.out.println("");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    showCatalogOptions();
                    break;
                case 2:
                    return; // Exit the menu and return to the main menu
                default:
                    System.out.println("Invalid option. Please try again.");
                    break;
            }
        }
    }

    public void showCatalogOptions() {
        viewCatalog();

        System.out.println("1. Borrow a Book");
        System.out.println("2. Request a Book");
        System.out.println("3. Back to Student Menu");

        System.out.println("");
        System.out.print("Choose an option: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                borrowBook.borrowedBook();
                break;
            case 2:
                bookRequest.requestBook();
                break;
            case 3:
                return;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }

    // View Catalog method using Scanner
    public void viewCatalog() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM books"; // Fetch all books from the database
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.isBeforeFirst()) { // If no data is available
                System.out.println("No books available.");
            } else {
                System.out.println(
                        "----------------------------------------------------------------------------------------------------");
                System.out.println("\t\t\t\t\t\tCATALOG LIST");
                System.out.println("");
                System.out.printf("%-5s %-20s %-20s %-20s %-15s %-10s %-10s%n",
                        "ID", "Title", "Author", "Publisher", "Published Date", "Genre", "Status");
                System.out.println(
                        "----------------------------------------------------------------------------------------------------");

                // Iterate through the result set and print each book's details
                while (rs.next()) {
                    System.out.printf("%-5d %-20s %-20s %-20s %-15s %-10s %-10s%n",
                            rs.getInt("id"), rs.getString("title"), rs.getString("author"),
                            rs.getString("publisher"), rs.getDate("published_date"),
                            rs.getString("genre"), rs.getString("status"));

                    System.out.println(
                            "----------------------------------------------------------------------------------------------------");
                    System.out.println("  ");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching catalog: " + e.getMessage());
        }
    }
}
