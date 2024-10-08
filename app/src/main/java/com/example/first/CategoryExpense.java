package com.example.first;

public class CategoryExpense {
    private String category;
    private double totalAmount;

    public CategoryExpense(String category, double totalAmount) {
        this.category = category;
        this.totalAmount = totalAmount;
    }

    public String getCategory() {
        return category;
    }

    public double getTotal() {
        return totalAmount;
    }
}

