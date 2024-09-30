package libriquest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class BookRequest {
    public static Scanner scanner = new Scanner(System.in);

    public String title;
    public String author;
    public String publisher;

    public BookRequest(String title, String author, String publisher) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
    }
    
    public BookRequest() {
        this.title = "";
        this.author = "";
        this.publisher = "";
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getPublisher() {
        return publisher;
    }

    // Request Book method using Scanner
    public static void requestBook() {
        System.out.println("-----------------------------------------------");
        System.out.println("\tREQUEST A BOOK");
        System.out.println("");
        System.out.print("Book Title: ");
        String title = scanner.nextLine();
        System.out.print("Author: ");
        String author = scanner.nextLine();
        System.out.print("Publisher: ");
        String publisher = scanner.nextLine();

        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty()) {
            System.out.println("All fields must be filled.");
        } else {
            // Insert the request into the database
            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "INSERT INTO requests (title, author, publisher) VALUES (?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, title);
                stmt.setString(2, author);
                stmt.setString(3, publisher);
                stmt.executeUpdate(); // Execute the insert statement

                System.out.println("");
                System.out.println("BOOK REQUEST SUBMITTED SUCCESSFULY AND UNDER REVIEW BY THE ADMINS!");
            } catch (SQLException e) {
                System.out.println("Failed to submit the request: " + e.getMessage());
            }
        }
    }

    // View Requests method
    public static void viewRequests() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM requests";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            if (!rs.isBeforeFirst()) { // No requests available
                System.out.println("No book requests available.");
            } else {
                System.out.println("---------------------------------------------------------");
                System.out.println("\t\t\tREQUEST LIST");
                System.out.println("");
                System.out.printf("%-5s %-20s %-20s %-15s%n",
                        "ID", "Title", "Author", "Publisher");
                System.out.println("---------------------------------------------------------");

                while (rs.next()) {
                    System.out.printf("%-5d %-20s %-20s %-15s%n",
                            rs.getInt("request_id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("publisher"));
                }

                System.out.println("---------------------------------------------------------");
                System.out.println("\tOPTIONS:");
                System.out.println("");
                System.out.println("1. Edit");
                System.out.println("2. Delete");
                System.out.println("3. Return to Admin Menu");
                System.out.println("");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (option == 1) {
                    System.out.print("Enter ID to edit: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    editRequest(id);
                } else if (option == 2) {
                    System.out.print("Enter ID to delete: ");
                    int id = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    deleteRequest(id);
                } 
            }

        } catch (SQLException e) {
            System.out.println("Error fetching requests: " + e.getMessage());
        }
    }

    // Method to edit a request (admin only)
    public static void editRequest(int requestId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM requests WHERE request_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, requestId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.print("New Title (Leave blank to keep current): ");
                String title = scanner.nextLine();
                System.out.print("New Author (Leave blank to keep current): ");
                String author = scanner.nextLine();
                System.out.print("New Publisher (Leave blank to keep current): ");
                String publisher = scanner.nextLine();

                // Update only if values are provided
                String updateSql = "UPDATE requests SET title = ?, author = ?, publisher = ? WHERE request_id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, title.isEmpty() ? rs.getString("title") : title);
                updateStmt.setString(2, author.isEmpty() ? rs.getString("author") : author);
                updateStmt.setString(3, publisher.isEmpty() ? rs.getString("publisher") : publisher);
                updateStmt.setInt(4, requestId);

                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("");
                    System.out.println("REQUEST UPDATED SUCCESSFULLY!");
                } else {
                    System.out.println("ERROR UPDATING REQUEST!");
                }
            } else {
                System.out.println("");
                System.out.println("REQUEST NOT FOUND!");
            }

        } catch (SQLException e) {
            System.out.println("Error editing request: " + e.getMessage());
        }
    }

    // Method to delete a request (admin only)
    public static void deleteRequest(int requestId) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM requests WHERE request_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, requestId);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("");
                System.out.println("REQUEST DELETED SUCCESSFULLY.");
            } else {
                System.out.println("");
                System.out.println("REQUEST NOT FOUND!");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting request: " + e.getMessage());
        }
    }
}
