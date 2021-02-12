package com.trecapps.falsehoodsearch.resourcetests;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.trecapps.falsehoodsearch.controllers.ResourceController;
import com.trecapps.falsehoodsearch.models.Institution;
import com.trecapps.falsehoodsearch.models.InstitutionEntry;
import com.trecapps.falsehoodsearch.models.MediaOutlet;
import com.trecapps.falsehoodsearch.models.MediaOutletEntry;
import com.trecapps.falsehoodsearch.repos.MediaOutletRepo;
import com.trecapps.falsehoodsearch.repos.RegionRepo;
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


public class MediaOutletTest {

	static FalsehoodApp sharedApp;
	
	static MediaOutletEntry medEntries[] = new MediaOutletEntry[3];
	
	@BeforeAll
	public static void setUpSharedApp() throws URISyntaxException
	{
		sharedApp = new FalsehoodApp();
		
		initializeOutlets(sharedApp);
	}
	
	public static void initializeOutlets(FalsehoodApp app) throws URISyntaxException
	{
		if(medEntries[0] == null)
		medEntries[0] = new MediaOutletEntry(
				new MediaOutlet(null, (short)1750, "Daily Prophet"),
				"The Prie Outlet of the Wizarding World");
		if(medEntries[1] == null)
		medEntries[1] = new MediaOutletEntry(
				new MediaOutlet(null, (short)-2000, "Holonet"),
				"The Galaxy's version of the Internet, reporting on the developments of the clone wars!");
		if(medEntries[2] == null)
		medEntries[2] = new MediaOutletEntry(
				new MediaOutlet(null, (short)1960, "Daily Bugle"),
				"Founded by JJJ, this outlet has it out for Spider-Man.");

		MediaOutletRepo pRepo = app.getMediaOutletRepo();
		FalsehoodStorageHolder storage = app.getStorageHolder();

		pRepo.save(medEntries[0].getOutlet());
		storage.addNewFile("MediaOutlet-" + 0, medEntries[0].getText());

		pRepo.save(medEntries[1].getOutlet());
		storage.addNewFile("MediaOutlet-" + 1, medEntries[1].getText());

		pRepo.save(medEntries[2].getOutlet());
		storage.addNewFile("MediaOutlet-" + 2, medEntries[2].getText());
	}

	@Test
	@Order(4)
	public void searchOutlets()
	{
		ResourceController pfController = sharedApp.getResourceController();

		List<MediaOutlet> regions = pfController.searchOutlets("Holo");
		assertEquals(1, regions.size());
		assertEquals("Holonet", regions.get(0).getName());

		MediaOutletEntry reg = pfController.getOutlet(1);
		assertNotNull(reg);
		assertEquals(1,reg.getOutlet().getOutletId());
	}

//	@Test
//	@Order(1)
//	public void getOutlet()
//	{
//		PublicAttributeService attService = sharedApp.getAttService();
//
//		MediaOutletEntry re = attService.(0);
//
//		assertNotNull(re);
//		assertNotNull(re.getContents());
//		assertNotNull(re.getInstitution());
//		assertEquals(0L, re.getInstitution().getId().longValue());
//	}
}
