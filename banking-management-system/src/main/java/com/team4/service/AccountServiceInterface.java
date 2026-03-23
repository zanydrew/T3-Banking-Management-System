package com.team4.service;

public interface AccountServiceInterface {
    void withdraw(String accountNumber, double amount);

    void transfer(String fromAccountNumber, String toAccountNumber, double amount);

    void deposit(String accountNumber, double amount);

//    Account findAccountByNumber(String accountNumber);

}
