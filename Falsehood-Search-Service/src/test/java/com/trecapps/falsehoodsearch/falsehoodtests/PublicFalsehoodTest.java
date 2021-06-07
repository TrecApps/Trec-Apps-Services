package com.trecapps.falsehoodsearch.falsehoodtests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import com.trecapps.falsehoodsearch.controllers.FalsehoodController;
import com.trecapps.falsehoodsearch.models.*;
import com.trecapps.falsehoodsearch.repos.PublicFalsehoodRepo;
import com.trecapps.falsehoodsearch.resourcetests.InstitutionTest;
import com.trecapps.falsehoodsearch.resourcetests.PublicFigureTest;
import com.trecapps.falsehoodsearch.resourcetests.RegionTest;
import com.trecapps.falsehoodsearch.services.PublicAttributeService;
import com.trecapps.falsehoodsearch.services.PublicFigureService;
import com.trecapps.falsehoodsearch.services.SearchPublicFalsehood;
import com.trecapps.falsehoodsearch.testobj.FalsehoodApp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;


@TestMethodOrder(OrderAnnotation.class)
public class PublicFalsehoodTest {
	static FalsehoodApp sharedApp;
	
	static FullPublicFalsehood[] falsehoods = new FullPublicFalsehood[10];
	
	static List<PublicFigure> figures;
	static List<Region> regions = new LinkedList<>();
	static List<Institution> institutions = new LinkedList<>();
	
	static final long DATE_2010 = 1290000000000L;
	static final long DATE_2014 = 1403000000000L;
	static final long DATE_2017 = 1507900000000L;
	static final long DATE_2020 = 1603000000000L;
	static final long DATE_2008 = 1210000000000L;
	
	@BeforeAll
	public static void setUpSharedApp() throws URISyntaxException
	{
		sharedApp = new FalsehoodApp();
		
		RegionTest.initializePublicFigures(sharedApp);
		InstitutionTest.initializePublicFigures(sharedApp);
		PublicFigureTest.initializePublicFigures(sharedApp);
		
		initializeFalsehoods(sharedApp);
		
	}
	
	@Test
	@Order(3)
	public void searchByDates()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);



		search.setFrom(new Date(DATE_2017 - 50L));
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		search.setTo(new Date(DATE_2020 - 1L));
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		search.setFrom(null);
		f = fController.searchFalsehoodByParams(search);
		assertEquals(8, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(8, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		search.setTerms(null);
	}
	
	@Test
	@Order(3)
	public void searchByFigure()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		
		search.setOfficial(figures.get(1));
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		search.setOfficial(figures.get(2));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Voldemort");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
	}
	

	
	@Test
	@Order(3)
	public void searchBySeverity()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		
		search.setMaximum(Severity.OBJECTIVE_FALSEHOOD);
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(8, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(8, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms(null);
		
		search.setMinimum(Severity.HYPOCRISY);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms(null);
		
		search.setMaximum(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(6, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(6, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms(null);
	}
	
	
	@Test
	@Order(3)
	public void searchByDatesAndSeverity()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		
		// Start with Minimum
		search.setMinimum(Severity.OBJECTIVE_FALSEHOOD);
		search.setFrom(new Date(DATE_2010));
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		search.setTo(new Date(DATE_2014 - 100));
		
		f = fController.searchFalsehoodByParams(search);
		
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		search.setTo(new Date(DATE_2014 + 100));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		search.setFrom(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		// Blank Slate for Maximum Severity
		search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		
		search.setMaximum(Severity.MISLEADING);
		search.setTo(new Date(DATE_2020 -100));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(5, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(5, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		// Blank Slate for minimum and maximum severity
		search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		
		search.setMaximum(Severity.MISLEADING);
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		
		search.setTo(new Date(DATE_2017));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
	}
	
	@Test
	@Order(3)
	public void searchByDatesFigureAndSeverity()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchPublicFalsehood search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		
		// Start with Minimum
		search.setOfficial(figures.get(0));
		search.setMinimum(Severity.OBJECTIVE_FALSEHOOD);
		search.setFrom(new Date(DATE_2010));
		
		List<PublicFalsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		search.setTo(new Date(DATE_2014 - 100));
		
		f = fController.searchFalsehoodByParams(search);
		
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		search.setTo(new Date(DATE_2014 + 100));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		search.setFrom(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		// Blank Slate for Maximum Severity
		search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		search.setOfficial(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		search.setTo(new Date(DATE_2020 -100));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		search.setTerms(null);
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		search.setTerms(null);
		
		// Blank Slate for minimum and maximum severity
		search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		search.setOfficial(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		
		search.setTo(new Date(DATE_2017));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		search.setTerms(null);
		
		search.setFrom(new Date(DATE_2014 + 200));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		search.setTerms(null);
		
		
		
		
		search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		
		// Start with Minimum, No Time Constraints this time
		search.setOfficial(figures.get(0));
		search.setMinimum(Severity.OBJECTIVE_FALSEHOOD);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		// Blank Slate for Maximum Severity, No Time Constraints this time
		search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		search.setOfficial(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		search.setTerms(null);
	
		
		// Blank Slate for minimum and maximum severity, No Time Constraints this time
		search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		search.setOfficial(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(0, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(0, f.size());
		search.setTerms(null);
		
		
		// Before, Official
		search = new SearchPublicFalsehood();
		search.setOfficialType((byte)20);
		search.setOfficial(figures.get(1));
		search.setTo(new Date(DATE_2014));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		// Between, Official
		search.setFrom(new Date(DATE_2010));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		// Max Severity, Official
		search.setTo(null);
		search.setFrom(null);
		search.setMaximum(Severity.OBJECTIVE_FALSEHOOD);
		
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		// Severity, Official
		search.setMinimum(Severity.HYPOCRISY);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		// Min Severity, Official
		search.setMaximum(null);
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
	}
	
	
	public static void initializeFalsehoods(FalsehoodApp app) throws URISyntaxException
	{
		PublicAttributeService attService = app.getAttService();
		PublicFigureService figureService = app.getPublicFigureService();
		
		figures = figureService.getPublicFigures(0, 10);
		
		institutions.add(attService.getInstitution(0L).getInstitution());
		institutions.add(attService.getInstitution(1L).getInstitution());
		institutions.add(attService.getInstitution(2L).getInstitution());
		
		regions.add(attService.getRegion(0L).getRegion());
		regions.add(attService.getRegion(1L).getRegion());
		regions.add(attService.getRegion(2L).getRegion());
		
		if(falsehoods[0] == null)
			falsehoods[0] = new FullPublicFalsehood("Public Falsehood 1",
					new PublicFalsehood(null, null, (byte)0, figures.get(0), PublicFalsehood.POLITICIAN, regions.get(1),
							institutions.get(2), (byte)1, new Date(DATE_2010),"Vader|Palpatine|Empire"),
					null, null);
		if(falsehoods[1] == null)
			falsehoods[1] = new FullPublicFalsehood("Public Falsehood 2",
					new PublicFalsehood(null, null, (byte)0, figures.get(1), PublicFalsehood.LAW_ENFORCEMENT, regions.get(2),
							institutions.get(0), (byte)3, new Date(DATE_2017),"Vader|Voldemort|Thanos"),
					null, null);
		if(falsehoods[2] == null)
			falsehoods[2] = new FullPublicFalsehood("Public Falsehood 3",
					new PublicFalsehood(null, null, (byte)0, figures.get(2), PublicFalsehood.ECONOMIST, regions.get(0),
							institutions.get(1), (byte)4, new Date(DATE_2014),"Voldemort|Dumbledore|Ministry"),
					null, null);
		if(falsehoods[3] == null)
			falsehoods[3] = new FullPublicFalsehood("Public Falsehood 4",
					new PublicFalsehood(null, null, (byte)0, figures.get(0), PublicFalsehood.ENVIRONMENTALIST, regions.get(1),
							institutions.get(2), (byte)5, new Date(DATE_2020),"Thanos|Ironman|Thor|Serena"),
					null, null);
		if(falsehoods[4] == null)
			falsehoods[4] = new FullPublicFalsehood("Public Falsehood 5",
					new PublicFalsehood(null, null, (byte)0, figures.get(1), PublicFalsehood.INTELLIGENCE, regions.get(2),
							institutions.get(0), (byte)0, new Date(DATE_2008),"Loki|Sif|Serena"),
					null, null);
		
		if(falsehoods[5] == null)
			falsehoods[5] = new FullPublicFalsehood("Public Falsehood 6",
					new PublicFalsehood(null, null, (byte)0, figures.get(0), PublicFalsehood.POLITICIAN, regions.get(1),
							institutions.get(2), (byte)1, new Date(DATE_2020),"Vader|Palpatine|Empire"),
					null, null);
		if(falsehoods[6] == null)
			falsehoods[6] = new FullPublicFalsehood("Public Falsehood 7",
					new PublicFalsehood(null, null, (byte)0, figures.get(1), PublicFalsehood.LAW_ENFORCEMENT, regions.get(2),
							institutions.get(1), (byte)3, new Date(DATE_2014),"Vader|Voldemort|Thanos"),
					null, null);
		if(falsehoods[7] == null)
			falsehoods[7] = new FullPublicFalsehood("Public Falsehood 8",
					new PublicFalsehood(null, null, (byte)0, figures.get(2), PublicFalsehood.ECONOMIST, regions.get(0),
							institutions.get(0), (byte)4, new Date(DATE_2017),"Voldemort|Dumbledore|Ministry"),
					null, null);
		if(falsehoods[8] == null)
			falsehoods[8] = new FullPublicFalsehood("Public Falsehood 9",
					new PublicFalsehood(null, null, (byte)0, figures.get(0), PublicFalsehood.ENVIRONMENTALIST, regions.get(1),
							institutions.get(2), (byte)5, new Date(DATE_2008),"Thanos|Ironman|Thor|Serena"),
					null, null);
		if(falsehoods[9] == null)
			falsehoods[9] = new FullPublicFalsehood("Public Falsehood 10",
					new PublicFalsehood(null, null, (byte)0, figures.get(1), PublicFalsehood.INTELLIGENCE, regions.get(2),
							institutions.get(0), (byte)0, new Date(DATE_2010),"Loki|Sif|Serena"),
					null, null);

		PublicFalsehoodRepo repo = app.getPublicFalsehoodRepo();
		
		repo.save(falsehoods[0].clone((byte)2).getMetadata());
		repo.save(falsehoods[1].clone((byte)2).getMetadata());
		repo.save(falsehoods[2].clone((byte)2).getMetadata());
		repo.save(falsehoods[3].clone((byte)2).getMetadata());
		repo.save(falsehoods[4].clone((byte)2).getMetadata());
		repo.save(falsehoods[5].clone((byte)2).getMetadata());
		repo.save(falsehoods[6].clone((byte)2).getMetadata());
		repo.save(falsehoods[7].clone((byte)2).getMetadata());
		repo.save(falsehoods[8].clone((byte)2).getMetadata());
		repo.save(falsehoods[9].clone((byte)2).getMetadata());
		
		repo.save(falsehoods[0].clone((byte)6).getMetadata());
		repo.save(falsehoods[1].clone((byte)6).getMetadata());
		repo.save(falsehoods[2].clone((byte)6).getMetadata());
		repo.save(falsehoods[3].clone((byte)6).getMetadata());
		repo.save(falsehoods[4].clone((byte)6).getMetadata());
		repo.save(falsehoods[5].clone((byte)6).getMetadata());
		repo.save(falsehoods[6].clone((byte)6).getMetadata());
		repo.save(falsehoods[7].clone((byte)6).getMetadata());
		repo.save(falsehoods[8].clone((byte)6).getMetadata());
		repo.save(falsehoods[9].clone((byte)6).getMetadata());

		assertEquals(repo.findAll().size(), 20);
	}
	

}
