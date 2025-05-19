package com.pluralsight;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BucketItemManager manager;

        System.out.println("🎯 Welcome to your Bucket List App!");
        System.out.println("Choose storage option:");
        System.out.println("1. File-based storage");
        System.out.println("2. Azure SQL Database");
        System.out.print("Enter choice (1 or 2): ");
        String storageChoice = scanner.nextLine();

        if (storageChoice.equals("1")) {
            manager = new FileBucketManager("bucketlist.txt");
        } else if (storageChoice.equals("2")) {
            manager = new DatabaseManager();
        } else {
            System.out.println("⚠️ Invalid choice. Exiting...");
            return;
        }

        while (true) {
            System.out.println("\n📋 Menu:");
            System.out.println("1. View all items");
            System.out.println("2. Add a new item");
            System.out.println("3. Mark item as done");
            System.out.println("4. Remove item");
            System.out.println("5. Update item title");
            System.out.println("6. Filter by category");
            System.out.println("7. Sort by priority");
            System.out.println("8. Exit");
            System.out.print("\nChoose an option (1–8): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showAllItems(manager);
                case "2" -> addItem(manager, scanner);
                case "3" -> markItemAsDone(manager, scanner);
                case "4" -> removeItem(manager, scanner);
                case "5" -> updateItem(manager, scanner);
                case "6" -> filterByCategory(manager, scanner);
                case "7" -> sortByPriority(manager);
                case "8" -> {
                    System.out.println("👋 Goodbye and good luck with your dreams!");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice. Please enter a number between 1 and 8.");
            }
        }
    }

    private static void showAllItems(BucketItemManager manager) {
        List<BucketItem> items = manager.getAllItems();
        if (items.isEmpty()) {
            System.out.println("📝 Your bucket list is empty. Add something!");
        } else {
            System.out.println("\n✨ Your Bucket List:");
            for (BucketItem item : items) {
                System.out.println("- " + item);
            }
        }
    }

    private static void addItem(BucketItemManager manager, Scanner scanner) {
        System.out.print("Enter item title: ");
        String title = scanner.nextLine();

        System.out.print("Enter a short description: ");
        String description = scanner.nextLine();

        System.out.print("Enter target date (yyyy-MM-dd): ");
        LocalDate targetDate = LocalDate.parse(scanner.nextLine());

        System.out.print("Enter category: ");
        String category = scanner.nextLine();

        System.out.print("Enter priority (1=High, 5=Low): ");
        int priority = Integer.parseInt(scanner.nextLine());

        System.out.print("Any notes? ");
        String notes = scanner.nextLine();

        System.out.print("Optional image URL: ");
        String imageUrl = scanner.nextLine();

        BucketItem item = new BucketItem(0, title, description, targetDate, category, priority);
        item.setNotes(notes);
        item.setImageUrl(imageUrl);

        manager.addItem(item);
        System.out.println("✅ Item added!");
    }

    private static void markItemAsDone(BucketItemManager manager, Scanner scanner) {
        System.out.print("Enter the title of the item to mark as done: ");
        String title = scanner.nextLine();
        manager.markItemAsDone(title);
        System.out.println("✅ Item marked as done!");
    }

    private static void removeItem(BucketItemManager manager, Scanner scanner) {
        System.out.print("Enter the title of the item to remove: ");
        String title = scanner.nextLine();
        manager.removeItem(title);
        System.out.println("🗑️ Item removed!");
    }

    private static void updateItem(BucketItemManager manager, Scanner scanner) {
        System.out.print("Enter the current title of the item: ");
        String oldTitle = scanner.nextLine();

        System.out.print("Enter the new title: ");
        String newTitle = scanner.nextLine();

        if (manager instanceof FileBucketManager fileManager) {
            fileManager.updateItem(oldTitle, newTitle);
        } else if (manager instanceof DatabaseManager dbManager) {
            dbManager.updateItem(oldTitle, newTitle);
        } else {
            System.out.println("⚠️ Update not supported in this manager.");
        }
    }

    private static void filterByCategory(BucketItemManager manager, Scanner scanner) {
        System.out.print("Enter category to filter by: ");
        String category = scanner.nextLine().toLowerCase();
        List<BucketItem> items = manager.getAllItems();
        items.stream()
            .filter(item -> item.getCategory() != null && item.getCategory().toLowerCase().contains(category))
            .forEach(item -> System.out.println("- " + item));
    }

    private static void sortByPriority(BucketItemManager manager) {
        List<BucketItem> items = manager.getAllItems();
        items.stream()
            .sorted((a, b) -> Integer.compare(a.getPriority(), b.getPriority()))
            .forEach(item -> System.out.println("- " + item));
    }
}