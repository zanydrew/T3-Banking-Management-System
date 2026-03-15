package com.team4.model.transaction;

import com.team4.service.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    // Since Transaction record should never change, using final for fields is the best choice

    private final String transactionID;
    private final TransactionType type;
    private final LocalDateTime transactionDate;
    private final String fromAccount;
    private final String toAccount;
    private final double amount;
    private final String description;
    private TransactionStatus status;
    private final String initiatedBy;


}