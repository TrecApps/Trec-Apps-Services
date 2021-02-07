package com.trecapps.falsehoodauth.controllers;

import java.util.List;

import com.trecapps.falsehoodauth.models.FalsehoodUser;
import com.trecapps.falsehoodauth.models.PublicFigureEntry;
import com.trecapps.falsehoodauth.services.FalsehoodUserService;
import com.trecapps.falsehoodauth.services.PublicFigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/PublicFigure")
public class PublicFigureController extends AuthenticationControllerBase
{

	PublicFigureService pfService;
	

	public static final int MIN_CREDIT_ADD_FIGURE = 40;
	
	public static final int MIN_CREDIT_APPROVE_FIGURE = 200;
	
	public static final int MIN_CREDIT_VIEW_NON_APPROVE = 20;

	@Autowired
	public PublicFigureController(@Autowired FalsehoodUserService service,
								  @Autowired PublicFigureService pfService)
	{
		super(service);
		this.pfService = pfService;
	}
	
	@PostMapping("/Add")
	public ResponseEntity<String> addPublicFigure(RequestEntity<PublicFigureEntry> entry)
	{
		FalsehoodUser user = super.getUser(entry);
		
		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_ADD_FIGURE);
		
		if(ret != null)
			return ret;
		
		String response = pfService.submitPublicFigure(entry.getBody(), user);
		
		if("".equals(response))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		
		return new ResponseEntity<String>(response, HttpStatus.BAD_REQUEST);
	}

}
