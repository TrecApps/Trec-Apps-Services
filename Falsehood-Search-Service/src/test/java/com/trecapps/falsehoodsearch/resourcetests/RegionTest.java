package com.trecapps.falsehoodsearch.resourcetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import com.trecapps.falsehoodsearch.controllers.ResourceController;
import com.trecapps.falsehoodsearch.models.Institution;
import com.trecapps.falsehoodsearch.models.InstitutionEntry;
import com.trecapps.falsehoodsearch.models.Region;
import com.trecapps.falsehoodsearch.models.RegionEntry;
import com.trecapps.falsehoodsearch.repos.PublicFigureRepo;
import com.trecapps.falsehoodsearch.repos.RegionRepo;
import com.trecapps.falsehoodsearch.services.FalsehoodStorageHolder;
import com.trecapps.falsehoodsearch.services.PublicAttributeService;
import com.trecapps.falsehoodsearch.testobj.FalsehoodApp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;


@TestMethodOrder(OrderAnnotation.class)
public class RegionTest {
	static FalsehoodApp sharedApp;
	
	static RegionEntry regEntries[] = new RegionEntry[3];
	
	static String INIT_CORU = "Planet that serves as the Capital of the Old Republic and later the Galactic Empire.";
	static String ADD_CORU = "It was replaced by the Hosnian Prime System for the New Republic.";
	
	@BeforeAll
	public static void setUpSharedApp() throws URISyntaxException
	{
		sharedApp = new FalsehoodApp();
		
		initializePublicFigures(sharedApp);
	}
	
	public static void initializePublicFigures(FalsehoodApp app) throws URISyntaxException
	{
		if(regEntries[0] == null)
			regEntries[0] = new RegionEntry(new Region(null, "Coruscant"), 
					INIT_CORU);
		if(regEntries[1] == null)
			regEntries[1] = new RegionEntry(new Region(null, "District 12"), 
					"Region of Panem, located in the Appalachian region and known for coal production");
		if(regEntries[2] == null)
			regEntries[2] = new RegionEntry(new Region(null, "Morag"), 
					"Old planet, known for intense flooding, once held the Power Stone");

		RegionRepo pRepo = app.getRegionRepo();
		FalsehoodStorageHolder storage = app.getStorageHolder();

		pRepo.save(regEntries[0].getRegion());
		storage.addNewFile("Region-" + 0, regEntries[0].getContents());

		pRepo.save(regEntries[1].getRegion());
		storage.addNewFile("Region-" + 1, regEntries[1].getContents());

		pRepo.save(regEntries[2].getRegion());
		storage.addNewFile("Region-" + 2, regEntries[2].getContents());
	}
	@Test
	@Order(4)
	public void searchRegion()
	{
		ResourceController pfController = sharedApp.getResourceController();

		List<Region> regions = pfController.getRegionsBySearchTerm("Coru");
		assertEquals(1, regions.size());
		assertEquals("Coruscant", regions.get(0).getName());

		RegionEntry reg = pfController.getRegionsById(1L);
		assertNotNull(reg);
		assertEquals(1L,reg.getRegion().getId());
	}

	@Test
	@Order(1)
	public void getRegion()
	{
		PublicAttributeService attService = sharedApp.getAttService();

		RegionEntry re = attService.getRegion(0);

		assertNotNull(re);
		assertNotNull(re.getContents());
		assertNotNull(re.getRegion());
		assertEquals(0L, re.getRegion().getId().longValue());
	}
}
