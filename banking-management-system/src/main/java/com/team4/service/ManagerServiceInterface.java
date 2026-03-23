package com.team4.service;

import com.team4.model.account.Account;
import com.team4.model.transaction.Transaction;
import com.team4.model.user.User;

import java.util.List;

public interface ManagerServiceInterface {

    /** Manager Service are:
     * --- Manage Customer Account ---
     * Open Customer account
     * Close Customer account
     * Block Customer account
     *
     * --- View Customer Account ---
     *
     * ++ For implement : action(can do)
     * Manager can ...(eg. open, close, block customer account, control customers' accounts actions like deposit, withdraw, transfer, loan)
     * Customer can...
     * when customers' accounts got blocked/closed/open, they can/cannot...
     **/

    void blockAccount(String accountNumber);

    void openAccount(String accountNumber);

    void closeAccount(String accountNumber);

    // View Customer Accounts
    Account viewAccount(String accountNumber);
    List<Account> viewAllAccounts();
    List<Transaction> viewAccountTransactions(String accountNumber);

    // --- View & Manage Customer Information ---
    User viewCustomer(int userId);
    User viewCustomerByUsername(String username);
    List<User> viewAllCustomers(); // excludes managers
    List<Account> viewAccountsByUserId(int userId); // all accounts of one user
    void updateCustomerPhone(int userId, String newPhone);
    void updateCustomerFullName(int userId, String newFullName);
    void deleteCustomer(int userId);

    // --- Control Customer Actions ---
    void managerDeposit(String accountNumber, double amount, String managerUsername);
    void managerWithdraw(String accountNumber, double amount, String managerUsername);
    void managerTransfer(String fromAccount, String toAccount, double amount, String managerUsername);
}
