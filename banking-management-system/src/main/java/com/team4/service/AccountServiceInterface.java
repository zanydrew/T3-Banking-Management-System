package com.team4.service;

public interface AccountServiceInterface {

    /** Account Service are:
     * Deposit
     * Withdraw
     * Transfer
     * Loan (implement later)
     * --- View Account information ---
     **/

    void withdraw(String accountNumber, double amount);

    void transfer(String fromAccountNumber, String toAccountNumber, double amount);

    void deposit(String accountNumber, double amount);

//    Account getAccountByNumber(String accountNumber);

}
