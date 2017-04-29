package com.userfront.service;

import java.util.List;
import java.util.Set;

import com.userfront.domain.User;
import com.userfront.domain.security.UserRole;

public interface UserService {      // If we don't define some method from here, in interface. Spring will not know which method the call, when it's automatically wired.  
	
	User findByUsername(String username);

    User findByEmail(String email);

    boolean checkUserExists(String username, String email);

    boolean checkUsernameExists(String username);

    boolean checkEmailExists(String email);
    
    void save (User user);
    
    User createUser(User user, Set<UserRole> userRoles);
    
    User saveUser (User user); 
    
    List<User> findUserList();

    void enableUser (String username);

    void disableUser (String username);
}
