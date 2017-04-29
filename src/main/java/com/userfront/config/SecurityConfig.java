package com.userfront.config;

import java.security.SecureRandom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.userfront.service.UserServiceImpl.UserSecurityService;

@Configuration      // Spring will know - This is now Configuration class, this is config for Security 
@EnableWebSecurity  // To Enable Spring Security
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private Environment env;

    @Autowired
    private UserSecurityService userSecurityService;        // Customise Security Service that we will develop

    private static final String SALT = "salt";              // Salt will be use to Encode or Encrypt our password - Salt should be protected carefully. -Example how works: When you Sing up username and password, you don't want to stored password directly into db without encrypt them 
    														// Salt which can be used as seed to generate a password, every time differently   
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {        // Here we define Crypt method- and we use Bean Annot to registered this method as a bean in the Spring Container 
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }

    private static final String[] PUBLIC_MATCHERS = {       // String array = List of specific path that we will like to Access publicly without SPRING SECURITY-protection 
            "/webjars/**",									// and thats because this data(paths) should be public
            "/css/**",
            "/js/**",
            "/images/**",
            "/",
            "/about/**",
            "/contact/**",									// This: /** - means this path will be available(without Security)
            "/error/**/*",
            "/console/**",
            "/signup"
    };

    @Override												// Override previous method in WebSecurityConfigurerAdapter class
    protected void configure(HttpSecurity http) throws Exception {     // This is HTTP configure
        http
                .authorizeRequests().
//                antMatchers("/**").
                antMatchers(PUBLIC_MATCHERS).
                permitAll().anyRequest().authenticated();

        http
                .csrf().disable().cors().disable()          // This will be disable protection of csrf and cors vision(we don't need cors issue to try to work on development) 
                .formLogin().failureUrl("/index?error").defaultSuccessUrl("/userFront").loginPage("/index").permitAll()      // We define our Login URL, we have formLogin() - we suppose that login will be a POST, and when the Login FialuerURL(redirect here "index?error"). - IF Login success (defaultSuccessUrl("/userFront")) send him on "/userFront". - By defailt login page will be loginPage("/index")
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/index?logout").deleteCookies("remember-me").permitAll()    // Also we define Logout functionality: When ever we try to access /logout -this URL, will be trying logout. -And when is sucessfull will redirect on: /index?logout(we have here 2 parametars, second is logout compare to error parameter from FormLogin.- And also we want Delete to Cookies of remember-me function - this will disable User -remember me function
                .and()
                .rememberMe();     // We try to add rememberMe() function. 
    }



    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//    	 auth.inMemoryAuthentication().withUser("user").password("password").roles("USER"); //This is in-memory authentication
        auth.userDetailsService(userSecurityService).passwordEncoder(passwordEncoder());       // We define to Authentication mechanism and we say SpringSecurity to use passwordEncoder
    }


}
