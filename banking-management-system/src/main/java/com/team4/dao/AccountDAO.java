package com.team4.dao;


import com.team4.model.account.Account;
import com.team4.model.account.AccountStatus;
import com.team4.model.account.AccountType;

import java.sql.SQLException;
import java.util.List;

public interface AccountDAO {
    // find account by account number
    // SELECT * from accounts WHERE account_number = ?
    Account findAccountByNumber(String accountNumber);
    List<Account> findAccountsByUserId(int userId) throws SQLException;
    List<Account> findAllAccounts() throws SQLException;
    // update balance
    /*
     * UPDATE accounts
     * SET balance = ?
     * WHERE account_number = ?
     */
    Account updateBalance(String accountNumber, double newBalance);

    // update status
    /*
     * UPDATE accounts
     * SET status = ?
     * WHERE account_number = ?
     */

    void updateStatus(String accountNumber, AccountStatus accountStatus);

    // create account
    /*
     * INSERT INTO accounts(account_number, user_id, balance, status)
     * VALUES(?,?,?,?)
     * */
//    void createAccount(Account account);
    void createAccount(String accountNumber, int userId, String holderName,
                       String email, String pin, AccountType type,
                       double balance) throws SQLException;

}
