package com.trecapps.falsehoodauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter
{
    @Autowired
    TrecOauthClientRepository trecOauthClientRepository;
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**").authorizeRequests()
                .antMatchers("/login**").permitAll()
                .anyRequest().authenticated().and()
        .securityContext().securityContextRepository(trecOauthClientRepository);;
    }
}
