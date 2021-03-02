package com.trecapps.falsehoodauth.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class TrecAuthority implements GrantedAuthority
{
    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public TrecAuthority(String authority) {
        this.authority = authority;
    }

    public TrecAuthority() {
        this.authority = "";
    }

    String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}
