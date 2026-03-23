package com.team4.dao.impl;


import com.team4.dao.TransactionDAO;
import com.team4.model.transaction.Transaction;
import com.team4.model.transaction.TransactionStatus;
import com.team4.model.transaction.TransactionType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySQLTransactionDAO implements TransactionDAO {

    private final Connection connection;

    private static final String NULL_ACCOUNT_PLACEHOLDER = "-";

    public MySQLTransactionDAO(Connection connection) {
        this.connection = connection;
    }
    @Override
    public void saveTransaction(Transaction transaction) {
        String sql = "INSERT INTO transactions(transaction_id, transaction_type, transaction_date, amount, from_account_number, to_account_number, description, transaction_status, initiated_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {

//            String transactionID = transaction.getTransactionID();
//            String type = transaction.getType().name();
//            String transactionDate = transaction.getTransactionDate().toString();
//            String fromAccount = transaction.getFromAccount();
//            String toAccount = transaction.getToAccount();
//            double amount = transaction.getAmount();
//            String status = transaction.getStatus().name();
//            String description = transaction.getDescription();
//            String initiatedBy = transaction.getInitiatedBy();

            stmt.setString(1, transaction.getTransactionID());
            stmt.setString(2, transaction.getType().name());
            stmt.setString(3, transaction.getTransactionDate().toString());
            stmt.setDouble(4, transaction.getAmount());
            stmt.setString(5, transaction.getFromAccount() != null ? transaction.getFromAccount() : NULL_ACCOUNT_PLACEHOLDER);
            stmt.setString(6, transaction.getToAccount()   != null ? transaction.getToAccount()   : NULL_ACCOUNT_PLACEHOLDER);
            stmt.setString(7, transaction.getDescription());
            stmt.setString(8, transaction.getStatus().name());
            stmt.setString(9, transaction.getInitiatedBy());

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Transaction> findTransactionsByAccountNumber(String accountNumber) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM transactions WHERE from_account_number = ? OR to_account_number = ? ORDER BY transaction_date DESC";

        try(PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, accountNumber);
            stmt.setString(2, accountNumber);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                TransactionType type     = TransactionType.valueOf(rs.getString("transaction_type"));
                TransactionStatus status = TransactionStatus.valueOf(rs.getString("transaction_status"));
                String fromAccount       = rs.getString("from_account_number");
                String toAccount         = rs.getString("to_account_number");

                Transaction.Builder builder = new Transaction.Builder()
                        .type(type)
                        .amount(rs.getDouble("amount"))
                        .description(rs.getString("description"))
                        .initiatedBy(rs.getString("initiated_by"));

                // Skip placeholder — only set real account numbers
                if (fromAccount != null && !fromAccount.equals(NULL_ACCOUNT_PLACEHOLDER))
                    builder.fromAccount(fromAccount);
                if (toAccount != null && !toAccount.equals(NULL_ACCOUNT_PLACEHOLDER))
                    builder.toAccount(toAccount);

                Transaction transaction = builder.build();

                // Replay status transitions
                switch (status) {
                    case COMPLETED:
                        transaction.markCompleted();
                        break;

                    case FAILED:
                        transaction.markFailed();
                        break;

                    case REVERSED:
                        transaction.markCompleted();
                        transaction.reverse();
                        break;

                    // PENDING: no action needed
                    default:
                        break;
                }
                transactions.add(transaction);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transactions;
    }
}
