package com.team4.service;


import com.team4.dao.AccountDAO;
import com.team4.dao.TransactionDAO;
import com.team4.dao.UserDAO;
import com.team4.model.account.Account;
import com.team4.model.account.AccountStatus;
import com.team4.model.account.AccountType;
import com.team4.model.transaction.Transaction;
import com.team4.model.user.User;
import com.team4.model.user.UserRole;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class ManagerService implements  ManagerServiceInterface{

    private final UserDAO userDAO;
    private final AccountDAO accountDAO; // reuses commit/rollback logics
    private final TransactionDAO transactionDAO;
    private final Connection connection;
    private final AccountService accountService;

    public ManagerService(AccountDAO accountDAO, TransactionDAO transactionDAO,
                          AccountService accountService, UserDAO userDAO,
                          Connection connection) {
        this.accountDAO     = accountDAO;
        this.transactionDAO = transactionDAO;
        this.accountService = accountService;
        this.userDAO        = userDAO;
        this.connection     = connection;
    }

    // helpers

    private Account getAccountOrThrow(String accountNumber){
        try {
            Account account = accountDAO.findAccountByNumber(accountNumber);
            if (account == null)
                throw new IllegalArgumentException("Account not found: " + accountNumber);
            return account;
        } catch (Exception e) {
            throw new RuntimeException("Database error while fetching account.",e);
        }
    }

    private User getCustomerOrThrow(int userId) {
        try {
            User user = userDAO.findUserById(userId);
            if (user == null)
                throw new IllegalArgumentException("User not found: " + userId);
            if (user.getRole() != UserRole.CUSTOMER)
                throw new IllegalArgumentException("User " + userId + " is not a customer.");
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching user.", e);
        }
    }

    /**
     * Only ACTIVE accounts can have money moved in/out.
     * BLOCKED: manager froze it — no operations until reopened.
     * CLOSED:  permanently shut — no operations ever.
     */
    private void checkAccountOperable(Account account) {
        switch (account.getAccountStatus()) {
            case BLOCKED:
                throw new IllegalStateException(
                        "Account " + account.getAccountNumber() + " is BLOCKED. Unblock it before performing transactions."
                );
            case CLOSED:
                throw new IllegalStateException(
                        "Account " + account.getAccountNumber() + " is CLOSED and cannot be used."
                );
            default:
                // do nothing (account is operable)
                break;
        }
    }
    private void updateStatusWithTransaction(String accountNumber, AccountStatus newStatus) {
        try {
            connection.setAutoCommit(false);
            accountDAO.updateStatus(accountNumber, newStatus);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            throw new RuntimeException("Failed to update account status.", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    private void saveTransaction(Transaction transaction) {
        try {
            transaction.markCompleted();
            transactionDAO.saveTransaction(transaction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save transaction record.", e);
        }
    }

    // === Manage Customer Accounts ===

    // open a BLOCKED or RESTRICTED(will implement in the future) account
    @Override
    public void openAccount(String accountNumber) {
        Account account = getAccountOrThrow(accountNumber);

        if (account.getAccountStatus() == AccountStatus.ACTIVE)
            throw new IllegalStateException("Account is already ACTIVE.");
//        if (account.getAccountStatus() == AccountStatus.CLOSED)
//            throw new IllegalStateException("Cannot reopen a CLOSED account.");

        updateStatusWithTransaction(accountNumber, AccountStatus.ACTIVE);
        System.out.println("Account " + accountNumber + " has been ACTIVATED.");
    }

    @Override
    public void closeAccount(String accountNumber) {
        Account account = getAccountOrThrow(accountNumber);

        if (account.getAccountStatus() == AccountStatus.CLOSED)
            throw new IllegalStateException("Account is already CLOSED.");
        if (account.getBalance() > 0)
            throw new IllegalStateException("Cannot close account with remaining balance of $" + account.getBalance() + ". Please withdraw or transfer funds first.");

        updateStatusWithTransaction(accountNumber, AccountStatus.CLOSED);
        System.out.println("Account " + accountNumber + " has been CLOSED.");
    }

    @Override
    public void blockAccount(String accountNumber) {
        Account account = getAccountOrThrow(accountNumber);

        if (account.getAccountStatus() == AccountStatus.BLOCKED)
            throw new IllegalStateException("Account is already BLOCKED.");
        if (account.getAccountStatus() == AccountStatus.CLOSED)
            throw new IllegalStateException("Cannot block a CLOSED account.");

        updateStatusWithTransaction(accountNumber, AccountStatus.BLOCKED);
        System.out.println("Account " + accountNumber + " has been BLOCKED.");
    }

    // === View Customer Accounts ===

    @Override
    public Account viewAccount(String accountNumber) {
        return getAccountOrThrow(accountNumber);
    }

    @Override
    public List<Account> viewAccountsByUserId(int userId) {
        getCustomerOrThrow(userId); // verify user exists
        try {
            return accountDAO.findAccountsByUserId(userId);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve accounts for user " + userId, e);
        }
    }

    @Override
    public List<Account> viewAllAccounts() {
        try {
            return accountDAO.findAllAccounts();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve accounts.", e);
        }
    }

    @Override
    public List<Transaction> viewAccountTransactions(String accountNumber) {
        getAccountOrThrow(accountNumber); // verify account exists
        return transactionDAO.findTransactionsByAccountNumber(accountNumber);
    }

    // === View Customer Information ===

    @Override
    public User viewCustomer(int userId) {
        return getCustomerOrThrow(userId);
    }

    @Override
    public User viewCustomerByUsername(String username) {
        try {
            User user = userDAO.findUserByUsername(username);
            if (user == null)
                throw new IllegalArgumentException("User not found: " + username);
            if (user.getRole() != UserRole.CUSTOMER)
                throw new IllegalArgumentException(username + " is not a customer.");
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Database error while fetching user.", e);
        }
    }

    @Override
    public List<User> viewAllCustomers() {
        try {
            return userDAO.findAllUsers().stream()
                    .filter(u -> u.getRole() == UserRole.CUSTOMER)
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new RuntimeException("Failed to retrieve customers.", e);
        }
    }

    //==== Manage Customer information ===


    @Override
    public void updateCustomerPhone(int userId, String newPhone) {
        getCustomerOrThrow(userId);
        try {
            connection.setAutoCommit(false);
            userDAO.updateUserPhone(userId, newPhone);
            connection.commit();
            System.out.println("Phone updated for user " + userId);
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Failed to update phone.", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void updateCustomerFullName(int userId, String newFullName) {
        getCustomerOrThrow(userId);
        try {
            connection.setAutoCommit(false);
            userDAO.updateUserFullName(userId, newFullName);
            connection.commit();
            System.out.println("Full name updated for user " + userId);
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Failed to update full name.", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void deleteCustomer(int userId) {
        getCustomerOrThrow(userId);
        try {
            List<Account> accounts = accountDAO.findAccountsByUserId(userId);
            // look for every account of that user that is not closed

            /* Why customer accounts must be CLOSED first before delete that user:
             * Technical reason:
             *  If you delete a user who still has accounts, MySQL will **throw a foreign key violation error** the accounts row references a user that no longer exists.
             * Business reason:
             * You don't want to delete a customer who still has money sitting in an account.
             */

            boolean hasOpenAccounts = accounts.stream()
                    .anyMatch(a -> a.getAccountStatus() != AccountStatus.CLOSED);


            if (hasOpenAccounts)
                throw new IllegalStateException(
                        "Cannot delete customer " + userId + ": one or more accounts are not CLOSED.");

            connection.setAutoCommit(false);
            userDAO.deleteUser(userId);
            connection.commit();
            System.out.println("Customer " + userId + " deleted.");

        } catch (IllegalStateException e) {
            throw e; // re-throw validation errors as-is
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Failed to delete customer.", e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    @Override
    public void createCustomer(String username, String password, String fullName,
                               String phone, String dob) {
        // Pre-validate before hitting the model setters
        // so we can show friendly messages in the GUI

        if (username.isBlank())  throw new IllegalArgumentException("Username is required.");
        if (password.isBlank())  throw new IllegalArgumentException("Password is required.");
        if (fullName.isBlank())  throw new IllegalArgumentException("Full name is required.");
        if (phone.isBlank())     throw new IllegalArgumentException("Phone is required.");
        if (dob.isBlank())       throw new IllegalArgumentException("Date of birth is required.");


        // Phone: setter requires digits , so replace user input that is number to digit
        String digitsOnly = phone.replaceAll("[^0-9]", "");

        try {
            if (userDAO.findUserByUsername(username) != null)
                throw new IllegalArgumentException("Username \"" + username + "\" is already taken.");

            connection.setAutoCommit(false);
            // These will throw IllegalArgumentException if validation fails inside User setters
            userDAO.createUser(username, password, fullName, digitsOnly, dob, UserRole.CUSTOMER);
            connection.commit();

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Failed to create customer: " + e.getMessage(), e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }


    @Override
    public void createAccount(int userId, String holderName, String email,
                              String pin, AccountType type, double initialBalance) {
        getCustomerOrThrow(userId);

        if (holderName.isBlank()) throw new IllegalArgumentException("Holder name is required.");
        if (email.isBlank())      throw new IllegalArgumentException("Email is required.");
        if (pin.isBlank())        throw new IllegalArgumentException("PIN is required.");
        if (type == null)         throw new IllegalArgumentException("Account type is required.");

        String accountNumber = "ACC" + String.format("%07d", userId) + (System.currentTimeMillis() % 1000);
        // ACC + 4-digit userId + 4-digit timestamp suffix = ACC12341234 = 11 chars
        accountNumber = "ACC" + String.format("%04d", userId % 10000)
                + String.format("%04d", System.currentTimeMillis() % 10000);

        try {
            connection.setAutoCommit(false);
            // Account setters will validate pin (4 digits), email, holderName, balance
            accountDAO.createAccount(accountNumber, userId, holderName, email, pin, type, initialBalance);
            connection.commit();

        } catch (IllegalArgumentException e) {
            throw e;
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            throw new RuntimeException("Failed to create account: " + e.getMessage(), e);
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    @Override
    public void managerDeposit(String accountNumber, double amount, String managerUsername) {

    }

    @Override
    public void managerWithdraw(String accountNumber, double amount, String managerUsername) {

    }

    @Override
    public void managerTransfer(String fromAccount, String toAccount, double amount, String managerUsername) {

    }


}

