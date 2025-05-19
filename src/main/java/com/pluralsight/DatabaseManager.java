package com.pluralsight;

import java.util.List;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class DatabaseManager implements BucketItemManager {

    private final String connectionString;

    public DatabaseManager(String connectionString) {
        this.connectionString = connectionString;
    }

    private Connection getConnection() throws SQLException {
        String url = "jdbc:sqlserver://skills4it.database.windows.net:1433;" +
                "database=yearup;" +
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "authentication=ActiveDirectoryDefault;" +
                "hostNameInCertificate=*.database.windows.net;" +
                "loginTimeout=30;";

        return DriverManager.getConnection(url);
    }



    @Override
    public void addItem(BucketItem item) {
        String sql = "INSERT INTO BucketItems (title, isDone) VALUES (?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getTitle());
            stmt.setBoolean(2, item.isDone());
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding item: " + e.getMessage());
        }
    }

    @Override
    public void removeItem(String title) {
        String sql = "DELETE FROM BucketItems WHERE title = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error removing item: " + e.getMessage());
        }
    }

    @Override
    public void updateItem(String title) {

    }

    public void updateItem(String title, String _newTitle) {
        System.out.print("Enter new title: ");
        Scanner scanner = new Scanner(System.in);
        String newTitle = scanner.nextLine();

        String sql = "UPDATE BucketItems SET title = ? WHERE title = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating item: " + e.getMessage());
        }
    }



    @Override
    public void markItemAsDone(String title) {
        String sql = "UPDATE BucketItems SET isDone = 1 WHERE title = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error marking item as done: " + e.getMessage());
        }
    }

    @Override
    public List<BucketItem> getAllItems() {
        List<BucketItem> items = new ArrayList<>();
        String sql = "SELECT title, isDone FROM BucketItems";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String title = rs.getString("title");
                boolean isDone = rs.getBoolean("isDone");
                BucketItem item = new BucketItem(title);
                item.setDone(isDone);
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving items: " + e.getMessage());
        }
        return items;
    }
}
