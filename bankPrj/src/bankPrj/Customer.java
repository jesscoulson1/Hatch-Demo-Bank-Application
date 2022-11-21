package bankPrj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Customer {
	Scanner scan = new Scanner(System.in);
	String customerId;
	
	String verifyId() {
		boolean runAgain=true;
		System.out.println("\nVerify id");
		System.out.println("-------------");
		do {
			System.out.print("\nPlease enter your customer ID: ");
			 customerId= scan.next();
			try {
				//step1 register driver class
				Class.forName("com.mysql.cj.jdbc.Driver");
				 Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","R00tR3d!");			
				Statement stmt = con.createStatement();
				PreparedStatement ps =con.prepareStatement( "select fname from customer where custid=?");
				ps.setString(1,customerId);
				ResultSet rs =ps.executeQuery();
				rs.next();
				String firstName = rs.getString(1);
				System.out.println("\nHello "+firstName+"!");
				runAgain=false;	
			}		

			catch (Exception e) {
				customerId=null;
				System.out.println("\nNo user with that ID can be found. Please try again or contact "
						+ "customer support to set up an account.");
				System.out.print("Enter Y if you would like to try again: ");
				String tryAgain=scan.next();
				if(tryAgain.equalsIgnoreCase("Y")) {
					runAgain=true;
				}
				else {
					runAgain=false;
				}
			}
			
		}
		while(runAgain==true);
		return customerId;
	}
	
		        

	
	
	
	
	void menu() {
		boolean runAgain=true;
		if (customerId!=null) {
			do{
			
				System.out.println("\nThe following options are avaliable:");
				System.out.println("A. Withdraw funds");
				System.out.println("B. Deposit funds");
				System.out.println("C. Request a loan");
				System.out.println("D. Log out");
		
				boolean valid = false;
				do {
					System.out.print("\nEnter the task you wish to complete (A/B/C/D): ");
		
					String choice = scan.next();
		
					if (choice.equalsIgnoreCase("A")) {
						withdraw(customerId);
						valid = true;
					}
					else if(choice.equalsIgnoreCase("B")) {
						deposit();
						valid=true;
					}
					else if(choice.equalsIgnoreCase("C"))
					{
						loan();
						valid=true;
					}
					else if(choice.equalsIgnoreCase("D")) {
						runAgain=false;
						valid=true;
						
					}
					else {
						System.out.println("That was not a valid option, please enter A, B, C, or D");
					}
				}
				while(valid==false);
			
		}
		while(runAgain==true);
		}
		}
	
	

	
	
	void withdraw(String customerId) {
		Scanner scan = new Scanner(System.in);
		System.out.println("\nWithdrawal Request");
		System.out.println("-----------------");
		System.out.println("\nAs a security measure we need to confirm your account number before you can make a withdrawal");
		boolean runAgain=true;
		do {
			
			try {
				System.out.print("\nPlease enter your account number: ");
				String accountNum = scan.next();
				System.out.print("Please enter the amount you would like to withdraw: £ ");
				int amount = scan.nextInt();
				LocalDateTime date= LocalDateTime.now();
				DateTimeFormatter dateFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String dateFormatted = date.format(dateFormatObj);

				
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","R00tR3d!");
				Statement stmt = con.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM trandetails"); //counts the current number of rows in table
				rs.next();
				int count = rs.getInt("rowcount"); // assigns row number to int variable
				rs.close();
				count++; // increments row number by 1 to get current transaction number
				String tranNum=("T00"+count); // adds T00 in front of row number to make transaction number in correct format
			
				PreparedStatement ps = con.prepareStatement("INSERT INTO trandetails VALUES(?,?,?,'Cash','Withdrawal',?)");
				ps.setString(1,tranNum);
				ps.setString(2,accountNum);
				ps.setString(3,dateFormatted);
				ps.setInt(4,amount);
				ps.executeUpdate();
				PreparedStatement ps1 = con.prepareStatement("select * from tranDetails where tnumber=? ");

				ps1.setString(1,tranNum);

				ResultSet rs1 = ps1.executeQuery();

				rs1.next();
				System.out.println("\nTransaction successful");
				System.out.println("\nTransaction details: ");
				System.out.println("Transaction number: "+rs1.getString(1));
				System.out.println("Account number: "+rs1.getString(2));
				System.out.println("Transaction date: "+rs1.getString(3));
				System.out.println("Transaction medium: "+rs1.getString(4));
				System.out.println("Transacation type: "+rs1.getString(5));
				System.out.println("Transaction amount: "+rs1.getInt(6));
	
				System.out.print("\nPlease enter Y to make another withdrawal, or any other key to exit: ");
			}
	
			
			
			catch(Exception e) {
				System.out.println("Withdrawal unsuccessful.");
				System.out.println("Please ensure you have entered the correct Acccount Number.");
				System.out.print("Please enter Y to try again, or any other key to exit: ");
			}
			
			finally {
			String choice=scan.next();
				if (choice.equalsIgnoreCase("Y")) {
					runAgain=true;
				}
			
				else {
					runAgain=false;
					}
			}
		}
		while (runAgain==true);
	}
	
	
	void deposit() {
		System.out.println("\nDeposit Request");
		System.out.println("------------------");
		System.out.println("\nAs a security measure we need to confirm your account number before you can make a deposit");
		boolean runAgain=true;
		do {
			
			try {
				System.out.print("\nPlease enter your account number: ");
				String accountNum = scan.next();
				System.out.print("Please enter the amount you would like to deposit: £ ");
				int amount = scan.nextInt();
				System.out.print("Is your deposit via cash or cheque: ");
				String type = scan.next();
				LocalDateTime date= LocalDateTime.now();
				DateTimeFormatter dateFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String dateFormatted = date.format(dateFormatObj);
				
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","R00tR3d!");
				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM trandetails");

				rs.next();
				int count = rs.getInt("rowcount");
				rs.close();
				count++;
				String tranNum=("T00"+count);
			
				PreparedStatement ps = con.prepareStatement("INSERT INTO trandetails VALUES(?,?,?,?,'Deposit',?)");
				ps.setString(1,tranNum);
				ps.setString(2,accountNum);
				ps.setString(3,dateFormatted);
				ps.setString(4, type);
				ps.setInt(5,amount);
				ps.executeUpdate();
				PreparedStatement ps1 = con.prepareStatement("select * from tranDetails where tnumber=? ");

				ps1.setString(1,tranNum);

				ResultSet rs1 = ps1.executeQuery();

				rs1.next();
				System.out.println("\nTransaction successful");
				System.out.println("\nTransaction details: ");
				System.out.println("Transaction number: "+rs1.getString(1));
				System.out.println("Account number: "+rs1.getString(2));
				System.out.println("Transaction date: "+rs1.getString(3));
				System.out.println("Transaction medium: "+rs1.getString(4));
				System.out.println("Transacation type: "+rs1.getString(5));
				System.out.println("Transaction amount: "+rs1.getInt(6));
	
				System.out.print("\nPlease enter Y to make another deposit, or any other key to exit: ");
			}
			catch(Exception e) {
				System.out.println("Deposit unsuccessful.");
				System.out.println(e);
				System.out.println("Please ensure you have entered the correct Acccount Number.");
				System.out.print("Please enter Y to try again, or any other key to exit: ");
			}
			
			finally {
			String choice=scan.next();
				if (choice.equalsIgnoreCase("Y")) {
					runAgain=true;
				}
			
				else {
					runAgain=false;
					}
			}
		}
		while (runAgain==true);
	}
	
	void loan() {
		System.out.println("\nLoan Request");

		System.out.println("Before we can provide you with a loan we need you to provide some information."
				+ "\nPlease provide the following information to proceed with your request:");
		System.out.print("Requested loan amount: £");
		Scanner scan = new Scanner(System.in);
		scan.next();
		System.out.print("Requested repayment period (in months): ");
		scan.next();
		System.out.print("Yearly income: £");
		scan.next();
		System.out.println("\nUnfortunetly you do not meet our lending criteria.\nYou are able to apply again in the future.");
		
	}
}
