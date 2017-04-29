package com.userfront.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.PrimaryTransaction;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.SavingsTransaction;
import com.userfront.domain.User;
import com.userfront.service.AccountService;
import com.userfront.service.TransactionService;
import com.userfront.service.UserService;


/*************** Controller **************************
 * 
 *  We need Controller to mapping the HTML file.
 *  @RequestMapping on class level means: "anyone
 *  method we define in class which return some path
 *  we append this: "/account" before that page".
 *  
 *****************************************************/
@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private TransactionService transactionService;
	
	@RequestMapping("/primaryAccount")
	public String primaryAccount(Model model, Principal principal) {
		List<PrimaryTransaction> primaryTransactionList = transactionService.findPrimaryTransactionList(principal.getName());
		
		User user = userService.findByUsername(principal.getName());   // We retrieve user from the principal
        PrimaryAccount primaryAccount = user.getPrimaryAccount();      // We retrieve PrimaryAccount account of that user

        model.addAttribute("primaryAccount", primaryAccount);		   // Adding primaryAccount to the attribute
        model.addAttribute("primaryTransactionList", primaryTransactionList);  // add transaction List to model
		
		return "primaryAccount";    								   // We return primaryAccount.html(template)
	}

	@RequestMapping("/savingsAccount")
    public String savingsAccount(Model model, Principal principal) {
		List<SavingsTransaction> savingsTransactionList = transactionService.findSavingsTransactionList(principal.getName());
        User user = userService.findByUsername(principal.getName());
        SavingsAccount savingsAccount = user.getSavingsAccount();

        model.addAttribute("savingsAccount", savingsAccount);
        model.addAttribute("savingsTransactionList", savingsTransactionList);

        return "savingsAccount";
    }
	
	@RequestMapping(value = "/deposit", method = RequestMethod.GET)  // First one is always GET and second is POST method
    public String deposit(Model model) {							 // We have a GET method for /deposit which will be returned to deposit.html page.
        model.addAttribute("accountType", "");						 // We have 2 variables added to model, we have also Empty value 
        model.addAttribute("amount", "");

        return "deposit";
    }

    @RequestMapping(value = "/deposit", method = RequestMethod.POST)  // After we submit form from deposit template we are basicly POST to this method and we retrieve Amount variable by using the Model-attribute(Annotation)
    public String depositPOST(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal) {    // And we asing value from: @ModelAttribute("amount")  to --> String amount
        accountService.deposit(accountType, Double.parseDouble(amount), principal);																	// and we retrieve accountType --> @ModelAttribute("accountType") assign the value --> String accountType.
        																																			// deposit() takes accountType, amount and principal as parameter.
        return "redirect:/userFront";																												// After deposit completed, we retrieve redirect to the UserFront
    }
    
    @RequestMapping(value = "/withdraw", method = RequestMethod.GET)
    public String withdraw(Model model) {
        model.addAttribute("accountType", "");
        model.addAttribute("amount", "");

        return "withdraw";
    }

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public String withdrawPOST(@ModelAttribute("amount") String amount, @ModelAttribute("accountType") String accountType, Principal principal) {
        accountService.withdraw(accountType, Double.parseDouble(amount), principal);

        return "redirect:/userFront";
    }
}
