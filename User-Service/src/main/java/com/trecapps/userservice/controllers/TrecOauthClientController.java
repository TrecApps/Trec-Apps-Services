package com.trecapps.userservice.controllers;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.models.primary.TrecOauthClient;
import com.trecapps.userservice.services.TrecAccountService;
import com.trecapps.userservice.services.TrecOauthClientService;

@RestController
public class TrecOauthClientController {
	
	@Autowired
	TrecAccountService tcService;
	
	@Autowired
	TrecOauthClientService clientService;
	

	@GetMapping(value="/Secure/RegisterClientApp")
	ModelAndView getClientIdRegister()
	{
		ModelAndView ret = new ModelAndView();
		ret.setViewName("ClientRegister");
		
		
		
		return ret;
	}
	
	@PostMapping(value="/Secure/RegisterClientApp", consumes =MediaType.APPLICATION_FORM_URLENCODED_VALUE)
	void postNewClient(@RequestBody MultiValueMap<String, String> formData, HttpServletResponse res) throws IOException
	{
		Authentication authorized = SecurityContextHolder.getContext().getAuthentication();
		if(authorized.isAuthenticated())
		{
			String username = authorized.getPrincipal().toString();
			
			TrecAccount account = tcService.getAccountByUserName(username);
			
			String appName = formData.getFirst("appName");
			String appType = formData.getFirst("appType");
			int type = TrecOauthClient.CLIENT_TYPE_WEB_SERVICE;
			switch(appType)
			{
			case "SinglePage":
				type = TrecOauthClient.CLIENT_TYPE_SINGLE_PAGE;
				break;
			case "NativeApp":
				type = TrecOauthClient.CLIENT_TYPE_NATIVE_APP;
				break;
			case "MobileApp":
				type = TrecOauthClient.CLIENT_TYPE_MOBILE_APP;
				break;
			}
			String clientId = clientService.createNewClient(appName, type, account);
			
			res.sendRedirect("/Secure/RegisterResults?clientId=" + clientId);
		}
	}
	
	@GetMapping(value="/Secure/RegisterResults")
	ModelAndView getRegisterResults(@RequestParam("clientId") String id)
	{
		TrecOauthClient client = (TrecOauthClient) clientService.loadClientByClientId(id);
		
		ModelAndView view = new ModelAndView();
		
		view.setViewName("RegisterResults");
		
		if(client == null)
		{
			// We don't have the client, so show the fail version and hide the success version
			view.getModelMap().addAttribute("failHide", "");
			view.getModelMap().addAttribute("workMode", "hidden");
		}
		else
		{
			view.getModelMap().addAttribute("failHide", "hidden");
			view.getModelMap().addAttribute("workMode", "");
			
			view.getModelMap().addAttribute("clientName", client.getName());
			view.getModelMap().addAttribute("clientId", client.getClientId());
			
			int clientType = client.getClientType();
			
			if(clientType == 0)
			{
				view.getModelMap().addAttribute("secretHide", "");
				view.getModelMap().addAttribute("clientSecret", client.getClientSecret());
				view.getModelMap().addAttribute("clientType", "Web Application");
			}
			else
			{
				view.getModelMap().addAttribute("secretHide", "hidden");
				switch(clientType)
				{
				case 1:
					view.getModelMap().addAttribute("clientType", "Single Page Application");
					break;
				case 2:
					view.getModelMap().addAttribute("clientType", "Native Application");
					break;
				case 3:
					view.getModelMap().addAttribute("clientType", "Mobile Application");
					break;
				default:
					view.getModelMap().addAttribute("clientType", "[Error] Contact the developer!" + clientType);
				}
			}
			
			
		}
		return view;
		
	}
}
