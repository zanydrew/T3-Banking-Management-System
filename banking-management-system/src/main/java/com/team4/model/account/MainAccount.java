package com.team4.model.account;

// import com.team4.model.AccountType;
public class MainAccount extends Account {

    public MainAccount(String accountNumber, int userId, String holderName, String email, String accountPin, AccountType accountType,
                       AccountStatus accountStatus, double balance) {
        super(accountNumber, userId, holderName, email, accountPin, AccountType.MAIN, accountStatus, balance);
    }

    // Used for DB (trusted data, no validation)
    public MainAccount(String accountNumber, int userId, String holderName, String email, String accountPin, AccountType accountType,
                       AccountStatus accountStatus, double balance, boolean trusted) {
        super(accountNumber, userId, holderName, email, accountPin, AccountType.MAIN, accountStatus, balance, trusted);
    }

    @Override
    public String toString() {
        return "User ID: " + getUserId() + "\nAccount Number: " + getAccountNumber() + " \nHolder Name: " + getHolderName() + "\nAccount Type: "
                + getAccountType() + "\nBalance: $"
                + getBalance() + "\nEmail: " + getEmail() + "\nAccount status: "
                + getAccountStatus();
    }
}
