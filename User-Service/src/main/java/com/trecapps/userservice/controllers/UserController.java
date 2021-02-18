package com.trecapps.userservice.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.trecapps.userservice.models.TrecAccountExt;
import com.trecapps.userservice.security.TrecAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.trecapps.userservice.models.LogIn;
import com.trecapps.userservice.models.NewUser;
import com.trecapps.userservice.models.ReturnObj;
import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.services.JwtTokenService;
import com.trecapps.userservice.services.TrecAccountService;

import java.io.IOException;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	TrecAccountService accountService;
	
	@Autowired
	JwtTokenService tokenService;
	
	@GetMapping(value="/CreateUser")
	public ModelAndView formNewUser()
	{
		ModelAndView view = new ModelAndView();
		view.setViewName("register");
		return view;
	}
	
	@PostMapping("/CreateUser")
	ResponseEntity<ReturnObj> createUser(RequestEntity<NewUser> entity)
	{
		var body = entity.getBody();
		
		TrecAccount account = accountService.createAccount(body);
		
		if(account == null)
			return new ResponseEntity<ReturnObj>(HttpStatus.BAD_REQUEST);
		
		ReturnObj ret = generateAuth(account);
		
		if(ret == null)
			return new ResponseEntity<ReturnObj>(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ReturnObj>(ret, HttpStatus.OK);
	}
	
	private ReturnObj generateAuth(TrecAccount account)
	{
		String token = tokenService.generateToken(account);
		
		if(token == null)
			return null;
		
		return new ReturnObj(token, account.getUsername(), account.getFirstName(), account.getLastName(), account.getColor());
	}
	
	@GetMapping("/UserExists")
	Boolean userExists(@RequestParam String username)
	{
		return accountService.getAccountByUserName(username) != null;
	}

	@PostMapping("/LogIn")
	void logIn(RequestEntity<LogIn> entity, HttpServletResponse response) throws IOException {
		LogIn login = entity.getBody();
		
		if(login == null)
		{
			System.out.println("Login Object was Null!");
			response.setStatus(HttpStatus.BAD_REQUEST.value());
			response.getWriter().write("Null Login Object Detected!");
			return;
		}
		
		
		TrecAccount account = null;
		if(login.getUsername() != null)
		{
			System.out.println("Login using username");
			account = accountService.logInUsername(login.getUsername(), login.getPassword());
		}
		else if(login.getEmail() != null)
		{
			System.out.println("Login using email");
			account = accountService.logInEmail(login.getEmail(), login.getPassword());
		}
		
		if(account == null)
		{
			System.out.println("Account was null!");
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			return;
		}

		SecurityContextHolder.getContext().setAuthentication(new TrecAuthentication(account));

	}

	@GetMapping("/Account")
	ResponseEntity<TrecAccountExt> getAccount()
	{
		TrecAuthentication user = ((TrecAuthentication)SecurityContextHolder.getContext().getAuthentication());
		System.out.println(user);
		TrecAccount acc = user.getAccount();
		acc.setOauthUse(0);
		acc.setFailedLoginAttempts((byte)0);
		acc.setLockInit(null);
		acc.setToken(null);
		acc.setValidationToken(null);
		acc.setValidTimeFromActivity((byte)0);

		return new ResponseEntity<>(acc.getExternalRep(), HttpStatus.OK);
	}
	
	@GetMapping("/Validate")
	Boolean validate()
	{
		TrecAuthentication user = ((TrecAuthentication)SecurityContextHolder.getContext().getAuthentication());
		TrecAccount acc = ((TrecAuthentication)user).getAccount();

		// Eventually, plan to add text validation in addition to email validation
		if(acc.getIsValidated() > 1)
			return false;
		
		accountService.sendverificationEmail(acc);
		return true;
	}
	
	@PostMapping("/Validate")
	ResponseEntity<String> validateWithToken(HttpServletRequest req)
	{
		TrecAuthentication user = ((TrecAuthentication)SecurityContextHolder.getContext().getAuthentication());
		TrecAccount account = user.getAccount();

		String validationToken = req.getHeader("Verification");
		
		if(validationToken == null)
			return new ResponseEntity<String>("Validation Token not provided in 'Verification' header", HttpStatus.BAD_REQUEST);
		
		account = accountService.verifyAccount(account.getUsername(), validationToken);
		
		if(account == null)
			return new ResponseEntity<String>("Validation Token provided in 'Verification' header not correct", HttpStatus.BAD_REQUEST);
		
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}

	
	@PutMapping(value = "/UpdateUser")
	ResponseEntity<String> updateUser(RequestEntity<TrecAccount> entry)
	{

		TrecAuthentication auth = ((TrecAuthentication)SecurityContextHolder.getContext().getAuthentication());
		TrecAccount user = auth.getAccount();

		TrecAccount newSettings = entry.getBody();
		
		user.setColor(newSettings.getColor());
		user.setFirstName(newSettings.getFirstName());
		user.setLastName(newSettings.getLastName());
		user.setBackupEmail(newSettings.getBackupEmail());
		user.setLockTime(newSettings.getLockTime());
		user.setMaxLoginAttempts(newSettings.getMaxLoginAttempts());
		user.setTimeForValidToken(newSettings.getTimeForValidToken());
		user.setValidTimeFromActivity(newSettings.getValidTimeFromActivity());
		
		if(accountService.updateUser(user))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		return new ResponseEntity<String>("Failed to Update User!", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	
	@PostMapping(value = "/UpdatePassword", consumes = "application/x-www-form-urlencoded")
	Boolean updatePassword(RequestEntity<MultiValueMap<String, String>> entry)
	{
		TrecAuthentication auth = ((TrecAuthentication)SecurityContextHolder.getContext().getAuthentication());
		TrecAccount user = auth.getAccount();

		MultiValueMap<String, String> map = entry.getBody();
		
		String username = map.getFirst("username");
		String oldPassword = map.getFirst("oldPassword");
		String newPassword = map.getFirst("newPassword");

		TrecAccount account;
		
		if(username.indexOf('@') != -1)
		{
			account = accountService.logInEmail(username, oldPassword);
		}
		else
		{
			account = accountService.logInUsername(username, oldPassword);
		}
		
		
		if(account == null)
		{
			return false;
		}
		if(!account.equals(user))
		{
			return false;
		}
		
		return accountService.updatePassword(account.getAccountId(), oldPassword, newPassword) != null;
	}
	
	@GetMapping("/Logout")
	Boolean logout()
	{
		return true;
	}
}
