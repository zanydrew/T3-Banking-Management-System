package com.team4.model;

public class MainAccount extends Account {
    public MainAccount(String accountNumber, String holderName, String email, String accountPin,
            AccountType accountType, AccountStatus accountStatus, double balance) {
        super(accountNumber, holderName, email, accountPin, AccountType.MAIN, accountStatus, balance);
    }

    @Override
    public String toString() {
        return "Account Number: " + getAccountNumber() + " \nHolder Name: " + getHolderName() + "\nBalance: $"
                + getBalance() + "\nEmail: " + getEmail() + "\nAccount Type: " + getAccountType() + "\nAccount status: "
                + getAccountStatus();
    }
}
