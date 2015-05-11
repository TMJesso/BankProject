/**
 * @mentor Professor Dr. Awny Alnusair<br><br>
 * 
 * @college Indiana University Kokomo<br><br><br>
 * 
 * @author Theral Jessop<br>
 * 
 * Apr 6, 2015<br>
 * Gold.java<br>
 *
 */
package com.accounts;

import com.customers.Customer;
import com.transactions.Transaction;

/**
 * gold class
 * 
 *
 */
public class Gold extends Account {
	private static final long serialVersionUID = -144957960012585567L;
	private double goldInterestAmount;
	private double goldInterestRate;
	
	/** default constructor
	 * 
	 */
	public Gold() {
		
	}
	
	/**
	 * @param accountNumber
	 * @param accountBalance
	 * @param customer
	 */
	public Gold(String accountNumber, double accountBalance, Customer customer, Transaction transaction) {
		super(accountNumber, accountBalance, customer, transaction);
		this.goldInterestAmount = 0.0;
		this.goldInterestRate = 5.0;

	}

	/** all parameter constructor<br><br>
	 * 
	 * this is for adding individual records<br>
	 * nevertheless it is not necessary using serializable
	 * 
	 * @param accountNumber
	 * @param accountBalance
	 * @param customer
	 * @param transaction
	 * @param goldInterestAmount
	 * @param goldInterestRate
	 */
	public Gold(String accountNumber, double accountBalance, Customer customer, Transaction transaction, double goldInterestAmount, double goldInterestRate) {
		super(accountNumber, accountBalance, customer, transaction);
		this.goldInterestAmount = goldInterestAmount;
		this.goldInterestRate = goldInterestRate;
	}
	
	
	/**
	 * @return the goldInterestAmount
	 */
	public double getGoldInterestAmount() {
		return goldInterestAmount;
	}

	/**
	 * @return the goldInterestRate
	 */
	public double getGoldInterestRate() {
		return goldInterestRate;
	}

	/**
	 * @param goldInterestAmount the goldInterestAmount to set
	 */
	public void setGoldInterestAmount(double goldInterestAmount) {
		this.goldInterestAmount = goldInterestAmount;
	}

	/**
	 * @param goldInterestRate the goldInterestRate to set
	 */
	public void setGoldInterestRate(double goldInterestRate) {
		if (goldInterestRate < 0) {
			System.out.print("\nInvalid Gold interest rate! Reseting to default value!\n");
			goldInterestRate = 5.0;
		} else if (goldInterestRate > 0.0 && goldInterestRate < 1.0) {
			goldInterestRate *= 100;
		}
		this.goldInterestRate = goldInterestRate;
		System.out.print("\nGold interest rate successfully changed!\n");
	}
	
	/** make deposit<br><br>
	 * 
	 */
	public boolean makeDeposit(double amount) {
		if (amount < 0) {
			return false;
		}
		this.setAccountBalance(this.getAccountBalance() + amount);
		return true;
	}
	
	/** make withdrawal<br><br>
	 * 
	 */
	public double makeWithdrawal(double amount) {
		if (amount < 0) {
			return -1.0;
		}
		this.setAccountBalance(this.getAccountBalance() - amount);
		return amount;
	}

	// ********************************************** display gold account info **********************************************
	public String toString() {
			return String.format("%s \t %s \t %s \t $%12.2f \t %10.2f%% \t\t $%12.2f\n", "Gold", this.getCustomer(), this.getAccountNumber(), this.getAccountBalance(), this.getGoldInterestRate(), this.getGoldInterestAmount());
	}
}
