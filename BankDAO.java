package com.codomax.bank;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

public class BankDAO {
 public void create(String no,String name,String type,BigDecimal opening)throws SQLException{
  String q="INSERT INTO accounts(account_number,holder_name,account_type,balance) VALUES(?,?,?,?)";
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){
   p.setString(1,no);p.setString(2,name);p.setString(3,type);p.setBigDecimal(4,opening);p.executeUpdate();
  }
 }
 public List<Account> all()throws SQLException{
  List<Account> list=new ArrayList<>();
  String q="SELECT * FROM accounts ORDER BY id";
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement p=c.prepareStatement(q);ResultSet r=p.executeQuery()){
   while(r.next())list.add(new Account(r.getLong("id"),r.getString("account_number"),r.getString("holder_name"),r.getString("account_type"),r.getBigDecimal("balance"),r.getTimestamp("created_at")));
  }return list;
 }
 public Account find(String no)throws SQLException{
  String q="SELECT * FROM accounts WHERE account_number=?";
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){
   p.setString(1,no);try(ResultSet r=p.executeQuery()){if(!r.next())return null;return new Account(r.getLong("id"),r.getString("account_number"),r.getString("holder_name"),r.getString("account_type"),r.getBigDecimal("balance"),r.getTimestamp("created_at"));}
  }
 }
 public void money(String no,BigDecimal amount,boolean deposit)throws SQLException{
  positive(amount);String update=deposit?"UPDATE accounts SET balance=balance+? WHERE account_number=?":"UPDATE accounts SET balance=balance-? WHERE account_number=? AND balance>=?";
  String log="INSERT INTO transactions(account_id,transaction_type,amount,description) SELECT id,?,?,? FROM accounts WHERE account_number=?";
  try(Connection c=DatabaseConnection.getConnection()){
   c.setAutoCommit(false);
   try(PreparedStatement p=c.prepareStatement(update);PreparedStatement t=c.prepareStatement(log)){
    p.setBigDecimal(1,amount);p.setString(2,no);if(!deposit)p.setBigDecimal(3,amount);
    if(p.executeUpdate()!=1)throw new SQLException("Account not found or insufficient balance.");
    t.setString(1,deposit?"DEPOSIT":"WITHDRAW");t.setBigDecimal(2,amount);t.setString(3,deposit?"Cash deposit":"Cash withdrawal");t.setString(4,no);t.executeUpdate();c.commit();
   }catch(Exception e){c.rollback();if(e instanceof SQLException s)throw s;throw new SQLException(e);}
  }
 }
 public void transfer(String from,String to,BigDecimal amount)throws SQLException{
  positive(amount);if(from.equals(to))throw new IllegalArgumentException("Choose two different accounts.");
  try(Connection c=DatabaseConnection.getConnection()){
   c.setAutoCommit(false);
   try{
    try(PreparedStatement p=c.prepareStatement("UPDATE accounts SET balance=balance-? WHERE account_number=? AND balance>=?")){
     p.setBigDecimal(1,amount);p.setString(2,from);p.setBigDecimal(3,amount);if(p.executeUpdate()!=1)throw new SQLException("Sender not found or insufficient balance.");
    }
    try(PreparedStatement p=c.prepareStatement("UPDATE accounts SET balance=balance+? WHERE account_number=?")){
     p.setBigDecimal(1,amount);p.setString(2,to);if(p.executeUpdate()!=1)throw new SQLException("Recipient account not found.");
    }
    addTransferLog(c,from,to,amount,"TRANSFER_OUT","Transfer to "+to);
    addTransferLog(c,to,from,amount,"TRANSFER_IN","Transfer from "+from);
    c.commit();
   }catch(Exception e){c.rollback();if(e instanceof SQLException s)throw s;if(e instanceof RuntimeException r)throw r;throw new SQLException(e);}
  }
 }
 private void addTransferLog(Connection c,String account,String related,BigDecimal amount,String kind,String desc)throws SQLException{
  String q="INSERT INTO transactions(account_id,transaction_type,amount,description,related_account_id) SELECT a.id,?,?,?,b.id FROM accounts a JOIN accounts b ON b.account_number=? WHERE a.account_number=?";
  try(PreparedStatement p=c.prepareStatement(q)){p.setString(1,kind);p.setBigDecimal(2,amount);p.setString(3,desc);p.setString(4,related);p.setString(5,account);if(p.executeUpdate()!=1)throw new SQLException("Could not save transfer history.");}
 }
 public List<String> history(String no)throws SQLException{
  List<String> rows=new ArrayList<>();String q="SELECT t.* FROM transactions t JOIN accounts a ON t.account_id=a.id WHERE a.account_number=? ORDER BY t.created_at DESC,t.id DESC";
  try(Connection c=DatabaseConnection.getConnection();PreparedStatement p=c.prepareStatement(q)){p.setString(1,no);try(ResultSet r=p.executeQuery()){while(r.next())rows.add(r.getTimestamp("created_at")+" | "+r.getString("transaction_type")+" | "+r.getBigDecimal("amount")+" | "+r.getString("description"));}}return rows;
 }
 private void positive(BigDecimal a){if(a.compareTo(BigDecimal.ZERO)<=0||a.scale()>2)throw new IllegalArgumentException("Enter an amount greater than zero with at most 2 decimal places.");}
}
