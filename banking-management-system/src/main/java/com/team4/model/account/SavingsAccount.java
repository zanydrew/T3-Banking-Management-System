package com.team4.model;

public class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, String holderName, String email, String accountPin,
            AccountType accountType, AccountStatus accountStatus, double balance) {
        super(accountNumber, holderName, email, accountPin, AccountType.SAVINGS, accountStatus, balance);
    }

    @Override
    public String toString() {
        return "Account Number: " + getAccountNumber() + " \nHolder Name: " + getHolderName() + "\nBalance: $"
                + getBalance() + "\nEmail: " + getEmail() + "\nAccount Type: " + getAccountType() + "\nAccount status: "
                + getAccountStatus();
    }
}
