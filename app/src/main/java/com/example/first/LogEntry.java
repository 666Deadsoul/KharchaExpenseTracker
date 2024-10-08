package com.example.first;

public class LogEntry {
    private int id; // This will hold the auto-generated ID
    private String category;
    private String item;
    private double amount;
    private String date;

    // Constructor
    public LogEntry(int id, String category, String item, double amount, String date) {
        this.id = id; // Set the ID
        this.category = category;
        this.item = item;
        this.amount = amount;
        this.date = date;
    }

    // Getters
    public int getId() {
        return id; // Getter for the ID
    }

    public String getCategory() {
        return category;
    }

    public String getItem() {
        return item;
    }

    public double getAmount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
