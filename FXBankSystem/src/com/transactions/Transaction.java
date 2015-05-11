package com.transactions;

import java.io.Serializable;

/** transaction class<br>
 * it tracks all transactions that change the account balance and is<br>
 * used throughout the client code<br>
 * 
 * @mentor Dr. Awny Alnusair<br><br>
 * 
 * @college Indiana University Kokomo<br><br><br>
 * 
 * @author Theral Jessop<br><br>
 * Apr 6, 2015<br>
 * Transaction.java<br>
 * 
 * 
 * transaction constructor<br>
 * 
 * @param createDate java.util.Date
 * @param customerID from the Customer
 * @param accountNumber from the Account
 * @param description a description of the transaction (Deposit, Withdraw, EOM calculations, etc.)
 * @param amount of the transaction
 * @param transactionNumber a unique transaction number
 */
public class Transaction implements Serializable{
	private static final long serialVersionUID = -3071907899278997557L;
	private java.util.Date createDate;
	private String customerID;
	private String accountNumber;
	private String description;
	private double amount;
	private long transactionNumber;
	
	/** default constructor
	 * 
	 */
	Transaction() {
	}
	
	/** transaction constructor<br><br>
	 * 
	 * this is for the account transactions<br>
	 * 
	 * @param createDate java.util.Date
	 * @param customerID from the Customer
	 * @param accountNumber from the Account
	 * @param description a description of the transaction (Deposit, Withdraw, EOM calculations, etc.)
	 * @param amount of the transaction
	 * @param transactionNumber a unique transaction number
	 */
	public Transaction(java.util.Date createDate, String customerID, String accountNumber, String description, double amount, long transactionNumber) {
		this.createDate = createDate;
		this.customerID = customerID;
		this.accountNumber = accountNumber;
		this.description = description;
		this.amount = amount;
		this.transactionNumber = transactionNumber;
	}
	
	/** transaction constructor<br><br>
	 * 
	 * this is for the customer transaction<br>
	 * 
	 * @param createDate
	 * @param customerID
	 * @param description
	 * @param transactionNumber
	 */
	public Transaction(java.util.Date createDate, String customerID, String description, long transactionNumber) {
		this.createDate = createDate;
		this.customerID = customerID;
		this.accountNumber = "No Accounts";
		this.description = description;
		this.amount = 0.00;
		this.transactionNumber = transactionNumber;
	}

	/**
	 * @return the createDate
	 */
	public java.util.Date getCreateDate() {
		return createDate;
	}

	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return customerID;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return the transactionNumber
	 */
	public long getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(java.util.Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	/**
	 * @param accountNumber the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @param transactionNumber the transactionNumber to set
	 */
	public void setTransactionNumber(int transactionNumber) {
		this.transactionNumber = transactionNumber;
	}
	
	// **************************************** display transactions ****************************************
	public String toString() {
		return String.format("%d \t %s \t %s \t %s \t %s \t\t $%12.2f \n", this.transactionNumber, this.createDate, this.customerID, this.accountNumber, this.description, this.amount);
	}
}
