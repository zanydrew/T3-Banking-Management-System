CREATE TABLE sessions (
                          `session_id`      VARCHAR(64) PRIMARY KEY,
                          `user_id`         INT NOT NULL,
                          `username`        VARCHAR(50) NOT NULL,
                          `role`            ENUM('CUSTOMER','MANAGER') NOT NULL,
                          `login_at`        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          `last_active_at`  TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          `is_active`       BOOLEAN DEFAULT TRUE,
                          FOREIGN KEY (user_id) REFERENCES users(user_id)
);