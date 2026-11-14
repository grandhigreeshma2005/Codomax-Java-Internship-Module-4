package com.codomax.bank;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
 private static final Scanner in=new Scanner(System.in);private static final BankDAO dao=new BankDAO();
 public static void main(String[] args){
  try(Connection c=DatabaseConnection.getConnection()){System.out.println("Connected to MySQL successfully.");menu();}
  catch(SQLException e){System.out.println("Database connection error: "+e.getMessage());System.out.println("Check MySQL, schema.sql and DB_PASSWORD.");}
 }
 private static void menu(){
  while(true){
   System.out.println("\n=== BANK MANAGEMENT SYSTEM ===\n1.Create account\n2.View all accounts\n3.Search account\n4.Deposit\n5.Withdraw\n6.Transfer\n7.Transaction history\n8.Exit");
   System.out.print("Choose: ");String choice=in.nextLine().trim();
   try{switch(choice){
    case "1" -> create();case "2" -> dao.all().forEach(System.out::println);
    case "3" -> {Account a=dao.find(text("Account number: "));System.out.println(a==null?"Account not found.":a);}
    case "4" -> {dao.money(text("Account number: "),amount("Amount: "),true);System.out.println("Deposit successful.");}
    case "5" -> {dao.money(text("Account number: "),amount("Amount: "),false);System.out.println("Withdrawal successful.");}
    case "6" -> {dao.transfer(text("From account: "),text("To account: "),amount("Amount: "));System.out.println("Transfer successful.");}
    case "7" -> {var h=dao.history(text("Account number: "));if(h.isEmpty())System.out.println("No transactions.");else h.forEach(System.out::println);}
    case "8" -> {System.out.println("Goodbye!");return;}default -> System.out.println("Choose 1 to 8.");
   }}catch(SQLException|IllegalArgumentException e){System.out.println("Operation failed: "+e.getMessage());}
  }
 }
 private static void create()throws SQLException{
  String no=text("Account number: "),name=text("Holder name: "),type=text("Type (SAVINGS/CURRENT): ").toUpperCase();
  if(!type.equals("SAVINGS")&&!type.equals("CURRENT"))throw new IllegalArgumentException("Type must be SAVINGS or CURRENT.");
  dao.create(no,name,type,amount("Opening balance (0 allowed): ",true));System.out.println("Account created.");
 }
 private static String text(String p){System.out.print(p);String s=in.nextLine().trim();if(s.isEmpty())throw new IllegalArgumentException("Input cannot be empty.");return s;}
 private static BigDecimal amount(String p){return amount(p,false);}
 private static BigDecimal amount(String p,boolean zero){System.out.print(p);try{BigDecimal a=new BigDecimal(in.nextLine().trim());if(a.scale()>2||a.compareTo(BigDecimal.ZERO)<0||(!zero&&a.signum()==0))throw new IllegalArgumentException("Invalid amount.");return a;}catch(NumberFormatException e){throw new IllegalArgumentException("Enter a valid number.");}}
}
