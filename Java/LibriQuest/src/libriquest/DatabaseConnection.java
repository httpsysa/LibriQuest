package libriquest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/libriquest_db";
    private static final String USER = "root"; // default XAMPP user
    private static final String PASSWORD = ""; // default XAMPP password (empty)

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
