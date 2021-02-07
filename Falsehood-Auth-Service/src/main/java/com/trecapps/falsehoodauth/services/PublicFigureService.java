package com.trecapps.falsehoodauth.services;

import java.io.IOException;
import java.util.List;

import com.trecapps.falsehoodauth.models.FalsehoodUser;
import com.trecapps.falsehoodauth.models.PublicFigure;
import com.trecapps.falsehoodauth.models.PublicFigureEntry;
import com.trecapps.falsehoodauth.repos.PublicFigureRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;



@Service
public class PublicFigureService 
{
	FalsehoodStorageHolder awsStorage;

	PublicFigureRepo figureRepo;

	FalsehoodUserService userService;

	@Autowired
	public PublicFigureService(@Autowired FalsehoodUserService userService,
							   @Autowired PublicFigureRepo figureRepo,
							   @Autowired FalsehoodStorageHolder awsStorage)
	{
		this.awsStorage = awsStorage;
		this.figureRepo = figureRepo;
		this.userService = userService;
	}
	
	
	public String submitPublicFigure(PublicFigureEntry publicFigure, FalsehoodUser user)
	{
		if(publicFigure == null)
		{
			return "Public Figure Entry was null";
		}
		
		if(publicFigure.getFigure() == null)
		{
			return "Public Figure Metadata was null";
		}
		
		if(publicFigure.getText() == null)
		{
			return "Public Figure text was null";
		}
		
		PublicFigure pFigure = publicFigure.getFigure();
		
		pFigure = figureRepo.save(pFigure);
		
		if(!"Success".equals(awsStorage.addNewFile("PublicFigure-" + pFigure.getId(), publicFigure.getText())))
		{
			figureRepo.delete(pFigure);
			return "Failed to Save Public Figure to Storage!";
		}
		
		
		
		return "";
	}
	
	

	
	public List<PublicFigure> getPublicFigures(boolean showAll, int page, int pageSize)
	{
		return figureRepo.findAll(PageRequest.of(page, pageSize)).toList();
	}
	

}
