package com.trecapps.pictures.services;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PictureStorageHolder {
	PictureStorageAws awsStorage;
	
	PictureStorageLocal localStorage;
	
	static final String NULL_STRING = "null";
	
	public PictureStorageHolder(
			@Value("${LOCAL_STORAGE:null}") String localStorageStr,
			@Value("${S3_ACCESS:null}")String awsKeyId,
			@Value("${S3_SECRET:null}")String awsSecret, 
			@Value("${S3_REGION:us-east-1}")String awsRegion)
	{
		if(localStorageStr == null || NULL_STRING.equals(localStorageStr))
		{
			localStorage = null;
		}
		else localStorage = new PictureStorageLocal(localStorageStr);
		
		if(awsKeyId == null || NULL_STRING.equals(awsKeyId) ||
			awsSecret == null || NULL_STRING.equals(awsSecret))
		{
			awsStorage = null;
		}
		else awsStorage = new PictureStorageAws(awsKeyId, awsSecret, awsRegion);
		
	}
	
	public JSONObject getJSONObj(String key) throws IOException {
	
		if(awsStorage != null)
			return awsStorage.getJSONObj(key);
		if(localStorage != null)
			return localStorage.getJSONObj(key);
		return null;
	}
	
	public String addJsonFile(String key, JSONObject obj)
	{		
		if(awsStorage != null)
		{
			return awsStorage.addJsonFile(key, obj);
		}
		
		if(localStorage != null)
		{
			return localStorage.addJsonFile(key, obj);
		}
		
		return  "Not Activated";
	}
	
	public String retrieveContents(String key) throws IOException
	{
		if(awsStorage != null)
		{
			return awsStorage.retrieveContents(key);
		}
		
		if(localStorage != null)
		{
			return localStorage.retrieveContents(key);
		}
		
		return  "Not Activated";
	}
	
	public String addNewFile(String key, String content)
	{
		if(awsStorage != null)
		{
			return awsStorage.addNewFile(key, content);
		}
		
		if(localStorage != null)
		{
			return localStorage.addNewFile(key, content);
		}
		
		return  "Not Activated";
	}
	
	
	public String appendFile(String key, String content)
	{		
		if(awsStorage != null)
		{
			return awsStorage.appendFile(key, content);
		}
		
		if(localStorage != null)
		{
			return localStorage.appendFile(key, content);
		}
		
		return  "Not Activated";
	}
	
	
	public String removeFile(String key)
	{

		if(awsStorage != null)
		{
			return awsStorage.removeFile(key);
		}
		
		if(localStorage != null)
		{
			return localStorage.removeFile(key);
		}
		
		return  "Not Activated";
	}
}
