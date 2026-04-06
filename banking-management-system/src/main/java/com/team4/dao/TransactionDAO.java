package com.team4.dao;

import com.team4.model.transaction.Transaction;

import java.util.List;

public interface TransactionDAO {

    // method to store new transaction everytime deposit/withdraw/transfer happen
    void saveTransaction(Transaction transaction);

    // method to retrieve transaction history (View account information → transaction history)
    List<Transaction> findTransactionsByAccountNumber(String accountNumber);
}
