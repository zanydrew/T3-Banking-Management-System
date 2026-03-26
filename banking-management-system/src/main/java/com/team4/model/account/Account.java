package com.team4.model.account;

import com.team4.service.AccountServiceInterface;

import java.util.regex.Pattern;

public abstract class Account implements AccountServiceInterface {
    private String accountNumber;
    private int userId;
    private String customerId;
    private String holderName;
    private String email;
    private String accountPin;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private double balance;

//    public abstract void updatedBalance(String accountNumber, double newBalance);

    // ==== Constructor ====
    public Account(String accountNumber,int userId, String holderName, String email, String accountPin,
            AccountStatus accountStatus,
            double balance) {
        setAccountNumber(accountNumber);
        this.userId = userId;
        setHolderName(holderName);
        setEmail(email);
        setAccountPin(accountPin);
        // setAccountType(accountType);
        setAccountStatus(accountStatus);
        setBalance(balance);
    }

    // === Getters ===
    public String getAccountNumber() {
        return accountNumber;
    }
    public int getUserId() {
        return userId;
    }

    public String getHolderName() {
        return holderName;
    }

    public String getEmail() {
        return email;
    }

    public String getAccountPin() {
        return accountPin;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public double getBalance() {
        return balance;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    // === Setters ===
    protected void setAccountStatus(AccountStatus accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    protected void setAccountNumber(String accountNumber) {
        if (accountNumber.isEmpty() || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account must have account number.");
        } else if (accountNumber.length() > 12) {
            throw new IllegalArgumentException("Invalid account number");
        }
        this.accountNumber = accountNumber.trim();
    }

    // did not take full name because 1 account could have multiple holders
    protected void setHolderName(String holderName) {
        if (holderName.isEmpty() || holderName.isBlank()) {
            throw new IllegalArgumentException("Full name must be filled.");
        }
        this.holderName = holderName.trim();
    }

    protected void setEmail(String email) {
        if (email.isEmpty() || email.isBlank()) {
            this.email = "No Email";
        } else if (!(isValidEmail(email))) {
            throw new IllegalArgumentException(
                    "Email must be valid");
        }
        this.email = email.trim();
    }

    protected void setAccountPin(String accountPin) {
        if (accountPin.isEmpty() || accountPin.isBlank()) {
            throw new IllegalArgumentException("Pin must be filled.");
        } else if (!isDigits(accountPin) || accountPin.length() != 4) {
            throw new IllegalArgumentException("Pin must have 4 Numbers");
        }
        this.accountPin = accountPin;
    }

    protected void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    protected void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance must be >= 0.");
        }
        this.balance = balance;
    }

    // === Helpers ===

    public boolean isValidEmail(String e) {
        final String email_regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"
                + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(email_regex);

        if (e == null || !(pattern.matcher(e).matches())) {
            return false;
        }
        return true;
    }

    public boolean isDigits(String s) {
        if (s.isBlank())
            return false;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c < '0' || c > '9')
                return false;
        }
        return true;
    }
}
