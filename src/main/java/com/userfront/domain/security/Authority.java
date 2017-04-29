package com.userfront.domain.security;

import org.springframework.security.core.GrantedAuthority;


public class Authority implements GrantedAuthority {    // Represents an authority granted to an Authentication object. 

    private final String authority;

    public Authority(String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {                      // We have this method to return this authority String 
        return authority;
    }
}
