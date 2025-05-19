package com.pluralsight;

public class Main {
    public static void main(String[] args) {

        FileBucketManager mg = new FileBucketManager("items.txt");
        BucketItem bc = new BucketItem("BungeeJump");
        mg.addItem(bc);


        System.out.println("Hello world!");
    }
}