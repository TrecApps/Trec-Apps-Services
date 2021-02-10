package com.trecapps.falsehoodsearch.testobj;

import com.trecapps.falsehoodsearch.controllers.FalsehoodController;
import com.trecapps.falsehoodsearch.controllers.ResourceController;
import com.trecapps.falsehoodsearch.repos.*;
import com.trecapps.falsehoodsearch.services.*;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;


public class FalsehoodApp {

	// Service

	FalsehoodStorageHolder storageHolder;
	
	PublicFigureService publicFigureService;
	
	PublicAttributeService attService;
	
	PublicFalsehoodService publicFalsehoodService;
	
	MediaOutletService outletService;
	
	FalsehoodService falsehoodService;

	// Controllers

	FalsehoodController falsehoodController;

	ResourceController resourceController;

	// Repos

	PublicFigureRepo publicFigureRepo;

	MediaOutletRepo mediaOutletRepo;

	RegionRepo regionRepo;

	InstitutionRepo institutionRepo;

	PublicFalsehoodRepo publicFalsehoodRepo;

	FalsehoodRepo falsehoodRepo;
	
	public FalsehoodApp()
	{
		// Set Up Repos
		publicFalsehoodRepo = new PublicFalsehoodRepository();
		falsehoodRepo = new FalsehoodRepository();
		publicFigureRepo = new PublicFigureRepository();
		mediaOutletRepo = new MediaOutletRepository();
		regionRepo = new RegionRepository();
		institutionRepo = new InstitutionRepository();

		
		if(storageHolder == null)
			storageHolder = new FalsehoodStorageHolder(System.getProperty("user.dir") + File.separator + "build" + File.separator + "storage",
					null,
					null,
					null);

		publicFigureService = new PublicFigureService(publicFigureRepo, storageHolder);
		attService = new PublicAttributeService(storageHolder, regionRepo, institutionRepo);
		publicFalsehoodService = new PublicFalsehoodService(storageHolder, publicFalsehoodRepo);
		outletService = new MediaOutletService(mediaOutletRepo, storageHolder);
		
		falsehoodService = new FalsehoodService(falsehoodRepo, storageHolder);
		
		// Initialize the Controllers now that the Services are initialized
		falsehoodController = new FalsehoodController(publicFalsehoodService, falsehoodService);
		resourceController = new ResourceController(publicFalsehoodService, attService, publicFigureService, outletService);
	}



	public PublicFigureRepo getPublicFigureRepo() {
		return publicFigureRepo;
	}

	public MediaOutletRepo getMediaOutletRepo() {
		return mediaOutletRepo;
	}

	public RegionRepo getRegionRepo() {
		return regionRepo;
	}

	public InstitutionRepo getInstitutionRepo() {
		return institutionRepo;
	}

	public PublicFalsehoodRepo getPublicFalsehoodRepo() {
		return publicFalsehoodRepo;
	}

	public FalsehoodRepo getFalsehoodRepo() {
		return falsehoodRepo;
	}

	public FalsehoodStorageHolder getStorageHolder() {
		return storageHolder;
	}

	public ResourceController getResourceController() {
		return resourceController;
	}

	/**
	 * @return the publicFigureService
	 */
	public PublicFigureService getPublicFigureService() {
		return publicFigureService;
	}

	/**
	 * @return the attService
	 */
	public PublicAttributeService getAttService() {
		return attService;
	}

	/**
	 * @return the publicFalsehoodService
	 */
	public PublicFalsehoodService getPublicFalsehoodService() {
		return publicFalsehoodService;
	}



	/**
	 * @return the outletService
	 */
	public MediaOutletService getOutletService() {
		return outletService;
	}

	/**
	 * @return the falsehoodService
	 */
	public FalsehoodService getFalsehoodService() {
		return falsehoodService;
	}

	/**
	 * @return the falsehoodController
	 */
	public FalsehoodController getFalsehoodController() {
		return falsehoodController;
	}


	
}
