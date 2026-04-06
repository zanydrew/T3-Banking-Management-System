package com.team4;

import com.team4.dao.AccountDAO;
import com.team4.dao.TransactionDAO;
import com.team4.dao.UserDAO;
import com.team4.dao.impl.MySQLAccountDAO;
import com.team4.dao.impl.MySQLTransactionDAO;
import com.team4.dao.impl.MySQLUserDAO;
import com.team4.model.account.Account;
import com.team4.service.AccountService;
import com.team4.service.ManagerService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        final String URL ="jdbc:mysql://127.0.0.1:8889/banking_management_system";
        final String USER = "root";
        final String PASSWORD = "root";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            // test manager service
            UserDAO userDAO = new MySQLUserDAO(connection);
            AccountDAO accountDAO =  new MySQLAccountDAO(connection); // reuses commit/rollback logics
            TransactionDAO transactionDAO = new MySQLTransactionDAO(connection);
            AccountService accountService = new AccountService(accountDAO, connection, transactionDAO);

            ManagerService service = new ManagerService(accountDAO, transactionDAO, accountService, userDAO, connection);
            List<Account> accounts = service.viewAllAccounts();

            for (Account acc : accounts) {
                System.out.println("---------------");
                System.out.println("Account Number: " + acc.getAccountNumber());
                System.out.println("Balance: " + acc.getBalance());
                System.out.println("Status: " + acc.getAccountStatus());
            }
            connection.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
