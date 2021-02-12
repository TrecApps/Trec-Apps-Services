package com.trecapps.falsehoodsearch.resourcetests;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.trecapps.falsehoodsearch.controllers.ResourceController;
import com.trecapps.falsehoodsearch.models.Institution;
import com.trecapps.falsehoodsearch.models.InstitutionEntry;
import com.trecapps.falsehoodsearch.models.PublicFigure;
import com.trecapps.falsehoodsearch.models.PublicFigureEntry;
import com.trecapps.falsehoodsearch.repos.PublicFigureRepo;
import com.trecapps.falsehoodsearch.services.FalsehoodStorageHolder;
import com.trecapps.falsehoodsearch.services.PublicAttributeService;
import com.trecapps.falsehoodsearch.testobj.FalsehoodApp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PublicFigureTest {

	static FalsehoodApp sharedApp;
	
	static PublicFigureEntry pubEntries[] = new PublicFigureEntry[3];
	
	@BeforeAll
	public static void setUpSharedApp() throws URISyntaxException
	{
		sharedApp = new FalsehoodApp();

		
		initializePublicFigures(sharedApp);
	}
	
	public static void initializePublicFigures(FalsehoodApp sharedApp) throws URISyntaxException
	{
		if(pubEntries[0] == null)
		pubEntries[0] = new PublicFigureEntry(new PublicFigure(null, "Darth", null, "Vader"),
				"This man is second in command to the Galactic Empire");
		if(pubEntries[1] == null)
		pubEntries[1] = new PublicFigureEntry(new PublicFigure(null, "Tom", "Marvelo", "Riddle"), 
				"He prefers to be known as Lord Voldemort, but don't use his name!");
		if(pubEntries[2] == null)
		pubEntries[2] = new PublicFigureEntry(new PublicFigure(null, "General", "Thunderbolt", "Ross"),
				"This man is a General who was obsessed with capturing the Hulk but moved on to pursue other ambitions like the Sokovia Accords");

		PublicFigureRepo pRepo = sharedApp.getPublicFigureRepo();
		FalsehoodStorageHolder storage = sharedApp.getStorageHolder();

		pRepo.save(pubEntries[0].getFigure());
		storage.addNewFile("PublicFigure-" + 0, pubEntries[0].getText());

		pRepo.save(pubEntries[1].getFigure());
		storage.addNewFile("PublicFigure-" + 1, pubEntries[1].getText());

		pRepo.save(pubEntries[2].getFigure());
		storage.addNewFile("PublicFigure-" + 2, pubEntries[2].getText());

	}

	@Test
	@Order(4)
	public void searchFigures()
	{
		ResourceController pfController = sharedApp.getResourceController();

		List<PublicFigure> regions = pfController.getFigures("Darth");
		assertEquals(1, regions.size());
		assertEquals("Darth", regions.get(0).getFirstname());
		assertEquals("Vader", regions.get(0).getLastName());

		PublicFigureEntry reg = pfController.getPublicFigure(1L);
		assertNotNull(reg);
		assertEquals(1L,reg.getFigure().getId());
	}

//	@Test
//	@Order(1)
//	public void getInstitution()
//	{
//		PublicAttributeService attService = sharedApp.getAttService();
//
//		InstitutionEntry re = attService.getInstitution(0);
//
//		assertNotNull(re);
//		assertNotNull(re.getContents());
//		assertNotNull(re.getInstitution());
//		assertEquals(0L, re.getInstitution().getId().longValue());
//	}

}
