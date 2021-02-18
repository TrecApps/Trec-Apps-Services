package com.trecapps.userservice.services;

import com.trecapps.userservice.security.TrecSecurityContext;
import com.trecapps.userservice.services.TrecAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class TrecSecurityConfig extends WebSecurityConfigurerAdapter 
{
	@Autowired
	TrecAccountService accountService;

	@Autowired
	TrecSecurityContext secContext;

	String restrictedEndpoints[] = {
		"auth/oauth2/authorize",
		"users/Validate",
		"users/Account",
		"users/UpdateUser",
		"users/UpdatePassword",
		"clients/*"
	};

	@Override
	protected void configure(HttpSecurity http) throws Exception
	{
		http.csrf().disable().
				sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
				.userDetailsService(accountService)
				.securityContext().securityContextRepository(secContext).and()
				.authorizeRequests().antMatchers("/**").permitAll().and()
				.authorizeRequests().antMatchers(restrictedEndpoints).authenticated().and()
				.formLogin().loginPage("/login");
		// http.authorizeRequests().antMatchers("/*").permitAll();
	}


}