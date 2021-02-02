package com.trecapps.falsehoodsearch.services;

import java.io.IOException;
import java.util.List;

import com.trecapps.falsehoodsearch.models.PublicFigure;
import com.trecapps.falsehoodsearch.models.PublicFigureEntry;
import com.trecapps.falsehoodsearch.repos.PublicFigureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.trecapps.falsehoodsearch.services.FalsehoodStorageHolder;

@Service
public class PublicFigureService 
{
	FalsehoodStorageHolder awsStorage;

	PublicFigureRepo figureRepo;

	@Autowired
	public PublicFigureService(@Autowired PublicFigureRepo figureRepo,
							   @Autowired FalsehoodStorageHolder awsStorage)
	{
		this.awsStorage = awsStorage;
		this.figureRepo = figureRepo;
	}

	
	public List<PublicFigure> getPublicFigures(int page, int pageSize)
	{
		return figureRepo.findAll(PageRequest.of(page, pageSize)).toList();
	}
	
	public List<PublicFigure> getPublicFigure(String entry)
	{
		String names[] = entry.replace('_', ' ').trim().split(" ");
		
		if(names.length == 0)
		{
			return null;
		}
		if(names.length == 1)
		{
			return figureRepo.findLikeName(names[0]);
		}
		if(names.length == 2)
		{
			return figureRepo.findLikeName(names[0], names[1]);
		}
		String middle = "";
		for(int rust = 1; rust < names.length -1; rust++)
		{
			middle += names[rust] + " ";
		}
		
		return figureRepo.findLikeName(names[0], middle.trim(), names[names.length-1]);
	}
	
	public PublicFigureEntry getEntryById(Long id)
	{
		try {
			return new PublicFigureEntry(figureRepo.getOne(id),awsStorage.retrieveContents("PublicFigure-" + id));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
}
