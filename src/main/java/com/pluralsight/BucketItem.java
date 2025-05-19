package com.pluralsight;


    public class BucketItem {
        private String title;
        private String description;
        private boolean isDone;

        public BucketItem(String title) {
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


        public void setTitle(String title) {
            title = title;
        }
        public void setDone(boolean done) {
            isDone = done;
        }

        @Override
        public String toString() {
            return (isDone ? "[X] " : "[ ] ") + title;
        }
    }

