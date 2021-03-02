package com.trecapps.falsehoodauth.controllers;


import com.trecapps.falsehoodauth.models.*;
import com.trecapps.falsehoodauth.services.PublicAttributeService;
import com.trecapps.falsehoodauth.services.PublicFalsehoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/Update/PublicFalsehood")
public class AuthPublicFalsehoodController extends AuthenticationControllerBase
{
    PublicFalsehoodService service;

    
    PublicAttributeService attService;

    public static final int MIN_CREDIT_SUBMIT_NEW = 5;

    public static final int MIN_CREDIT_APPROVE_REJECT = 60;

	public static final int MIN_CREDIT_APPROVE_REJECT_RESOURCE = 200;
    
    public static final int MIN_CREDIT_ADD_RECOURSE = 35;

    @Autowired
    public AuthPublicFalsehoodController(@Autowired PublicFalsehoodService service,
                                         @Autowired PublicAttributeService attService)
    {
        super();
        this.service = service;
        this.attService = attService;
    }

    @PostMapping("/Insert")
    public ResponseEntity<String> insertFalsehood(RequestEntity<FullPublicFalsehood> entity, HttpServletRequest req)
    {
        FalsehoodUser user = (FalsehoodUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_SUBMIT_NEW);

        if(ret != null)
            return ret;

        FullPublicFalsehood falsehood = entity.getBody();

        if(falsehood == null || falsehood.getContents() == null || falsehood.getMetadata() == null)
        {
            return new ResponseEntity<String>("Bad Data", HttpStatus.BAD_REQUEST);
        }

        PublicFalsehood meta = falsehood.getMetadata();
        


        meta.setStatus(FalsehoodStatus.SUBMITTED.GetValue());
        
		if(meta.getTags() != null && meta.getTags().length() > 400)
			return new ResponseEntity<String>(String.format("Falsehood keys field was %d characters (400 max)", meta.getTags().length()), HttpStatus.BAD_REQUEST);

        ///meta.setDateMade(new Date(Calendar.getInstance().getTime().getTime()));



        meta = service.insertNewFalsehood(meta);



        if(!service.insertEntryToStorage(meta, falsehood.getContents(),user, req))
        {
            return new ResponseEntity<String>("Failed to Write Falsehood to Storage!", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
    }

	@PutMapping("/Approve")
	public ResponseEntity<String> approveFalsehood(RequestEntity<VerdictSubmission> entity, HttpServletRequest req)
	{
		FalsehoodUser user = (FalsehoodUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT);

		if(ret != null)
			return ret;

		VerdictSubmission falsehood = entity.getBody();

		String result = service.addVerdict(falsehood.getFalsehood(), 1, falsehood.getComment(), user, req);

		if("".equals(result))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
	}

	@PutMapping("/Reject")
	public ResponseEntity<String> rejectFalsehood(RequestEntity<VerdictSubmission> entity, HttpServletRequest req)
	{
		FalsehoodUser user = (FalsehoodUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		ResponseEntity<String> ret = super.validateUser(user, MIN_CREDIT_APPROVE_REJECT);

		if(ret != null)
			return ret;

		VerdictSubmission falsehood = entity.getBody();

		String result = service.addVerdict(falsehood.getFalsehood(), falsehood.isShouldStrike() ? -1 : 0, falsehood.getComment(), user, req);

		if("".equals(result))
			return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
		else return new ResponseEntity<String>(result, HttpStatus.BAD_REQUEST);
	}

}
