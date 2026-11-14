CREATE DATABASE IF NOT EXISTS codomax_bank;
USE codomax_bank;
CREATE TABLE IF NOT EXISTS accounts (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 account_number VARCHAR(20) NOT NULL UNIQUE,
 holder_name VARCHAR(100) NOT NULL,
 account_type VARCHAR(20) NOT NULL,
 balance DECIMAL(12,2) NOT NULL DEFAULT 0.00,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 CHECK (balance >= 0)
);
CREATE TABLE IF NOT EXISTS transactions (
 id BIGINT PRIMARY KEY AUTO_INCREMENT,
 account_id BIGINT NOT NULL,
 transaction_type VARCHAR(20) NOT NULL,
 amount DECIMAL(12,2) NOT NULL,
 description VARCHAR(255),
 related_account_id BIGINT NULL,
 created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
 FOREIGN KEY (account_id) REFERENCES accounts(id),
 FOREIGN KEY (related_account_id) REFERENCES accounts(id),
 CHECK (amount > 0)
);
