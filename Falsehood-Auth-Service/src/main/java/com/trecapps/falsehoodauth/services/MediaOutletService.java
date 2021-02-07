package com.trecapps.falsehoodauth.services;

import java.io.IOException;
import java.util.List;

import com.trecapps.falsehoodauth.models.FalsehoodUser;
import com.trecapps.falsehoodauth.models.MediaOutlet;
import com.trecapps.falsehoodauth.models.MediaOutletEntry;
import com.trecapps.falsehoodauth.repos.MediaOutletRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MediaOutletService {

	MediaOutletRepo moRepo;
	FalsehoodStorageHolder awsStorage;

	FalsehoodUserService userService;

	@Autowired
	public MediaOutletService(@Autowired MediaOutletRepo moRepo,
			@Autowired FalsehoodStorageHolder awsStorage,
			@Autowired FalsehoodUserService userService)
	{
		this.moRepo = moRepo;
		this.awsStorage = awsStorage;
		this.userService = userService;
	}
	
	
	public String submitMediaOutlet(MediaOutletEntry outletEntry, FalsehoodUser user)
	{
		if(outletEntry == null)
		{
			return "Public Figure Entry was null";
		}
		
		if(outletEntry.getOutlet() == null)
		{
			return "Public Figure Metadata was null";
		}
		
		if(outletEntry.getText() == null)
		{
			return "Public Figure text was null";
		}
		
		MediaOutlet pFigure = outletEntry.getOutlet();
		
		pFigure = moRepo.save(pFigure);
		
		if(!"Success".equals(awsStorage.addNewFile("MediaOutlet-" + pFigure.getOutletId(), outletEntry.getText())))
		{
			moRepo.delete(pFigure);
			return "Failed to Save Public Figure to Storage!";
		}
		
		
		
		return "";
	}
	
	

	List<MediaOutlet> GetMediaOutlets(){
		return moRepo.findAll();
	}
	
	public MediaOutlet GetMediaOutlet(Integer id)
	{
		return moRepo.getOne(id);
	}
	
	public MediaOutletEntry GetOutletEntry(Integer id)
	{
		try {
			return new MediaOutletEntry( moRepo.getOne(id),awsStorage.retrieveContents("MediaOutlet-" + id));
		} catch(IOException e)
		{
			return null;
		}
	}
	
	public MediaOutlet GetMediaOutlet(String name)
	{
		return moRepo.getOutletByName(name);
	}
	
	public List<MediaOutlet> SearchOutletByName(String name) {
		return moRepo.getOutletLikeName(name);
	}
}
