package com.team4.service;

import com.team4.model.account.Account;
import com.team4.model.user.Customer;

public interface AccountServiceInterface {
    void withdraw(Account accountNumber, double amount);

    void transfer(Account sender, Account reciever, double amount);

    void deposit(Account accountNumber, double amount);

    void loan(Customer loaner, double amount);

    boolean can(TransactionType accountTransactionType);
}
