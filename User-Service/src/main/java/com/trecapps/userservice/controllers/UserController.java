package com.trecapps.userservice.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.trecapps.userservice.models.LogIn;
import com.trecapps.userservice.models.NewUser;
import com.trecapps.userservice.models.PasswordChange;
import com.trecapps.userservice.models.ReturnAccount;
import com.trecapps.userservice.models.ReturnObj;
import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.services.JwtTokenService;
import com.trecapps.userservice.services.TrecAccountService;

@RestController
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
	ResponseEntity<ReturnObj> logIn(RequestEntity<LogIn> entity)
	{
		LogIn login = entity.getBody();
		
		if(login == null)
		{
			System.out.println("Login Object was Null!");
			return new ResponseEntity<ReturnObj>(HttpStatus.BAD_REQUEST);
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
			return new ResponseEntity<ReturnObj>(HttpStatus.UNAUTHORIZED);
		}
		ReturnObj ret = generateAuth(account);
		
		if(ret == null)
			return new ResponseEntity<ReturnObj>(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ReturnObj>(ret, HttpStatus.OK);
	}
	
	@GetMapping("/Validate")
	Boolean validate(HttpServletRequest req)
	{
		String token = req.getHeader("Authorization");
		if(token == null)
			return false;
		
		TrecAccount account = tokenService.verifyToken(token);
		
		if(account == null)
			return false;
		
		if(account.getIsValidated() != 0)
			return true;
		
		accountService.sendverificationEmail(account);
		return true;
	}
	
	@PostMapping("/Validate")
	ResponseEntity<String> validateWithToken(HttpServletRequest req)
	{
		String token = req.getHeader("Authorization");
		String validationToken = req.getHeader("Verification");
		
		if(token == null)
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		
		TrecAccount account = tokenService.verifyToken(token);
		
		if(account == null)
			return new ResponseEntity<String>(HttpStatus.UNAUTHORIZED);
		
		if(validationToken == null)
			return new ResponseEntity<String>("Validation Token not provided in 'Verification' header", HttpStatus.BAD_REQUEST);
		
		account = accountService.verifyAccount(account.getUsername(), validationToken);
		
		if(account == null)
			return new ResponseEntity<String>("Validation Token provided in 'Verification' header not correct", HttpStatus.BAD_REQUEST);
		
		String ret = tokenService.generateToken(account);
		
		if(ret == null)
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		return new ResponseEntity<String>(ret, HttpStatus.OK);
	}
	
	@GetMapping("/UpdateUser")
	ModelAndView getUpdatePage(@RequestParam("Redirect") String returnUrl)
	{
		ModelAndView view = new ModelAndView();
		view.setViewName("AccountUpdate");
		
		ModelMap map = view.getModelMap();
		
		if(returnUrl != null)
			map.addAttribute("redirect", returnUrl);
		else
			map.addAttribute("redirect", "");
		
		return view;
	}
	
	
	@PostMapping("/UpdateUser")
	ResponseEntity<ReturnAccount> logInToUpdate(@RequestBody LogIn login)
	{
		TrecAccount account = null;
		if(login.getUsername() != null)
		{
			account = accountService.logInUsername(login.getUsername(), login.getPassword());
		}
		else if(login.getEmail() != null)
		{
			account = accountService.logInEmail(login.getEmail(), login.getPassword());
		}
		
		if(account == null)
			return new ResponseEntity<ReturnAccount>(HttpStatus.UNAUTHORIZED);
		
		ReturnObj ret = generateAuth(account);
		
		if(ret == null)
			return new ResponseEntity<ReturnAccount>(HttpStatus.INTERNAL_SERVER_ERROR);
		return new ResponseEntity<ReturnAccount>(new ReturnAccount(account, ret.getToken()), HttpStatus.OK);
	}
	
	
	
	@PutMapping(value = "/UpdateUser")
	ResponseEntity<String> updateUser(RequestEntity<TrecAccount> entry)
	{
		HttpHeaders headers = entry.getHeaders();
		
		String token = headers.getFirst("Authorization");
		
		TrecAccount user = tokenService.verifyToken(token);
		
		if(user == null)
		{
			return new ResponseEntity<String>("Could Not Authenticate User", HttpStatus.UNAUTHORIZED);
		}
		
		
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
	
	
	@GetMapping("/UpdatePassword")
	ModelAndView getUpdatePasswordPage(@RequestParam("Redirect") String returnUrl)
	{
		ModelAndView view = new ModelAndView();
		view.setViewName("AccountUpdate");
		
		ModelMap map = view.getModelMap();
		
		if(returnUrl != null)
			map.addAttribute("redirect", returnUrl);
		else
			map.addAttribute("redirect", "");
		
		return view;
	}
	
	
	@PostMapping(value = "/UpdatePassword", consumes = "application/x-www-form-urlencoded")
	Boolean updatePassword(RequestEntity<MultiValueMap<String, String>> entry)
	{
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
		
		return accountService.updatePassword(account.getAccountId(), oldPassword, newPassword) != null;
	}
	
	@GetMapping("/Logout")
	Boolean logout()
	{
		return true;
	}
}
