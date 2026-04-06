CREATE TABLE accounts (
                          `account_number` VARCHAR(30) PRIMARY KEY,
                          `id` INT NOT NULL,
                          `holder_name` VARCHAR(100) NOT NULL,
                          `email` VARCHAR(100) UNIQUE NOT NULL,
                          `account_pin` VARCHAR(10)  NOT NULL,
                          `balance` DECIMAL(12,2) DEFAULT 0,
                          `account_status` ENUM('ACTIVE','BLOCKED','CLOSED') DEFAULT 'ACTIVE',
                          `account_type` ENUM('SAVINGS','MAIN', 'LOAN') DEFAULT 'MAIN',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                          FOREIGN KEY (id) REFERENCES users(user_id)
);


INSERT INTO accounts (account_number, id, holder_name, email, account_pin, balance, account_status, account_type) VALUES
                                                                                                                      ('ACC100001', 1, 'John Doe', 'john.doe1@example.com', '1234', 1500.50, 'ACTIVE', 'MAIN'),
                                                                                                                      ('ACC100002', 2, 'Alice Smith', 'alice.smith@example.com', '5678', 2500.00, 'ACTIVE', 'SAVINGS'),
                                                                                                                      ('ACC100003', 3, 'Michael Brown', 'michael.brown3@example.com', '3456', 500.75, 'BLOCKED', 'MAIN'),
                                                                                                                      ('ACC100004', 4, 'Emily Davis', 'emily.davis4@example.com', '4567', 3200.00, 'ACTIVE', 'SAVINGS'),
                                                                                                                      ('ACC100005', 5, 'David Wilson', 'david.wilson5@example.com', '5678', 0.00, 'CLOSED', 'LOAN'),
                                                                                                                      ('ACC100006', 6, 'Sarah Taylor', 'sarah.taylor6@example.com', '6789', 780.20, 'ACTIVE', 'MAIN'),
                                                                                                                      ('ACC100007', 7, 'Chris Anderson', 'chris.anderson7@example.com', '7890', 12000.99, 'ACTIVE', 'SAVINGS'),
                                                                                                                      ('ACC100008', 8, 'Olivia Thomas', 'olivia.thomas8@example.com', '8901', 60.00, 'BLOCKED', 'MAIN'),
                                                                                                                      ('ACC100009', 9, 'Daniel Jackson', 'daniel.jackson9@example.com', '9012', 450.10, 'ACTIVE', 'LOAN'),
                                                                                                                      ('ACC100010', 10, 'Sophia White', 'sophia.white10@example.com', '1122', 9999.99, 'ACTIVE', 'MAIN'),
                                                                                                                      ('ACC100011', 11, 'Thor Odinson', 'thor@asgard.univ', '1010', 0.00, 'ACTIVE', 'MAIN'),
                                                                                                                      ('ACC100012', 12, 'Bruce Banner', 'bruce.b@gamma.lab', '0000', 12500.00, 'ACTIVE', 'SAVINGS'),
                                                                                                                      ('ACC100013', 13, 'Scott Lang', 'scott.l@pymtech.net', '9876', 45.10, 'CLOSED', 'MAIN'),
                                                                                                                      ('ACC100014', 14, 'Hope van Dyne', 'hope.v@pym.com', '5432', 8200.00, 'ACTIVE', 'SAVINGS'),
                                                                                                                      ('ACC100015', 15, 'Sam Wilson', 'sam.w@vets.gov', '2020', 1850.00, 'ACTIVE', 'MAIN'),
                                                                                                                      ('ACC100016', 16, 'Bucky Barnes', 'bucky.b@serum.org', '1917', 300.50, 'ACTIVE', 'LOAN'),
                                                                                                                      ('ACC100017', 17, 'T\'Challa', 'tchalla@wakanda.af', '0001', 750000.00, 'ACTIVE', 'MAIN'),
                                                                                                                    ('ACC100018', 18, 'Carol Diver', 'carol.d@airforce.mil', '2019', 4200.00, 'ACTIVE', 'MAIN'),
                                                                                                                    ('ACC100019', 19, 'Stephen Strange', 'stephen.s@sanctum.org', '7777', 9800.00, 'ACTIVE', 'SAVINGS'),
                                                                                                                    ('ACC100020', 20, 'Nick Fury', 'nick.f@shield.org', '0009', 1200.00, 'ACTIVE', 'MAIN');
