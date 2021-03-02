package com.trecapps.falsehoodauth.controllers;

import com.trecapps.falsehoodauth.models.FalsehoodUser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;


public class AuthenticationControllerBase {
	

	ResponseEntity<String> validateUser(FalsehoodUser user, int minCredit)
	{
		if(user == null)
		{
			return new ResponseEntity<>("Could Not Authenticate User", HttpStatus.UNAUTHORIZED);
		}
		
		if( user.getCredit() < minCredit)
		{
			return new ResponseEntity<>("Could Not Authenticate User", HttpStatus.FORBIDDEN);
		}
		
		
		return null;
	}
}
