CREATE TABLE transactions (
                              `transaction_id` VARCHAR(50) PRIMARY KEY,
                              `transaction_type` ENUM('DEPOSIT','WITHDRAW','TRANSFER','LOAN') NOT NULL,
                              `transaction_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                              `amount` DECIMAL(12,2) NOT NULL,
                              `from_account_number` VARCHAR(20) NOT NULL,
                              `to_account_number` VARCHAR(20) NOT NULL,
                              `description` VARCHAR(255),
                              `transaction_status` ENUM('PENDING','COMPLETED','FAILED', 'REVERSED') DEFAULT 'PENDING',
                              `initiated_by` VARCHAR(30) NOT NULL,
                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                              FOREIGN KEY (from_account_number) REFERENCES accounts(account_number),
                              FOREIGN KEY (to_account_number) REFERENCES accounts(account_number)
);