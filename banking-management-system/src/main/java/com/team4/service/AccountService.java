package com.team4.service;

import com.team4.model.account.Account;

public class AccountService implements AccountServiceInterface {
    Account account;

    @Override
    public Account findAccountByNumber(String accountNumber) {
        if (!accountNumber.equals(account.getAccountNumber())) {
            throw new IllegalArgumentException("Cannot find this account number.");
        }
        return account;

    }

    @Override
    public void deposit(String accountNumber, double amount) {
        if (findAccountByNumber(accountNumber) == null) {
            throw new IllegalArgumentException("Cannot find this account number.");
        }
        findAccountByNumber(accountNumber);
        if (amount <= 0) {
            throw new IllegalArgumentException("Cannot deposit amount that <= 0. Amount must be greater than 0.");
        }

        double newBalance = account.getBalance() + amount;
        account.updatedBalance(accountNumber, newBalance);

    }

    @Override
    public void transfer(String sender, Account reciever, double amount) {
        // TODO Auto-generated method stub

    }

    @Override
    public void withdraw(String accountNumber, double amount) {
        if (findAccountByNumber(accountNumber) == null) {
            throw new IllegalArgumentException("Cannot find this account number.");
        }
        findAccountByNumber(accountNumber);
        if (amount <= 0) {
            throw new IllegalArgumentException("Cannot withdraw amount that <= 0. Amount must be greater than 0.");
        }

        double newBalance = account.getBalance() - amount;
        account.updatedBalance(accountNumber, newBalance);

    }

}
