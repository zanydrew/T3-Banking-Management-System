package com.team4.model.account;

public class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, int userId, String holderName, String email, String accountPin, AccountType accountType,
                          AccountStatus accountStatus, double balance) {
        super(accountNumber, userId, holderName, email, accountPin, AccountType.SAVINGS, accountStatus, balance);
    }

    // Used for DB (trusted data, no validation)
    public SavingsAccount(String accountNumber, int userId, String holderName, String email, String accountPin, AccountType accountType,
                          AccountStatus accountStatus, double balance, boolean trusted) {
        super(accountNumber, userId, holderName, email, accountPin, AccountType.SAVINGS, accountStatus, balance, trusted);
    }


    @Override
    public String toString() {
        return "User ID: " + getUserId() + "\nAccount Number: " + getAccountNumber() + " \nHolder Name: " + getHolderName() + "\nAccount Type: "
                + getAccountType() + "\nBalance: $"
                + getBalance() + "\nEmail: " + getEmail() + "\nAccount status: "
                + getAccountStatus();
    }
}
