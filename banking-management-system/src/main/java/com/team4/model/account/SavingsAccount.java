package com.team4.model.account;

public class SavingsAccount extends Account {
    public SavingsAccount(String accountNumber, String holderName, String email, String accountPin,
            AccountStatus accountStatus, double balance) {
        super(accountNumber, holderName, email, accountPin, accountStatus, balance);
    }

    @Override
    public void updatedBalance(String accountNumber, double newBalance) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        return "Account Number: " + getAccountNumber() + " \nHolder Name: " + getHolderName() + "\nAccount Type: "
                + getAccountType() + "\nBalance: $"
                + getBalance() + "\nEmail: " + getEmail() + "\nAccount status: "
                + getAccountStatus();
    }
}
