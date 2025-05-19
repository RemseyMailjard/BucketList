package com.pluralsight;

public class BucketItem {
    private String title;
    private String description;
    private boolean isDone;

    public BucketItem(String title) {
        this.title = title;
        this.isDone = false;
        this.description = ""; // optional default
    }

    public BucketItem(String title, String description) {
        this.title = title;
        this.description = description;
        this.isDone = false;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return isDone;
    }

    public String getDescription() {
        return description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(boolean done) {
        isDone = done;
    }

    @Override
    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + title + (description.isEmpty() ? "" : " - " + description);
    }
}
