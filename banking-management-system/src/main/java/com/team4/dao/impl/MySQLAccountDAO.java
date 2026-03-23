package com.team4.dao.impl;

import com.team4.dao.AccountDAO;
import com.team4.model.account.Account;
import com.team4.model.account.AccountStatus;
import com.team4.model.account.MainAccount;
import com.team4.model.account.SavingsAccount;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLAccountDAO implements AccountDAO {

    private final Connection connection;

    public MySQLAccountDAO(Connection connection) {
        this.connection = connection;
    }

    // create mapRow() method for reusable in extract data from the row in DB
//    private Account mapRow(ResultSet rs) throws SQLException {
//        // make sure row found in DB
//        System.out.println("Row found in DB!");
//        // extract data from the row
//
//        int userId = rs.getInt("id");
//        String holderName = rs.getString("holder_name");
//        String email = rs.getString("email");
//        String accountPin = rs.getString("account_pin");
//        double balance = rs.getDouble("balance");
//        String accountStatus = rs.getString("account_status");
//        String accountType = rs.getString("account_type");
//
//        if(accountType.equalsIgnoreCase("SAVINGS")){
//            return new SavingsAccount(accountNumber, userId, holderName, email, accountPin, AccountStatus.valueOf(accountStatus), balance);
//        }
//        else if(accountType.equalsIgnoreCase("MAIN")){
//            return new MainAccount(accountNumber, userId, holderName, email, accountPin, AccountStatus.valueOf(accountStatus), balance);
//        }
////                else if(accountType.equalsIgnoreCase("LOAN")){
////                    return new LoanAccount(accountNumber, holderName, email, accountPin, AccountStatus.valueOf(accountStatus), balance);
////                }
//
    ////                // AccountStatus.valueOf(accountStatus) is to convert string to enum
//    }

    @Override
    public Account findAccountByNumber(String accountNumber) {

        String sql = "SELECT * FROM accounts WHERE account_number = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            // parameterIndex is used to set the value of a ? placeholder in a SQL statement

            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            // move cursor to the next row to check if the data exist in the database

            if (rs.next()) {
                // make sure row found in DB
                System.out.println("Row found in DB!");

                // extract data from the row

                int userId = rs.getInt("id");
                String holderName = rs.getString("holder_name");
                String email = rs.getString("email");
                String accountPin = rs.getString("account_pin");
                double balance = rs.getDouble("balance");
                String accountStatus = rs.getString("account_status");
                String accountType = rs.getString("account_type");

                if(accountType.equalsIgnoreCase("SAVINGS")){
                    return new SavingsAccount(accountNumber, userId, holderName, email, accountPin, AccountStatus.valueOf(accountStatus), balance);
                }
                else if(accountType.equalsIgnoreCase("MAIN")){
                    return new MainAccount(accountNumber, userId, holderName, email, accountPin, AccountStatus.valueOf(accountStatus), balance);
                }
//                else if(accountType.equalsIgnoreCase("LOAN")){
//                    return new LoanAccount(accountNumber, holderName, email, accountPin, AccountStatus.valueOf(accountStatus), balance);
//                }

//                // AccountStatus.valueOf(accountStatus) is to convert string to enum
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Account> findAccountsByUserId(int userId) throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String accountNumber = rs.getString("account_number");
                String holderName    = rs.getString("holder_name");
                String email         = rs.getString("email");
                String accountPin    = rs.getString("account_pin");
                double balance       = rs.getDouble("balance");
                String accountStatus = rs.getString("account_status");
                String accountType   = rs.getString("account_type");

                if (accountType.equalsIgnoreCase("SAVINGS"))
                    accounts.add(new SavingsAccount(accountNumber, userId, holderName, email, accountPin, AccountStatus.valueOf(accountStatus), balance));
                else if (accountType.equalsIgnoreCase("MAIN"))
                    accounts.add(new MainAccount(accountNumber, userId, holderName, email, accountPin, AccountStatus.valueOf(accountStatus), balance));
            }
        }
        return accounts;
    }

    @Override
    public List<Account> findAllAccounts() throws SQLException {
        List<Account> accounts = new ArrayList<>();
        String sql = "SELECT * FROM accounts";

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String accountNumber  = rs.getString("account_number");
                int userId            = rs.getInt("user_id");
                String holderName     = rs.getString("holder_name");
                String email          = rs.getString("email");
                String accountPin     = rs.getString("account_pin");
                double balance        = rs.getDouble("balance");
                String accountStatus  = rs.getString("account_status");
                String accountType    = rs.getString("account_type");

                if (accountType.equalsIgnoreCase("SAVINGS"))
                    accounts.add(new SavingsAccount(accountNumber, userId, holderName, email, accountPin, AccountStatus.valueOf(accountStatus), balance));
                else if (accountType.equalsIgnoreCase("MAIN"))
                    accounts.add(new MainAccount(accountNumber, userId, holderName, email, accountPin, AccountStatus.valueOf(accountStatus), balance));
            }
        }
        return accounts;
    }

    @Override
    public void updateStatus(String accountNumber, AccountStatus accountStatus) {
        String sql = "UPDATE accounts SET account_status = ? WHERE account_number = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, accountStatus.name());
            stmt.setString(2, accountNumber);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createAccount(Account account) {
        String sql = "INSERT INTO accounts(account_number, id, holder_name, email, account_pin, balance, account_status, account_type) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            String accountNumber = "ACC" + System.currentTimeMillis();

            stmt.setString(1, accountNumber);
            stmt.setInt(2, account.getUserId());
            stmt.setString(3, account.getHolderName());
            stmt.setString(4, account.getEmail());
            stmt.setString(5, account.getAccountPin());
            stmt.setDouble(6, 0.0);
            stmt.setString(7, "ACTIVE");
            stmt.setString(8, account.getAccountType().name());


            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Account updateBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE accounts SET balance = ? WHERE account_number = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setDouble(1, newBalance);
            stmt.setString(2, accountNumber);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
