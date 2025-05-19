package com.pluralsight;

import java.util.List;

public class DatabaseManager implements BucketItemManager {
    @Override
    public void addItem(BucketItem item) {
      //SQL Server + Some code to add the databse
    }

    @Override
    public void removeItem(String title) {

    }

    @Override
    public void updateItem(String title) {

    }

    @Override
    public void markAsDone(String title) {

    }

    @Override
    public List<BucketItem> getAllItems() {
        return List.of();
    }
}
