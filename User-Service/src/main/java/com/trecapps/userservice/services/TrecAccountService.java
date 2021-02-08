package com.trecapps.userservice.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.trecapps.userservice.models.NewUser;
import com.trecapps.userservice.models.primary.TrecAccount;

@Service
public interface TrecAccountService extends UserDetailsService {

	TrecAccount getAccountById(long id);
	TrecAccount getAccountByUserName(String username);
	TrecAccount getAccountByEmail(String userEmail);
	TrecAccount updatePassword(long id, String oldPassword, String newPassword);
	
	TrecAccount verifyAccount(String username, String validationCode);
	void sendverificationEmail(TrecAccount account);
	
	TrecAccount createAccount(NewUser newUser);
	TrecAccount logInUsername(String username, String password);
	TrecAccount logInEmail(String username, String password);
	boolean deleteAccount(long id, String password);
	boolean verifyLoggedIn(long id, String username, String password);
	
	boolean updateUser(TrecAccount account);
}
