package com.trecapps.userservice.controllers;

import com.trecapps.userservice.security.TrecAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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
	boolean canCreate()
	{
		TrecAuthentication auth = ((TrecAuthentication)SecurityContextHolder.getContext().getAuthentication());
		return canCreate(auth.getAccount());


	}

	@GetMapping(value = "/create")
	ResponseEntity<TrecOauthClient> createClient(@RequestParam(required = true, name = "name")String name)
	{
		TrecAuthentication auth = ((TrecAuthentication)SecurityContextHolder.getContext().getAuthentication());
		TrecAccount account = auth.getAccount();
		if(!canCreate(account))
		{
			return new ResponseEntity<TrecOauthClient>((TrecOauthClient) null, HttpStatus.FORBIDDEN);
		}

		TrecOauthClient newClient = clientService.createNewClient(name, 0,
				account);

		newClient.setOwner(null);

		return new ResponseEntity<TrecOauthClient>(newClient, HttpStatus.OK);
	}

	private boolean canCreate(TrecAccount account)
	{
		byte accountRoles = account.getPrivileges();
		return (accountRoles & 0b00000001) > 0;
	}

}
