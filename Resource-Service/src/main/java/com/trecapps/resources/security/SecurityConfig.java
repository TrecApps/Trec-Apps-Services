package com.trecapps.resources.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("search/**").permitAll().and()
                .authorizeRequests().antMatchers("update/**")
                .hasAnyRole("REGULAR_EMPLOYEE", "FALSEHOODS_FACT").and()
                .oauth2Login().loginProcessingUrl("http://localhost:8081/auth/oauth2/login");
    }
}
