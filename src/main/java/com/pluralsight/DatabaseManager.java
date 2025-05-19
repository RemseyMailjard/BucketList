package com.pluralsight;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager implements BucketItemManager {

    private final String connectionString;

    public DatabaseManager() {
        this.connectionString =
            "jdbc:sqlserver://skills4it.database.windows.net:1433;" +
            "database=yearup;" +
            "user=bucket_user;" +
            "password=SuperSecurePassword123!;" +
            "encrypt=true;" +
            "trustServerCertificate=false;" +
            "loginTimeout=30;";
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(connectionString);
    }

    @Override
    public void addItem(BucketItem item) {
        String sql = "INSERT INTO BucketItems (title, description, isDone, createdDate, targetDate, category, priority, notes, imageUrl) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, item.getTitle());
            stmt.setString(2, item.getDescription());
            stmt.setBoolean(3, item.isDone());
            stmt.setDate(4, Date.valueOf(item.getCreatedDate()));
            stmt.setDate(5, item.getTargetDate() != null ? Date.valueOf(item.getTargetDate()) : null);
            stmt.setString(6, item.getCategory());
            stmt.setInt(7, item.getPriority());
            stmt.setString(8, item.getNotes());
            stmt.setString(9, item.getImageUrl());
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
    public void updateItem(String oldTitle, String newTitle) {
        String sql = "UPDATE BucketItems SET title = ? WHERE title = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newTitle);
            stmt.setString(2, oldTitle);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating item: " + e.getMessage());
        }
    }

    @Override
    public void updateItem(String title) {
        System.out.print("Enter new title: ");
        java.util.Scanner scanner = new java.util.Scanner(System.in);
        String newTitle = scanner.nextLine();
        updateItem(title, newTitle);
    }

    @Override
    public void markItemAsDone(String title) {
        String sql = "UPDATE BucketItems SET isDone = 1, completedDate = ? WHERE title = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(LocalDate.now()));
            stmt.setString(2, title);
            stmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error marking item as done: " + e.getMessage());
        }
    }

    @Override
    public List<BucketItem> getAllItems() {
        List<BucketItem> items = new ArrayList<>();
        String sql = "SELECT * FROM BucketItems";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                BucketItem item = new BucketItem(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("description"),
                    rs.getDate("targetDate") != null ? rs.getDate("targetDate").toLocalDate() : null,
                    rs.getString("category"),
                    rs.getInt("priority")
                );
                item.setDone(rs.getBoolean("isDone"));
                item.setNotes(rs.getString("notes"));
                item.setImageUrl(rs.getString("imageUrl"));
                items.add(item);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving items: " + e.getMessage());
        }
        return items;
    }
}