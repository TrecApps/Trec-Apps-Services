package com.trecapps.userservice.models;

import org.springframework.stereotype.Component;

import com.trecapps.userservice.models.primary.TrecAccount;

@Component
public class ReturnAccount 
{
	TrecAccount account;

	String token;

	/**
	 * 
	 */
	public ReturnAccount() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the account
	 */
	public TrecAccount getAccount() {
		return account;
	}
	
	

	/**
	 * @param account
	 * @param token
	 */
	public ReturnAccount(TrecAccount account, String token) {
		super();
		setAccount(account);
		this.token = token;
	}

	/**
	 * @param account the account to set
	 */
	public void setAccount(TrecAccount account) {
		
		account.setLockInit(null);
		account.setRecentFailedLogin(null);
		account.setValidationToken(null);
		account.setToken(null);
		
		this.account = account;
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}
	
	
}
