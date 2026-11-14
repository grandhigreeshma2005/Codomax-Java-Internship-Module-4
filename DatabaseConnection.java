package com.codomax.bank;
import java.sql.*;
public final class DatabaseConnection {
 private DatabaseConnection() {}
 public static Connection getConnection() throws SQLException {
  String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/codomax_bank?useSSL=false&serverTimezone=UTC");
  String user=System.getenv().getOrDefault("DB_USER","root");
  String password=System.getenv("DB_PASSWORD");
  if(password==null) throw new SQLException("Set DB_PASSWORD environment variable first.");
  return DriverManager.getConnection(url,user,password);
 }
}
