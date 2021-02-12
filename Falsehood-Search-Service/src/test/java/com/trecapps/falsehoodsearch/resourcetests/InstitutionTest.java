package com.trecapps.falsehoodsearch.resourcetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import com.trecapps.falsehoodsearch.controllers.ResourceController;
import com.trecapps.falsehoodsearch.jsonmodels.EventObj;
import com.trecapps.falsehoodsearch.jsonmodels.VerdictListObj;
import com.trecapps.falsehoodsearch.jsonmodels.VerdictObj;
import com.trecapps.falsehoodsearch.models.Institution;
import com.trecapps.falsehoodsearch.models.InstitutionEntry;
import com.trecapps.falsehoodsearch.repos.InstitutionRepo;
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
public class InstitutionTest {
	static FalsehoodApp sharedApp;
	
	static InstitutionEntry intEntries[] = new InstitutionEntry[3];
	
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
		if(intEntries[0] == null)
			intEntries[0] = new InstitutionEntry(new Institution(null, "Coruscant"), 
					INIT_CORU);
		if(intEntries[1] == null)
			intEntries[1] = new InstitutionEntry(new Institution(null, "District 12"), 
					"Institution of Panem, located in the Appalachian region and known for coal production");
		if(intEntries[2] == null)
			intEntries[2] = new InstitutionEntry(new Institution(null, "Morag"), 
					"Old planet, known for intense flooding, once held the Power Stone");

		InstitutionRepo pRepo = app.getInstitutionRepo();
		FalsehoodStorageHolder storage = app.getStorageHolder();


		pRepo.save(intEntries[0].getInstitution());
		storage.addNewFile("Institution-" + 0, intEntries[0].getContents());

		pRepo.save(intEntries[1].getInstitution());
		storage.addNewFile("Institution-" + 1, intEntries[1].getContents());

		pRepo.save(intEntries[2].getInstitution());
		storage.addNewFile("Institution-" + 2, intEntries[2].getContents());

	}	
	
	@Test
	@Order(4)
	public void searchInstitutions()
	{
		ResourceController pfController = sharedApp.getResourceController();
		
		List<Institution> regions = pfController.getInstitutionBySearchTerm("Coru");
		assertEquals(1, regions.size());
		assertEquals("Coruscant", regions.get(0).getName());
		
		InstitutionEntry reg = pfController.getInstitutionById(1L);
		assertNotNull(reg);
		assertEquals(1L,reg.getInstitution().getId());
	}
	
	@Test
	@Order(1)
	public void getInstitution()
	{
		PublicAttributeService attService = sharedApp.getAttService();
		
		InstitutionEntry re = attService.getInstitution(0);
		
		assertNotNull(re);
		assertNotNull(re.getContents());
		assertNotNull(re.getInstitution());
		assertEquals(0L, re.getInstitution().getId().longValue());
	}

}
