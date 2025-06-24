package com.expensesplitter.expensesplitter.model;

public class Debt {
    private String debtorId;
    private String creditorId;
    private double amount;

    public Debt() {} // No-arg constructor

    public Debt(String debtorId, String creditorId, double amount) {
        this.debtorId = debtorId;
        this.creditorId = creditorId;
        this.amount = amount;
    }

    public String getDebtorId() { return debtorId; }
    public void setDebtorId(String debtorId) { this.debtorId = debtorId; }
    public String getCreditorId() { return creditorId; }
    public void setCreditorId(String creditorId) { this.creditorId = creditorId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
