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


    // === Builder ===
    /*
     * Why need Builder?
     * - Since we just need a record of the transaction, it would be weird to create
     * an object for transaction everytime.
     * - So we use Builder (a static inner class) for only instanciate the record
     * which has only needed information
     * and clean printed. It's also improve encapsulation(since nested static class
     * doesn't need to instanciate from its outer class)
     */

    public static class Builder {
        private TransactionType type;
        private String fromAccount;
        private String toAccount;
        private double amount;
        private String description = "";
        private String initiatedBy = "system";

        public Builder type(TransactionType type) {
            this.type = type;
            return this;
        }

        public Builder fromAccount(String accountNumber) {
            this.fromAccount = accountNumber;
            return this;
        }

        public Builder toAccount(String accountNumber) {
            this.toAccount = accountNumber;
            return this;
        }

        public Builder amount(double amount) {
            if (amount <= 0) {
                throw new IllegalArgumentException("Amount must be greater than 0.");
            }
            this.amount = amount;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder initiatedBy(String username) {
            this.initiatedBy = username;
            return this;
        }

        public Transaction build() {
            validate();
            return new Transaction(this);
        }
}