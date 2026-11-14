# JDBC Architecture and Flow

```text
User -> Main.java (console)
     -> BankDAO.java (SQL operations)
     -> DatabaseConnection.java
     -> JDBC API (Connection / PreparedStatement / ResultSet)
     -> MySQL Connector/J driver
     -> MySQL database (accounts, transactions)
```

1. User selects an operation in the CLI.
2. Main validates input and calls BankDAO.
3. BankDAO opens a JDBC connection.
4. PreparedStatement executes parameterized SQL.
5. ResultSet reads rows returned by SELECT.
6. Try-with-resources closes connections and statements.
7. Money transfers use a transaction: commit if every step succeeds; rollback if a step fails.
