package com.team4.model.transaction;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private Transaction(Builder builder) {
        this.transactionID = UUID.randomUUID().toString();
        this.type = builder.type;
        this.transactionDate = LocalDateTime.now();
        this.fromAccount = builder.fromAccount;
        this.toAccount = builder.toAccount;
        this.amount = builder.amount;
        this.status = TransactionStatus.PENDING;
        this.description = builder.description;
        this.initiatedBy = builder.initiatedBy;
    }

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

        private void validate() {
            if (type == null)
                throw new IllegalStateException("TransactionType is required.");
            if (amount <= 0)
                throw new IllegalStateException("Amount is required.");

            switch (type) {
                case DEPOSIT:
                    if (toAccount == null)
                        throw new IllegalStateException(type + " requires a target account.");

                    break;
                case WITHDRAW:
                    if (fromAccount == null)
                        throw new IllegalStateException(type + " requires a target account.");

                    break;
                case TRANSFER:
                    if (fromAccount == null || toAccount == null)
                        throw new IllegalStateException("TRANSFER requires both source and target accounts.");
                    if (fromAccount.equals(toAccount))
                        throw new IllegalStateException("Source and target accounts must differ.");
                    break;
            }
        }
    }


    // === Getters ===
    public String getTransactionID() {
        return transactionID;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public String getFromAccount() {
        return fromAccount;
    }

    public String getToAccount() {
        return toAccount;
    }

    public double getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getInitiatedBy() {
        return initiatedBy;
    }

    public void markCompleted() {
        if (this.status != TransactionStatus.PENDING) {
            throw new IllegalStateException("Only PENDING transactions can be completed.");
        }
        this.status = TransactionStatus.COMPLETED;
    }

    public void markFailed() {
        if (this.status == TransactionStatus.COMPLETED) {
            throw new IllegalStateException("Completed transaction cannot be failed.");
        }
        this.status = TransactionStatus.FAILED;
    }

    /*
     * Reverse a completed transaction.
     * Typically called by a Manager; the caller is responsible for
     * reversing the actual account balances.
     */

    public void reverse() {
        if (this.status != TransactionStatus.COMPLETED)
            throw new IllegalStateException("Only COMPLETED transactions can be reversed.");
        this.status = TransactionStatus.REVERSED;
    }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format(
                "[%s] %s | %s | $%.2f | %s → %s | By: %s | %s",
                transactionID.substring(0, 8),
                transactionDate.format(fmt),
                type,
                amount,
                fromAccount != null ? fromAccount : "—",
                toAccount != null ? toAccount : "—",
                initiatedBy,
                status);
    }

    // @Override
    // public String toString() {
    // DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    // return String.format(
    // "[%s] %s | %s | $%.2f | %s - %s | By: %s | %s",
    // transactionID.substring(0, 8),
    // transactionDate.format(fmt),
    // type,
    // amount,
    // fromAccount,
    // toAccount,
    // initiatedBy,
    // status);
    // }
}
