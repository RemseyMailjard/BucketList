package com.pluralsight;

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
            manager = new DatabaseManager(); // now uses hardcoded connection string for bucket_user
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
            System.out.println("6. Exit");
            System.out.print("\nChoose an option (1–6): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> showAllItems(manager);
                case "2" -> addItem(manager, scanner);
                case "3" -> markItemAsDone(manager, scanner);
                case "4" -> removeItem(manager, scanner);
                case "5" -> updateItem(manager, scanner);
                case "6" -> {
                    System.out.println("👋 Goodbye and good luck with your dreams!");
                    return;
                }
                default -> System.out.println("⚠️ Invalid choice. Please enter a number between 1 and 6.");
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

        BucketItem item = new BucketItem(title, description);
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
}
