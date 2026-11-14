# Codomax Module 04 — Java Database & JDBC

A console-based Banking Management System built with Java, JDBC and MySQL. This educational project stores accounts and transaction history in real MySQL tables.

## Features
- Create, list and search accounts
- Deposit, withdraw and transfer money
- View transaction history
- JDBC `Connection`, `PreparedStatement` and `ResultSet`
- CRUD operations and parameterized SQL
- Database transactions with commit/rollback
- Try-with-resources for closing database resources
- Database password supplied through an environment variable

> Learning project only. It is not a real banking service and must not be used for real money or sensitive financial data.

## Requirements
- JDK 17+
- MySQL Server 8+
- Maven 3.8+

## Setup
1. Start MySQL Server.
2. Open MySQL Workbench or the MySQL command line.
3. Run `src/main/resources/schema.sql`. It creates database `codomax_bank` and its two tables.
4. Set your MySQL connection settings. Never commit your password to GitHub.

Windows PowerShell:
```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/codomax_bank?useSSL=false&serverTimezone=UTC"
$env:DB_USER="root"
$env:DB_PASSWORD="your_mysql_password"
```
Windows Command Prompt:
```cmd
set DB_URL=jdbc:mysql://localhost:3306/codomax_bank?useSSL=false&serverTimezone=UTC
set DB_USER=root
set DB_PASSWORD=your_mysql_password
```
5. Open a terminal in the folder containing `pom.xml` and run:
```bash
mvn clean compile
mvn exec:java
```
Maven downloads MySQL Connector/J automatically.

## Test checklist
Create two accounts, deposit into one, withdraw a smaller amount, transfer to the other, then check balances and transaction histories. Also test an overdraw attempt and confirm it is rejected.

## Project structure
```text
pom.xml
src/main/java/com/codomax/bank/
  Main.java
  DatabaseConnection.java
  BankDAO.java
  Account.java
src/main/resources/schema.sql
docs/
  DATABASE_DESIGN.md
  JDBC_FLOW.md
README.md
.gitignore
```

## GitHub upload
Create a public repository named `Codomax-Java-Internship-Module-4`. Upload the contents of the extracted project folder, not the ZIP itself. Keep `pom.xml`, `src`, `docs`, `.gitignore` and `README.md` at repository root.
