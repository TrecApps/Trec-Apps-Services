package com.trecapps.userservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.models.primary.TrecOauthClient;
import com.trecapps.userservice.repositories.primary.TrecOauthClientRepo;


@Service
public class TrecOauthClientService // implements ClientDetailsService 
{

	@Autowired
	TrecOauthClientRepo oauthRepo;
	
	// chose a Character random from this String 
    final String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                + "0123456789"
                                + "abcdefghijklmnopqrstuvxyz";
    final int CLIENT_STRING_LENGTH = 25;
	
	
	
	public TrecOauthClient loadClientByClientId(String clientId) 
	{
		return oauthRepo.existsById(clientId) ? oauthRepo.getOne(clientId) : null;
	}
	
	public String createNewClient(String name, int type, TrecAccount owner)
	{
		String newId;
		
		do
		{
			newId = generateRandomString();
		}while(oauthRepo.getTrecOauthClientByClientId(newId) != null);
		
		String newSecret = null;
		if(type == 0)
		{
			newSecret = generateRandomString();
		}
		
		TrecOauthClient client = new TrecOauthClient(name, owner, newId, null, type, newSecret);
		System.out.println(client);
		oauthRepo.save(client);
		
		return newId;
	}
	
	
	private String generateRandomString()
	{
		StringBuilder sb = new StringBuilder();
		for(int c = 0; c < CLIENT_STRING_LENGTH; c++)
		{
			int ch = (int) (Math.random() * AlphaNumericString.length());
			sb.append(AlphaNumericString.charAt(ch));
		}
		return sb.toString();
	}
}
