package com.team4.model;

public class Account {
    private int accountID;
    private String accountHolderName;
    private double balance;

    public Account(int acccountID, String accountHolderName, double balance) {
        setAcountID(acccountID);
        setAccountHolderName(accountHolderName);
        setBalance(balance);
    }

    // setter
    public void setAcountID(int accountID) {
        if (accountID <= 0) {
            throw new IllegalArgumentException();
        }
        this.accountID = accountID;
    }

    public void setAccountHolderName(String accountHolderName) {
        if (accountHolderName.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.accountHolderName = accountHolderName;
    }

    public void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException();
        }
        this.balance = balance;
    }

    // Getter
    public int getAccountID() {
        return accountID;
    }

    public String getAccountHoldername() {
        return accountHolderName;
    }

    public double getBalance() {
        return balance;
    }

    @Override
    public String toString() {
        return "Account ID: " + accountID + " | Name: " + accountHolderName + " | Balance: $" + balance;
    }

}
