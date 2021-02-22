package com.trecapps.falsehoodauth.services;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;

import com.trecapps.falsehoodauth.models.*;
import com.trecapps.falsehoodauth.repos.InstitutionRepo;
import com.trecapps.falsehoodauth.repos.RegionRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublicAttributeService {

	InstitutionRepo iRepo;

	RegionRepo rRepo;

	FalsehoodStorageHolder storage;

	@Autowired
	EntityManager em;
	
	FalsehoodUserService userService;
	

	@Autowired
	public PublicAttributeService(@Autowired FalsehoodStorageHolder storage,
									@Autowired RegionRepo rRepo,
									@Autowired InstitutionRepo iRepo,
									@Autowired FalsehoodUserService userService)
	{
		this.iRepo = iRepo;
		this.rRepo = rRepo;
		this.storage = storage;
		this.userService = userService;
	}


	
	public InstitutionEntry getInstitution(long id)
	{
		if(!iRepo.existsById(id))
		{
			System.out.println("Repository " + id + " Doesn't Exist!");
			return null;
		}
		Institution i = iRepo.getOne(id);
		String s;
		try {
			s = storage.retrieveContents("Institution-" + id);
		} catch (IOException e) {
			s = "ERROR: " + e.getMessage();
		}
		return new InstitutionEntry(i,s);
	}


	public RegionEntry getRegion(long id)
	{
		if(!rRepo.existsById(id))
			return null;
		
		Region i = rRepo.getOne(id);
		String s;
		try {
			s = storage.retrieveContents("Region-" + id);
		} catch (IOException e) {
			s = "ERROR: " + e.getMessage();
		}
		return new RegionEntry(i,s);
	}
	

}
