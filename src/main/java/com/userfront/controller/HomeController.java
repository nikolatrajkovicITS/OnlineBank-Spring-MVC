package com.userfront.controller;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.userfront.dao.RoleDao;
import com.userfront.domain.PrimaryAccount;
import com.userfront.domain.SavingsAccount;
import com.userfront.domain.User;
import com.userfront.domain.security.UserRole;
import com.userfront.service.UserService;

/*************** Controller ****************************** 
 * 
 *  This class will be register as a Bean in the Spring
 *  Container.
 *  Spring knows this Bean exist and then he will register 
 *  corresponding path.
 *   
 *********************************************************/
@Controller
public class HomeController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private RoleDao roleDao;
	
	@RequestMapping("/")    // root path 
	public String home() {
		return "redirect:/index";
	}
	
	@RequestMapping("/index")
    public String index() {
        return "index";
    }
	
	@RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        User user = new User();

        model.addAttribute("user", user);       // This metod return "user" variable on signup page 

        return "signup";
    }
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupPost(@ModelAttribute("user") User user,  Model model) {       // @ModelAttribute("user") vracamo user-a kog smo prethodno submitovali u formi za singup - I dajemo tog user u ovaj objekat: User user 

        if(userService.checkUserExists(user.getUsername(), user.getEmail()))  {

            if (userService.checkEmailExists(user.getEmail())) {
                model.addAttribute("emailExists", true);
            }

            if (userService.checkUsernameExists(user.getUsername())) {
                model.addAttribute("usernameExists", true);
            }

            return "signup";
        } else {
        	 Set<UserRole> userRoles = new HashSet<>();
             userRoles.add(new UserRole(user, roleDao.findByName("ROLE_USER")));     // this appercase for ROLE_USER is standard in Spring 

            userService.createUser(user, userRoles);

            return "redirect:/";
        }
    }
	
	@RequestMapping("/userFront")       // Here we define userFront mapping, and Two parameters, one is Principal other is Model. Principal in SpringSecurity context is the object who has login in the userFront 
	public String userFront(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());  // We use principal here to get principals name(username spceficly). We use userService.findByUsername = to retrieve user instance who has login in the system.And we assing that instance in user instance which we create here.
        PrimaryAccount primaryAccount = user.getPrimaryAccount();     // We define PrimaryAccount instance which will be assing in to user.getPrimaryAccount = basicly we retrieve user PrimaryAccount and SavingsAccount
        SavingsAccount savingsAccount = user.getSavingsAccount();

        model.addAttribute("primaryAccount", primaryAccount);         // And we gonna add that Primary and Savings acc in Model
        model.addAttribute("savingsAccount", savingsAccount);

        return "userFront";               // We retrieve userFront, and in userFront.html we will have the PrimaryAccount and SavingsAccount two instance available to be use.
    }
}
