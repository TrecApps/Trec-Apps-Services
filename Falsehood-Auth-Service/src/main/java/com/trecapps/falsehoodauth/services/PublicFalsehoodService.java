package com.trecapps.falsehoodauth.services;


import com.trecapps.falsehoodauth.jsonmodels.EventObj;
import com.trecapps.falsehoodauth.jsonmodels.VerdictListObj;
import com.trecapps.falsehoodauth.jsonmodels.VerdictObj;
import com.trecapps.falsehoodauth.models.FalsehoodStatus;
import com.trecapps.falsehoodauth.models.FalsehoodUser;
import com.trecapps.falsehoodauth.models.PublicFalsehood;
import com.trecapps.falsehoodauth.repos.FalsehoodUserRepo;
import com.trecapps.falsehoodauth.repos.PublicFalsehoodRepo;
import org.springframework.stereotype.Service;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.math.BigInteger;
import java.sql.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

@Service
public class PublicFalsehoodService {

	public static final int MIN_CREDIT_APPROVE_REJECT = 60;
    PublicFalsehoodRepo pfRepo;

    FalsehoodStorageHolder s3BucketManager;

    FalsehoodUserRepo uServe;
    
    @Autowired
    public PublicFalsehoodService(@Autowired FalsehoodStorageHolder s3BucketManager,
                                  @Autowired PublicFalsehoodRepo pfRepo,
                                  @Autowired FalsehoodUserRepo uServe)
    {
        this.pfRepo = pfRepo;
        this.s3BucketManager = s3BucketManager;
        this.uServe = uServe;
    }
    
	public String addVerdict(BigInteger id, int approve, String comment, FalsehoodUser user, HttpServletRequest ip)
	{
		if(user.getCredit() < MIN_CREDIT_APPROVE_REJECT)
			return "Not Enough Credit";

		if(!pfRepo.existsById(id))
			return "Falsehood Doesn't exist";

		PublicFalsehood f = pfRepo.getOne(id);
		if(f.getStatus() != FalsehoodStatus.SUBMITTED.GetValue())
			return "Cannot cast Verdict on established Public Falsehood! File an Appeal to update the status";

		JSONObject verdictJson = null;
		VerdictListObj verdicts = new VerdictListObj();
		try {
			verdictJson = s3BucketManager.getJSONObj("publicFalsehood-" + f.getId());
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
				creator = uServe.getOne(event.getUserId());
				break;
			}
		}

		if(creator != null && user.getUserId().equals(creator.getUserId()))
			return "Submitter of the Falsehood cannot be the same one to Add a verdict!";
		if(creator != null && user != null)
			System.out.println("Creator ID: " + creator.getUserId() + " User ID: " + user.getUserId());
		verdicts.setApproversAvailable(uServe.getUsersAboveCredit(MIN_CREDIT_APPROVE_REJECT));

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
		verdictList.add(newVerdict);
		verdicts.setVerdicts(verdictList);

		verdictJson = verdicts.toJsonObject();

		if(!"Success".equals(s3BucketManager.addJsonFile("publicFalsehood-" + f.getId(), verdictJson)))
		{
			return "failed to Write Verdict to storage!";
		}

		if(verdicts.isApproved())
		{
			f.setStatus(FalsehoodStatus.VERIFIED.GetValue());
			pfRepo.save(f);
		}
		else if(verdicts.isRejected())
		{
			System.out.println("Rejecting Public Falsehood!");
			f.setStatus(FalsehoodStatus.REJECTED.GetValue());
			pfRepo.save(f);
			
			if(verdicts.shouldStrike() && creator != null)
			{
				creator.setCredit(creator.getCredit() - 5);
				uServe.save(creator);
			}
		}


		return "";
	}
    


    public PublicFalsehood insertNewFalsehood(PublicFalsehood f)
    {
        f.setId(null);

        return pfRepo.save(f);
    }

    public PublicFalsehood updateNewFalsehood(PublicFalsehood f)
    {
        return pfRepo.save(f);
    }

    public boolean insertEntryToStorage(PublicFalsehood f, String contents,FalsehoodUser user,HttpServletRequest ip)
    {
        String objectId = "publicFalsehood-" + f.getId();
        
        VerdictListObj verdicts = new VerdictListObj();
		verdicts.setApproversAvailable(uServe.getUsersAboveCredit(MIN_CREDIT_APPROVE_REJECT));

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

		JSONObject insertJson = verdicts.toJsonObject();

		if(!"Success".equals(s3BucketManager.addJsonFile(objectId, insertJson)))
		{
			return false;
		}
        return "Success".equals(s3BucketManager.addNewFile(objectId, contents));

    }

    public boolean appendEntryToStorage(String contents, PublicFalsehood f)
    {
    	String objectId = "publicFalsehood-" + f.getId();
        return "Success".equals(s3BucketManager.appendFile(objectId, contents));
    }
}
