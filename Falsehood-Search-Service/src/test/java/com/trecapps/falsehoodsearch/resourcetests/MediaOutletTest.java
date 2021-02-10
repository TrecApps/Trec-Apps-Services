package com.trecapps.falsehoodsearch.resourcetests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.URISyntaxException;

import com.trecapps.falsehoodsearch.models.MediaOutlet;
import com.trecapps.falsehoodsearch.models.MediaOutletEntry;
import com.trecapps.falsehoodsearch.repos.MediaOutletRepo;
import com.trecapps.falsehoodsearch.repos.RegionRepo;
import com.trecapps.falsehoodsearch.services.FalsehoodStorageHolder;
import com.trecapps.falsehoodsearch.testobj.FalsehoodApp;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;


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
	

}
