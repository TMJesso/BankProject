/**
 * @mentor Professor Dr. Awny Alnusair<br><br>
 * 
 * @college Indiana University Kokomo<br><br><br>
 * 
 * @author Theral Jessop<br>
 * Apr 6, 2015<br>
 * Banker.java<br>
 *
 */
package com.utilities;

import java.util.*;

/**
 * general banking utilities used through out the application
 *
 */
public class BankUtilities {
	private static Random randGen = new Random();

	// generate unique info
	
	// *********************** generate unique transaction numbers ***********************
	public long generateUniqueTransNumber() {
		return Math.abs( Calendar.getInstance().getTimeInMillis());
	}

	// *********************** generate unique account numbers ***********************
	public int generateUniqueAccountNumber() {
		return genRandomNumber(10000, 1000000, 1000000);
		//return (int) Math.abs( Calendar.getInstance().getTimeInMillis());
	}
	
	// *********************** generate date ***********************
	public java.util.Date generateDate() {
		return new java.util.Date();
	}
	
	/** generate unique account number<br><br>
	 * 
	 * generates two random letters 
	 * 
	 * @return
	 */
	public String generateUniqueAcctNumber() {
		int num1 = 0, num2 = 0;
		num1 = genRandomNumber(65, 90, 100);
		num2 = genRandomNumber(65, 90, 100);
		while(num1 == num2) {
			num2 = genRandomNumber(65, 90, 100);
		}
		String output = "";
		output += (char) num1;
		output += (char) num2;
		output += genRandomNumber(1000, 10000, 10000);
		return output;
	}
	
	// generates a random number based upon the criteria that the user specifies by digits
	// makes the random number generator very portable between this and other methods
	public static int genRandomNumber(int minNumber, int maxNumber, int digits) {
		boolean chkExit = true;
		int isValueOk = randGen.nextInt(digits+1);
		while (chkExit) {
			if (isValueOk < minNumber || isValueOk > maxNumber) {
				isValueOk = randGen.nextInt(digits+1);
			} else {
				chkExit = false;
			}
		}
		return isValueOk;
	}

	

}
