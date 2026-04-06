package com.team4.model.account;

import java.util.regex.Pattern;

/**
 * Abstract base class for all account types.
 *
 * Design decisions:
 * - ONE constructor that always validates.
 * - Another constructor for loading trusted data in the database (no validation needed)
 * - All validation logic lives HERE.
 * - Subclasses (SavingsAccount, MainAccount) set their own AccountType in their constructor. Account base doesn't need to know the type.
 */

public abstract class Account{
    private String accountNumber;
    private int userId;
    private String customerId;
    private String holderName;
    private String email;
    private String accountPin;
    private AccountType accountType;
    private AccountStatus accountStatus;
    private double balance;

    // ==== Constructor ====
    public Account(String accountNumber,int userId, String holderName, String email, String accountPin,AccountType accountType,
            AccountStatus accountStatus,
            double balance) {
        setAccountNumber(accountNumber);
        this.userId = userId;
        setHolderName(holderName);
        setEmail(email);
        setAccountPin(accountPin);
         setAccountType(accountType);
        setAccountStatus(accountStatus);
        setBalance(balance);
    }

    /**
     * Another constructor for load Account from trusted DB data, no validation.
     * Use ONLY inside DAO(Data Access Object) implementations.
     */
    protected Account(String accountNumber, int userId, String holderName,
                      String email, String accountPin, AccountType accountType,
                      AccountStatus accountStatus, double balance, boolean trusted){
        this.accountNumber = accountNumber;
        this.userId = userId;
        this.holderName = holderName;
        this.email = email;
        this.accountPin = accountPin;
        this.accountType = accountType;
        this.accountStatus = accountStatus;
        this.balance = balance;


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
        if (userId <= 0) throw new IllegalArgumentException("User ID must be positive.");
        this.userId = userId;
    }

    protected void setAccountNumber(String accountNumber) {
        if (accountNumber.isEmpty() || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account must have account number.");
        } else if (accountNumber.length() > 20) {
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
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email must be filled.");
        }
        String trimmed = email.trim();
        if (!EMAIL_PATTERN.matcher(trimmed).matches())
            throw new IllegalArgumentException("Email must be a valid email address.");
        this.email = trimmed;
    }

    protected void setAccountPin(String accountPin) {
        if (accountPin.isEmpty() || accountPin.isBlank()) {
            throw new IllegalArgumentException("Pin must be filled.");
        }
        String pin = accountPin.trim();
        if (!isDigits(pin) || pin.length() != 4)
            throw new IllegalArgumentException("Account PIN must be exactly 4 digits.");
        this.accountPin = pin;
    }

    protected void setAccountType(AccountType accountType) {
        if (accountType == null) throw new IllegalArgumentException("Account type is required.");
        this.accountType = accountType;
    }

    protected void setBalance(double balance) {
        if (balance < 0) {
            throw new IllegalArgumentException("Balance must be >= 0.");
        }
        this.balance = balance;
    }

    // === Helpers ===

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");


//    public boolean isValidEmail(String e) {
//        final String email_regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"
//                + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//        Pattern pattern = Pattern.compile(email_regex);
//
//        if (e == null || !(pattern.matcher(e).matches())) {
//            return false;
//        }
//        return true;
//    }

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
