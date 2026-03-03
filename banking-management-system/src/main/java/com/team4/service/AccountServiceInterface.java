package com.team4.service;

import com.team4.model.account.Account;

public interface AccountServiceInterface {
    void withdraw(String accountNumber, double amount);

    void transfer(String sender, Account reciever, double amount);

    void deposit(String accountNumber, double amount);

    Account findAccountByNumber(String accountNumber);

    void updatedBalance(String accountNumber, double nnewBalance);

}
