package com.team4.service;

import com.team4.dao.AccountDAO;
import com.team4.model.account.Account;

import java.sql.Connection;
import java.sql.SQLException;

public class AccountService implements AccountServiceInterface {

    private final AccountDAO accountDAO;
    private final Connection connection;

    public AccountService(AccountDAO accountDAO, Connection connection) {
        this.accountDAO = accountDAO;
        this.connection =  connection ;
    }


    @Override
    public void deposit(String accountNumber, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Cannot deposit amount that <= 0. Amount must be greater than 0.");
        }

        try {

            connection.setAutoCommit(false);
            Account account = accountDAO.findAccountByNumber(accountNumber);

            if (account == null) {
                throw new IllegalArgumentException("Account not found.");
            }
            double newBalance = account.getBalance() + amount;
            accountDAO.updateBalance(accountNumber, newBalance);

        } catch (Exception e) {
            // if there's any fail, undo change
            rollback();
            throw new RuntimeException("Deposit failed: " + e.getMessage(),e);
        } finally {
            resetAutoCommit();
        }

    }

    @Override
    public void withdraw(String accountNumber, double amount) {

        if (amount <= 0) {
            throw new IllegalArgumentException("Cannot withdraw amount that <= 0. Amount must be greater than 0.");
        }

        try {

            connection.setAutoCommit(false);
            Account account = accountDAO.findAccountByNumber(accountNumber);

            if (account == null) {
                throw new IllegalArgumentException("Account not found.");
            }
            if(account.getBalance() < amount) {
                throw new IllegalArgumentException("Cannot withdraw amount that below the balance.");
            }
            double newBalance = account.getBalance() + amount;
            accountDAO.updateBalance(accountNumber, newBalance);
            connection.commit();

        } catch (Exception e) {
            // if there's any fail, undo change
            rollback();
            throw new RuntimeException("Withdrawal failed: " + e.getMessage(),e);
        } finally {
            resetAutoCommit();
        }

    }

    @Override
    public void transfer(String fromAccountNumber, String toAccountNumber, double amount) {
//        withdraw(fromAccount, amount);
//        deposit(toAccount, amount);

        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be greater than 0.");

        try {
            connection.setAutoCommit(false);

            Account from = accountDAO.findAccountByNumber(fromAccountNumber);
            Account to   = accountDAO.findAccountByNumber(toAccountNumber);

            if (from == null) throw new IllegalArgumentException("Source account not found.");
            if (to == null)   throw new IllegalArgumentException("Target account not found.");
            if (from.getBalance() < amount) throw new IllegalArgumentException("Insufficient balance.");

            // Both updates in the same transaction, either both commit or both rollback
            accountDAO.updateBalance(fromAccountNumber, from.getBalance() - amount);
            accountDAO.updateBalance(toAccountNumber,   to.getBalance()   + amount);
            connection.commit();

        } catch (Exception e) {
            rollback();
            throw new RuntimeException("Transfer failed: " + e.getMessage(), e);
        } finally {
            resetAutoCommit();
        }
    }

    private void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetAutoCommit() {
        try {
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


// Account findAccountByNumber(String accountNumber) {
    // if (!accountNumber.equals(account.getAccountNumber())) {
    // System.out.println("Cannot find this account number.");
    // }
    // return account;
    // }
    // Account findAccountByNumber(String accountNumber) {
    // if (!account.findAccountByNumber(accountNumber)) {
    // System.out.println("Cannot find this account number.");
    // }
    // return account.findAccountNumber();

    // if (!accountNumber.equals(account.getAccountNumber())) {
    // System.out.println("Cannot find this account number.");
    // }
    // return account;
    // }

//    @Override
//    public Account getAccountByNumber(String accountNumber) {
//        return accountDAO.findAccountByNumber(accountNumber);
//    }

}
