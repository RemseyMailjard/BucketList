package com.pluralsight;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FileBucketManager implements BucketItemManager {
    private final String filePath;

    public FileBucketManager(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void addItem(BucketItem item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(String.join("|",
                item.getTitle(),
                String.valueOf(item.isDone()),
                item.getDescription(),
                item.getTargetDate() != null ? item.getTargetDate().toString() : "",
                item.getCategory(),
                String.valueOf(item.getPriority())
            ));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing item: " + e.getMessage());
        }
    }

    @Override
    public void removeItem(String title) {
        List<BucketItem> updatedList = new ArrayList<>();
        for (BucketItem item : getAllItems()) {
            if (!item.getTitle().equalsIgnoreCase(title)) {
                updatedList.add(item);
            }
        }
        writeAllItems(updatedList);
    }

    @Override
    public void updateItem(String oldTitle, String newTitle) {
        List<BucketItem> items = getAllItems();
        boolean updated = false;

        for (BucketItem item : items) {
            if (item.getTitle().equalsIgnoreCase(oldTitle)) {
                item.setTitle(newTitle);
                updated = true;
                break;
            }
        }

        if (updated) {
            writeAllItems(items);
            System.out.println("Item updated successfully.");
        } else {
            System.out.println("Item not found.");
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
        List<BucketItem> items = getAllItems();
        for (BucketItem item : items) {
            if (item.getTitle().equalsIgnoreCase(title)) {
                item.setDone(true);
                break;
            }
        }
        writeAllItems(items);
    }

    @Override
    public List<BucketItem> getAllItems() {
        List<BucketItem> items = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("|");

                if (parts.length >= 6) {
                    int id = 0;
                    String title = parts[0];
                    boolean isDone = Boolean.parseBoolean(parts[1]);
                    String description = parts[2];
                    LocalDate targetDate = parts[3].isEmpty() ? null : LocalDate.parse(parts[3]);
                    String category = parts[4];
                    int priority = Integer.parseInt(parts[5]);

                    BucketItem item = new BucketItem(id, title, description, targetDate, category, priority);
                    item.setDone(isDone);
                    items.add(item);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading items: " + e.getMessage());
        }
        return items;
    }

    private void writeAllItems(List<BucketItem> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (BucketItem item : items) {
                writer.write(String.join("|",
                    item.getTitle(),
                    String.valueOf(item.isDone()),
                    item.getDescription(),
                    item.getTargetDate() != null ? item.getTargetDate().toString() : "",
                    item.getCategory(),
                    String.valueOf(item.getPriority())
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing updated list: " + e.getMessage());
        }
    }
}