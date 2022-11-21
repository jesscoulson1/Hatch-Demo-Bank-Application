package bankPrj;

import java.util.Scanner;

public class Menu {

	
	public static void main(String args[]) {
		String choice;
		boolean valid = false;
		Scanner scan = new Scanner(System.in);
		System.out.println("Welcome to the bank! ");
		System.out.println("----------------------");

		do {
			System.out.println("\nThe following options are avaliable: \nA. Login as customer\nB. Login as admin\nC. Exit\n");
			System.out.print("Please enter your choice (A/B/C): ");
		
			 choice = scan.next();
			if(choice.equalsIgnoreCase("A")) {
				Customer customer = new Customer();
				customer.verifyId();
				customer.menu();
				valid=false;
			}
			else if (choice.equalsIgnoreCase("B")) {
				Admin admin = new Admin();
				boolean validAID =admin.verifyId();
				admin.menu(validAID);
			
				valid = false;
			}
			else if(choice.equalsIgnoreCase("C")) {
				System.out.println("Thank you for using our bank!");
				System.out.println("Goodbye!");
				valid=true;
				break;
			}
			else {
				System.out.println("That was not a valid option, please enter A,B or C");
			}
		}
		while(valid==false);
	}
	
}
