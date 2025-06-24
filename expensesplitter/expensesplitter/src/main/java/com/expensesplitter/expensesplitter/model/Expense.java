package com.expensesplitter.expensesplitter.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "expenses")
public class Expense {
    @Id
    private String id;
    private double amount;
    private String paidBy; // Store user ID as string

    public Expense() {} // No-arg constructor required by JPA

    public Expense(String id, double amount, String paidBy) {
        this.id = id;
        this.amount = amount;
        this.paidBy = paidBy;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getPaidBy() { return paidBy; }
    public void setPaidBy(String paidBy) { this.paidBy = paidBy; }
}
