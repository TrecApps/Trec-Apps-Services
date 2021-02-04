package com.trecapps.userservice.services;

import java.sql.Date;
import java.util.Calendar;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.trecapps.userservice.models.NewUser;
import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.models.secondary.UserSalt;
import com.trecapps.userservice.repositories.primary.TrecAccountRepo;
import com.trecapps.userservice.repositories.secondary.UserSaltRepo;

@Service("TrecAccountService")
public class TrecAccountServiceImp implements TrecAccountService//,  UserDetailsService
{

	@Autowired
	TrecAccountRepo trecRepo;
	
	@Autowired
	UserSaltRepo saltRepo;
	
	@Autowired
	TrecEmailService emailer;
	
	// chose a Character random from this String 
    final String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                + "0123456789"
                                + "abcdefghijklmnopqrstuvxyz";
    final int RANDOM_STRING_LENGTH = 20;
	
	
	public TrecAccount getAccountById(long id)
	{	
		return trecRepo.getOne(id);
	}

	public TrecAccount getAccountByUserName(String username) 
	{
		return trecRepo.getTrecAccountByUsername(username);
	}

	public TrecAccount getAccountByEmail(String userEmail) 
	{
		return trecRepo.getTrecAccountByMainEmail(userEmail);
	}

	public TrecAccount updatePassword(long id, String oldPassword, String newPassword) 
	{
		TrecAccount account = trecRepo.getOne(id);
		if(account == null)
			return null;
		
		
		UserSalt us = saltRepo.getOne(account.getAccountId());
		
		if(us == null)
			return null;
		
		String currentHash = account.getToken();
		if(currentHash == null)
			return null;
		
		if(currentHash.equals(BCrypt.hashpw(oldPassword, us.getSalt())))
		{
			String newSalt = BCrypt.gensalt();
			
			us.setSalt(newSalt);
			
			us = saltRepo.save(us);
			
			account.setToken(BCrypt.hashpw(newPassword, newSalt));
			
			return trecRepo.save(account);
		}
		
		
		return null;
	}

	public TrecAccount createAccount(NewUser newUser) 
	{
		TrecAccount exists = trecRepo.getTrecAccountByUsername(newUser.getUsername());
		if(exists != null)
			return null;
		
		TrecAccount account = newUser.getTrecAccount();
		
		String newSalt = BCrypt.gensalt();
		
		
		
		
		
		account.setToken(BCrypt.hashpw(newUser.getPassword(), newSalt));
		
		
		account = trecRepo.save(account);
		
		UserSalt us = new UserSalt(account.getAccountId(), newSalt);
		
		
		us = saltRepo.save(us);
		
		return account;
	}

	public TrecAccount logInUsername(String username, String password) 
	{
		TrecAccount exists = trecRepo.getTrecAccountByUsername(username);
		if(exists == null)
			return null;
		
		UserSalt us = saltRepo.getOne(exists.getAccountId());
		if(us == null)
			return null;
		
		String curToken = exists.getToken();
		
		if(curToken == null)
			return null;
		
		if(curToken.equals(BCrypt.hashpw(password, us.getSalt())))
		{
			return exists;
		}
		
		exists.setFailedLoginAttempts((byte)(exists.getFailedLoginAttempts() + 1));
		
		byte failed = exists.getFailedLoginAttempts();
		
		if(failed >= exists.getMaxLoginAttempts())
		{
			exists.setLockInit(new Date(Calendar.getInstance().getTime().getTime()));
		}
		
		trecRepo.save(exists);
		
		return null;
	}

	public TrecAccount logInEmail(String username, String password) {
		TrecAccount exists = trecRepo.getTrecAccountByMainEmail(username);
		if(exists == null)
			return null;
		
		UserSalt us = saltRepo.getOne(exists.getAccountId());
		if(us == null)
			return null;
		
		String curToken = exists.getToken();
		
		if(curToken == null)
			return null;
		
		if(curToken.equals(BCrypt.hashpw(password, us.getSalt())))
		{
			return exists;
		}
		
		exists.setFailedLoginAttempts((byte)(exists.getFailedLoginAttempts() + 1));
		
		byte failed = exists.getFailedLoginAttempts();
		
		if(failed >= exists.getMaxLoginAttempts())
		{
			exists.setLockInit(new Date(Calendar.getInstance().getTime().getTime()));
		}
		
		trecRepo.save(exists);
		
		return null;
	}

	public boolean deleteAccount(long id, String password) {
		
		TrecAccount account = trecRepo.getOne(id);
		if(account == null)
			return false;
		
		UserSalt us = saltRepo.getOne(account.getAccountId());
		if(us == null)
			return false;
		
		String curToken = account.getToken();
		
		if(curToken == null)
			return false;
		
		if(curToken.equals(BCrypt.hashpw(password, us.getSalt())))
			return false;
		trecRepo.delete(account);
		return true; 
	}

//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//		TrecAccount exists = trecRepo.getTrecAccountByUsername(username);
//		if(exists == null)
//			throw new UsernameNotFoundException("Could not find user by the name '" + username + "'");
//		return exists;
//	}

	public boolean verifyLoggedIn(long id, String username, String password) {
		TrecAccount ta = trecRepo.getOne(id);
		if(ta == null) return false;
		
		UserSalt us = saltRepo.getOne(ta.getAccountId());
		if(us == null)
			return false;
		
		String curToken = ta.getToken();
		
		if(curToken == null)
			return false;
		
		if(curToken.equals(BCrypt.hashpw(password, us.getSalt())))
			return false;
		
		trecRepo.save(ta);
		return true;
	}

	@Override
	public TrecAccount verifyAccount(String username, String validationCode) {
		TrecAccount exists = trecRepo.getTrecAccountByUsername(username);
		if(exists == null)
		{
			throw new UsernameNotFoundException("Could not find user by the name '" + username + "'");
		}
		if(exists.getValidationToken().equals(validationCode))
		{
			exists.setIsValidated(1);
			return trecRepo.save(exists);
		}
		return null;
	}

	@Override
	public void sendverificationEmail(TrecAccount account) {
		String email = account.getMainEmail();
		String validationHash = generateRandomString();
		
		account.setValidationToken(validationHash);
		trecRepo.save(account);
		try {
			emailer.sendValidationEmail(email, "Trec-Account Verification", validationHash);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	private String generateRandomString()
	{
		StringBuilder sb = new StringBuilder();
		for(int c = 0; c < RANDOM_STRING_LENGTH; c++)
		{
			int ch = (int) (Math.random() * AlphaNumericString.length());
			sb.append(AlphaNumericString.charAt(ch));
		}
		return sb.toString();
	}

	@Override
	public boolean updateUser(TrecAccount account) {
		TrecAccount token = trecRepo.getOne(account.getAccountId());
		if(token == null)
			return false;
		account.setToken(token.getToken());
		return trecRepo.save(account) != null;
	}


}
