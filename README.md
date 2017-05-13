# BankProject
Bank Project using JavaFX menus

This system is a simple banking program

When it first starts it will ask you for the Bank Name
It will then open a simple menu that will allow you to
create customers and accounts, Checking, Regular and Gold.
Whether it is a Customer or Account, the ID for the Customer
and the Account Number for the accounts must be unique.
You are allowed to enter your own Customer ID's or Account
Numbers or have the system create one for you.

Customers can have multiple accounts but an account must have
one customer. You will not be able to remove a customer from
the system unless all accounts that this customer has
are also removed.

All transactions are tracked that affect the Account Balance.
When an Account is removed it is added to the Transaction
table and also when a Customer is removed, it also is added
to the Transaction table.

A Gold Account, by default, is a interest baring account at 5%
compounded monthly. It also has no limit, and does not track deficit
amount. In otherwords it is the only Account that can have a
negative balance.

A Regular Account, by default, is a interest baring account at 6%
compounded monthly and it has a $10.00 interest fee. When
End-Of-Month (EOM) is calculated the interest that is generated,
the $10.00 interest fee will be subtracted from it.

A Checking Account, by default, is not an interest baring account.
This account tracks transactions and the first two per month are free
and all others will be charged a $3.00 fee for each transation
throughout that month until EOM is calculated.

This is the basic description of the system.

