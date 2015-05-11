package com.utilities;

import java.io.*;
import java.util.*;

import javax.swing.*;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import com.accounts.Account;
import com.accounts.Checking;
import com.accounts.Gold;
import com.accounts.Regular;
import com.customers.Customer;
import com.transactions.Transaction;

// http://docs.oracle.com/javase/tutorial/essential/io/index.html

public class BankMethods {
	// file names for regular files and backup files and their paths
	public String[] filename = new String[10]; // contains the path and file names of data files
	public String[] backname = new String[10]; // contains the path and file name of the backup data files
	public String[] paths = new String[2];
	// text fill color and base color
	public String[] txtFillColor = new String[7];
	// fonts used
	public Font[] arialFont = new Font[5];
	public String bankName; // bank name that will be stored in the config dat file
	public int bakup; // a backup token that will keep track of the backup number so backups will never over write each other
	public double minBal;
	// in order to make a variable active for a individual form
	public String autoAcctNum;
	// the general bank containers
	public ArrayList<Customer> customers = new ArrayList<Customer>();
	public ArrayList<Account> accounts = new ArrayList<Account>();
	public ArrayList<Account> eomErrors = new ArrayList<Account>();
	public ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	// instance of the BankUtilities class
	public BankUtilities bu = new BankUtilities();

	/** load font<br><br>
	 * 
	 */
	public void loadFont() {
		arialFont[0] = Font.font("Arial", 14);
		arialFont[1] = Font.font("Arial", 18);
		arialFont[2] = Font.font("Arial", FontWeight.BOLD, 18);
		arialFont[3] = Font.font("Arial", 25);
		arialFont[4] = Font.font("Times New Romans", 14);
	}

	/** load colors<br><br>
	 * 
	 */
	public void loadColors() {
		txtFillColor[0] = "-fx-text-fill: blue;";
		txtFillColor[1] = "-fx-text-fill: red;";
		txtFillColor[2] = "-fx-text-fill: black;";
		txtFillColor[3] = "-fx-text-fill: white;";
		txtFillColor[4] = "-fx-text-fill: green;";
		txtFillColor[5] = "-fx-base: blue;";
		txtFillColor[6] = "-fx-base: red;";
	}
	/** load file names<br><br>
	 * 
	 * this also loads:<br>
	 * the backup<br>
	 * the paths<br>
	 * the fonts<br>
	 * and the colors<br>
	 * 
	 *  0 - config<br>
	 *  1 - customer<br>
	 *  2 - checking<br>
	 *  3 - regular<br>
	 *  4 - gold<br>
	 *  5 - transactions<br>
	 *  8 - accounts<br>
	 *  9 - oemErrors<br>
	 * 
	 */
	public void loadFileName() {
		loadPaths();
		loadBackup();
		loadFont();
		loadColors();
		filename[0] = paths[0] + "/config.dat"; // individually saved
		filename[1] = paths[0] + "/customers.dat"; // individually saved
		filename[2] = paths[0] + "/checking.dat"; // individually saved
		filename[3] = paths[0] + "/regular.dat"; // individually saved
		filename[4] = paths[0] + "/gold.dat"; // individually saved
		filename[5] = paths[0] + "/transactions.dat"; // individually saved
		filename[6] = paths[0] + "/oaccounts.dat"; // saved as an object
		filename[7] = paths[0] + "/ocustomers.dat"; // saved as an object
		filename[8] = paths[0] + "/otransactions.dat"; // saved as an object
		filename[9] = paths[0] + "/oeomerrors.dat"; // saved as an object
	}

	/** load backup<br><br>
	 * 
	 * this loads the filename that will be used to store backup copies of the files<br>
	 * the extension will be added when the file is initially created<br><br>
	 * 
	 * example: config0.dat and if that is used then config1.dat<br>
	 * 
	 */
	public void loadBackup() {
		loadPaths();
		backname[0] = paths[1] + "/config"; // individually saved
		backname[1] = paths[1] + "/customers"; // individually saved
		backname[2] = paths[1] + "/checking"; // individually saved
		backname[3] = paths[1] + "/regular"; // individually saved
		backname[4] = paths[1] + "/gold"; // individually saved
		backname[5] = paths[1] + "/transactions"; // individually saved
		backname[6] = paths[1] + "/oaccounts"; // saved as an object
		backname[7] = paths[1] + "/ocustomers"; // saved as an object
		backname[8] = paths[1] + "/otransactions"; // saved as an object
		backname[9] = paths[1] + "/oeomerrors"; // saved as an object
	}

	/** load paths<br><br>
	 * 
	 * this loads the paths into an array that will used to create the folders<br>
	 * for the first time and also this will add to the file names so the text<br>
	 * files will be stored in sub folders rather in the main folder with the source code<br>
	 * 
	 */
	public void loadPaths() {
		paths[0] = "data";
		paths[1] = "backup";
		try {
			File myPath;
			for (int x = 0; x < paths.length; x++) {
				myPath = new File(paths[x]);
				if (myPath.isDirectory()) {
					continue;
				} else {
					myPath.mkdir();
				}
			}
		} catch (SecurityException e) {
			JOptionPane.showMessageDialog(null, "Security setting for creating folders invalid! Unable to create Paths!", "Error Paths", JOptionPane.ERROR_MESSAGE);
		}
	}


	/** get customer pane<br><br>
	 * 
	 * declare the scene for making the customer screen for adding<br>
	 * new customers<br><br>
	 * 
	 * 0 - create Customer<br>
	 * 1 - lookup Customer<br>
	 * 
	 * 
	 * @return the boarder pane that will generate the stage and scene
	 */
	public BorderPane getCustPane(){

		// set up the labels
		Label lblID = new Label("Customer ID");
		lblID.setFont(arialFont[1]);
		lblID.setStyle(txtFillColor[4]);

		Label lblName = new Label("Customers Name");
		lblName.setFont(arialFont[1]);
		lblName.setStyle(txtFillColor[4]);

		Label lblMessage = new Label("");
		lblMessage.setFont(arialFont[1]);

		// create the text boxes and adjust their font and font size and establish the tool tips that
		// will be used for each individual box

		TextField txtID = new TextField();
		txtID.setFont(arialFont[1]);
		txtID.setStyle(txtFillColor[0]);
		txtID.setPromptText("Customer ID");
		txtID.setTooltip(new Tooltip("Type the number 0 and press Enter to automaticly generate Customer ID\nPress Enter to validate and verify this Customer ID\ndoes NOT already exists!"));

		TextField txtName = new TextField();
		txtName.setFont(arialFont[1]);
		txtName.setStyle(txtFillColor[0]);
		txtName.setPromptText("Customer Name");
		txtName.setTooltip(new Tooltip("Enter the Customer's First and Last name\nand press Enter"));

		// generate buttons and tool tips

		Button btnAdd = new Button("Add");
		btnAdd.setFont(arialFont[2]);
		btnAdd.setStyle(txtFillColor[5]);
		btnAdd.setTooltip(new Tooltip("Add entered customer into the bank system"));

		Button btnExit = new Button("Exit");
		btnExit.setFont(arialFont[2]);
		btnExit.setStyle(txtFillColor[6]);
		btnExit.setTooltip(new Tooltip("Exit!\nPressing this will not save the currently entered information!"));

		// put the ID label and text box together vertically

		VBox idPane = new VBox(2);
		idPane.setAlignment(Pos.CENTER);
		idPane.getChildren().addAll(lblID, txtID);

		// put the Name label and text box together vertically

		VBox namePane = new VBox(2);
		namePane.setAlignment(Pos.CENTER);
		namePane.getChildren().addAll(lblName, txtName);

		// put in a label that will be used as a message center for the user

		HBox messagePane = new HBox(1);
		messagePane.getChildren().add(lblMessage);
		messagePane.setAlignment(Pos.CENTER);

		// add the ID and Name panes horizontally to the txtPane which
		// will be put in the top pane of the window

		HBox txtPane = new HBox(2);
		txtPane.setPadding(new Insets(30,30,30,30));  
		txtPane.getChildren().addAll(idPane, namePane);
		txtPane.setAlignment(Pos.TOP_CENTER);

		// add the buttons horizontally to the bottom pane

		HBox bottomPane = new HBox(2);
		bottomPane.setPadding(new Insets(15,15,15,15));;
		bottomPane.getChildren().addAll(btnAdd, btnExit);
		bottomPane.setAlignment(Pos.CENTER);

		// generate the Panes and set the border color to blue

		BorderPane custMainPane = new BorderPane();
		custMainPane.setTop(txtPane);
		custMainPane.setCenter(messagePane);
		custMainPane.setBottom(bottomPane);
		custMainPane.setStyle("-fx-border-color: blue");

		// *************************************************** txtID - key pressed ***************************************************
		txtID.setOnKeyPressed(e -> {
			lblMessage.setText("");
		});

		// *************************************************** txtID - action ***************************************************
		txtID.setOnAction(e -> {
			if (txtID.getText().equals("0")) {
				txtID.setText(bu.generateUniqueAcctNumber());
			} else if (txtID.getText().isEmpty()) {
				lblMessage.setText("Customer ID cannot be blank!");
				lblMessage.setStyle(txtFillColor[1]);
				lblMessage.setFont(arialFont[1]);
				txtID.requestFocus();
			} else {
				boolean isOk = true;
				for (Customer c: customers) {
					if (c.getCustomerID().equals(txtID.getText())) {
						isOk = false;
					}
				}
				if (isOk) {
					txtName.requestFocus();
				} else {
					lblMessage.setText("Customer ID already Exists!\nPlease choose a different Customer ID");
					lblMessage.setStyle(txtFillColor[1]);
				}
			}
		});

		// *************************************************** txtName - key pressed ***************************************************
		txtName.setOnKeyPressed(e -> {
			lblMessage.setText(" ");
			lblMessage.setStyle(txtFillColor[2]);
		});

		// *************************************************** txtName - action ***************************************************
		txtName.setOnAction(e -> {
			if (txtName.getText().isEmpty()) {
				lblMessage.setText("You must enter the Customers Name!");
				lblMessage.setStyle(txtFillColor[1]);
			} else {
				btnAdd.requestFocus();
			}
		});

		// *************************************************** btnAdd - key pressed ***************************************************
		// 0 - customerID is empty<br>
		// 1 - customerName is empty<br>
		// 2 - unknown error occurred<br>
		// 3 - Customer ID already exists<br>
		// 4 - <br>
		// 5 - Customer was created successfully<br>
		// 6 - Customer was NOT created<br><br>

		btnAdd.setOnKeyPressed(e -> {
			int cc = 0;
			Stage myStage = (Stage) txtID.getScene().getWindow();
			cc = createCustomer(txtID, txtName, lblMessage);
			switch (cc) {
			case 0:
				lblMessage.setText("Customer ID is Empty!");
				lblMessage.setStyle(txtFillColor[1]);
				txtID.requestFocus();
				break;
			case 1:
				lblMessage.setText("Customer Name is Empty!");
				lblMessage.setStyle(txtFillColor[1]);
				txtName.requestFocus();
				break;
			case 2:
				lblMessage.setText("An Unknown Error Occurred! Please notify Technical Support!");
				lblMessage.setStyle(txtFillColor[1]);
				txtID.requestFocus();
				break;
			case 3:
				lblMessage.setText("Customer ID already Exists!");
				lblMessage.setStyle(txtFillColor[1]);
				txtID.requestFocus();
				break;
			case 5:
				lblMessage.setText("Customer was successfully created");
				lblMessage.setStyle(txtFillColor[0]);
				txtID.setText("");
				txtName.setText("");
				myStage.requestFocus();
				txtID.requestFocus();
				break;
			case 6:
				lblMessage.setText("*************************\nCustomer was NOT Created!\n*************************");
				lblMessage.setStyle(txtFillColor[1]);
				txtID.requestFocus();
				break;
			}
		});

		// *************************************************** btnAdd - action ***************************************************
		// 0 - customerID is empty<br>
		// 1 - customerName is empty<br>
		// 2 - unknown error occurred<br>
		// 3 - Customer ID already exists<br>
		// 4 - <br>
		// 5 - Customer was created successfully<br>
		// 6 - Customer was NOT created<br><br>

		btnAdd.setOnAction(e -> {
			int cc = 0;
			Stage myStage = (Stage) txtID.getScene().getWindow();
			cc = createCustomer(txtID, txtName, lblMessage);
			switch (cc) {
			case 0:
				lblMessage.setText("Customer ID is Empty!");
				lblMessage.setStyle(txtFillColor[1]);
				txtID.requestFocus();
				break;
			case 1:
				lblMessage.setText("Customer Name is Empty!");
				lblMessage.setStyle(txtFillColor[1]);
				txtName.requestFocus();
				break;
			case 2:
				lblMessage.setText("An Unknown Error Occurred! Please notify Technical Support!");
				lblMessage.setStyle(txtFillColor[1]);
				txtID.requestFocus();
				break;
			case 3:
				lblMessage.setText("Customer ID already Exists!");
				lblMessage.setStyle(txtFillColor[1]);
				txtID.requestFocus();
				break;
			case 5:
				lblMessage.setText("Customer was successfully created");
				lblMessage.setStyle(txtFillColor[0]);
				txtID.setText("");
				txtName.setText("");
				myStage.requestFocus();
				txtID.requestFocus();
				break;
			case 6:
				lblMessage.setText("*************************\nCustomer was NOT Created!\n*************************");
				lblMessage.setStyle(txtFillColor[1]);
				txtID.requestFocus();
				break;
			}
		});

		// *************************************************** btnExit - action ***************************************************
		btnExit.setOnAction(e -> {
			btnExit.getScene().getWindow().hide();
		});

		return custMainPane;
	}

	/** create Customer<br><br>
	 * 
	 * 0 - customerID is empty<br>
	 * 1 - customerName is empty<br>
	 * 2 - unknown error occurred<br>
	 * 3 - Cutomer ID already exists<br>
	 * 4 - <br>
	 * 5 - Customer was created successfully<br>
	 * 6 - Customer was NOT created<br><br>
	 * 
	 * a method that will perform the creation of the Customer<br>
	 * this will allow for multiple areas to create the Customer<br>
	 * and easier to maintain and debug the code<br>
	 * 
	 * @param txtID the TextField for CustomerID
	 * @param txtName the TextField for CustomerName
	 * @param message the message that will be displayed to the user on the form
	 * @return whether it was successful (true or false)
	 */
	public int createCustomer(TextField txtID, TextField txtName, Label lblMessage) {
		// what is the current size of the customer table (array list)
		// this will be used to determine if the customer was added successfully
		int element = customers.size();

		// did the user enter any information in the ID box
		if(txtID.getText().isEmpty()) {
			lblMessage.setText("Please Enter Customer ID");
			lblMessage.setFont(arialFont[1]);
			lblMessage.setStyle(txtFillColor[1]);
			return 0;
		} else if (txtName.getText().isEmpty()) { // did the user enter any information in the Name box
			lblMessage.setText("Please Enter Customer Name");
			lblMessage.setFont(arialFont[1]);
			lblMessage.setStyle(txtFillColor[1]);
			return 1;
		} else { // since there were no errors that I detected, add the customer to the table
			for (Customer c: customers) {
				if (c.getCustomerID().equals(txtID.getText())) {
					return 3;
				}
			}
			Customer customer = new Customer(txtID.getText(), txtName.getText());
			customers.add(customer);

			// check to see if the customer was added to the table and return true if yes and false if not
			if (element < customers.size()) {
				lblMessage.setText("Customer Created successfully!");
				lblMessage.setFont(arialFont[1]);
				lblMessage.setStyle(txtFillColor[0]);
				return 5;
			} else { // notify the user that the customer was not added
				if (element == customers.size()) {
					lblMessage.setText("Customer Creation Unsessful!!");
					lblMessage.setFont(arialFont[1]);
					lblMessage.setStyle(txtFillColor[1]);
					return 6;
				}
			}
		}
		// an unknown error occurred
		return 2;
	}

	/** add Account pane<br><br>
	 * 
	 * this paints the scene for the account pane<br>
	 * and generates the code for adding a new Account<br><br>
	 * 
	 * 0 - Checking<br>
	 * 1 - Regular<br>
	 * 2 - Gold<br>
	 * 
	 * @param acctNum this determines which account to create: Checking, Regular, or Gold
	 * @return the boarder pane that will generate the form
	 */
	public BorderPane addAccountPane(int acctNum) {

		// declare and initialize account type variables

		String acctType = "";

		switch (acctNum) {
		case 0: // checking
			acctType = "Checking Account";
			autoAcctNum = "CA";
			break;
		case 1:// regular
			acctType = "Regular Account";
			autoAcctNum = "RA";
			break;
		case 2: // gold
			acctType = "Gold Account";
			autoAcctNum = "GA";
			break;
		default:
		}

		// declare buttons and set their styles

		Button btnAdd = new Button("Add");
		btnAdd.setFont(arialFont[1]);
		btnAdd.setStyle(txtFillColor[5]);
		btnAdd.setTooltip(new Tooltip("Add this Account into the banking system"));

		Button btnExit = new Button("Exit");
		btnExit.setFont(arialFont[1]);
		btnExit.setStyle(txtFillColor[6]);
		btnExit.setTooltip(new Tooltip("Exit! \nPressing this will not save the currently entered information!"));

		// declare text area and set their styles and tooltips

		TextArea listCust = new TextArea();
		listCust.setPrefRowCount(5);
		listCust.setPrefColumnCount(35);
		listCust.setEditable(false);
		listCust.setTooltip(new Tooltip("Highlight the Customer ID you want and it will be put in the Customer ID box!"));
		listCust.setFont(arialFont[1]);

		// create left pane with listCust

		FlowPane leftPane = new FlowPane();
		leftPane.setAlignment(Pos.CENTER);
		leftPane.setHgap(5);
		leftPane.setVgap(5);
		leftPane.getChildren().addAll(listCust);

		// set up the account type heading

		Label lbltxtDisplayBoxAccounts = new Label(acctType);  // heading
		lbltxtDisplayBoxAccounts.setFont(arialFont[3]);
		lbltxtDisplayBoxAccounts.setStyle(txtFillColor[0]);

		// set up the error display message area

		Label lbltxtDisplayBoxMessage = new Label(" "); // clear error reporting

		// declare the heading and labels

		Label lblAccountNumber = new Label("Account Number");
		lblAccountNumber.setFont(arialFont[1]);
		lblAccountNumber.setStyle(txtFillColor[0]);

		Label lblCustomerID = new Label("Customer ID");
		lblCustomerID.setFont(arialFont[1]);
		lblCustomerID.setStyle(txtFillColor[0]);

		Label lblOpeningBalance = new Label("Opening Balance");
		lblOpeningBalance.setFont(arialFont[1]);
		lblOpeningBalance.setStyle(txtFillColor[0]);

		// declare the text boxes and tool tips

		TextField txtAccountNumber = new TextField();
		txtAccountNumber.setFont(arialFont[1]);
		txtAccountNumber.setStyle(txtFillColor[0]);
		txtAccountNumber.setPromptText("Hover for Instructions");
		txtAccountNumber.setTooltip(new Tooltip("Type the number 0 and press Enter to generate automatic Account Number\nPress Enter between fields"));

		TextField txtCustomerID = new TextField();
		txtCustomerID.setFont(arialFont[1]);
		txtCustomerID.setStyle(txtFillColor[0]);
		txtCustomerID.setPromptText("Customer ID");
		txtCustomerID.setTooltip(new Tooltip("Enter Customer ID or type the number 0 to show a list of available customers\nPress Enter between fields"));

		TextField txtOpeningBalance = new TextField();
		txtOpeningBalance.setFont(arialFont[1]);
		txtOpeningBalance.setStyle(txtFillColor[0]);
		txtOpeningBalance.setPromptText("Example: 789.74");
		txtOpeningBalance.setTooltip(new Tooltip("Opening Balance of this Account, Example: 854.34\nPress Enter between fields"));

		// put account number label and text box together in a vertical box

		VBox accountPane = new VBox(2);
		accountPane.getChildren().addAll(lblAccountNumber, txtAccountNumber);
		accountPane.setAlignment(Pos.CENTER);

		// put the customer id label and text box together in a vertical box

		VBox customerPane = new VBox(2);
		customerPane.getChildren().addAll(lblCustomerID,txtCustomerID);
		customerPane.setAlignment(Pos.CENTER);

		// put the opening balance label and text box in a vertical box

		VBox balancePane = new VBox(2);
		balancePane.getChildren().addAll(lblOpeningBalance,txtOpeningBalance);
		balancePane.setAlignment(Pos.CENTER);

		// put the display message label in a horizontal box

		HBox accountDisplayPane = new HBox(1);
		accountDisplayPane.setPadding(new Insets(20,15,25,15));
		accountDisplayPane.getChildren().addAll(lbltxtDisplayBoxAccounts);
		accountDisplayPane.setAlignment(Pos.CENTER);

		// add the customer, account, and opeening balance panes into an horizontal box

		HBox topPane = new HBox(3);
		topPane.setPadding(new Insets(15,15,15,15));
		topPane.getChildren().addAll(customerPane,accountPane,balancePane);
		topPane.setAlignment(Pos.CENTER);

		// add the account type display label and top pane into a vertical box

		VBox finaTopPane = new VBox(2);
		finaTopPane.getChildren().addAll(accountDisplayPane, topPane);
		finaTopPane.setAlignment(Pos.CENTER);

		// add the label display box to a horizontal box

		VBox centerPane = new VBox(2);
		centerPane.setPadding(new Insets(55,15,55,15));
		centerPane.getChildren().addAll(lbltxtDisplayBoxMessage, leftPane);
		centerPane.setAlignment(Pos.CENTER);

		// add the buttons to the bottom pane

		HBox bottomPane = new HBox(2);
		bottomPane.setPadding(new Insets(15,15,15,15));
		bottomPane.getChildren().addAll(btnAdd, btnExit);
		bottomPane.setAlignment(Pos.CENTER);

		// generate the actual panes that will create the form
		// and return it to the scene

		BorderPane mainPane = new BorderPane();
		mainPane.setTop(finaTopPane);
		mainPane.setCenter(centerPane);
		mainPane.setBottom(bottomPane);
		mainPane.setStyle("-fx-border-color: blue");


		// *********************************************** txtAccountNumber - key pressed ***********************************************

		txtAccountNumber.setOnKeyPressed(e -> {
			acctKeyAction(txtAccountNumber, txtOpeningBalance, lbltxtDisplayBoxMessage);
		});


		// *********************************************** txtAccountNumber - action ***********************************************
		txtAccountNumber.setOnAction(e -> {
			acctAction(txtAccountNumber, txtOpeningBalance, lbltxtDisplayBoxMessage);
		});

		// *********************************************** listCust - mouse clicked ***********************************************
		listCust.setOnMouseClicked(e -> {
			txtCustomerID.setText(listCust.getSelectedText().trim());
			txtCustomerID.requestFocus();
		});

		// *********************************************** txtCustomerID - action ***********************************************
		txtCustomerID.setOnAction(e -> {
			boolean isOk = false;
			// if the user enters 0 show customers in list box
			if (txtCustomerID.getText().equals("0")) {
				listCust.clear();
				for (Customer c: customers) {
					listCust.appendText(c.toString() + "\n");
				}
				listCust.setFont(arialFont[1]);
				listCust.setStyle(txtFillColor[0]);
			} else {
				// check to see if the customer ID entered exists
				Customer customer = customers.get(0);
				for (Customer c: customers) {
					if (c.getCustomerID().equals(txtCustomerID.getText())) {
						isOk = true;
						customer = c;
						break;
					}
				}
				// was it found
				if (isOk) {
					// show customer to user and move to next field
					lbltxtDisplayBoxMessage.setText(customer.getCustomerID() + "\t\t" + customer.getCustomerName() + "\t\tFound!");
					lbltxtDisplayBoxMessage.setStyle(txtFillColor[0]);
					lbltxtDisplayBoxMessage.setFont(arialFont[1]);
					txtAccountNumber.requestFocus();
				} else {
					// display error message to user
					lbltxtDisplayBoxMessage.setText("Customer ID was NOT found! Please re-enter Customer ID!");
					lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
					lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				}
			}
		});


		// *********************************************** txtCustomerID - key pressed ***********************************************

		txtCustomerID.setOnKeyPressed(e -> {
			lbltxtDisplayBoxMessage.setText(" ");
		});

		// *********************************************** txtOpeningBalance - key pressed ***********************************************

		txtOpeningBalance.setOnKeyPressed(e -> {
			lbltxtDisplayBoxMessage.setText(" ");
		});

		// *********************************************** txtOpeningBalance - action ***********************************************

		txtOpeningBalance.setOnAction(e -> {
			double amount = -1;
			try {
				// validate opening balance
				amount = Double.parseDouble(txtOpeningBalance.getText());
				if (amount < 0.0) {
					lbltxtDisplayBoxMessage.setText("Negative Opening Balances are not allowed!\nPlease enter a positive Opening Balance!");
					lbltxtDisplayBoxMessage.setFont(arialFont[1]);
					lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
					Stage myStage = (Stage) txtOpeningBalance.getScene().getWindow();
					myStage.requestFocus();
					txtOpeningBalance.requestFocus();
				} else {
					btnAdd.requestFocus();
				}
			} catch (NumberFormatException h) {
				lbltxtDisplayBoxMessage.setText("I was expecting a Account Opening Balance, Please re-enter");
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				txtOpeningBalance.requestFocus();
			} catch (InputMismatchException  g) {
				lbltxtDisplayBoxMessage.setText("You must enter the Opening Balance to the Account");
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				txtOpeningBalance.requestFocus();
			} catch (NoSuchElementException f) {
				lbltxtDisplayBoxMessage.setText("Out of Range, Please re-enter the Opening Balance Ammount!");
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				txtOpeningBalance.requestFocus();
			}
		});

		// *********************************************** btnAdd - key pressed ***********************************************
		//  0 - customer id is empty
		//  1 - account number is empty
		//  2 - opening balance is empty
		//  3 - account number already exists
		//  4 - opening balance is negative
		//  5 - customer id was not found
		//  6 - invalid opening balance
		//  7 - 
		//  8 - 
		//  9 - 
		// 10 - account successfully created
		// 11 - account was not created

		btnAdd.setOnKeyPressed(e -> {
			int isAcctOk = 0;
			// validate and create account
			isAcctOk = createAccount( txtAccountNumber, txtCustomerID, txtOpeningBalance, acctNum);
			// display results to user
			switch (isAcctOk) {
			case 0:
				lbltxtDisplayBoxMessage.setText("Customer ID is Empty!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtCustomerID.requestFocus();
				break;
			case 1:
				lbltxtDisplayBoxMessage.setText("Account Number is Empty!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtAccountNumber.requestFocus();
				break;
			case 2:
				lbltxtDisplayBoxMessage.setText("Opening Balance is Empty!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtOpeningBalance.requestFocus();
				break;
			case 3:
				lbltxtDisplayBoxMessage.setText("Account Number already exists!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtAccountNumber.requestFocus();
				break;
			case 4: 
				lbltxtDisplayBoxMessage.setText("Opening Balance is negative! Cannot open Account with a negative balance!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtOpeningBalance.requestFocus();
				break;
			case 5:
				lbltxtDisplayBoxMessage.setText("Customer ID was not found!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtCustomerID.requestFocus();
				break;
			case 6:
				lbltxtDisplayBoxMessage.setText("Invalid Opening Balance! Must be in dollar format! (example 56.23)");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtOpeningBalance.requestFocus();
				break;
			case 10:
				lbltxtDisplayBoxMessage.setText("Account created Successfully!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[0]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);	    		 
				txtAccountNumber.setDisable(false);
				txtCustomerID.setText("");
				txtAccountNumber.setText("");
				txtOpeningBalance.setText("");
				listCust.clear();
				txtCustomerID.requestFocus();
				break;
			case 11:
				lbltxtDisplayBoxMessage.setText("Account was NOT created!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtCustomerID.requestFocus();
				break;
			}
		});

		// *********************************************** btnAdd - action ***********************************************
		//  0 - customer id is empty
		//  1 - account number is empty
		//  2 - opening balance is empty
		//  3 - account number already exists
		//  4 - opening balance is negative
		//  5 - customer id was not found
		//  6 - invalid opening balance
		//  7 - 
		//  8 - 
		//  9 - 
		// 10 - account successfully created
		// 11 - account was not created

		btnAdd.setOnAction(e -> {
			int isAcctOk = 0;
			// validate and create account
			isAcctOk = createAccount( txtAccountNumber, txtCustomerID, txtOpeningBalance, acctNum);
			// display results to user
			switch (isAcctOk) {
			case 0:
				lbltxtDisplayBoxMessage.setText("Customer ID is Empty!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtCustomerID.requestFocus();
				break;
			case 1:
				lbltxtDisplayBoxMessage.setText("Account Number is Empty!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtAccountNumber.requestFocus();
				break;
			case 2:
				lbltxtDisplayBoxMessage.setText("Opening Balance is Empty!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtOpeningBalance.requestFocus();
				break;
			case 3:
				lbltxtDisplayBoxMessage.setText("Account Number already exists!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtAccountNumber.requestFocus();
				break;
			case 4: 
				lbltxtDisplayBoxMessage.setText("Opening Balance is negative! Cannot open Account with a negative balance!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtOpeningBalance.requestFocus();
				break;
			case 5:
				lbltxtDisplayBoxMessage.setText("Customer ID was not found!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtCustomerID.requestFocus();
				break;
			case 6:
				lbltxtDisplayBoxMessage.setText("Invalid Opening Balance! Must be in dollar format! (example 56.23)");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtOpeningBalance.requestFocus();
				break;
			case 10:
				lbltxtDisplayBoxMessage.setText("Account created Successfully!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[0]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);	    		 
				txtAccountNumber.setDisable(false);
				txtCustomerID.setText("");
				txtAccountNumber.setText("");
				txtOpeningBalance.setText("");
				txtCustomerID.requestFocus();
				break;
			case 11:
				lbltxtDisplayBoxMessage.setText("Account was NOT created!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtCustomerID.requestFocus();
				break;
			}
		});

		// if the user clicks the exit button close the window
		btnExit.setOnAction(e -> {
			btnExit.getScene().getWindow().hide();
		});		     
		return mainPane;
	}

	/** account action<br><br>
	 * 
	 * account action processes the account number text field<br>
	 * 
	 * @param txtAccountNumber
	 */
	public void acctAction(TextField txtAccountNumber, TextField txtOpeningBalance, Label lbltxtDisplayBoxMessage) {
		boolean isAcctFound = false;
		if (txtAccountNumber.getText().equals("0")) {
			txtAccountNumber.setText(autoAcctNum + bu.generateUniqueAccountNumber());
		} else if (txtAccountNumber.getText().isEmpty()){
			lbltxtDisplayBoxMessage.setText("Account Number cannot be blank!\nPlease enter Account Number or press 0 to automaticlly generate one!");
			lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
			lbltxtDisplayBoxMessage.setFont(arialFont[1]);
			txtAccountNumber.requestFocus();
		} else {
			for (Account a: accounts) {
				if (a.getAccountNumber().equals(txtAccountNumber.getText())) {
					isAcctFound = true;
				}
			}
			if (isAcctFound) {
				lbltxtDisplayBoxMessage.setText("Account Number already used! Please choose a different Account Number!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
			} else {
				lbltxtDisplayBoxMessage.setText("Account Number accepted!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[0]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
				txtOpeningBalance.requestFocus();
			}
		}

	}

	/** check status<br><br>
	 * 
	 * this checks the status of the customer and to see<br>
	 * if the customer has an account and sets the active<br>
	 * flag accordingly<br><br>
	 * 
	 * true:  has an account<br>
	 * false: does NOT have an account<br>
	 * 
	 */
	public void checkStatus() {
		// first if the customer table is empty don't do anything
		if (customers.isEmpty()) {
			return;
		// then if the account table is empty then just loop through the customer table
		// and set all the flags to false
		} else if (accounts.isEmpty()) {
			for (Customer c: customers) {
				c.setActive(false);
			}
		// customers and accounts present
		// loop through the customers and accounts and compare customer ID's
		// if a customer has an account set the flag to true
		} else {
			for (Customer c: customers) {
				c.setActive(false);
				for (Account a: accounts) {
					if (c.getCustomerID().equals(a.getCustomer().getCustomerID())) {
						c.setActive(true);
						break;
					}
				}
			}
		}
	}

	/** account action<br><br>
	 * 
	 * account action processes the account number text field<br>
	 * 
	 * @param txtAccountNumber
	 */
	public void acctKeyAction(TextField txtAccountNumber, TextField txtOpeningBalance, Label lbltxtDisplayBoxMessage) {
		boolean isAcctFound = false;
		// user enters 0 in the account number then generate an account number
		if (txtAccountNumber.getText().equals("0")) {
			txtAccountNumber.setText(autoAcctNum + bu.generateUniqueAccountNumber());
		// did the user enter anything
		} else if (txtAccountNumber.getText().isEmpty()){
			lbltxtDisplayBoxMessage.setText("Account Number cannot be blank!\nPlease enter Account Number or press 0 to automaticlly generate one!");
			lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
			lbltxtDisplayBoxMessage.setFont(arialFont[1]);
			txtAccountNumber.requestFocus();
			
		} else {
			// check to see if the entered account already exists
			for (Account a: accounts) {
				if (a.getAccountNumber().equals(txtAccountNumber.getText())) {
					isAcctFound = true;
				}
			}
			// notify user account already exists
			if (isAcctFound) {
				lbltxtDisplayBoxMessage.setText("Account Number already used! Please choose a different Account Number!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[1]);
			// notify user the account nunber can be used
			} else {
				lbltxtDisplayBoxMessage.setText("Account Number accepted!");
				lbltxtDisplayBoxMessage.setStyle(txtFillColor[0]);
				lbltxtDisplayBoxMessage.setFont(arialFont[1]);
			}
		}

	}


	// ********************************************************************** processTransaction pane **********************************************************************

	/** process transaction pane<br><br>
	 * 
	 * this will paint the window for making deposits and withdrawals<br>
	 * the following types are allowed<br><br>
	 * 
	 * 0 - deposit<br>
	 * 1 - withdrawal<br>
	 * 
	 * @param tranType this will tell the window which type is calling
	 * @return it will return the format to the stage that is calling
	 */
	public BorderPane processTransactionsPane(int tranType){
		// declare and initialize the type of transaction
		// 0 - deposit
		// 1 - withdrawal

		String strTitle = "";

		switch (tranType) {
		case 0:
			strTitle = "Make Deposit";
			break;
		case 1:
			strTitle = "Make Withdrawal";
			break;
		}

		// declare labels

		Label lblPageTitle = new Label(strTitle);
		lblPageTitle.setFont(arialFont[3]);
		lblPageTitle.setStyle(txtFillColor[0]);

		Label lblAccountNum = new Label("Account #");
		lblAccountNum.setFont(arialFont[1]);
		lblAccountNum.setStyle(txtFillColor[0]);

		Label lblAmount = new Label("Amount");
		lblAmount.setFont(arialFont[1]);
		lblAmount.setStyle(txtFillColor[0]);

		Label lblSuccess = new Label("");
		lblSuccess.setFont(arialFont[1]);
		lblSuccess.setStyle(txtFillColor[1]);

		// declare text fields

		TextField txtAccountNum = new TextField();
		txtAccountNum.setFont(arialFont[1]);
		txtAccountNum.setStyle(txtFillColor[0]);
		txtAccountNum.setTooltip(new Tooltip("Type 0 and press Enter to look up Account Numbers"));
		txtAccountNum.setPromptText("Bank Account Number");

		TextField txtAmount = new TextField();
		txtAmount.setFont(arialFont[1]);
		txtAmount.setStyle(txtFillColor[0]);
		txtAmount.setTooltip(new Tooltip("Enter the Opening Balance"));
		txtAmount.setPromptText("Example: 853.46");

		// declare text areas

		TextArea lstAccount = new TextArea();
		lstAccount.setPrefRowCount(5);
		lstAccount.setEditable(false);
		lstAccount.setFont(arialFont[1]);
		lstAccount.setStyle(txtFillColor[0]);

		// declare buttons

		Button btnAdd = new Button("Add");
		btnAdd.setFont(arialFont[2]);
		btnAdd.setStyle(txtFillColor[5] + txtFillColor[3]);
		btnAdd.setDisable(true);

		Button btnExit = new Button("Exit");
		btnExit.setFont(arialFont[2]);
		btnExit.setStyle(txtFillColor[6] + txtFillColor[3]);

		// set up top pane

		HBox topPane = new HBox(4);
		topPane.setPadding(new Insets(35, 5, 35,5));
		topPane.getChildren().add(lblPageTitle);
		topPane.setAlignment(Pos.TOP_CENTER);

		// set up account pane

		VBox accountPane = new VBox(5);
		accountPane.getChildren().addAll(lblAccountNum, txtAccountNum);
		accountPane.setAlignment(Pos.CENTER);

		// set up amount pane

		VBox amountPane = new VBox(5);
		amountPane.getChildren().addAll(lblAmount, txtAmount);
		amountPane.setAlignment(Pos.CENTER);

		// group account pane and amount pane together
		HBox groupPane = new HBox(5);
		groupPane.getChildren().addAll(accountPane, amountPane);
		groupPane.setAlignment(Pos.CENTER);

		// set up list pane

		VBox listPane = new VBox(5);
		listPane.getChildren().add(lstAccount);
		listPane.setMaxWidth(325);
		listPane.setAlignment(Pos.CENTER);

		// set up error and message pane

		VBox successPane = new VBox(5);
		successPane.setPadding(new Insets(35,5,35,5));
		successPane.getChildren().add(lblSuccess);
		successPane.setAlignment(Pos.CENTER);

		// set up center pane with group pane, error and message pane, list pane

		VBox centerPane = new VBox(5);
		centerPane.getChildren().addAll(groupPane, successPane, listPane);
		centerPane.setAlignment(Pos.CENTER);

		// set up button pane
		HBox bottomPane = new HBox(5);
		bottomPane.setPadding(new Insets(15,5,15,5));
		bottomPane.getChildren().addAll(btnAdd, btnExit);
		bottomPane.setAlignment(Pos.BOTTOM_CENTER);

		// set up the panes to display

		BorderPane mainPane = new BorderPane();
		mainPane.setTop(topPane);
		mainPane.setCenter(centerPane);
		mainPane.setBottom(bottomPane);
		mainPane.setStyle("-fx-border-color: blue");

		// ******************************************************** txtAccountNum - action ********************************************************
		
		txtAccountNum.setOnAction(e -> {
			// user enters 0 then list the available accounts
			if (txtAccountNum.getText().equals("0")) {
				for (Account a: accounts) {
					lstAccount.appendText(a.getAccountNumber() + "\n");
				}
			// did the user enter anything
			} else if (txtAccountNum.getText().isEmpty()) {
				lblSuccess.setText("You must enter an Account Number!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAccountNum.requestFocus();
			// validate account number
			} else {
				boolean isFound = false;
				for (Account a: accounts) {
					if (a.getAccountNumber().equals(txtAccountNum.getText())) {
						isFound = true;
						break;
					}
				}
				// was it found
				if (isFound) {
					lblSuccess.setText("Account Number Found!");
					btnAdd.setDisable(false);
					lblSuccess.setStyle(txtFillColor[0]);
					txtAmount.requestFocus();
				// notify the user that the account number was not found
				} else {
					lblSuccess.setText("Account Number was not found! Please try again!");
					lblSuccess.setStyle(txtFillColor[1]);
					txtAccountNum.requestFocus();
				}
			}
		});

		// ******************************************************** txtAccountNum - key pressed ********************************************************
		
		txtAccountNum.setOnKeyPressed(e -> {
			lblSuccess.setText(" ");
		});

		// ******************************************************** txtAmount - action ********************************************************
		
		txtAmount.setOnAction(e -> {
			if (txtAmount.getText().isEmpty()) {
				lblSuccess.setText("You must enter an Amount!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAmount.requestFocus();
				btnAdd.setDisable(true);
			} else {
				btnAdd.requestFocus();
			}
		});

		// ******************************************************** txtAmount - key pressed  ********************************************************
		txtAmount.setOnKeyPressed(e -> {
			lblSuccess.setText(" ");
		});

		// ******************************************************** button add - action ********************************************************
		//  0 - txtAccountNum cannot be empty
		//  1 - txtAmount cannot be empty
		//  2 - txtAmount cannot be negative
		//  3 - txtAccountNum was NOT found
		//  4 - 
		//  5 - Deposit successful
		//  6 - Deposit was NOT successful


		//  7 - Withdrawal was NOT successful
		//  8 - Withdrawal was successful
		//  9 - 
		// 10 - 
		// 11 - 
		// 12 - Unknown Error has occurred!
		btnAdd.setOnAction(e -> {
			int intButton = 0;
			// validate amount and account number
			intButton = chkButtonAdd(txtAmount, txtAccountNum, lblSuccess, tranType);
			// display results to the user
			switch (intButton) {
			case 0:
				lblSuccess.setText("Account Number CANNOT be blank!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAccountNum.requestFocus();
				break;
			case 1:
				lblSuccess.setText("Deposit Amount CANNOT be blank!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAmount.requestFocus();
				break;
			case 2:
				lblSuccess.setText("Deposit Amount CANNOT be negative!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAmount.requestFocus();
				break;
			case 3:
				lblSuccess.setText("Account Number was NOT found!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAccountNum.requestFocus();
				break;
			case 5:
				lblSuccess.setText("Deposit was successfully applied!");
				lblSuccess.setStyle(txtFillColor[4]);
				txtAccountNum.setText("");
				txtAmount.setText("");
				lstAccount.clear();
				txtAccountNum.requestFocus();
				break;
			case 6:
				lblSuccess.setText("Depost was NOT successful!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAccountNum.requestFocus();
				break;
			case 7:
				lblSuccess.setText("Withdrawal was NOT successful!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAccountNum.requestFocus();
				break;
			case 8:
				lblSuccess.setText("Withdrawal was successfully applied!");
				lblSuccess.setStyle(txtFillColor[4]);
				txtAccountNum.setText("");
				txtAmount.setText("");
				lstAccount.clear();
				txtAccountNum.requestFocus();
				break;
			case 12:
				lblSuccess.setText("Unknown Error: Please contact Customer Support!");
				lblSuccess.setStyle(txtFillColor[1]);
				lblSuccess.setFont(arialFont[2]);
				txtAccountNum.requestFocus();
				break;
			}
		});

		// ******************************************************** button add - key pressed ********************************************************
		//  0 - txtAccountNum cannot be empty
		//  1 - txtAmount cannot be empty
		//  2 - txtAmount cannot be negative
		//  3 - txtAccountNum was NOT found
		//  4 - 
		//  5 - Deposit successful
		//  6 - Deposit was NOT successful


		//  7 - Withdrawal was NOT successful
		//  8 - Withdrawal was successful
		//  9 - 
		// 10 - 
		// 11 - 
		// 12 - Unknown Error has occurred!

		btnAdd.setOnKeyPressed(e -> {
			int intButton = 0;
			// validate amount and account number
			intButton = chkButtonAdd(txtAmount, txtAccountNum, lblSuccess, tranType);
			// display results to user
			switch (intButton) {
			case 0:
				lblSuccess.setText("Account Number CANNOT be blank!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAccountNum.requestFocus();
				break;
			case 1:
				lblSuccess.setText("Deposit Amount CANNOT be blank!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAmount.requestFocus();
				break;
			case 2:
				lblSuccess.setText("Deposit Amount CANNOT be negative!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAmount.requestFocus();
				break;
			case 3:
				lblSuccess.setText("Account Number was NOT found!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAccountNum.requestFocus();
				break;
			case 5:
				lblSuccess.setText("Deposit was successfully applied!");
				lblSuccess.setStyle(txtFillColor[4]);
				txtAccountNum.setText("");
				txtAmount.setText("");
				lstAccount.clear();
				txtAccountNum.requestFocus();
				break;
			case 6:
				lblSuccess.setText("Depost was NOT successful!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAccountNum.requestFocus();
				break;
			case 7:
				lblSuccess.setText("Withdrawal was NOT successful!");
				lblSuccess.setStyle(txtFillColor[1]);
				txtAccountNum.requestFocus();
				break;
			case 8:
				lblSuccess.setText("Withdrawal was successfully applied!");
				lblSuccess.setStyle(txtFillColor[4]);
				txtAccountNum.setText("");
				txtAmount.setText("");
				lstAccount.clear();
				txtAccountNum.requestFocus();
				break;
			case 12:
				lblSuccess.setText("Unknown Error: Please contact Customer Support!");
				lblSuccess.setStyle(txtFillColor[1]);
				lblSuccess.setFont(arialFont[2]);
				txtAccountNum.requestFocus();
				break;
			}

		});

		// ******************************************************** button exit ********************************************************
		
		btnExit.setOnAction(e -> {
			btnExit.getParent().getScene().getWindow().hide();
		});

		// ******************************************************** lstAccount - on mouse clicked ********************************************************
		// put the selected text from the list box into the account number field
		
		lstAccount.setOnMouseClicked(e -> {
			txtAccountNum.setText(lstAccount.getSelectedText().trim());
			txtAccountNum.requestFocus();
		});
		return mainPane;
	}

	//  0 - txtAccountNum cannot be empty
	//  1 - txtAmount cannot be empty
	//  2 - txtAmount cannot be negative
	//  3 - txtAccountNum was NOT found
	//  4 - 
	//  5 - Deposit successful
	//  6 - Deposit was NOT successful


	//  7 - Withdrawal was NOT successful
	//  8 - Withdrawal was successful
	//  9 - 
	// 10 - 
	// 11 - 
	// 12 - Unknown Error has occurred!
	/** check button add<br><br>
	 * 
	 * tranType<br>
	 * --------------------<br>
	 * 0 - Deposit<br>
	 * 1 - Withdrawal<br>
	 * 
	 * @param txtAmount the amount the user enters on the form
	 * @param txtAccountNum the account number the user enters on the form
	 * @param lblSuccess the message box to communicate with the user
	 * @param tranType the type of transaction (0 - Deposit, 1 - Withdrawal)
	 * @return
	 */
	public int chkButtonAdd(TextField txtAmount, TextField txtAccountNum, Label lblSuccess, int tranType) {

		Account account = accounts.get(0);
		String desc = "";
		double amount = getAmount(txtAmount), oldAmount = 0.0;
		if (txtAccountNum.getText().isEmpty()) {
			return 0;
		} else if (txtAmount.getText().isEmpty()) {
			txtAmount.setText("0.00");
			return 1;
		} else {
			// user is depositing
			if (tranType == 0) { // deposit
				boolean isOk = false, isFound = false;
				// validate account number
				for (Account a: accounts) {
					if (a.getAccountNumber().equals(txtAccountNum.getText())) {
						account = a;
						isOk = a.makeDeposit(amount);
						isFound = true;
						break;
					}
				}
				// notify the user account number NOT found
				if (!isFound) {
					return 3;
				}
				// set up description for transaction
				if (account instanceof Checking) {
					desc = "Checking Account Deposit";
				} else if (account instanceof Regular) {
					desc = "Regular Account Deposit ";
				} else if (account instanceof Gold) {
					desc = "Gold  Account  Deposit  ";
				}
				// was the deposit successful, create a transaction 
				if (isOk) {
					createTransaction(account.getCustomer().getCustomerID(), txtAccountNum.getText(), desc, amount);
					lblSuccess.setText("Successfully Deposited the funds!");
					lblSuccess.setStyle(txtFillColor[0]);
					txtAccountNum.setText("");
					txtAmount.setText("");
					txtAccountNum.requestFocus();
					return 5;
				// notify user deposit was not successful
				} else {
					lblSuccess.setText("Deposit was NOT successFul!");
					lblSuccess.setStyle(txtFillColor[1]);
					txtAccountNum.requestFocus();
					return 6;
				}
			// user is withdrawing
			} else if (tranType == 1) { // ****************************************** withdrawal ******************************************
				boolean isFound = false;
				oldAmount = amount;
				// validate account number
				for (Account a: accounts) {
					if (a.getAccountNumber().equals(txtAccountNum.getText())) {
						account = a;
						amount = account.makeWithdrawal(amount);
						isFound = true;
						break;
					}
				}
				// notify user account number was NOT found
				if (!isFound) {
					return 3; 
				}
				// set up description for transaction
				if (account instanceof Checking) {
					desc = "Checking Account Withdrawal";
				} else if (account instanceof Regular) {
					desc = "Regular Account Withdrawal";
				} else if (account instanceof Gold) {
					desc = "Gold Account Withdrawal";
				}
				// check to see if amount withdrawn was the amount requested
				// if not set the description for the transaction accordingly
				if (oldAmount != amount) {
					desc += " - Avail Bal";
				}
				// if the amount returned is -1 it means it was a negative amount entered
				if (amount == -1.0) {
					return 7;
				} else {
					// withdrawal was successful
					createTransaction(account.getCustomer().getCustomerID(), txtAccountNum.getText(), desc, (amount * -1.0));
					lblSuccess.setText(desc + " - SUCCESSFUL!");
					lblSuccess.setStyle(txtFillColor[0]);
					return 8;
				}
			}
		}
		return 12;
	}

	/** remove screen<br><br>
	 * 
	 * this removes an Account or a Customer<br><br>
	 * 
	 * 0 - Remove Customer<br>
	 * 1 - Remove Account<br>
	 * 
	 * 
	 * @param remNum is the type of remove being performed
	 * @return the BorderPane to the calling function
	 */
	public BorderPane removeScreen(int remNum){
		String txtTitle = "", txtLabel = "";
		switch (remNum) {
		case 0:
			txtTitle = "Remove Customer";
			txtLabel = "Customer #";
			break;
		case 1:
			txtTitle = "Remove Account";
			txtLabel = "Account #";
			break;
		}
		// declare labels, titles, and error messages areas
		Label lblTitle = new Label(txtTitle);
		lblTitle.setFont(arialFont[3]);
		lblTitle.setStyle(txtFillColor[0]);

		Label lblCustomerNum = new Label(txtLabel);
		lblCustomerNum.setFont(arialFont[1]);
		lblCustomerNum.setStyle(txtFillColor[0]);

		Label lblMessage = new Label(" ");
		lblMessage.setFont(arialFont[1]);
		lblMessage.setStyle(txtFillColor[1]);

		// declare text fields
		
		TextField txtCustomerNum = new TextField();
		txtCustomerNum.setFont(arialFont[1]);
		txtCustomerNum.setStyle(txtFillColor[0]);
		txtCustomerNum.setTooltip(new Tooltip(((remNum==0)?"Manually enter Customer ID and press Enter to validate it!" : "Manually enter Account Number and press Enter to validate it!")));
		txtCustomerNum.setMaxWidth(200);

		// declare text area
		
		TextArea txtOutput = new TextArea();
		txtOutput.setPrefRowCount(1);
		txtOutput.setPrefColumnCount(35);
		txtOutput.setFont(arialFont[1]);
		txtOutput.setStyle(txtFillColor[1]);
		txtOutput.setMaxWidth(600);

		// declare buttons
		
		Button btnRemove = new Button("Remove");
		btnRemove.setFont(arialFont[2]);
		btnRemove.setStyle(txtFillColor[5]);

		Button btnExit = new Button("Exit");
		btnExit.setPadding(new Insets(5, 35, 5, 35));
		btnExit.setFont(arialFont[2]);
		btnExit.setStyle(txtFillColor[6]);

		// set up error pane
		
		VBox errorPane = new VBox(8);
		errorPane.getChildren().add(lblMessage);
		errorPane.setAlignment(Pos.CENTER);

		// set up title pane
		
		VBox titlePane = new VBox(8);
		titlePane.setPadding(new Insets(15, 15, 25, 15));
		titlePane.getChildren().add(lblTitle);
		titlePane.setAlignment(Pos.CENTER);

		// set up customer number pane
		
		VBox custNumPane = new VBox(8);
		custNumPane.getChildren().addAll(lblCustomerNum, txtCustomerNum);
		custNumPane.setPadding(new Insets(25, 15, 25, 15));
		custNumPane.setAlignment(Pos.CENTER);

		//set up output pane
		
		VBox outputPane = new VBox(8);
		outputPane.getChildren().add(txtOutput);
		outputPane.setAlignment(Pos.CENTER);

		// set up top pane
		
		VBox topPane = new VBox(8);
		topPane.getChildren().addAll(titlePane, custNumPane);
		topPane.setAlignment(Pos.CENTER);

		// set up button pane
		
		HBox buttonPane = new HBox(8);
		buttonPane.setPadding(new Insets(25, 15, 25, 15));
		buttonPane.getChildren().addAll(btnRemove, btnExit);
		buttonPane.setAlignment(Pos.CENTER);

		// set up bottom pane
		
		VBox bottomPane = new VBox(8);
		bottomPane.getChildren().addAll(errorPane, buttonPane);
		bottomPane.setAlignment(Pos.CENTER);

		// set up the main pane
		
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(topPane);
		mainPane.setCenter(outputPane);
		mainPane.setBottom(bottomPane);

		// *************************************** customer number - action ***************************************
		
		txtCustomerNum.setOnAction(e -> {
			// did the user enter anything
			if (txtCustomerNum.getText().isEmpty()) {
				lblMessage.setText(((remNum == 0)? "You must enter a Customer Number" : "You must enter an Account Number"));
				lblMessage.setFont(arialFont[1]);
				lblMessage.setStyle(txtFillColor[1]);
			// user wants to remove customer
			} else if (remNum == 0) { // remove customer
				// verify customer is able to be removed
				checkStatus();
				boolean isFound = false;
				Customer customer = customers.get(0);
				for (Customer c: customers) {
					if (c.getCustomerID().equals(txtCustomerNum.getText())) {
						if (!c.getActive()) {
							customer = c;
							isFound = true;
						}
						break;
					}
				}
				// customer is ok to be removed
				if (isFound) {
					txtOutput.clear();
					txtOutput.appendText(customer.getCustomerID() + " " + customer.getCustomerName() + ((customer.getActive())? " Active":" Inactive"));
					btnRemove.requestFocus();
				// notify user that the customer number entered cannot be removed
				} else {
					lblMessage.setText("Customer Number was either not found or was not able to be removed!");
					lblMessage.setStyle(txtFillColor[1]);
					txtCustomerNum.requestFocus();
				}
				// user is removing an account
			} else if (remNum == 1) { // remove account
				boolean isFound = false;
				Account account = accounts.get(0);
				// validate account number
				for (Account a: accounts) {
					if (a.getAccountNumber().equals(txtCustomerNum.getText())) {
						isFound = true;
						account = a;
						break;
					}
				}
				// was it found
				if (isFound) {
					txtOutput.clear();
					txtOutput.setText(account.getAccountNumber() + " " + account.getCustomer().toString());
				// notify user it wasn't found
				} else {
					lblMessage.setText("Account Number was not Found");
					lblMessage.setStyle(txtFillColor[1]);
					txtCustomerNum.requestFocus();
				}
			}
		});
		
		// ********************************************* customer number - key pressed *********************************************
		// clear error message
		txtCustomerNum.setOnKeyPressed(e -> {
			lblMessage.setText("");
		});

		// ******************************************** btn remove - action ********************************************
		btnRemove.setOnAction(e -> {
			String description = "";
			// user is removing a customer
			if (remNum==0) { // remove customer
				// validate customer
				for (Customer c: customers) {
					if (c.getCustomerID().equals(txtCustomerNum.getText())) {
						if (!c.getActive()) {
							// set the description with customer id and name
							description = c.getCustomerID() + "\t" + c.getCustomerName() + " Removed";
							// make transaction
							createCustomerTransaction(c.getCustomerID(), description);
							// remove customer
							customers.remove(c);
							// notify user
							lblMessage.setText(description);
							lblMessage.setStyle(txtFillColor[0]);
							txtOutput.clear();
							break;
						}
					}
				}
			// user is removing account
			} else if (remNum==1) { // remove account
				// validate account number
				for (Account a: accounts) {
					if (a.getAccountNumber().equals(txtCustomerNum.getText())) {
						// set up description
						if (a instanceof Checking) {
							description = "Removed Checking Account";
						} else if (a instanceof Regular) {
							description = "Removed Regular Account";
						} else {
							description = "Removed Gold Account";
						}
						// make transaction
						createTransaction(a.getCustomer().getCustomerID(), a.getAccountNumber(), description, (a.getAccountBalance() * -1));
						// remove account
						accounts.remove(a);
						// update customer status
						checkStatus();
						// notify user
						lblMessage.setText(description);
						lblMessage.setStyle(txtFillColor[0]);
						txtOutput.clear();
						break;
					}
				}
			}
		});
		
		// ******************************************* button exit - action *******************************************
		btnExit.setOnAction(e -> {
			btnExit.getParent().getScene().getWindow().hide();
		});

		return mainPane;
	}

	/** get amount<br><br>
	 * 
	 * this will parse the text field amount and return the number<br>
	 * 
	 * @param txtAmount the TextField of the amount
	 * @return the amount
	 */
	public double getAmount(TextField txtAmount) {
		double amount = 0.0;
		try {
			// can the amount be a double
			amount = Double.parseDouble(txtAmount.getText());
			// user entered a negative number so get the absolute value of it
			if (amount < 0.0) {
				amount = Math.abs(amount);
			}
		// any errors will return -1.0 as the answer to notify the user
		} catch (NumberFormatException e) {
			amount = -1.0;
			e.getStackTrace();
		} catch (NullPointerException e) {
			amount = -1.0;
			e.getStackTrace();
		} catch (Exception e) {
			amount = -1.0;
			e.getStackTrace();
		}
		return amount;
	}
	
	/** get balance<br><br>
	 * 
	 * 
	 * @param txtAmount the balance to convert from a string to a double
	 * @return the amount that was converted
	 */
	public double getBalance(TextField txtAmount) {
		double amount = 0.0;
		try {
			// can the amount be a double
			amount = Double.parseDouble(txtAmount.getText());
			// if the user entered a negative convert it to a positive
			if (amount < 0.0) {
				amount = Math.abs(amount);
			}
		// display any errors
		} catch (NumberFormatException e) {
			e.getStackTrace();
		} catch (NullPointerException e) {
			e.getStackTrace();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return amount;
	}
	
	
	/** create transactions<br><br>
	 * 
	 * creates a transaction and returns the transaction<br>
	 * primarily for creating accounts<br>
	 * 
	 * @param customerID String
	 * @param accountNumber String
	 * @param description String
	 * @param amount double
	 */
	public Transaction createTransaction(String customerID, String accountNumber, String description, double amount) {
		Transaction transaction = new Transaction(new java.util.Date(), customerID, accountNumber, description, amount, bu.generateUniqueTransNumber());
		transactions.add(transaction);
		return transaction;
	}

	
	/** create transactions<br><br>
	 * 
	 * this creates a transaction without returning the transaction<br>
	 * 
	 * @param customerID String
	 * @param accountNumber String
	 * @param description String
	 * @param amount double
	 */
	public void processTransaction(String customerID, String accountNumber, String description, double amount) {
		Transaction transaction = new Transaction(new java.util.Date(), customerID, accountNumber, description, amount, bu.generateUniqueTransNumber());
		transactions.add(transaction);
	}

	/** create customer transaction<br>
	 * this add to the transaction table the removal of a customer<br>
	 * the description will include the cutomer id and name<br>
	 * 
	 * 
	 * @param description the description of the transaction
	 */
	public void createCustomerTransaction(String customerID, String description) {
		transactions.add(new Transaction(new java.util.Date(), customerID, description, bu.generateUniqueTransNumber()));
	}


	/** create Account<br><br>
	 * 
	 * using the acctNum will determine whether it is for Checking, Regular or Gold<br><br>
	 * 
	 * acctNum<br>
	 * ----------------<br>
	 * 0 - Checking<br>
	 * 1 - Regular<br>
	 * 2 - Gold<br>
	 * 
	 * returns<br>
	 * -----------------
	 *  0 - customer id is empty
	 *  1 - account number is empty
	 *  2 - opening balance is empty
	 *  3 - account number already exists
	 *  4 - opening balance is negative
	 *  5 - customer id was not found
	 *  6 - invalid opening balance
	 *  7 - 
	 *  8 - 
	 *  9 - 
	 * 10 - account successfully created
	 * 11 - account was not created
	 * 12 - 
	 * 
	 * 
	 * 
	 * @param txtAccountNumber the account number from the form
	 * @param txtCustomerID the customer id from the form
	 * @param txtOpeningBalance the opening balance from the form
	 * @param lbltxtDisplayBoxMessage the display box on the form
	 * @param acctNum the number that will determine Checking, Regular, or Gold
	 * 
	 * @return whether it was successful or not (true or false)
	 */
	public int createAccount( TextField txtAccountNumber, TextField txtCustomerID, TextField txtOpeningBalance, int acctNum) {

		// check to see if the text boxes are empty and notify user
		if (txtCustomerID.getText().isEmpty()) {
			return 0;
		} else if (txtAccountNumber.getText().isEmpty()) {
			return 1;
		} else if (txtOpeningBalance.getText().isEmpty()) {
			return 2;
		} else {
			boolean isAcctFound = false;
			for (Account a: accounts) {
				if (a.getAccountNumber().equals(txtAccountNumber.getText())) {
					isAcctFound = true;
				}
			}
			if (isAcctFound) {
				return 3;
			}

			int element = accounts.size();
			double amount = -1.0;
			boolean isOk = false;
			try {
				amount = Double.parseDouble(txtOpeningBalance.getText());
				if (amount < 0.0) {
					return 4;
				} else {
					isOk = true;
				}
			} catch (NumberFormatException h) {
				return 6;

			} catch (InputMismatchException  g) {
				return 6;

			} catch (NoSuchElementException f) {
				return 6;
			}
			// amount entered is ok
			if (isOk) {
				String desc = "";
				boolean custIDFound = false;
				Customer customer = customers.get(0);
				// validate customer id
				for (int x = 0; x < customers.size(); x++) {
					if (customers.get(x).getCustomerID().equals(txtCustomerID.getText())) {
						custIDFound = true;
						customer = customers.get(x);
						customer.setActive(true);
						switch (acctNum) {
						case 0: // checking
							desc = "Checking Opening Balance";
							accounts.add(new Checking(txtAccountNumber.getText(), amount, customer, createTransaction(customer.getCustomerID(), txtAccountNumber.getText(), desc, amount)));
							break;
						case 1: // regular
							desc = "Regular Opening Balance";
							accounts.add(new Regular(txtAccountNumber.getText(), amount, customer, createTransaction(customer.getCustomerID(), txtAccountNumber.getText(), desc, amount)));
							break;
						case 2: // gold
							desc = "Gold Opening Balance        ";
							accounts.add(new Gold(txtAccountNumber.getText(), amount, customer, createTransaction(customer.getCustomerID(), txtAccountNumber.getText(), desc, amount)));
							break;
						}
						break;
					}
				}
				// customer id was NOT found
				if (!custIDFound) {
					return 5;
				} else {
					// notify the user
					if (element < accounts.size()) {
						return 10;
					} else {
						checkStatus();
						return 11;
					}
				}
			}
		}
		return 11;
	}

	// ***************************************************************************** object file system *****************************************************************************

	// ***************************************************************** load *****************************************************************

	// *************************** config ***************************

	// the config holds the bank name and a backup integer for filenames
	public void loadConfigData() {
		File file = new File(filename[0]);
		// if the file doesn't exist
		if (!(file.exists())) {
			boolean isOk = false;
			// get the bank name from the user
			bankName = JOptionPane.showInputDialog("Please Enter the Bank Name");
			if (bankName.isEmpty()){
				isOk = false;
			} else {
				isOk = true;
			}
			// keep asking for the bank name until they enter one
			while (!isOk) {
				bankName = JOptionPane.showInputDialog("Bank Name cannot be blank!\n\nPlease Enter the Bank Name");
				if (bankName.isEmpty()){
					isOk = false;
				} else {
					isOk = true;
				}
			}
			bakup = 0;
			return;
		}
		// since the file exists read it
		try (DataInputStream input = new DataInputStream(new FileInputStream(filename[0]));){
			while (true) {
				bankName = input.readUTF();
				bakup = input.readInt();
			}
		} catch (EOFException e) {
			//JOptionPane.showMessageDialog(null, "Configuration Data Loaded successfully!", "Config Load Data", JOptionPane.INFORMATION_MESSAGE);
			e.getStackTrace();
			return;
		} catch (FileNotFoundException e) {
			//JOptionPane.showMessageDialog(null, "Configuration file " + filename[0] + " does not exist!", "File Error", JOptionPane.ERROR_MESSAGE);
			e.getStackTrace();
			return;
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Configuration File Read Error", "File Read Error", JOptionPane.ERROR_MESSAGE);
			e.getStackTrace();
		}
	}

	// ************************************** customer **************************************

	@SuppressWarnings("unchecked")
	public void loadCustomerObjectData() {
		File info = new File(filename[7]);
		if (info.exists()) {
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename[7]));) {
				customers = (ArrayList<Customer>) input.readObject();

			} catch (EOFException e) {
				e.getStackTrace();
				return;

			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "File " + filename[7] + " not found!", "Customer Read Error", JOptionPane.ERROR_MESSAGE);
				e.getStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Customer Read Error", "Read Error", JOptionPane.ERROR_MESSAGE);
				e.getStackTrace();
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Unable to create object", "Error Reading Customer", JOptionPane.ERROR);
				e.getStackTrace();
			}
		}
	}


	// ************************************** account **************************************

	@SuppressWarnings("unchecked")
	public void loadAccountObjectData() {
		File info = new File(filename[6]);
		if (info.exists()) {
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename[6]));) {
				accounts = (ArrayList<Account>) input.readObject();
				
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "File " + filename[6] + " not found!", "Account Read Error", JOptionPane.ERROR_MESSAGE);
				e.getStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Account Read Error", "Read Error", JOptionPane.ERROR_MESSAGE);
				e.getStackTrace();
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Unable to create object", "Error Reading Account", JOptionPane.ERROR);
				e.getStackTrace();
			}
		}
		loadEOMErrorObjectData();
	}

	// ************************************** eomError **************************************

	@SuppressWarnings("unchecked")
	public void loadEOMErrorObjectData() {
		File info = new File(filename[9]);
		if (info.exists()) {
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename[9]));) {
				eomErrors = (ArrayList<Account>) input.readObject();
				
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "File " + filename[9] + " not found!", "EOM Error Read Error", JOptionPane.ERROR_MESSAGE);
				e.getStackTrace();
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Account Read Error", "Read Error", JOptionPane.ERROR_MESSAGE);
				e.getStackTrace();
			}
		}
	}
	
	// ************************************** transactions **************************************

	@SuppressWarnings("unchecked")
	public void loadTransactionObjectData() {
		File info = new File(filename[8]);
		if (info.exists()) {  
			try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(filename[8]));) {
				transactions = (ArrayList<Transaction>) input.readObject();

			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, "File " + filename[8] + " not found!", "Transaction Read Error", JOptionPane.ERROR_MESSAGE);
				e.getStackTrace();
			} catch (IOException e) {
				JOptionPane.showMessageDialog(null, "Transaction Read Error", "Read Error", JOptionPane.ERROR_MESSAGE);
				e.getStackTrace();
			} catch (ClassNotFoundException e) {
				JOptionPane.showMessageDialog(null, "Unable to create object", "Error Reading Transaction", JOptionPane.ERROR);
				e.getStackTrace();
			}
		} else {
			return;
		}
	}

	// ***************************************************************** save *****************************************************************

	// ************************************** config **************************************

	public void saveConfigData() {
		try (DataOutputStream output = new DataOutputStream(new FileOutputStream(filename[0], false));) {
			output.writeUTF(bankName);
			output.writeInt(bakup);

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Config File Write Error", "File Write Error", JOptionPane.ERROR_MESSAGE);
			e.getStackTrace();
		}
		// JOptionPane.showMessageDialog(null, "Config Data Saved successfully!", "Config Save Data", JOptionPane.INFORMATION_MESSAGE);
	}

	// ************************************** customer **************************************

	public void saveCustomerObjectData() {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename[7], false));) {
			output.writeObject(customers);

		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File " + filename[7] + " not found!", "Customer Save Error", JOptionPane.ERROR_MESSAGE);
			e.getStackTrace();

		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Customer Read Error", "Save Error", JOptionPane.ERROR_MESSAGE);
			e.getStackTrace();

		}
	}

	// ************************************** accounts **************************************

	public void saveAccountObjectData() {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename[6], false));) {
			output.writeObject(accounts);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File " + filename[6] + " not found!", "Account Save Error", JOptionPane.ERROR_MESSAGE);
			e.getStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Account Read Error", "Save Error", JOptionPane.ERROR_MESSAGE);
			e.getStackTrace();
		}
		saveEOMErrorObjectData();
	}
	
	// ************************************** eomErrors **************************************
	
	public void saveEOMErrorObjectData() {
		if (eomErrors.isEmpty()) {
			return;
		}
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename[9]));) {
			output.writeObject(eomErrors);
			
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File " + filename[9] + " not found!", "EOM Save Error", JOptionPane.ERROR_MESSAGE);
			e.getStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Account Read Error", "Save Error", JOptionPane.ERROR_MESSAGE);
			e.getStackTrace();
		}
	}
	
	// ************************************** transactions **************************************

	public void saveTransactionObjectData() {
		try (ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(filename[8], false));) {
			output.writeObject(transactions);
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, "File " + filename[7] + " not found!", "Transaction Save Error", JOptionPane.ERROR_MESSAGE);
			e.getStackTrace();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "Transaction Read Error", "Save Error", JOptionPane.ERROR_MESSAGE);
			e.getStackTrace();
		}
	}

	// *********************************** create new System Data *****************************************

	// ************* this will delete the files so be careful ****************************

	public void createNewSystemData() {
		boolean isOk = false;
		try {
			File startOver;
			boolean isAllOk = false;
			for (int y = 0; y < filename.length; y++) {
				startOver = new File(filename[y]);
				if (startOver.exists()) {
					isOk = copyDatToBakup(filename[y], backname[y] + bakup + ".dat");
					if (isOk) {
						startOver.delete();
						isAllOk = true;
					}
				}
			}
			if (isAllOk) {
				bakup++;
				customers.clear();
				accounts.clear();
				transactions.clear();
				boolean isNameOK = false;
				bankName = JOptionPane.showInputDialog("Please Enter the Bank Name");
				if (bankName.isEmpty()){
					isNameOK = false;
				} else {
					isNameOK = true;
				}
				while (!isNameOK) {
					bankName = JOptionPane.showInputDialog("Bank Name cannot be blank!\n\nPlease Enter the Bank Name");
					if (bankName.isEmpty()){
						isNameOK = false;
					} else {
						isNameOK = true;
					}
				}

				saveConfigData();
				loadConfigData();
				JOptionPane.showMessageDialog(null, "New file System READY for use!", "New Files", JOptionPane.INFORMATION_MESSAGE);
			} else {
				JOptionPane.showMessageDialog(null, "Error copying files! New System is NOT Ready for use!", "New Files", JOptionPane.ERROR_MESSAGE);
			}
		} catch (NullPointerException e) {
			e.getStackTrace();

		} catch (SecurityException e) {
			e.getStackTrace();
		}
	}

	/** copy data to backup<br><br>
	 * 
	 * @param fromFile
	 * @param toFile
	 * @return
	 */
	public boolean copyDatToBakup(String fromFile, String toFile) {
		if (fromFile.isEmpty()) {
			return false;
		}
		if (toFile.isEmpty()) {
			return false;
		}
		File sourceFile = new File(fromFile);
		File targetFile = new File(toFile);
		if (!sourceFile.exists()) {
			JOptionPane.showMessageDialog(null, sourceFile + " does not exist!", "File Not Found", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (targetFile.exists()) {
			JOptionPane.showMessageDialog(null, targetFile + " already exists!", "File already Exists", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		try (BufferedInputStream input = new BufferedInputStream(new FileInputStream(sourceFile));
				BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(targetFile));) {
			int r;
			while((r = input.read()) != -1) {
				output.write((byte)r);
			}
			return true;
		} catch (IOException e) {
			e.getStackTrace();
		}
		return false;
	}

	/** generate report<br><br>
	 * 
	 * 0 - Customer<br>
	 * 1 - Account<br>
	 * 2 - Transactions<br>
	 * 3 - Statistics
	 * 4 - EOM calculations
	 * 
	 * @param acctNum
	 * @return
	 */
	public BorderPane generateReport(int acctNum) {
		// declare title variable
		Label lblReports = new Label();
		lblReports.setFont(arialFont[3]);
		lblReports.setStyle(txtFillColor[0]);

		// declare output area
		TextArea taOutput = new TextArea();
		taOutput.setPrefRowCount(15);
		
		switch (acctNum) {
		// **************************************************************** customer ****************************************************************
		case 0:
			taOutput.setPrefColumnCount(60);
			taOutput.setFont(arialFont[0]);
			lblReports.setText("Customers " + customers.size());
			int counter = 0;
			double totalAccounts = 0.0;
			// set up the header
			taOutput.appendText(String.format("\t\t\t\t %s \t\t\t\t\t %s \t %s \t\t %s \t\t %s", "Customer Information", "|", "# of Accounts", "|", " Total  Balance\n"));
			// get customer information
			for (Customer c: customers) {
				for (Account a: accounts) {
					if (a.getCustomer().getCustomerID().equals(c.getCustomerID())) {
						counter++;
						totalAccounts += a.getAccountBalance();
						// JOptionPane.showMessageDialog(null, totalAccounts);
					}
				}
				// display customer information and reset totals for next customer
				taOutput.appendText(String.format("%s \t %s \t\t %3d \t\t\t %s \t\t $%12.2f\n", c.toString(), "|", counter, "|", totalAccounts));
				totalAccounts = 0.0;
				counter = 0;
			}
			taOutput.appendText("\n========================================================================================\n\n");
			break;
		// **************************************************************** account ****************************************************************
		case 1:
			taOutput.setPrefColumnCount(85);
			lblReports.setText("Accounts " + accounts.size());
			counter = 0;
			for (Account a: accounts) {
				// ******************************************** Checking ********************************************
				if (a instanceof Checking) {
					if (counter == 0) {
						taOutput.appendText(String.format("\n\t\t\t\t\t %s \t\t\t\t\t %s \t\t %s \t\t\t %s \t\t %s \t\t %s \n", "Customer Information","Account Number", "Balance", "# of Transactions", "Transaction Fee", "Total Fee"));
						taOutput.appendText("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
						counter++;
					}
					taOutput.appendText(a.toString());
				}
			}
			counter = 0;
			for (Account a: accounts) {
				// ******************************************** Regular ********************************************
				if (a instanceof Regular) {
					if (counter == 0) {
						taOutput.appendText(String.format("\n\t\t\t\t\t %s \t\t\t\t\t %s \t\t %s \t\t\t\t %s \t\t %s \t\t %s \n", "Customer Information", "Account Number", "Balance", "Inerest Rate", "Fixed Charge", "Total Interest"));
						taOutput.appendText("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
						counter++;
					}
					taOutput.appendText(a.toString());
				}
			}
			counter = 0;
			for (Account a: accounts) {
				// ******************************************** Gold ********************************************
				if (a instanceof Gold) {
					if (counter == 0) {
						taOutput.appendText(String.format("\n\t\t\t\t\t  %s \t\t\t\t\t %s \t\t %s \t\t %s\t\t %s\n", "Customer Information", "Account Number", "Balance", "Interest Rate", "Interest Amount"));
						taOutput.appendText("-----------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
						counter++;
					}
					taOutput.appendText(a.toString());
				}
			}
			break;
		// **************************************************************** transaction ****************************************************************
		case 2:
			taOutput.setPrefColumnCount(85);
			lblReports.setText("Transactions " + transactions.size());
			taOutput.setFont(arialFont[0]);
			for (Transaction t: transactions) {
				taOutput.appendText(t.toString());
			}
			break;
		// **************************************************************** statistics ****************************************************************
		case 3:
			taOutput.setPrefColumnCount(75);
			lblReports.setText("Statistics Report");
			taOutput.setFont(arialFont[0]);
			generateStatistics(taOutput);
			break;
		// **************************************************************** eom calculations ****************************************************************
		case 4:
			taOutput.setPrefColumnCount(75);
			taOutput.setPrefColumnCount(75);
			lblReports.setText("Calculate EOM interest and fees");
			taOutput.setFont(arialFont[0]);
			eomCalculations(taOutput);
			if (!eomErrors.isEmpty()) {

			}
			break;
		}
		
		// declare button
		
		Button btnExit = new Button("Exit");
		btnExit.setFont(arialFont[2]);
		btnExit.setStyle(txtFillColor[6] + txtFillColor[3]);
		taOutput.setEditable(false);

		// prepare display pane
		
		HBox displayPane = new HBox();
		displayPane.setPadding(new Insets(25,12,12,12));
		displayPane.getChildren().addAll(lblReports);
		displayPane.setAlignment(Pos.CENTER);

		// prepare the center pane
		
		FlowPane centerPane = new FlowPane();
		centerPane.setHgap(5);
		centerPane.setVgap(5);
		centerPane.getChildren().addAll(taOutput);
		centerPane.setAlignment(Pos.CENTER);

		// prepare bottom pane
		
		HBox bottomPane = new HBox(1);
		bottomPane.setPadding(new Insets(0,0,25,0));
		bottomPane.getChildren().addAll(btnExit);
		bottomPane.setAlignment(Pos.CENTER);

		// prepare main pane
		
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(displayPane);
		mainPane.setCenter(centerPane);
		mainPane.setBottom(bottomPane);
		mainPane.setStyle("-fx-border-color: blue");

		// ****************************************** button exit - action ******************************************
		
		btnExit.setOnAction(e -> {
			btnExit.getScene().getWindow().hide();
		});

		return mainPane;
	}

	/** generate statistics<br><br>
	 * 
	 * 
	 * @param taOutput
	 */
	public void generateStatistics(TextArea taOutput) {
		// number of accounts
		int numAccounts = 0, numRegularAccounts = 0, numCheckingAccounts = 0, numGoldAccounts = 0;
		// sum accounts
		double sumAccounts = 0.0, sumRegularAccounts = 0.0, sumCheckingAccounts = 0.0, sumGoldAccounts = 0.0;
		// number of zero balances
		int numZeroAccounts = 0, numRegularZeroAccounts = 0, numCheckingZeroAccounts = 0, numGoldZeroAccounts = 0;
		// average balance
		double avgAccounts = 0.0, avgRegularAccounts = 0.0, avgCheckingAccounts = 0.0, avgGoldAccounts = 0.0;
		// largest account balance
		double largestAccount = 0.0, largestRegularAccount = 0.0, largestCheckingAccount = 0.0, largestGoldAccount = 0.0;
		// largest account balance account number
		String largestAccountNumber = "not available                ", largestRegularAccountNumber = "not available                ", largestCheckingAccountNumber = "not available                ", largestGoldAccountNumber = "not available                ";
		numAccounts = accounts.size();
		for (int x = 0; x < accounts.size(); x++) {
			//------------------------------------Total---------------------------			
			sumAccounts += accounts.get(x).getAccountBalance();
			if (accounts.get(x).getAccountBalance() == 0.0) {
				numZeroAccounts++;
			}
			if (accounts.get(x).getAccountBalance() > largestAccount) {
				largestAccount = accounts.get(x).getAccountBalance();
				largestAccountNumber = accounts.get(x).getAccountNumber();
			}
			//-----------------------------------Gold-----------------------------
			if(accounts.get(x) instanceof Gold) {
				numGoldAccounts++;
				sumGoldAccounts += accounts.get(x).getAccountBalance();
				if (accounts.get(x).getAccountBalance() == 0.0) {
					numGoldZeroAccounts++;
				}
				if (accounts.get(x).getAccountBalance() > largestGoldAccount) {
					largestGoldAccount = accounts.get(x).getAccountBalance();
					largestGoldAccountNumber = accounts.get(x).getAccountNumber();
				}
			}
			//----------------------------------Regular-----------------------------
			if (accounts.get(x) instanceof Regular) {
				numRegularAccounts++;
				sumRegularAccounts += accounts.get(x).getAccountBalance();
				if (accounts.get(x).getAccountBalance() == 0.0) {
					numRegularZeroAccounts++;
				}
				if (accounts.get(x).getAccountBalance() > largestRegularAccount) {
					largestRegularAccount = accounts.get(x).getAccountBalance();
					largestRegularAccountNumber = accounts.get(x).getAccountNumber();
				}
			}
			//--------------------------------Checking-----------------------------
			if (accounts.get(x) instanceof Checking) {
				numCheckingAccounts++;
				sumCheckingAccounts += accounts.get(x).getAccountBalance();
				if (accounts.get(x).getAccountBalance() == 0.0) {
					numCheckingZeroAccounts++;
				}
				if (accounts.get(x).getAccountBalance() > largestCheckingAccount) {
					largestCheckingAccount = accounts.get(x).getAccountBalance();
					largestCheckingAccountNumber = accounts.get(x).getAccountNumber();
				}
			}

		}
		//-------------------------------calculate averages----------------------------		
		if (numAccounts == 0) {
			avgAccounts = 0.00;
		} else {
			avgAccounts = calcAverages(sumAccounts, numAccounts);
		}
		if (numGoldAccounts == 0) {
			avgGoldAccounts = 0.00;
		} else {
			avgGoldAccounts = calcAverages(sumGoldAccounts, numGoldAccounts);
		}
		if (numRegularAccounts == 0) {
			avgRegularAccounts = 0.00;
		} else {
			avgRegularAccounts = calcAverages(sumRegularAccounts, numRegularAccounts);
		}
		if (numCheckingAccounts == 0) {
			avgCheckingAccounts = 0.00;
		} else {
			avgCheckingAccounts = calcAverages(sumCheckingAccounts, numCheckingAccounts);
		}
		//------------------------------------display results-----------------------------------
		// (char) 68 < Statistics > 68 (char)
		taOutput.appendText(String.format("\n %s \t %s \t %s \t %s \t %s \t %s \t %s \n", "Type of Account","Number of Accounts", "Total Balance", "w/Zero Balance", "Average Balance", "Account Number of", "Largest Balance"));
		taOutput.appendText("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		taOutput.appendText(displayStatistics("Gold        ", numGoldAccounts, sumGoldAccounts, numGoldZeroAccounts, avgGoldAccounts, largestGoldAccountNumber, largestGoldAccount));
		taOutput.appendText("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		taOutput.appendText(displayStatistics("Regular     ", numRegularAccounts, sumRegularAccounts, numRegularZeroAccounts, avgRegularAccounts, largestRegularAccountNumber, largestRegularAccount));
		taOutput.appendText("----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");
		taOutput.appendText(displayStatistics("Checking    ", numCheckingAccounts, sumCheckingAccounts, numCheckingZeroAccounts, avgCheckingAccounts, largestCheckingAccountNumber, largestCheckingAccount));
		taOutput.appendText("===============================================================================================================\n");
		taOutput.appendText(displayStatistics("All Accounts", numAccounts, sumAccounts, numZeroAccounts, avgAccounts, largestAccountNumber, largestAccount));
		taOutput.appendText("===============================================================================================================\n");
		//taOutput.setFont(new Font("Arial", 8));

	}

	/** display statistics
	 * this makes changing the printed listing a lot easier than
	 * having to change 4 different version of the same thing
	 * 
	 * @param acctType the account type (Checking, Gold, Regular, All)
	 * @param numAccts the total number of accounts per account type
	 * @param sumAccts the sum of the balance of the accounts per account type
	 * @param numZero the number of accounts with a balance of zero balance per account type
	 * @param avgAccts the average balance of all the accounts per account type
	 * @param acctNumber the account number of the account with the largest balance per account type
	 * @param largeBal the largest balance of the accounts per account type
	 * 
	 */
	public String displayStatistics(String acctType, int numAccts, double sumAccts, int numZero, double avgAccts, String acctNumber, double largeBal) {
		return String.format("%s \t\t\t\t %3d \t\t\t $%12.2f \t\t\t %3d \t\t\t $%12.2f \t\t %-20s \t $%12.2f \n", acctType, numAccts, sumAccts, numZero, avgAccts, acctNumber, largeBal);
	}

	/** calculate averages <br><br>
	 * 
	 * @param num1 is the numerator
	 * @param num2 is the denominator
	 * @return the average
	 */
	public double calcAverages(double num1, double num2) {
		double avgNum = 0.0;
		try {
			avgNum = num1 / num2;
		} catch (ArithmeticException e) {
			JOptionPane.showMessageDialog(null, "Invalid Arithimatic Operation", "Math Error", JOptionPane.ERROR);
			e.getStackTrace();
		}
		return avgNum;
	}

	/** end of month calculations<br><br>
	 * 
	 * process accounts and calculate interest for accounts that<br>
	 * have the ability to generate interest only if the balance is positive<br><br>
	 * 
	 * apply transaction fees to the balance of Checking accounts only<br>
	 * if there is enough funds to do so<br>
	 * Will not allow the balance to go into a negative state<br>
	 * if there are not enough funds, the account will be added to a<br>
	 * error table and processed later allow the user to<br>
	 * add funds<br><br>
	 * 
	 * also keep track of the number of account and how many errors<br>
	 * and display a formated report at the end of the run<br>
	 * 
	 */
	public void eomCalculations(TextArea taOutput) {

		String description = "";
		if (accounts.isEmpty()) {
			System.out.print("\n\nThere are no accounts to process!\n\nTerminating EOM Calculations!\n\n");
			return;
		}
		// declare counting variables
		int counter = 0;
		int goodCounter = 0;
		int badCounter = 0;
		int errorCounter = 0;
		int checkCounter = 0;
		int goldCounter = 0;
		int regCounter = 0;
		int checkErrorCounter = 0;
		int goldErrorCounter = 0;
		int regErrorCounter = 0;
		// set up account types variables
		Checking chk;
		Gold gold;
		Regular reg;
		// loop through accounts and process each one
		for (Account a: accounts) {
			counter++;
// ************************************************* checking accounts *************************************************
			if (a instanceof Checking) {
				checkCounter++;
				chk = (Checking) a;
				// determine if transactions are zero and increment bad counter and loop back
				if (chk.getNumberOfTransactions() <= 2) {
					badCounter++;
					continue;
				}
				// check to see if the amount of the fees don't match the number of transaction times the fees (remember the first two are fee)
				if (((chk.getNumberOfTransactions() - 2) * chk.getCheckingTransactionFee()) != chk.getCheckingTransactionFeeAmount()) {
					// there is an error
					errorCounter++;
					checkErrorCounter++;
					description = "EOM Checking Fees Don't Match";
					// display message to user
					// displayDescription(description);
					// add transaction to tracker
					createTransaction(chk.getCustomer().getCustomerID(), chk.getAccountNumber(), description, 0.0);
					// add account to error table
					eomErrors.add(chk);
				} else {
					// calculated fees and fee amount are equal
					// now check to see if there are enough available funds to process
					if (chk.getAccountBalance() < chk.getCheckingTransactionFeeAmount()) {
						// there are not enough funds to process
						errorCounter++;
						checkErrorCounter++;
						description = "EOM Checking - Insuf. Funds";
						// add account to error table
						eomErrors.add(chk);
						// display message to user
						// displayDescription(description);
						// add transaction to tracker
						createTransaction(chk.getCustomer().getCustomerID(), chk.getAccountNumber(), description, 0.0);
					} else {
						// there are enough funds to process
						goodCounter++;
						description = "EOM Checking - Fees";
						// update account balance
						chk.setAccountBalance(chk.getAccountBalance() - chk.getCheckingTransactionFeeAmount());
						// display message to user
						// displayDescription(description);
						// add transaction to tracker
						createTransaction(chk.getCustomer().getCustomerID(), chk.getAccountNumber(), description, (chk.getCheckingTransactionFeeAmount() * -1));
						// reset transaction number and fee amount
						chk.setNumberOfTransactions(0);
						chk.setCheckingTransactionFeeAmount(0.0);
					}
				}
// ************************************************* gold accounts *************************************************
			} else if (a instanceof Gold) {
				goldCounter++;
				gold = (Gold) a;
				if(gold.getAccountBalance() <= 0.0) {
					// the account has no funds to calculate interest
					badCounter++;
					continue;
				} else {
					// account is able to add interest calculations
					goodCounter++;
					description = "EOM Gold - Interest       ";
					// display message to user
					// displayDescription(description);
					// calculate interest
					double interest = calculateInterest(gold, (gold.getGoldInterestRate() / 100.0));
					// apply interest to account
					gold.setAccountBalance(gold.getAccountBalance() + interest);
					// add transaction to tracker
					createTransaction(gold.getCustomer().getCustomerID(), gold.getAccountNumber(), description, interest);
					// add interest to interest field
					gold.setGoldInterestAmount(gold.getGoldInterestAmount() + interest);
				}
// ************************************************* regular accounts *************************************************
			} else {
				regCounter++;
				reg = (Regular) a;
				if (reg.getAccountBalance() <= 0.0) {
					// the account has no funds to calculate interest
					badCounter++;
					continue;
				} else {
					// account is able to add interest calculations
					if (reg.getAccountBalance() >= reg.getRegularFixedCharge()) {
						goodCounter++;
						description = "EOM Regular - Interest   ";
						// calculate interest
						double interest = calculateInterest(reg, (reg.getRegularInterestRate() / 100.0));
						// display message to user
						// displayDescription(description);
						// apply interest to account
						reg.setAccountBalance(reg.getAccountBalance() + (interest - reg.getRegularFixedCharge()));
						// add transaction to tracker
						createTransaction(reg.getCustomer().getCustomerID(), reg.getAccountNumber(), description, interest);
						// add interest to interest field
						reg.setRegularInterestAmount(reg.getRegularInterestAmount() + interest);
					} else {
						// there is not enough funds available to calculate interest and apply the fixed Charge
						regErrorCounter++;
						errorCounter++;
						description = "EOM Regular - Insufficient funds to apply fixed cost";
						// display message to user
						// displayDescription(description);
					}
				}
			}
		}
		// display report
		// all accounts
		taOutput.appendText("There were " + counter + " accounts processed!\n");
		taOutput.appendText("\nThere were " + errorCounter + " errors accounted for!\n");
		taOutput.appendText("\nThere were " + goodCounter + " accounts processed successfully!\n");
		// show those with zero balance
		taOutput.appendText("\nThere were " + badCounter + " accounts processed unsuccessfully! (zero balance or no transactions)\n\n");
		// out of all the accounts
		taOutput.appendText("\nOf the " + counter + " accounts processed, " + checkCounter + " were Checking accounts!\n");
		taOutput.appendText("\nOf the " + counter + " accounts processed, " + goldCounter + " were Gold accounts!\n");
		taOutput.appendText("\nOf the " + counter + " accounts processed, " + regCounter + " were Regular accounts!\n\n");
		// of the accounts how many of those were errors
		taOutput.appendText("\nOf the " + checkCounter + " Checking accounts processed, there were " + checkErrorCounter + " errors\n");
		taOutput.appendText("\nOf the " + goldCounter + " Gold accounts processed, there were " + goldErrorCounter + " errors\n");
		taOutput.appendText("\nOf the " + regCounter + " Regular accounts processed, there were " + regErrorCounter + " errors\n");


	}

	/** calculate interest<br><br>
	 * 
	 * @param account the account to calculate the interest for
	 * @param rate the rate in decimal form
	 * 
	 * @return the amount of the interest
	 */
	public double calculateInterest(Account account, double rate) {
		// calculate interest for 1 year compounded monthly7
		// I = P x (1 + r/n)^(n x t)
		// P = accountBalance : Principle
		// r = rate : interest rate (in decimal)
		// t = year : number of years, months, days, etc in this case it is years
		// n = numTimes : how often : months, quarters, days etc. in this case it is one month
		double years = 1.0;
		double numTimes = 1.0/12.0;
		double interest = ((account.getAccountBalance() * (Math.pow((1.0 + (rate / numTimes)), (numTimes * years)))) - account.getAccountBalance());
		return interest;
	}

	/** eom process<br><br>
	 * 
	 * @return
	 */
	public BorderPane eomProcess(){
		
		minBal = 0.0;
		
		// declare labels and title
		
		Label lblTitle = new Label("Process EOM Errors");
		lblTitle.setFont(arialFont[3]);
		lblTitle.setStyle(txtFillColor[0]);
		
		Label lblCustomerNum = new Label("Account Number");
		lblCustomerNum.setFont(arialFont[1]);
		lblCustomerNum.setStyle(txtFillColor[4]);
		
		Label lblOldBalance = new Label("Available Balance");
		lblOldBalance.setFont(arialFont[1]);
		lblOldBalance.setStyle(txtFillColor[4]);
		
		Label lblNewBalance = new Label("Minimum Deposit");
		lblNewBalance.setFont(arialFont[1]);
		lblNewBalance.setStyle(txtFillColor[4]);
		
		Label lblMessage = new Label(" ");
		lblMessage.setFont(arialFont[1]);
		lblMessage.setStyle(txtFillColor[1]);
		
		// declare text fields and tool tips
		
		TextField txtCustomerNum = new TextField();
		txtCustomerNum.setFont(arialFont[1]);
		txtCustomerNum.setStyle(txtFillColor[0]);
		txtCustomerNum.setMaxWidth(200);
		txtCustomerNum.setTooltip(new Tooltip("Type 0 and Enter to lookup Account Number\nAfter selection Press Enter to validate Account Number"));
		
		TextField txtOldBalance = new TextField();
		txtOldBalance.setFont(arialFont[1]);
		txtOldBalance.setStyle(txtFillColor[0]);
		txtOldBalance.setMaxWidth(200);
		txtOldBalance.setDisable(true);
		txtOldBalance.setEditable(false);
		txtOldBalance.setTooltip(new Tooltip("Displays the current balance\nCannot edit - Display only"));
		
		TextField txtNewBalance = new TextField();
		txtNewBalance.setFont(arialFont[1]);
		txtNewBalance.setStyle(txtFillColor[0]);
		txtNewBalance.setMaxWidth(200);
		txtNewBalance.setDisable(true);
		txtNewBalance.setTooltip(new Tooltip("Choose the minimum balance shown to deposit or\nEnter a different amount to deposit\nPress Enter to verify amount\n\nAmount entered must be at least the minimum balance"));
		
		// declare text area
		
		TextArea txtOutput = new TextArea();
		txtOutput.setPrefRowCount(5);
		txtOutput.setPrefColumnCount(35);
		txtOutput.setFont(arialFont[1]);
		txtOutput.setStyle(txtFillColor[0]);
		txtOutput.setMaxWidth(650);
		txtOutput.setEditable(false);
		txtOutput.setTooltip(new Tooltip("Select the Account Number and it will be automatically put into the Account Number field"));
		
		// declare buttons
		
		Button btnApply = new Button("Apply");
		btnApply.setFont(arialFont[2]);
		btnApply.setStyle(txtFillColor[5]);
		btnApply.setDisable(true);
		
		Button btnExit = new Button("Exit");
		btnExit.setPadding(new Insets(5, 35, 5, 35));
		btnExit.setFont(arialFont[2]);
		btnExit.setStyle(txtFillColor[6]);
		
		// prepare errorPane
		
		VBox errorPane = new VBox(8);
		errorPane.getChildren().add(lblMessage);
		errorPane.setPadding(new Insets(15, 15, 15, 15));
		errorPane.setAlignment(Pos.CENTER);
		
		// prepare customer number pane
		
		VBox custNumPane = new VBox(8);
		custNumPane.getChildren().addAll(lblCustomerNum, txtCustomerNum);
		custNumPane.setPadding(new Insets(25, 15, 25, 15));
		custNumPane.setAlignment(Pos.CENTER);
		
		// prepare old balance pane
		
		VBox oldBalPane = new VBox(8);
		oldBalPane.setPadding(new Insets(25, 15, 25, 15));
		oldBalPane.getChildren().addAll(lblOldBalance, txtOldBalance);
		oldBalPane.setAlignment(Pos.CENTER);
		
		// prepare new balance pane
		
		VBox newBalPane = new VBox(8);
		newBalPane.setPadding(new Insets(25, 15, 25, 15));
		newBalPane.getChildren().addAll(lblNewBalance, txtNewBalance);
		newBalPane.setAlignment(Pos.CENTER);
		
		// prepare input pane
		
		HBox inputPane = new HBox(8);
		inputPane.setPadding(new Insets(15, 15, 15, 15));
		inputPane.getChildren().addAll(custNumPane, oldBalPane, newBalPane);
		inputPane.setAlignment(Pos.CENTER);
		
		// prepare title pane
		
		VBox titlePane = new VBox(8);
		titlePane.setPadding(new Insets(15, 15, 25, 15));
		titlePane.getChildren().add(lblTitle);
		titlePane.setAlignment(Pos.CENTER);
	
		// prepare output pane
		
		VBox outputPane = new VBox(8);
		outputPane.getChildren().addAll(txtOutput, errorPane);
		outputPane.setAlignment(Pos.CENTER);
		
		// prepare top pane
		
		VBox topPane = new VBox(8);
		topPane.getChildren().addAll(titlePane, inputPane);
		topPane.setAlignment(Pos.CENTER);
		
		// prepare button pane
		
		HBox buttonPane = new HBox(8);
		buttonPane.setPadding(new Insets(25, 15, 25, 15));
		buttonPane.getChildren().addAll(btnApply, btnExit);
		buttonPane.setAlignment(Pos.CENTER);
		
		// prepare main pane
		
		BorderPane mainPane = new BorderPane();
		mainPane.setTop(topPane);
		mainPane.setCenter(outputPane);
		mainPane.setBottom(buttonPane);
		
		// **************************************************** customer number - key pressed ****************************************************
		
		txtCustomerNum.setOnKeyPressed(e -> {
			lblMessage.setText("");
		});
		
		// **************************************************** customer number - action ****************************************************
		
		txtCustomerNum.setOnAction(e -> {
			// did the user enter anything
			if (txtCustomerNum.getText().isEmpty()) {
				lblMessage.setText("Please enter a Customer Number!");
				lblMessage.setStyle(txtFillColor[1]);
				txtCustomerNum.requestFocus();
			// did the user enter 0 to display the errors
			} else if (txtCustomerNum.getText().equals("0")) {
				// if there are no errors disable all except the exit button
				if (eomErrors.isEmpty()) {
					lblMessage.setText("There are no more Errors to process");
					lblMessage.setStyle(txtFillColor[1]);
					btnApply.setDisable(true);
					txtCustomerNum.setDisable(true);
					txtOldBalance.setDisable(true);
					txtNewBalance.setDisable(true);
					txtOutput.setDisable(true);
					btnExit.requestFocus();
				}
				// display the errors to the user
				for (Account a: eomErrors) {
					txtOutput.appendText(a.getAccountNumber() + "  " + a.getCustomer().getCustomerID() + "  " + a.getCustomer().getCustomerName() + "   $" + (String.format("%12.2f",((Checking) a).getCheckingTransactionFeeAmount())).trim() + "\n");
				}
				txtCustomerNum.requestFocus();
			// it is good to go
			} else {
				// find the correct account 
				for (Account a: eomErrors) {
					if (a.getAccountNumber().equals(txtCustomerNum.getText())) {
						if (a instanceof Checking) {
							// prepare minimum amount the user must enter
							minBal = (((Checking) a).getCheckingTransactionFeeAmount() - ((Checking)a).getAccountBalance());
							// set the old balance
							txtOldBalance.setText(String.format("%12.2f", a.getAccountBalance()).trim());
							// enable old balance
							txtOldBalance.setDisable(false);
							// set the new balance with minimum balance
							txtNewBalance.setText(String.format("%12.2f", minBal).trim());
							// enable new balance
							txtNewBalance.setDisable(false);
							txtNewBalance.requestFocus();
						}
						
					}
				}
			}
		});
		
		// ************************************************* new balance - key pressed *************************************************
		
		txtNewBalance.setOnKeyPressed(e -> {
			lblMessage.setText("");
		});
		
		// ************************************************* new balance - action *************************************************
		txtNewBalance.setOnAction(e -> {
			// did the user enter anything
			if (txtNewBalance.getText().isEmpty()) {
				lblMessage.setText("You must enter at least " + String.format("%12.2f", minBal).trim());
				lblMessage.setStyle(txtFillColor[1]);
				btnApply.setDisable(true);
				// is the amount entered equal or greater than the minimum balance
			} else if (getBalance(txtNewBalance) < minBal) {
				lblMessage.setText("You must enter at least " + String.format("%12.2f", minBal).trim());
				lblMessage.setStyle(txtFillColor[1]);
				btnApply.setDisable(true);
			// passed all error checking, enable apply button
			} else {
				btnApply.setDisable(false);
				btnApply.requestFocus();
			}
		});
		
		// ************************************************* output - mouse clicked *************************************************
		
		txtOutput.setOnMouseClicked(e -> {
			// put the selected text from the text area into the customer number
			txtCustomerNum.setText(txtOutput.getSelectedText().trim());
			txtCustomerNum.requestFocus();
		});
		
		// ************************************************* button apply - action *************************************************
		
		btnApply.setOnAction(e -> {
			// did the user enter anything in the customer number
			if (txtCustomerNum.getText().isEmpty()) {
				lblMessage.setText("You must enter a Account Number to process");
				lblMessage.setStyle(txtFillColor[1]);
				txtCustomerNum.requestFocus();
			// did the user enter anything in the old balance
			} else if (txtOldBalance.getText().isEmpty()) {
				lblMessage.setText("You must choose which Account Number to process");
				lblMessage.setStyle(txtFillColor[1]);
				txtCustomerNum.requestFocus();
			// did the user enter anything in the new balance
			} else if (txtNewBalance.getText().isEmpty()) {
				lblMessage.setText("You must enter a minimum balance to apply to the Account Number");
				lblMessage.setStyle(txtFillColor[1]);
				txtNewBalance.requestFocus();
			// is the amount entered equal or greater than the minimum balance
			} else if (getBalance(txtNewBalance) < minBal) {
				lblMessage.setText("You must enter at least " + String.format("%12.2f", minBal).trim());
				lblMessage.setStyle(txtFillColor[1]);
				txtNewBalance.requestFocus();
			// passed all error checking
			} else {
				// declare usable variables
				Account a = eomErrors.get(0);
				Account a1 = accounts.get(0);
				Account acct = accounts.get(0);
				// validate account number
				for (int x = 0; x < eomErrors.size(); x++) {
					a = eomErrors.get(x);
					if (a.getAccountNumber().equals(txtCustomerNum.getText())) {
						// find the correct account in the accounts table
						for (int y = 0; y < accounts.size(); y++) {
							acct = accounts.get(y);
							if (acct instanceof Checking) {
								a1 = ((Checking) acct);
							} else if (acct instanceof Regular) {
								a1 = ((Regular) acct);
							} else {
								a1 = ((Gold) acct);
							}
							// was the account number found
							if (a1.getAccountNumber().equals(txtCustomerNum.getText())) {
								break;
							}
						}
						// declare back out variables and processing variables
						double procAmount = ((Checking) a1).getCheckingTransactionFeeAmount();
						double oldBalance = ((Checking) a1).getAccountBalance();
						double oldFee = ((Checking) a1).getCheckingTransactionFeeAmount();
						int oldTran = ((Checking) a1).getNumberOfTransactions();
						boolean isOk = ((Checking) a1).makeDeposit(getBalance(txtNewBalance));
						// was the deposit successful
						if (isOk) {
							// now try the withdrawal
							double wd = ((Checking) a1).makeWithdrawal(procAmount);
							// was it successful
							if (wd < minBal) {
								// it should never get here but the error checking is in place just in case
								lblMessage.setText("A very serious error has occurred\nNotify Supervisor immediately");
								lblMessage.setStyle(txtFillColor[1]);
								// back out by reseting all values to the point before it was started
								((Checking) a1).setAccountBalance(oldBalance);
								((Checking) a1).setNumberOfTransactions(oldTran);
								((Checking) a1).setCheckingTransactionFeeAmount(oldFee);
								// disable fields and apply button
								btnApply.setDisable(true);
								txtCustomerNum.setDisable(true);
								txtOldBalance.setDisable(true);
								txtNewBalance.setDisable(true);
								txtOutput.setDisable(true);
								btnExit.requestFocus();
								break;
							// it was successful
							} else {
								// make transaction for the deposit
								processTransaction(a1.getCustomer().getCustomerID(), a1.getAccountNumber(), "New EOM Checking - Deposit", getBalance(txtNewBalance));
								// make transaction for the withdrawal
								processTransaction(a1.getCustomer().getCustomerID(), a1.getAccountNumber(), "New EOM Checking - Fees", procAmount);
								// reset transaction fee amount and the number of transactions
								((Checking) a1).setCheckingTransactionFeeAmount(0.0);
								((Checking) a1).setNumberOfTransactions(0);
								// now remove this error from the error table
								eomErrors.remove(a);
								// notify user and reset the text fields and disable the apply button
								lblMessage.setText("Account successfully Processed!");
								lblMessage.setStyle(txtFillColor[0]);
								txtCustomerNum.setText("");
								txtOldBalance.setText("");
								txtOldBalance.setDisable(true);
								txtNewBalance.setText("");
								txtNewBalance.setDisable(true);
								txtOutput.clear();
								btnApply.setDisable(true);
								break;
							}
						// it was not ok
						} else {
							// should never get here but the error checking is in place just in case
							lblMessage.setText("Unable to process Deposit\nNotify Supervisor immediately");
							lblMessage.setStyle(txtFillColor[1]);
							btnApply.setDisable(true);
							txtOldBalance.setDisable(true);
							txtNewBalance.setDisable(true);
							txtCustomerNum.requestFocus();
							break;
						}
						
					}
				}
			}
		});
		
		// ************************************************* button exit - action *************************************************
		btnExit.setOnAction(e -> {
			btnExit.getParent().getScene().getWindow().hide();
		});
		
		return mainPane;
	}

} // end of BankMethods class
