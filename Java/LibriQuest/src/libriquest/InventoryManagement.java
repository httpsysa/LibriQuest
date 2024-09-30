package libriquest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Scanner;

public class InventoryManagement {
    static ArrayList<Book> inventoryBooks = new ArrayList<>();
    private static Scanner scanner = new Scanner(System.in);

    // Add Inventory method
    public void addInventory() {
        System.out.println("-----------------------------------------------");
        System.out.println("\tADD BOOK DETAILS");
        System.out.println("");
        System.out.print("Enter Title: ");
        String title = scanner.nextLine();
        System.out.print("Enter Author: ");
        String author = scanner.nextLine();
        System.out.print("Enter Publisher: ");
        String publisher = scanner.nextLine();
        System.out.print("Enter Published Date (YYYY-MM-DD): ");
        String publishedDate = scanner.nextLine();
        System.out.print("Enter Genre: ");
        String genre = scanner.nextLine();

        System.out.print("Enter Status (Available or Borrowed): ");
        String status = scanner.nextLine();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO books (title, author, publisher, published_date, genre, status) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title);
            stmt.setString(2, author);
            stmt.setString(3, publisher);
            stmt.setString(4, publishedDate);
            stmt.setString(5, genre);
            stmt.setString(6, status);
            stmt.executeUpdate();
            System.out.println("");
            System.out.println("BOOK ADDED TO INVENTORY!");
        } catch (SQLException e) {
            System.out.println("");
            System.out.println("ERROR ADDING BOOK!: " + e.getMessage());
        }
    }

    // View Inventory method
    public void viewInventory() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM books";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.isBeforeFirst()) {
                System.out.println("No inventory available.");
            } else {
                System.out.println(
                        "----------------------------------------------------------------------------------------------------");
                System.out.println("\t\t\t\t\tINVENTORY LIST");
                System.out.println("");
                System.out.printf("%-5s %-20s %-20s %-15s %-15s %-10s %-10s%n",
                        "ID", "Title", "Author", "Publisher", "Published Date", "Genre", "Status");
                System.out.println(
                        "----------------------------------------------------------------------------------------------------");
                while (rs.next()) {
                    System.out.printf("%-5d %-20s %-20s %-15s %-15s %-10s %-10s%n",
                            rs.getInt("id"), rs.getString("title"), rs.getString("author"),
                            rs.getString("publisher"), rs.getDate("published_date"),
                            rs.getString("genre"), rs.getString("status"));
                }

                System.out.println(
                        "----------------------------------------------------------------------------------------------------");
                System.out.println("\tOPTIONS");
                System.out.println("");
                System.out.println("1. Edit Inventory");
                System.out.println("2. Delete Inventory");
                System.out.println("3. Return to Admin Menu");
                System.out.println("");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine();

                if (option == 1) {
                    System.out.println("");
                    System.out.print("Enter ID to edit: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); 
                    editInventory(id);
                } else if (option == 2) {
                    System.out.print("Enter ID to delete: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();
                    deleteInventory(id);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error fetching inventory: " + e.getMessage());
        }
    }

    // Edit Inventory method
    public void editInventory(int id) {
        System.out.println("-----------------------------------------------");
        System.out.println("\tEDIT BOOK DETAILS");
        System.out.println("");
        System.out.print("New Title (Leave blank to keep current): ");
        String title = scanner.nextLine();
        System.out.print("New Author (Leave blank to keep current): ");
        String author = scanner.nextLine();
        System.out.print("New Publisher (Leave blank to keep current): ");
        String publisher = scanner.nextLine();
        System.out.print("New Published Date (YYYY-MM-DD, Leave blank to keep current): ");
        String publishedDate = scanner.nextLine();
        System.out.print("New Genre (Leave blank to keep current): ");
        String genre = scanner.nextLine();
        System.out.print("New Status (Available/Borrowed, Leave blank to keep current): ");
        String status = scanner.nextLine();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE books SET title = COALESCE(?, title), author = COALESCE(?, author), "
                    + "publisher = COALESCE(?, publisher), published_date = COALESCE(?, published_date), "
                    + "genre = COALESCE(?, genre), status = COALESCE(?, status) WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, title.isEmpty() ? null : title);
            stmt.setString(2, author.isEmpty() ? null : author);
            stmt.setString(3, publisher.isEmpty() ? null : publisher);
            stmt.setString(4, publishedDate.isEmpty() ? null : publishedDate);
            stmt.setString(5, genre.isEmpty() ? null : genre);
            stmt.setString(6, status.isEmpty() ? null : status);
            stmt.setInt(7, id);
            stmt.executeUpdate();
            System.out.println("BOOK UPDATED");
        } catch (SQLException e) {
            System.out.println("ERROR UPDATING BOOK!: " + e.getMessage());
        }
    }

    // Delete Inventory method
    public void deleteInventory(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM books WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("");
                System.out.println("BOOK DELETED FROM INVENTORY!");
            } else {
                System.out.println("");
                System.out.println("BOOK NOT FOUND!");
            }
        } catch (SQLException e) {
            System.out.println("");
            System.out.println("ERROR DELETING BOOK!: " + e.getMessage());
        }
    }


    // Get Inventory Books method remains unchanged
    public static ArrayList<Book> getInventoryBooks() {
        return inventoryBooks;
    }
}