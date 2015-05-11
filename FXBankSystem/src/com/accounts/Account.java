/**
 * @mentor Professor Dr. Awny Alnusair<br><br>
 * 
 * @college Indiana University Kokomo<br><br><br>
 * 
 * @author Theral Jessop<br>
 * Apr 6, 2015<br>
 * Account.java<br>
 *
 */
package com.accounts;

import java.io.Serializable;
import com.customers.Customer;
import com.transactions.Transaction;

public abstract class Account implements Serializable{
	private static final long serialVersionUID = 2983907122991279834L;
	private String accountNumber;
	private double accountBalance;
	
	private Customer customer;
	private Transaction transaction;
	
	/** default constructor <br><br>
	 * 
	 * 
	 */
	public Account() {
	}
	
	/** account constructor<br><br>
	 * 
	 * this will add the account and customer to the account table<br>
	 * and set the active status of the customer to true<br><br>
	 * 
	 * @param accountNumber the account number of the account
	 * @param accountBalance the opening balance of the account
	 * @param customer the owner of the account
	 * 
	 */
	public Account(String accountNumber, double accountBalance, Customer customer, Transaction transaction){
		this.accountNumber = accountNumber;
		this.accountBalance = accountBalance;
		this.customer = customer;
	}
	
	// get
	
	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return this.accountNumber;
	}
	
	/**
	 * @return the accountBalance
	 */
	public double getAccountBalance() {
		return this.accountBalance;
	}
	
	/**
	 * @return the customer
	 */
	public Customer getCustomer() {
		return this.customer;
	}
	
	/**
	 * 
	 * @return the transaction
	 */
	public Transaction getTransaction() {
		return this.transaction;
	}
	// set 
	
	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	
	/**
	 * @param accountBalance the accountBalance to set
	 */
	public void setAccountBalance(double accountBalance) {
		this.accountBalance = accountBalance;
	}
	
	/**
	 * @param customer the customer to set
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}
	
	/** 
	 * 
	 * @param transaction the transaction to set
	 */
	public void setTransation(Transaction transaction) {
		this.transaction = transaction;
	}
	
	// ********************* abstract methods *********************
	
	abstract public boolean makeDeposit(double amount);
	
	abstract public double makeWithdrawal(double amount);
	
}
