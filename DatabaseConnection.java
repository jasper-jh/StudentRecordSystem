package studentrecordsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/student_record_system"; // your DB name
    private static final String USER = "root";   // your MySQL username
    private static final String PASSWORD = "";   // your MySQL password (blank if none)

    public static Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Database connected successfully!");
        } catch (SQLException e) {
            System.err.println("❌ Database connection failed.");
          
            e.printStackTrace();
        }
        return connection;
    }
}
