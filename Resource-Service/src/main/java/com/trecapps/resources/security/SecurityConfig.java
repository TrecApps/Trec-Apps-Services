package com.trecapps.resources.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Autowired
    TrecOauthClientRepository trecOauthClientRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().authorizeRequests().antMatchers("/search/**").permitAll().and()
                .authorizeRequests().antMatchers("/tokenize").permitAll().and()
                .authorizeRequests().antMatchers("/update/**")
                .hasAnyRole("REGULAR_EMPLOYEE", "FALSEHOODS_FACT").and()
                .authorizeRequests().anyRequest().authenticated().and()
                .securityContext().securityContextRepository(trecOauthClientRepository);
    }
}
