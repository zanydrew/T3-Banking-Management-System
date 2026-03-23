package com.team4.model.account;

// import com.team4.model.AccountType;
public class MainAccount extends Account {

    public MainAccount(String accountNumber, int userId, String holderName, String email, String accountPin,
                       AccountStatus accountStatus, double balance) {
        super(accountNumber, userId, holderName, email, accountPin, accountStatus, balance);
    }

//    @Override
//    public void updatedBalance(String accountNumber, double newBalance) {
//
//    }

    @Override
    public void withdraw(String accountNumber, double amount) {

    }

    @Override
    public void transfer(String fromAccount, String toAccount, double amount) {

    }

    @Override
    public void deposit(String accountNumber, double amount) {

    }

//    @Override
//    public Account getAccountByNumber(String accountNumber) {
//        return null;
//    }

    @Override
    public AccountType getAccountType() {
        return AccountType.MAIN;
    }

    @Override
    public String toString() {
        return "User ID: " + getUserId() + "\nAccount Number: " + getAccountNumber() + " \nHolder Name: " + getHolderName() + "\nAccount Type: "
                + getAccountType() + "\nBalance: $"
                + getBalance() + "\nEmail: " + getEmail() + "\nAccount status: "
                + getAccountStatus();
    }
}
