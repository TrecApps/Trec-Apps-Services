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


	@Autowired
	public MediaOutletService(@Autowired MediaOutletRepo moRepo,
			@Autowired FalsehoodStorageHolder awsStorage)
	{
		this.moRepo = moRepo;
		this.awsStorage = awsStorage;
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
