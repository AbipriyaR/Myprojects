import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CalendarApp {

    // Event class to store event details
    static class Event {
        private final int id;
        private final String title;
        private final String date;
        private final String time;
        private final String description;

        public Event(int id, String title, String date, String time, String description) {
            this.id = id;
            this.title = title;
            this.date = date;
            this.time = time;
            this.description = description;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return "ID: " + id + "\n" +
                   "Title: " + title + "\n" +
                   "Date: " + date + "\n" +
                   "Time: " + time + "\n" +
                   "Description: " + description + "\n" +
                   "-----------------------------";
        }
    }

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/CalendarApp";
    private static final String DB_USER = "abi"; // Replace with your MySQL username
    private static final String DB_PASSWORD = "1234"; // Replace with your MySQL password

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            System.out.println("Connected to the database!");

            while (true) {
                System.out.println("\n===== Calendar Application =====");
                System.out.println("1. Add Event");
                System.out.println("2. View Events");
                System.out.println("3. Exit");
                System.out.print("Enter your choice: ");
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                switch (choice) {
                    case 1:
                        addEvent(connection, scanner);
                        break;
                    case 2:
                        viewEvents(connection);
                        break;
                    case 3:
                        System.out.println("Exiting application...");
                        return;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // Method to add a new event
    private static void addEvent(Connection connection, Scanner scanner) throws SQLException {
        System.out.print("Enter event title: ");
        String title = scanner.nextLine();

        System.out.print("Enter event date (YYYY-MM-DD): ");
        String date = scanner.nextLine();

        System.out.print("Enter event time (HH:MM:SS): ");
        String time = scanner.nextLine();

        System.out.print("Enter event description: ");
        String description = scanner.nextLine();

        String query = "INSERT INTO Events (EventTitle, EventDate, EventTime, EventDescription) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, title);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            pstmt.setString(4, description);
            pstmt.executeUpdate();
            System.out.println("Event added successfully!");
        }
    }

    // Method to view all events
    private static void viewEvents(Connection connection) throws SQLException {
        String query = "SELECT * FROM Events ORDER BY EventDate, EventTime";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("\n===== Stored Events =====");
            while (rs.next()) {
                int id = rs.getInt("EventID");
                String title = rs.getString("EventTitle");
                String date = rs.getString("EventDate");
                String time = rs.getString("EventTime");
                String description = rs.getString("EventDescription");

                System.out.println("ID: " + id);
                System.out.println("Title: " + title);
                System.out.println("Date: " + date);
                System.out.println("Time: " + time);
                System.out.println("Description: " + description);
                System.out.println("-----------------------------");
            }
        }
    }
}
