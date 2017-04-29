package com.userfront.service.UserServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.userfront.dao.UserDao;
import com.userfront.domain.User;

@Service     // registered the bean service
public class UserSecurityService implements UserDetailsService {

    /** The application logger */
    private static final Logger LOG = LoggerFactory.getLogger(UserSecurityService.class);

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {    // Username coming from the login
        User user = userDao.findByUsername(username);                                            // UserDao try to find username
        if (null == user) {
            LOG.warn("Username {} not found", username);
            throw new UsernameNotFoundException("Username " + username + " not found");
        }
        return user;         // We must return this message as UserDetails cus we are invoke UserDetailsService in SecurityConfig class -method(configureGlobal)
    }                        // We want return to user, we can do that if we didn't implements UserDetails class in our User class
}
