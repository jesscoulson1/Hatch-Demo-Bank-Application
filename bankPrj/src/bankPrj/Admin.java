package bankPrj;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class Admin {
	private String username = "admin";
	private String password = "password";
	boolean validId;
	
	///ADD CUSTOMER
	
	boolean verifyId() {
		System.out.println("\nPlease enter your credentials to verify your identity ");
		boolean runAgain =false;
		boolean validId =false;
		do {
			Scanner scan = new Scanner(System.in);
			System.out.print("\nEnter username: ");
			String inputUsername = scan.next();
			System.out.print("Enter password: ");
			String inputPass= scan.next();
			
			if(inputUsername.equals(username)&inputPass.equals(password)) {
				 validId=true;
				System.out.println("Valid username and password.\nAccess Granted.");
				System.out.println("\nWelcome Admin!");
				System.out.println("------------------\n");
				
				runAgain=false;
			}
			else {
			
				System.out.println("\nIncorrect username and/or password");
				System.out.print("Please enter Y if you would like to try again: ");
				String choice=scan.next();
				if (choice.equalsIgnoreCase("Y")) {
					runAgain=true;
				}
				
				
			}
		}

		
		while (runAgain==true);
		
		return validId;
	}
	
	
	void menu(boolean validId){
		boolean runAgain=true;
		if (validId==true) {
			do{
			
				Scanner scan = new Scanner(System.in);
				System.out.println("\nThe following options are avaliable:");
				System.out.println("A. Add account");
				System.out.println("B. View account");
				System.out.println("C. Add branch");
				System.out.println("D. View branch");
				System.out.println("E. View loans");
				System.out.println("F. View transaction details");
				System.out.println("G. Add customer");
				System.out.println("H. Log out");
				
				boolean valid = false;
				do {
					System.out.print("\n Enter the task you wish to complete (A/B/C/D/E/F/G/H): ");
		
					String choice = scan.next();
					if(choice.equalsIgnoreCase("A")) {
						addAccount();
						valid= true;
					}
					else if (choice.equalsIgnoreCase("B")) {
						viewAccount();
						valid = true;
					}
					else if(choice.equalsIgnoreCase("C")) {
						addBranch();
						valid=true;
					}
					else if(choice.equalsIgnoreCase("D"))
					{
						viewBranch();
						valid=true;
					}
					else if(choice.equalsIgnoreCase("E")) {
						viewLoans();
						valid=true;
					}
					else if(choice.equalsIgnoreCase("F")) {
						viewTrans();
						valid=true;
					}
					else if(choice.equalsIgnoreCase("G")) {
						addCustomer();
						valid=true;
					}
					else if(choice.equalsIgnoreCase("H")) {
						valid=true;
						runAgain=false;
					}
					else {
						System.out.println("That was not a valid option, please enter A,B,C,D,E,F or G");
						
					}
				}
				while(valid==false);
			
		}
			while(runAgain==true);
		}
	}
	
	
	//add option to view all accounts
	void viewAccount() {
	
		boolean runAgain=true;
		Scanner scan = new Scanner(System.in);
		System.out.println("\nView account");
		System.out.println("--------------");
		do {
			try {
	
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","R00tR3d!");
				Statement stmt = con.createStatement();
				System.out.println("\nWhat would you like to view?");
				System.out.println("A. One account \nB.All accounts");
				System.out.print("Enter A or B: ");
				String choice=scan.next();
					if(choice.equalsIgnoreCase("A")) {
						System.out.print("\nEnter the customer Id of the account you wish to view: ");
						String customerId=scan.next();
						
						PreparedStatement ps =con.prepareStatement( "select * from account where custid=?");
						ps.setString(1,customerId);
						ResultSet rs =ps.executeQuery();
						
						//getting meta data
						ResultSetMetaData rsmd = rs.getMetaData();
						int num = rsmd.getColumnCount();
						//printing coloumn names
					
						//printing account data
						while(rs.next()) {
							for(int i=1; i<num;i++) {
							System.out.println( rsmd.getColumnName(i)+": "+rs.getString(i));
							}
							System.out.println();
						}
					}
					else if(choice.equalsIgnoreCase("B")) {
					  ResultSet  rs = stmt.executeQuery("SELECT * from account");
						ResultSetMetaData rsmd = rs.getMetaData();
						int num = rsmd.getColumnCount();
						//printing coloumn names
					
						//printing account data
						while(rs.next()) {
							for(int i=1; i<num;i++) {
							System.out.println( rsmd.getColumnName(i)+": "+rs.getString(i));
							}
							System.out.println();
					}
					}
					else {
						System.out.println("That was not a valid option, you are able to try again.");
					}
		
					
					con.close();
					
				
				System.out.print("\nPlease enter Y if you would like to view more account details: ");
				String rAChoice=scan.next();
				if(rAChoice.equalsIgnoreCase("Y")) {
					runAgain=true;
				}
				else {
					runAgain=false;
				}
		}
			catch (Exception e) {
				System.out.println("That is not a valid customer ID. Please try again or add a customer.");
				System.out.print("Would you like to try again(Y): ");
				String choice=scan.next();
				if(choice.equalsIgnoreCase("Y")) {
					runAgain=true;
				}
				else {
					runAgain=false;
				}
			}

		}
		while(runAgain==true);
		
	}
	
	
	void addAccount() {
		Scanner scan = new Scanner(System.in);
		System.out.println("\nAdd account");
		System.out.println("--------------");
		boolean runAgain=true;
		do {
			try {
				System.out.println("\nPlease provide the following information in order to add an account:");
				System.out.print("Customer ID: ");
				String custID=scan.next();
				System.out.print("Branch ID: ");
				String branchID=scan.next();
				System.out.print("Opening balance: ");
				int balance = scan.nextInt();
				System.out.print("Account type: ");
				String accountType=scan.next();
				
				LocalDateTime date= LocalDateTime.now();
				DateTimeFormatter dateFormatObj = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String dateFormatted = date.format(dateFormatObj);
				
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","R00tR3d!");
				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM account");

				rs.next();
				int count = rs.getInt("rowcount");
				rs.close();
				count++;
				String accountNum=("A000"+count);
				PreparedStatement ps = con.prepareStatement("INSERT INTO account VALUES(?,?,?,?,?,?,'Active')");
				ps.setString(1,accountNum);
				ps.setString(2,custID);
				ps.setString(3,branchID);
				ps.setInt(4,balance );
				ps.setString(5,dateFormatted);
				ps.setString(6, accountType);
				ps.executeUpdate();
				PreparedStatement ps1 = con.prepareStatement("select * from account where acnumber=? ");
				ps1.setString(1, accountNum);
				ResultSet rs1 = ps1.executeQuery();
				
				System.out.println("Account creation successful");
				rs1.next();
				
				System.out.println("\nAccount details: ");
				System.out.println("Account number: "+rs1.getString(1));
				System.out.println("Customer ID: "+rs1.getString(2));
				System.out.println("Branch ID: "+rs1.getString(3));
				System.out.println("Opening balance: "+rs1.getInt(4));
				System.out.println("Account opening date: "+rs1.getString(5));
				System.out.println("Account Type: "+rs1.getString(6));
				System.out.println("Account status: "+rs1.getString(7));
	
				System.out.print("\nPlease enter Y to make another account, or any other key to exit: ");
				
			}
			catch(Exception e) {

				System.out.println("\nAccount creation unsuccessfull. Please ensure all details entered are correct.");
				System.out.print("Please enter Y if you would like to try again, or any other key to exit: ");
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
		while(runAgain==true);
	}
	
	
	void addCustomer() {
		Scanner scan = new Scanner(System.in);
		System.out.println("\nAdd customer");
		System.out.println("--------------");
		boolean runAgain=true;
		do {
			try {
				System.out.println("\nPlease provide the following information in order to add an account:");
				System.out.print("Customer first name: ");
				String custFN=scan.next();
				System.out.print("Customer middle name: ");
				String custMN=scan.next();
				System.out.print("Customer last name: ");
				String custLN = scan.next();
				System.out.print("Customer city: ");
				String custCity=scan.next();
				System.out.print("Customer mobile number:");
				String custNum=scan.next();
				System.out.print("Customer occupation: ");
				String custOcc=scan.next();
				System.out.print("\nCustomer date of birth: ");
				String custDOB=scan.next();
				
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","R00tR3d!");
				Statement stmt = con.createStatement();

				ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM customer");

				rs.next();
				int count = rs.getInt("rowcount");
				rs.close();
				count++;
				String custID=("C000"+count);
				PreparedStatement ps = con.prepareStatement("INSERT INTO customer VALUES(?,?,?,?,?,?,?,?)");
				ps.setString(1,custID);
				ps.setString(2,custFN);
				ps.setString(3,custMN);
				ps.setString(4,custLN );
				ps.setString(5,custCity);
				ps.setString(6, custNum);
				ps.setString(7,custOcc);
				ps.setString(8, custDOB);
				ps.executeUpdate();
				PreparedStatement ps1 = con.prepareStatement("select * from customer where custID=? ");
				ps1.setString(1, custID);
				ResultSet rs1 = ps1.executeQuery();
				
				System.out.println("Account creation successful");
				rs1.next();
				
				System.out.println("\nCustomer details: ");
				System.out.println("Customer ID: "+rs1.getString(1));
				System.out.println("First name: "+rs1.getString(2));
				System.out.println("Middle name: "+rs1.getString(3));
				System.out.println("Last name: "+rs1.getString(4));
				System.out.println("City: "+rs1.getString(5));
				System.out.println("Mobile number: "+rs1.getString(6));
				System.out.println("Occupation: "+rs1.getString(7));
				System.out.println("Date of birth: "+rs1.getString(8));
	
				System.out.print("\nPlease enter Y to add another customer, or any other key to exit: ");
				
			}
			catch(Exception e) {
				System.out.print(e);
				System.out.println("\nCustomer creation unsuccessfull. Please ensure all details entered are correct.");
				System.out.print("Please enter Y if you would like to try again, or any other key to exit: ");
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
		while(runAgain==true);
	}
	
	
	
	
	
	void viewBranch() {
		boolean runAgain=true;
		Scanner scan = new Scanner(System.in);
		System.out.println("\nView Branch");
		System.out.println("--------------");
		do {
			try {
	
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","R00tR3d!");
	
				
				Statement stmt = con.createStatement();
	
				System.out.println("\nWhat would you like to view:");
				System.out.println("A. One branch \nB. All branches");
				System.out.print("Enter A or B: ");
				String choice=scan.next();
				if(choice.equalsIgnoreCase("A")) {
					System.out.print("\nEnter the branch ID of the branch you wish to view: ");
					String branchId=scan.next();
				
					PreparedStatement ps =con.prepareStatement( "select * from branch where bid=?");
					ps.setString(1,branchId);
					ResultSet rs =ps.executeQuery();
					rs.next();
					System.out.println("\n"+rs.getString(1)+" "+rs.getString(2)+" "+ rs.getString(3));
					con.close();
					
				}
				else if (choice.equalsIgnoreCase("B")) {
				
	;			ResultSet rs = stmt.executeQuery("select * from branch");
				while(rs.next()) {
					System.out.println("\n"+rs.getString(1)+" "+rs.getString(2)+" "+ rs.getString(3));
				}
				//step 5 close connection
				con.close();
				}
				
				else {
					System.out.println("That is not a valid choice. Please try again or add a branch.");
					System.out.print("Would you like to try again(Y): ");
					choice=scan.next();
					if(choice.equalsIgnoreCase("Y")) {
						runAgain=true;
					}
					else {
						runAgain=false;
					}
				}
				System.out.print("\nPlease enter Y if you would like to view more branch details: ");
				choice=scan.next();
				if(choice.equalsIgnoreCase("Y")) {
					runAgain=true;
				}
				else {
					runAgain=false;
				}
		}
			catch (Exception e) {
				System.out.println("That is not a valid branch ID. Please try again or add a branch.");
				System.out.print("Would you like to try again(Y): ");
				String choice=scan.next();
				if(choice.equalsIgnoreCase("Y")) {
					runAgain=true;
				}
				else {
					runAgain=false;
				}
			}

		}
		while(runAgain==true);
	}
	
	void addBranch() {
		System.out.println("\nAdd Branch");
		System.out.println("-------------");
		Scanner scan = new Scanner(System.in);
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			//Step2 Create connection 
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","R00tR3d!");
			
			
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS rowcount FROM branch");
			rs.next();
			int count = rs.getInt("rowcount");
			rs.close();
			count++;
			String branchNum=("B000"+count);
			
			System.out.print("\nPlease enter the branch name: ");
			String bname = scan.nextLine();
			System.out.print("Please enter the branch city: ");
			String bcity = scan.next();
			//Step3 Create Statement object
			PreparedStatement stmt1 = con.prepareStatement("INSERT INTO branch VALUES(?,?,?)");
			stmt1.setString(1, branchNum);
			stmt1.setString(2, bname);
			stmt1.setString(3, bcity);
			stmt1.executeUpdate();
			
			System.out.println("\nBranch creation successful");
			System.out.println("Branch details: ");
			PreparedStatement stmt2 =con.prepareStatement("select * from branch where bid=?");
			stmt2.setString(1,branchNum);
			ResultSet rs1 = stmt2.executeQuery();
			rs1.next();
			System.out.println("Branch ID: "+rs1.getString(1));
			System.out.println("Branch name: "+rs1.getString(2));
			System.out.println("Branch city: "+rs1.getString(3));
		
			con.close();
		}
		catch(Exception e) {
			System.out.print("An error has occured, try again. "+e);
		}
	}
	
	void viewLoans() {
		System.out.println("\nView Loans");
		System.out.println("-------------");
		boolean runAgain=true;
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");
		
				//Step2 Create connection 
				Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","R00tR3d!");
	
				//Step3 Create Statement object
				Statement stmt = con.createStatement();
				//Step 4 Execute Query
				ResultSet rs = stmt.executeQuery("select * from loan");
				System.out.println("Cust ID Branch ID Loan amount");
				while(rs.next()) {
					System.out.println(rs.getString(1)+"   "+rs.getString(2)+"   "+ rs.getString(3));
				}
				//step 5 close connection
				con.close();

			}
			
			catch(Exception e) {
				System.out.println(e);
			}
	
	}
	
	void viewTrans() {
		System.out.println("\nView Transactions");
		System.out.println("-------------");
		try {
	
			Class.forName("com.mysql.cj.jdbc.Driver");
	
			//Step2 Create connection 
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/bank","root","R00tR3d!");

			//Step3 Create Statement object
			Statement stmt = con.createStatement();
			//Step 4 Execute Query
			ResultSet rs = stmt.executeQuery("select * from trandetails");
			while(rs.next()) {
				System.out.println(rs.getString(1)+"   "+rs.getString(2)+"   "+ rs.getString(3)+"   "+rs.getString(4)+
						"   "+rs.getString(5)+"   "+rs.getString(6));
				
			}
			//step 5 close connection
			con.close();
		
		}
		
		catch(Exception e) {
			System.out.println(e);
		}
		
	}
}
