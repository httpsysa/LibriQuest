package libriquest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class BorrowedBook extends Book {
    public static Scanner scanner = new Scanner(System.in);

    public String borrowTime;
    public String returnTime;
    public String studentName;
    public String contactNumber;
    public String email;

    // Constructor with all necessary fields
    public BorrowedBook(String title, String author, String publisher, String borrowTime, String returnTime,
            String studentName, String contactNumber, String email) {
        super(title, author, publisher, null, null, "Borrowed");
        this.borrowTime = borrowTime;
        this.returnTime = returnTime;
        this.studentName = studentName;
        this.contactNumber = contactNumber;
        this.email = email;
    }

    public BorrowedBook() {
        super(null, null, null, null, null, "Borrowed");
    }    

    public static void borrowedBook() {
        System.out.println("-----------------------------------------------");
        System.out.println("\tBORROW A BOOK");
        System.out.println("");
    
        System.out.print("Enter Book ID: ");
        String bookId = scanner.nextLine();
    
        // Check if the book ID is valid and available
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sqlCheck = "SELECT title, author, publisher FROM books WHERE id = ? AND status = 'Available'";
            PreparedStatement stmtCheck = conn.prepareStatement(sqlCheck);
            stmtCheck.setString(1, bookId);
            ResultSet rs = stmtCheck.executeQuery();
    
            if (!rs.next()) {
                System.out.println("Invalid Book ID or the book is not available. Please try again.");
                return; // Exit the method if the book is not valid or not available
            }
    
            // Retrieve book details from the result set
            String title = rs.getString("title");
            String author = rs.getString("author");
            String publisher = rs.getString("publisher");
    
            // Proceed with borrowing if the book is available
            System.out.print("Borrow Date & Time (yyyy-MM-dd HH:mm): ");
            String borrowTime = scanner.nextLine();
            System.out.print("Return Date & Time (yyyy-MM-dd HH:mm): ");
            String returnTime = scanner.nextLine();
            System.out.print("Student Name: ");
            String studentName = scanner.nextLine();
            System.out.print("Contact Number: ");
            String contactNumber = scanner.nextLine();
            System.out.print("Email Address: ");
            String email = scanner.nextLine();
    
            // Ensure all fields are filled
            if (borrowTime.isEmpty() || returnTime.isEmpty() || studentName.isEmpty() || contactNumber.isEmpty()
                    || email.isEmpty()) {
                System.out.println("All fields must be filled.");
            } else {
                try {
                    // Insert the borrow information into the 'borrowed' table, using the book details
                    String sqlInsert = "INSERT INTO borrowed (book_id, title, author, publisher, borrow_time, return_time, student_name, contact_number, email) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement stmtInsert = conn.prepareStatement(sqlInsert);
                    stmtInsert.setString(1, bookId); // Catalog ID for the book
                    stmtInsert.setString(2, title); // Book Title
                    stmtInsert.setString(3, author); // Author
                    stmtInsert.setString(4, publisher); // Publisher
                    stmtInsert.setString(5, borrowTime); // Borrow Date
                    stmtInsert.setString(6, returnTime); // Return Date
                    stmtInsert.setString(7, studentName); // Student Name
                    stmtInsert.setString(8, contactNumber); // Contact Number
                    stmtInsert.setString(9, email); // Email
                    stmtInsert.executeUpdate();
    
                    // Update the status of the book to 'Borrowed'
                    String sqlUpdate = "UPDATE books SET status = 'Borrowed' WHERE id = ?";
                    PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdate);
                    stmtUpdate.setString(1, bookId);
                    int rowsAffected = stmtUpdate.executeUpdate();
    
                    if (rowsAffected > 0) {
                        System.out.println("Book borrowed successfully and status updated.");
                    } else {
                        System.out.println("Failed to update the book status.");
                    }
                } catch (SQLException e) {
                    System.out.println("Failed to borrow the book: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking the book: " + e.getMessage());
        }
    }    
    
    // View Borrowed method
    public static void viewBorrowed() {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM borrowed";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            if (!rs.isBeforeFirst()) { // Check if there are any results
                System.out.println("\nNo borrowed books available.");
            } else {
                System.out.println("\t\t\t\tBORROWED LIST");
                System.out.printf("%-5s %-15s %-12s %-10s %-20s %-20s %-15s %-15s %-15s%n",
                        "ID", "Title", "Author", "Publisher", "Borrow Time", "Return Time", "Name",
                        "Number", "Email");
                System.out.println(
                        "-----------------------------------------------------------------------------------------------------------------------------");

                while (rs.next()) {
                    // Ensure everything is printed on the same line
                    System.out.printf("%-5d %-15s %-12s %-10s %-20s %-20s %-15s %-15s %-15s%n",
                            rs.getInt("id"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("publisher"),
                            rs.getString("borrow_time"),
                            rs.getString("return_time"),
                            rs.getString("student_name"),
                            rs.getString("contact_number"),
                            rs.getString("email"));
                }

                System.out.println(
                        "______________________________________________________________________________________________________________________________");

                System.out.println("\n\tOPTIONS");
                System.out.println("1. Edit");
                System.out.println("2. Delete");
                System.out.println("3. Return to Admin Menu");
                System.out.print("Choose an option: ");
                int option = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (option) {
                    case 1:
                        System.out.print("Enter ID to edit: ");
                        int editId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        editBorrowed(editId);
                        break;
                    case 2:
                        System.out.print("Enter ID to delete: ");
                        int deleteId = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                        deleteBorrowed(deleteId);
                        break;
                    case 3:
                        System.out.println("Returning to Admin Menu...");
                        return; // Return to Admin Menu
                    default:
                        System.out.println("Invalid option. Please try again.");
                        break;
                }
            }

        } catch (SQLException e) {
            System.out.println("Error fetching borrowed books: " + e.getMessage());
        }
    }

    // Edit Borrowed method
    public static void editBorrowed(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM borrowed WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                System.out.print("New Title (Leave blank to keep current): ");
                String title = scanner.nextLine();
                System.out.print("New Author (Leave blank to keep current): ");
                String author = scanner.nextLine();
                System.out.print("New Publisher (Leave blank to keep current): ");
                String publisher = scanner.nextLine();
                System.out.print("New Borrow Date (Leave blank to keep current): ");
                String borrowDate = scanner.nextLine();
                System.out.print("New Return Date (Leave blank to keep current): ");
                String returnDate = scanner.nextLine();

                String updateSql = "UPDATE borrowed SET title = ?, author = ?, publisher = ?, borrow_time = ?, return_time = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateSql);
                updateStmt.setString(1, title.isEmpty() ? rs.getString("title") : title);
                updateStmt.setString(2, author.isEmpty() ? rs.getString("author") : author);
                updateStmt.setString(3, publisher.isEmpty() ? rs.getString("publisher") : publisher);
                updateStmt.setString(4, borrowDate.isEmpty() ? rs.getString("borrow_time") : borrowDate);
                updateStmt.setString(5, returnDate.isEmpty() ? rs.getString("return_time") : returnDate);
                updateStmt.setInt(6, id);

                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Borrowed book updated.");
                } else {
                    System.out.println("Failed to update borrowed book.");
                }
            } else {
                System.out.println("Invalid ID.");
            }
        } catch (SQLException e) {
            System.out.println("Error editing borrowed book: " + e.getMessage());
        }
    }

    // Delete Borrowed method
    public static void deleteBorrowed(int id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "DELETE FROM borrowed WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);

            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Borrowed book deleted.");
            } else {
                System.out.println("Failed to delete borrowed book.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting borrowed book: " + e.getMessage());
        }
    }

    // Getters
    public String getBorrowTime() {
        return borrowTime;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String getEmail() {
        return email;
    }
}