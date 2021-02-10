package com.trecapps.falsehoodsearch.falsehoodtests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.trecapps.falsehoodsearch.controllers.FalsehoodController;
import com.trecapps.falsehoodsearch.models.*;
import com.trecapps.falsehoodsearch.repos.FalsehoodRepo;
import com.trecapps.falsehoodsearch.resourcetests.MediaOutletTest;
import com.trecapps.falsehoodsearch.resourcetests.PublicFigureTest;
import com.trecapps.falsehoodsearch.services.MediaOutletService;
import com.trecapps.falsehoodsearch.services.PublicFigureService;
import com.trecapps.falsehoodsearch.services.SearchFalsehood;
import com.trecapps.falsehoodsearch.testobj.FalsehoodApp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;



@TestMethodOrder(OrderAnnotation.class)
public class FalsehoodTest {

	static FalsehoodApp sharedApp;
	
	static FullFalsehood falsehoods[] = new FullFalsehood[10];
	
	static ArrayList<String> outlets = new ArrayList<>();
	
	static List<PublicFigure> figures;
	static List<MediaOutlet> rOutlets = new ArrayList<>();
	
	static final long DATE_2010 = 1290000000000L;
	static final long DATE_2014 = 1403000000000L;
	static final long DATE_2017 = 1507900000000L;
	static final long DATE_2020 = 1603000000000L;
	static final long DATE_2008 = 1210000000000L;
	
	public static FullFalsehood GetFalsehood(int index)
	{
		return falsehoods[index];
	}
	
	@BeforeAll
	public static void setUpSharedApp() throws URISyntaxException
	{
		sharedApp = new FalsehoodApp();
		
		MediaOutletTest.initializeOutlets(sharedApp);
		PublicFigureTest.initializePublicFigures(sharedApp);
		
		initializeOutlets(sharedApp);
		
	}
	
	@Test
	@Order(2)
	public void searchByDates()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		SearchFalsehood search = new SearchFalsehood();
		
		search.setFrom(new Date(DATE_2017 - 50L));
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
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
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		search.setFrom(null);
		f = fController.searchFalsehoodByParams(search);
		assertEquals(8, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(8, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms(null);
	}
	
	@Test
	@Order(2)
	public void searchByFigure()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		
		search.setAuthor(figures.get(1));
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Loki");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		search.setAuthor(figures.get(2));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms("Thor");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
	}
	
	@Test
	@Order(2)
	public void searchByOutlet()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		
		search.setOutlet(rOutlets.get(0));
		
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		search.setOutlet(rOutlets.get(1));
		
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
	

	
	@Test
	@Order(2)
	public void searchBySeverity()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		search.setMaximum(Severity.OBJECTIVE_FALSEHOOD);
		
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
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
	@Order(2)
	public void searchByDatesAndSeverity()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		// Start with Minimum
		search.setMinimum(Severity.OBJECTIVE_FALSEHOOD);
		search.setFrom(new Date(DATE_2010));
		
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
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
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		// Blank Slate for Maximum Severity
		search = new SearchFalsehood();
		
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
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
		
		// Blank Slate for minimum and maximum severity
		search = new SearchFalsehood();
		
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
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(1, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(1, f.size());
		search.setTerms(null);
	}
	
	@Test
	@Order(2)
	public void searchByDatesFigureAndSeverity()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		// Start with Minimum
		search.setAuthor(figures.get(0));
		search.setMinimum(Severity.OBJECTIVE_FALSEHOOD);
		search.setFrom(new Date(DATE_2010));
		
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
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
		search = new SearchFalsehood();
		search.setAuthor(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		search.setTo(new Date(DATE_2020 -100));
		
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
		
		search.setFrom(new Date(DATE_2014 + 200));
		
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
		
		// Blank Slate for minimum and maximum severity
		search = new SearchFalsehood();
		search.setAuthor(figures.get(0));
		
		search.setMaximum(Severity.MISLEADING);
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		
		search.setTo(new Date(DATE_2017));
		
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
		
		search.setFrom(new Date(DATE_2014 + 200));
		
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
		
		// Public Figure, Before
		search = new SearchFalsehood();
		search.setAuthor(figures.get(0));
		search.setTo(new Date(DATE_2017));
		
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
		
		// Public Figure, Between
		search.setFrom(new Date(DATE_2014));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		search.setTerms(null);
		
		// Public Figure, Min Severity
		search.setTo(null);
		search.setFrom(null);
		
		search.setMinimum(Severity.MISLEADING);
		
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
		
		// Public Figure, Severity
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
		
		// Public Figure, Max Severity
		search.setMinimum(null);
		
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
	}

	@Test
	@Order(2)
	public void searchByDatesFigureAndOutlet()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		// First do between, Outlet
		search.setOutlet(rOutlets.get(0));
		search.setFrom(new Date(DATE_2008));

		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(4, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(4, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);

		// now do Before, Outlet
		search.setFrom(null);
		search.setTo(new Date(DATE_2017));
		
		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);

		// now do Max Severity, Outlet
		search.setOutlet(rOutlets.get(1));
		search.setMaximum(Severity.OBJECTIVE_FALSEHOOD);
		search.setTo(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Thanos");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		// Min Severity, Outlet
		search.setMinimum(Severity.OBJECTIVE_FALSEHOOD);
		search.setMaximum(null);

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
		
		// Severity, Outlet
		search.setMinimum(Severity.FAULTY_LOGIC);
		search.setMaximum(Severity.MISLEADING);

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
		
		// Max Severity, Outlet, and Before
		search.setOutlet(rOutlets.get(1));
		search.setMaximum(Severity.LIE);
		search.setTo(new Date(DATE_2014));
		search.setMinimum(null);
		
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
		
		// Max Severity, Outlet, and Between
		search.setFrom(new Date(DATE_2010));
		search.setOutlet(rOutlets.get(1));
		search.setMaximum(Severity.OBJECTIVE_FALSEHOOD);
		

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
		
		// Min Severity, Outlet, Before
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		search.setMaximum(null);
		search.setTo(new Date(DATE_2017));
		search.setFrom(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		// Min Severity, Outlet, Between
		search.setFrom(new Date(2010));
		search.setMinimum(Severity.SUBJECTIVE_FALSEHOOD);
		search.setMaximum(null);

		f = fController.searchFalsehoodByParams(search);
		assertEquals(3, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(3, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		// Severity, Outlet, Before
		search.setMaximum(Severity.OBJECTIVE_FALSEHOOD);
		search.setFrom(null);

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
		
		// Severity, Outlet, Between
		search.setFrom(new Date(2010));
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
	}
	

	
	@Test
	@Order(4)
	public void searchByOutletAndFigure()
	{
		FalsehoodController fController = sharedApp.getFalsehoodController();
		
		SearchFalsehood search = new SearchFalsehood();
		
		// First do the most basic search specified by this mehtod's purpose
		search.setOutlet(rOutlets.get(0));
		search.setAuthor(figures.get(0));
		
		List<Falsehood> f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms("Vader");
		f = fController.searchFalsehoodByParams(search);
		assertEquals(2, f.size());
		f = fController.searchRFalsehoodByParams(search);
		assertEquals(2, f.size());
		search.setTerms(null);
		
		// By Max Severity
		search.setMaximum(Severity.HYPOCRISY);
		
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
		
		// By Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
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
		
		// By Min Severity
		search.setMaximum(null);

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
		
		// Start Before
		search.setTo(new Date(DATE_2020));
		search.setMinimum(null);
		
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
		
		// Before, Min Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
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
		
		// Before, Severity
		search.setMaximum(Severity.HYPOCRISY);
		
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
		
		// Before, Max Severity
		search.setMinimum(null);
		
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
		
		// Between
		search.setFrom(new Date(DATE_2010));
		search.setMaximum(null);
		
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
		
		// Before, Min Severity
		search.setMinimum(Severity.FAULTY_LOGIC);
		
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
		
		// Before, Severity
		search.setMaximum(Severity.HYPOCRISY);
		
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
		
		// Before, Max Severity
		search.setMinimum(null);
		
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
	}
	
	public static void initializeOutlets(FalsehoodApp app) throws URISyntaxException
	{
		MediaOutletService outletService = app.getOutletService();
		PublicFigureService figureService = app.getPublicFigureService();
		
		MediaOutlet outlet1 = outletService.GetMediaOutlet(0);
		outlets.add(outlet1.getName());
		rOutlets.add(outlet1);
		MediaOutlet outlet2 = outletService.GetMediaOutlet(1);
		outlets.add(outlet2.getName());
		rOutlets.add(outlet2);
		MediaOutlet outlet3 = outletService.GetMediaOutlet(2);
		outlets.add(outlet3.getName());
		rOutlets.add(outlet3);
		
		
		figures = figureService.getPublicFigures(0, 10);
		assertTrue(figures.size() > 2);
		
		if(falsehoods[0] == null)
		{
			falsehoods[0] = new FullFalsehood("The first Falsehood Detected", // Wed Nov 17 2010 06:20:00
					new Falsehood(null, outlet1, (byte)0, (byte)0, null, (byte)1, figures.get(0), null, "source1", new Date(DATE_2010),"","Vader|Palpatine|Empire"),
					null, null);
		}
		if(falsehoods[1] == null)
		{
			falsehoods[1] = new FullFalsehood("The Second Falsehood Detected", // Fri Oct 13 2017 07:06:40
					new Falsehood(null, outlet2, (byte)0, (byte)0, null, (byte)3, figures.get(0), null, "source2", new Date(DATE_2017),"","Vader|Voldemort|Thanos"),
					null, null);
		}
		if(falsehoods[2] == null)
		{
			falsehoods[2] = new FullFalsehood("The Third Falsehood Detected", // Tue Jun 17 2014 04:13:20
					new Falsehood(null, outlet3, (byte)0, (byte)0, null, (byte)4, figures.get(2), null, "source3", new Date(DATE_2014),"","Voldemort|Dumbledore;|Ministry"),
					null, null);
		}
		if(falsehoods[3] == null)
		{
			falsehoods[3] = new FullFalsehood("The Fourth Falsehood Detected", // Sat Oct 17 2020 23:46:40
					new Falsehood(null, outlet1, (byte)0, (byte)0, null, (byte)5, figures.get(2), null, "source4", new Date(DATE_2020),"","Thanos|Ironman|Thor|Serena"),
					null, null);
		}
		if(falsehoods[4] == null)
		{
			falsehoods[4] = new FullFalsehood("The Fifth Falsehood Detected", // Mon May 05 2008 09:06:40
					new Falsehood(null, outlet2, (byte)0, (byte)0, null, (byte)0, figures.get(1), null, "source5", new Date(DATE_2008),"","Loki|Sif|Serena"),
					null, null);
		}
		
		
		if(falsehoods[5] == null)
		{
			falsehoods[5] = new FullFalsehood("The first Falsehood Detected", // Wed Nov 17 2010 06:20:00
					new Falsehood(null, outlet1, (byte)0, (byte)0, null, (byte)1, figures.get(0), null, "source1", new Date(DATE_2017),"","Vader|Palpatine|Empire"),
					null, null);
		}
		if(falsehoods[6] == null)
		{
			falsehoods[6] = new FullFalsehood("The Second Falsehood Detected", // Fri Oct 13 2017 07:06:40
					new Falsehood(null, outlet2, (byte)0, (byte)0, null, (byte)3, figures.get(0), null, "source2", new Date(DATE_2014),"","Vader|Voldemort|Thanos"),
					null, null);
		}
		if(falsehoods[7] == null)
		{
			falsehoods[7] = new FullFalsehood("The Third Falsehood Detected", // Tue Jun 17 2014 04:13:20
					new Falsehood(null, outlet3, (byte)0, (byte)0, null, (byte)4, figures.get(2), null, "source3", new Date(DATE_2010),"","Voldemort|Dumbledore|Ministry"),
					null, null);
		}
		if(falsehoods[8] == null)
		{
			falsehoods[8] = new FullFalsehood("The Fourth Falsehood Detected", // Sat Oct 17 2020 23:46:40
					new Falsehood(null, outlet1, (byte)0, (byte)0, null, (byte)5, figures.get(2), null, "source4", new Date(DATE_2008),"","Thanos|Ironman|Thor|Serena"),
					null, null);
		}
		if(falsehoods[9] == null)
		{
			falsehoods[9] = new FullFalsehood("The Fifth Falsehood Detected", // Mon May 05 2008 09:06:40
					new Falsehood(null, outlet2, (byte)0, (byte)0, null, (byte)0, figures.get(1), null, "source5", new Date(DATE_2020),"","Loki|Sif|Serena"),
					null, null);
		}

		FalsehoodRepo fRepo = app.getFalsehoodRepo();
		
		fRepo.save(falsehoods[0].clone((byte)2).getMetadata());
		fRepo.save(falsehoods[1].clone((byte)2).getMetadata());
		fRepo.save(falsehoods[2].clone((byte)2).getMetadata());
		fRepo.save(falsehoods[3].clone((byte)2).getMetadata());
		fRepo.save(falsehoods[4].clone((byte)2).getMetadata());
		fRepo.save(falsehoods[5].clone((byte)2).getMetadata());
		fRepo.save(falsehoods[6].clone((byte)2).getMetadata());
		fRepo.save(falsehoods[7].clone((byte)2).getMetadata());
		fRepo.save(falsehoods[8].clone((byte)2).getMetadata());
		fRepo.save(falsehoods[9].clone((byte)2).getMetadata());
		
		fRepo.save(falsehoods[0].clone((byte)6).getMetadata());
		fRepo.save(falsehoods[1].clone((byte)6).getMetadata());
		fRepo.save(falsehoods[2].clone((byte)6).getMetadata());
		fRepo.save(falsehoods[3].clone((byte)6).getMetadata());
		fRepo.save(falsehoods[4].clone((byte)6).getMetadata());
		fRepo.save(falsehoods[5].clone((byte)6).getMetadata());
		fRepo.save(falsehoods[6].clone((byte)6).getMetadata());
		fRepo.save(falsehoods[7].clone((byte)6).getMetadata());
		fRepo.save(falsehoods[8].clone((byte)6).getMetadata());
		fRepo.save(falsehoods[9].clone((byte)6).getMetadata());

		assertEquals(fRepo.findAll().size(), 20);
	}



}
