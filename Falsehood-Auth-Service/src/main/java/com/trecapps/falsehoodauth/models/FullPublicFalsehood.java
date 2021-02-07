package com.trecapps.falsehoodauth.models;

import com.trecapps.falsehoodsearch.jsonmodels.EventObj;
import com.trecapps.falsehoodsearch.jsonmodels.VerdictObj;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FullPublicFalsehood {

    String contents;

    com.trecapps.falsehoodsearch.models.PublicFalsehood metadata;

	List<VerdictObj> verdicts;
	List<EventObj> events;

    public FullPublicFalsehood() {
    }
    
    /**
	 * @param contents
	 * @param metadata
	 * @param events
	 */
	public FullPublicFalsehood(String contents, com.trecapps.falsehoodsearch.models.PublicFalsehood metadata, List<VerdictObj> verdicts,
                               List<EventObj> events) {
		super();
		this.contents = contents;
		this.metadata = metadata;
		this.verdicts = verdicts;
		this.events = events;
	}

	public FullPublicFalsehood clone()
    {
    	return new FullPublicFalsehood(contents, metadata.clone(), verdicts, events);
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public com.trecapps.falsehoodsearch.models.PublicFalsehood getMetadata() {
        return metadata;
    }

    public void setMetadata(com.trecapps.falsehoodsearch.models.PublicFalsehood metadata) {
        this.metadata = metadata;
    }

	/**
	 * @return the verdicts
	 */
	public List<VerdictObj> getVerdicts() {
		return verdicts;
	}

	/**
	 * @param verdicts the verdicts to set
	 */
	public void setVerdicts(List<VerdictObj> verdicts) {
		this.verdicts = verdicts;
	}

	/**
	 * @return the events
	 */
	public List<EventObj> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(List<EventObj> events) {
		this.events = events;
	}

    
}
