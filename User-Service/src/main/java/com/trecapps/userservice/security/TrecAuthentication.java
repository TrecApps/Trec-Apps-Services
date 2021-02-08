package com.trecapps.userservice.security;

import com.trecapps.userservice.models.primary.TrecAccount;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import javax.security.auth.Subject;
import java.util.Collection;

public class TrecAuthentication implements Authentication {

    TrecAccount account;
    boolean isTrusted;

    TrecAuthentication(TrecAccount account)
    {
        this.account = account;
        isTrusted = true;
    }

    public TrecAccount getAccount()
    {
        return account;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return account.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return account.getToken();
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return account.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return account.isAccountNonExpired() && account.isAccountNonLocked() && isTrusted;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        isTrusted = isAuthenticated;
    }

    @Override
    public String getName() {
        return account.getUsername();
    }


}
