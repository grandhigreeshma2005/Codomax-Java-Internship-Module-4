# Database Design

## accounts
- `id`: BIGINT primary key, auto increment
- `account_number`: VARCHAR(20), unique and not null
- `holder_name`: VARCHAR(100), not null
- `account_type`: SAVINGS or CURRENT
- `balance`: DECIMAL(12,2), cannot be negative
- `created_at`: timestamp

## transactions
- `id`: BIGINT primary key, auto increment
- `account_id`: foreign key to accounts
- `transaction_type`: DEPOSIT, WITHDRAW, TRANSFER_IN or TRANSFER_OUT
- `amount`: positive DECIMAL(12,2)
- `description`: short explanation
- `related_account_id`: optional foreign key to the other account in a transfer
- `created_at`: timestamp

Relationship: one account can have many transactions (one-to-many). A transfer creates an outgoing and incoming history row.
