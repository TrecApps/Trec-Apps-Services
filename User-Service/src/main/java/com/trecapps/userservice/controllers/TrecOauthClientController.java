package com.trecapps.userservice.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.trecapps.userservice.security.TrecAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.models.primary.TrecOauthClient;
import com.trecapps.userservice.services.TrecAccountService;
import com.trecapps.userservice.services.TrecOauthClientService;

@RestController
@RequestMapping("/clients")
public class TrecOauthClientController {
	
	@Autowired
	TrecAccountService tcService;
	
	@Autowired
	TrecOauthClientService clientService;


	@GetMapping(value = "/canCreate")
	boolean canCreate(Authentication user)
	{
		TrecAccount account = ((TrecAuthentication)user).getAccount();

		byte accountRoles = account.getPriveledges();
		return (accountRoles & 0b00000001) > 0;
	}

	@GetMapping(value = "/create")
	ResponseEntity<TrecOauthClient> createClient(Authentication user, @RequestParam(required = true, name = "name")String name)
	{
		if(!canCreate(user))
		{
			return new ResponseEntity<TrecOauthClient>((TrecOauthClient) null, HttpStatus.FORBIDDEN);
		}

		TrecOauthClient newClient = clientService.createNewClient(name, 0,
				((TrecAuthentication)user).getAccount());

		newClient.setOwner(null);

		return new ResponseEntity<TrecOauthClient>(newClient, HttpStatus.OK);
	}

}
