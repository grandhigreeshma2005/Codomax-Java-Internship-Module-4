package com.codomax.bank;
import java.math.BigDecimal;
import java.sql.Timestamp;
public class Account {
 private final long id; private final String number,name,type; private final BigDecimal balance; private final Timestamp createdAt;
 public Account(long id,String number,String name,String type,BigDecimal balance,Timestamp createdAt){
  this.id=id;this.number=number;this.name=name;this.type=type;this.balance=balance;this.createdAt=createdAt;
 }
 @Override public String toString(){return "ID: "+id+" | Account: "+number+" | Holder: "+name+" | Type: "+type+" | Balance: "+balance+" | Created: "+createdAt;}
}
