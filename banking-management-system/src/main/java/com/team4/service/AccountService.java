package com.team4.service;

import com.team4.model.account.Account;

public class AccountService implements AccountServiceInterface {
    Account account;

    @Override
    public Account findAccountByNumber(String accountNumber) {

        return null;
    }

    @Override
    public void deposit(Account accountNumber, double amount) {

    }

    @Override
    public void transfer(Account sender, Account reciever, double amount) {

    }

    @Override
    public void withdraw(Account accountNumber, double amount) {

    }

}
