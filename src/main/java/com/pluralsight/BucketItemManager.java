package com.pluralsight;

import java.util.List;

public interface BucketItemManager {
    void addItem(BucketItem item);
    void removeItem(String title);
    void updateItem(String title);
    void markAsDone(String title);
    List<BucketItem> getAllItems();
}
