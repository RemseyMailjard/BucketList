package com.pluralsight;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BucketItem {
    private int id;
    private String title;
    private String description;
    private boolean isDone;
    private LocalDate createdDate;
    private LocalDate targetDate;
    private LocalDate completedDate;
    private String category;
    private int priority;
    private List<String> sharedWithEmails;
    private String imageUrl;
    private String notes;

    public BucketItem(int id, String title, String description, LocalDate targetDate,
                      String category, int priority) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.targetDate = targetDate;
        this.category = category;
        this.priority = priority;
        this.isDone = false;
        this.createdDate = LocalDate.now();
        this.sharedWithEmails = new ArrayList<>();
    }

    public int getId() {
        return id;
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

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public LocalDate getTargetDate() {
        return targetDate;
    }

    public LocalDate getCompletedDate() {
        return completedDate;
    }

    public String getCategory() {
        return category;
    }

    public int getPriority() {
        return priority;
    }

    public List<String> getSharedWithEmails() {
        return sharedWithEmails;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNotes() {
        return notes;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDone(boolean done) {
        this.isDone = done;
        if (done) {
            this.completedDate = LocalDate.now();
        } else {
            this.completedDate = null;
        }
    }

    public void setTargetDate(LocalDate targetDate) {
        this.targetDate = targetDate;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setSharedWithEmails(List<String> sharedWithEmails) {
        this.sharedWithEmails = sharedWithEmails;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return (isDone ? "[X] " : "[ ] ") + title + " (" + category + ") - " + description +
                " [Due: " + (targetDate != null ? targetDate : "N/A") + "]";
    }
}

