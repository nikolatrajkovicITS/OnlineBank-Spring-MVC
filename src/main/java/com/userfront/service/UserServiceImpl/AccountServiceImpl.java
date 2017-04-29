package com.userfront.service.UserServiceImpl;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.userfront.dao.PrimaryAccountDao;
import com.userfront.dao.SavingsAccountDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;

@Service
public class AccountServiceImpl implements AccountService {
	
	private static int nextAccountNumber = 11223145;

    @Autowired
    private PrimaryAccountDao primaryAccountDao;

    @Autowired
    private SavingsAccountDao savingsAccountDao;

    @Autowired
    private UserService userService;
    
    @Autowired
    private TransactionService transactionService;

    public PrimaryAccount createPrimaryAccount() {
        PrimaryAccount primaryAccount = new PrimaryAccount();                              // We define new Instance
        primaryAccount.setAccountBalance(new BigDecimal(0.0));							   // Set account Balance (AccountBalance is BiGdecimal type, we use new to create a newBigDecimal instance 
        primaryAccount.setAccountNumber(accountGen());									   // We use automatically generation strategy, that is define in PrimaryAccount = @GeneratedValue(strategy = GenerationType.AUTO)

        primaryAccountDao.save(primaryAccount);											   // Only if primaryAccount is persist or saved in db - The Account id - will be generated 
        																				   // We need primary ID, cus in UserServiceImpl we are setting the primaryAcc to the account we just created: user.setPrimaryAccount(accountService.createPrimaryAccount()
        return primaryAccountDao.findByAccountNumber(primaryAccount.getAccountNumber());   // We using find findByAccountNumber to retrieve by primaryAccountDao the account we just saved.
    }

    public SavingsAccount createSavingsAccount() {
        SavingsAccount savingsAccount = new SavingsAccount();
        savingsAccount.setAccountBalance(new BigDecimal(0.0));
        savingsAccount.setAccountNumber(accountGen());

        savingsAccountDao.save(savingsAccount);

        return savingsAccountDao.findByAccountNumber(savingsAccount.getAccountNumber());
    }
    
    public void deposit(String accountType, double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());             // Using userService to find the username(to return a user instance)

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();			 // return PrimaryAccount and set to variable primaryAccount
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().add(new BigDecimal(amount)));   // Setting new Value in accountBalance on way: return current AccountBalance and based on it add(method of BigDecimal) new value, then we define new BigDecimal instace based on AMOUNT
            primaryAccountDao.save(primaryAccount);								 // We use primaryAccountDao to save priamryAccount			

            Date date = new Date();
            																	 // Define a new PrimaryTransaction based on a parameters, date, description, type, status, amount, balance 
            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Deposit to Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryDepositTransaction(primaryTransaction);
            
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().add(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);                              // savings a new savingsAccount information

            Date date = new Date();												 // savingsTransaction has been initialised but not saved or persist yet
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Deposit to savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsDepositTransaction(savingsTransaction);
        }
    }
    
    public void withdraw(String accountType, double amount, Principal principal) {
        User user = userService.findByUsername(principal.getName());

        if (accountType.equalsIgnoreCase("Primary")) {
            PrimaryAccount primaryAccount = user.getPrimaryAccount();
            primaryAccount.setAccountBalance(primaryAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            primaryAccountDao.save(primaryAccount);

            Date date = new Date();

            PrimaryTransaction primaryTransaction = new PrimaryTransaction(date, "Withdraw from Primary Account", "Account", "Finished", amount, primaryAccount.getAccountBalance(), primaryAccount);
            transactionService.savePrimaryWithdrawTransaction(primaryTransaction);
        } else if (accountType.equalsIgnoreCase("Savings")) {
            SavingsAccount savingsAccount = user.getSavingsAccount();
            savingsAccount.setAccountBalance(savingsAccount.getAccountBalance().subtract(new BigDecimal(amount)));
            savingsAccountDao.save(savingsAccount);

            Date date = new Date();
            SavingsTransaction savingsTransaction = new SavingsTransaction(date, "Withdraw from savings Account", "Account", "Finished", amount, savingsAccount.getAccountBalance(), savingsAccount);
            transactionService.saveSavingsWithdrawTransaction(savingsTransaction);
        }
    }
    
    private int accountGen() {
        return ++nextAccountNumber;    // Every time when we have AutoIncrement this method will return a next AccountNumber(id).
    }

	

}
