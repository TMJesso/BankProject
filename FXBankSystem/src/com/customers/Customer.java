/**
 * @mentor Professor Dr. Awny Alnusair<br><br>
 * @college Indiana University Kokomo<br><br><br>
 * @course INFO-211 Informatics II<br><br>
 * 
 * 
 * @authors Ian Holtson<br>Jeremiah McKinney<br>Theral Jessop<br>
 * Apr 6, 2015<br>
 * Customer.java<br>
 *
 */
package com.customers;

import java.io.Serializable;

public class Customer implements Serializable{
	private static final long serialVersionUID = 6981023203602380035L;
	private String customerID;
	private String customerName;
	private boolean active;
	
	public Customer() {
		
	}
	/** customer constructor non-default<br><br>
	 * 
	 * 
	 * 
	 * @param id the customer ID
	 * @param name the customer name
	 * 
	 * and set the active status to false<br>
	 */
	public Customer(String id, String name) {
		this.customerID = id;
		this.customerName = name;
		this.active = false;
	}
	
	/** customer constructor for loading the individual data from the files<br><br>
	 * 
	 * if the customers are saved as an object this constructor is not needed<br>
	 * 
	 * @param id the customer ID
	 * @param name the customer name
	 * @param active this sets the status of the customer
	 */
	public Customer(String id, String name, boolean active) {
		this.customerID = id;
		this.customerName = name;
		this.active = active;
	}
	
	// get
	
	/**
	 * @return the customerID
	 */
	public String getCustomerID() {
		return this.customerID;
	}
	
	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return this.customerName;
	}
	
	/**
	 * 
	 * @return whether the customer is an active customer
	 */
	public boolean getActive() {
		return this.active;
	}
	
	// set
	
	/**
	 * @param customerID the customerID to set
	 */
	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}
	
	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	
	/**
	 * 
	 * @param active the active to set
	 */
	public void setActive(boolean active) {
		this.active = active;
	}

	// ***************************** display the customer info *****************************
	public String toString() {
		return String.format("%-10s \t %-40s \t\t %s", this.customerID, this.customerName, ((this.active)? "Active" : "Inactive"));
	}
	
	
}
