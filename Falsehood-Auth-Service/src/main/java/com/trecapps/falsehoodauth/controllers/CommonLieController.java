package com.trecapps.falsehoodauth.controllers;

import com.trecapps.falsehoodauth.models.CommonLieSubmission;
import com.trecapps.falsehoodauth.models.FalsehoodUser;
import com.trecapps.falsehoodauth.services.CommonLieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/CommonLie")
public class CommonLieController extends AuthenticationControllerBase
{
	CommonLieService clService;
	
	public static final int MIN_CREDIT_PROPOSE_COMMON_LIE = 40;

	@Autowired
	public CommonLieController(@Autowired CommonLieService clService)
	{
		super();
		this.clService = clService;
	}
	
	@PostMapping("/insert")
	ResponseEntity<String> insertCommonLie(RequestEntity<CommonLieSubmission> sub)
	{
		FalsehoodUser user = (FalsehoodUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		if(user == null)
		{
			return new ResponseEntity<String>("Could Not Authenticate User", HttpStatus.UNAUTHORIZED);
		}
		
		if( user.getCredit() < MIN_CREDIT_PROPOSE_COMMON_LIE)
		{
			return new ResponseEntity<String>("User did not have the Credibility needed", HttpStatus.FORBIDDEN);
		}
		
		String response = clService.submitCommonLie(sub.getBody());
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}
}
