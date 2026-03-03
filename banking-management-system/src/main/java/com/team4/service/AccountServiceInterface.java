package com.team4.service;

import com.team4.model.account.Account;

public interface AccountServiceInterface {
    void withdraw(Account accountNumber, double amount);

    void transfer(Account sender, Account reciever, double amount);

    void deposit(Account accountNumber, double amount);

    Account findAccountByNumber(String accountNumber);

}
