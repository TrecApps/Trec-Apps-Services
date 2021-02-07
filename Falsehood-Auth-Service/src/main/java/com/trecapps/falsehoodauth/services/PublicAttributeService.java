package com.trecapps.falsehoodauth.services;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;

import com.trecapps.falsehoodauth.models.*;
import com.trecapps.falsehoodauth.repos.InstitutionRepo;
import com.trecapps.falsehoodauth.repos.RegionRepo;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public String InsertAttribute(InstitutionEntry i, FalsehoodUser user)
	{
		if(i == null)
			return "Null value detected";
		if(user == null)
			return "null User detected";
		
		if(i.getContents() == null)
			return "Null contents detected";
		if(i.getInstitution() == null)
			return "Null metadata detected";
		
		Institution inst = i.getInstitution();
		
		inst.setId(null);
		
		inst = iRepo.saveAndFlush(inst);

		
		//iRepo.deleteById(null);
		
		if(!"Success".equals(storage.addNewFile("Institution-" + inst.getId(), i.getContents())))
		{
			iRepo.delete(inst);
			return "Failed to save to storage!";
		}
		
		return "";
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
	
	
	public String UpdateAttribute(InstitutionEntry i)
	{
		if(i == null)
			return "Null value detected";
		
		if(i.getContents() == null)
			return "Null contents detected";
		if(i.getInstitution() == null)
			return "Null metadata detected";
		
		Institution inst = i.getInstitution();
		
		if(!iRepo.existsById(inst.getId()))
			return "Expected Institution to already exist";
		
		if(!"Success".equals(storage.appendFile("Institution-" + inst.getId(), i.getContents())))
		{
			return "Failed to save to storage!";
		}
		return "";
	}
	
	public String InsertAttribute(RegionEntry i, FalsehoodUser user)
	{
		if(i == null)
			return "Null value detected";
		if(user == null)
			return "null User detected";
		
		if(i.getContents() == null)
			return "Null contents detected";
		if(i.getRegion() == null)
			return "Null metadata detected";
		
		Region reg = i.getRegion();
		
		reg.setId(null);
		
		reg = rRepo.save(reg);
		
		if(!"Success".equals(storage.addNewFile("Region-" + reg.getId(), i.getContents())))
		{
			rRepo.delete(reg);
			return "Failed to save to storage!";
		}
		
		return "";
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
	
	public String UpdateAttribute(RegionEntry i)
	{
		if(i == null)
			return "Null value detected";
		
		if(i.getContents() == null)
			return "Null contents detected";
		if(i.getRegion() == null)
			return "Null metadata detected";
		
		Region reg = i.getRegion();
		
		if(!rRepo.existsById(reg.getId()))
			return "Expected Region to already exist";
		
		if(!"Success".equals(storage.appendFile("Region-" + reg.getId(), i.getContents())))
		{
			return "Failed to save to storage!";
		}
		return "";
	}
}
