package com.trecapps.falsehoodauth.services;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import com.trecapps.falsehoodauth.jsonmodels.EventObj;
import com.trecapps.falsehoodauth.jsonmodels.VerdictListObj;
import com.trecapps.falsehoodauth.jsonmodels.VerdictObj;
import com.trecapps.falsehoodauth.models.Falsehood;
import com.trecapps.falsehoodauth.models.FalsehoodStatus;
import com.trecapps.falsehoodauth.models.FalsehoodUser;
import com.trecapps.falsehoodauth.repos.FalsehoodRepo;
import com.trecapps.falsehoodauth.repos.FalsehoodUserRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import javax.servlet.http.HttpServletRequest;

@Service
public class FalsehoodService {
	public static final int MIN_CREDIT_APPROVE_REJECT = 60;

	FalsehoodRepo fRepo;

	FalsehoodStorageHolder s3BucketManager;

	FalsehoodUserRepo userService;

	@Autowired
	public FalsehoodService(@Autowired FalsehoodRepo fRepo,
							@Autowired FalsehoodStorageHolder s3BucketManager,
							@Autowired FalsehoodUserRepo userService)
	{
		this.s3BucketManager = s3BucketManager;
		this.fRepo = fRepo;
		this.userService = userService;
	}
	

	
	public Falsehood insertNewFalsehood(Falsehood f)
	{
		f.setId(null);
		
		if(f.getTags() != null)
		{
			String tags = f.getTags();
			tags = tags.replace("\n", "|");
			f.setTags(tags);
		}
		
		f = fRepo.save(f);
		
		f.setContentId(f.getId().toString() + "-" + f.getSource());
		
		return fRepo.save(f);
	}
	
	public Falsehood updateNewFalsehood(Falsehood f)
	{
		return fRepo.save(f);
	}
	
	public boolean insertEntryToStorage(String contents, Falsehood f, FalsehoodUser user, HttpServletRequest ip)
	{
		String objectId = f.getId() + "-Falsehood";
		
		System.out.println("Object ID inserting is " + objectId);

		JSONObject insertJson = null;
		VerdictListObj verdicts = new VerdictListObj();
		try {
			insertJson = s3BucketManager.getJSONObj(f.getContentId());
			verdicts.initializeFromJson(insertJson);

			List<EventObj> verdictList = verdicts.getEvents();

			for(int rust = 0; rust < verdictList.size(); rust++)
			{
				if(verdictList.get(rust).getUserId() == user.getUserId())
				{
					verdictList.remove(rust);
					break;
				}
			}
			verdicts.setEvents(verdictList);
		} catch(Exception e)
		{
			System.out.println(e.getMessage());
		}

		verdicts.setApproversAvailable(userService.getUsersAboveCredit(MIN_CREDIT_APPROVE_REJECT));

		EventObj event = new EventObj(1, user.getUserId(),
				new Date(Calendar.getInstance().getTime().getTime()), null, null);

		event.setIpAddress(ip);

		List<EventObj> eventList = verdicts.getEvents();
		if(eventList == null)
		{
			eventList = new LinkedList<>();
			System.out.println("Detected Null EventList Somehow!");
		}
		eventList.add(event);
		verdicts.setEvents(eventList);

		insertJson = verdicts.toJsonObject();

		if(!"Success".equals(s3BucketManager.addJsonFile(objectId, insertJson)))
		{
			return false;
		}

		return "Success".equals(s3BucketManager.addNewFile(objectId, contents));
		
	}

	public String addVerdict(BigInteger id, int approve, String comment, FalsehoodUser user, HttpServletRequest ip)
	{
		if(user.getCredit() < MIN_CREDIT_APPROVE_REJECT)
			return "Not Enough Credit";

		if(!fRepo.existsById(id))
			return "Falsehood "+id+" Doesn't exist";

		Falsehood f = fRepo.getOne(id);
		if(f.getStatus() != FalsehoodStatus.SUBMITTED.GetValue())
			return "Cannot cast Verdict on established Falsehood! File an Appeal to update the status";

		String objectId = f.getId() + "-Falsehood";
		JSONObject verdictJson = null;
		VerdictListObj verdicts = new VerdictListObj();
		try {
			verdictJson = s3BucketManager.getJSONObj(objectId);
			verdicts.initializeFromJson(verdictJson);
		} catch(Exception e)
		{

		}
		var events = verdicts.getEvents();
		FalsehoodUser creator = null;
		for(EventObj event: events)
		{
			if(event.isApprove() > 0)
			{
				creator = userService.getOne(event.getUserId());
				break;
			}
		}
		verdicts.setApproversAvailable(userService.getUsersAboveCredit(MIN_CREDIT_APPROVE_REJECT));

		VerdictObj newVerdict = new VerdictObj(approve, user.getUserId(),
				new Date(Calendar.getInstance().getTime().getTime()), comment, null);

		newVerdict.setIpAddress(ip);

		List<VerdictObj> verdictList = verdicts.getVerdicts();

		for(int rust = 0; rust < verdictList.size(); rust++)
		{
			if(verdictList.get(rust).getUserId() == user.getUserId())
			{
				verdictList.remove(rust);
				break;
			}
		}
		verdicts.setVerdicts(verdictList);
		verdictList.add(newVerdict);

		verdictJson = verdicts.toJsonObject();



		if(creator != null && user.getUserId().equals(creator.getUserId()))
			return "Submitter cannot add a verdict!";

		if(!"Success".equals(s3BucketManager.addJsonFile(objectId, verdictJson)))
		{
			return "failed to Write Verdict to storage!";
		}

		if(verdicts.isApproved())
		{
			f.setStatus(FalsehoodStatus.VERIFIED.GetValue());
			fRepo.save(f);
		}
		else if(verdicts.isRejected())
		{
			f.setStatus(FalsehoodStatus.REJECTED.GetValue());
			fRepo.save(f);
			if(verdicts.shouldStrike() && creator != null)
			{
				// To-Do: Get the User
				creator.setCredit(creator.getCredit() - 5);
				userService.save(creator);
			}
		}


		return "";
	}
	
	public boolean appendEntryToStorage(String contents, Falsehood f, FalsehoodUser user, HttpServletRequest ip)
	{
		String objectId = f.getId() + "-Falsehood";
		System.out.println("Object ID inserting is " + objectId);

		JSONObject insertJson = null;
		VerdictListObj verdicts = new VerdictListObj();
		try {
			insertJson = s3BucketManager.getJSONObj(objectId);
			verdicts.initializeFromJson(insertJson);

			List<EventObj> verdictList = verdicts.getEvents();

			for(int rust = 0; rust < verdictList.size(); rust++)
			{
				if(verdictList.get(rust).getUserId() == user.getUserId())
				{
					verdictList.remove(rust);
					break;
				}
			}
			verdicts.setEvents(verdictList);
		} catch(Exception e)
		{

		}

		verdicts.setApproversAvailable(userService.getUsersAboveCredit(MIN_CREDIT_APPROVE_REJECT));

		EventObj event = new EventObj(1, user.getUserId(),
				new Date(Calendar.getInstance().getTime().getTime()), null, null);

		event.setIpAddress(ip);

		List<EventObj> eventList = verdicts.getEvents();
		eventList.add(event);
		verdicts.setEvents(eventList);

		insertJson = verdicts.toJsonObject();

		if(!"Success".equals(s3BucketManager.addJsonFile(objectId, insertJson)))
		{
			return false;
		}

		return "Success".equals(s3BucketManager.appendFile(objectId, contents));
	}
}
