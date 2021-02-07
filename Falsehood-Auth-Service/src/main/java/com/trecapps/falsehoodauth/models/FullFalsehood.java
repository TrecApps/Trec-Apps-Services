package com.trecapps.falsehoodauth.models;

import com.trecapps.falsehoodsearch.jsonmodels.EventObj;
import com.trecapps.falsehoodsearch.jsonmodels.VerdictObj;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class FullFalsehood {

	String contents;
	
	com.trecapps.falsehoodsearch.models.Falsehood metadata;
	
	List<VerdictObj> verdicts;
	List<EventObj> events;


	
	/**
	 * @param contents
	 * @param metadata
	 * @param events
	 */
	public FullFalsehood(String contents, com.trecapps.falsehoodsearch.models.Falsehood metadata, List<VerdictObj> verdicts, List<EventObj> events) {
		super();
		this.contents = contents;
		this.metadata = metadata;
		this.verdicts = verdicts;
		this.events = events;
	}

	public FullFalsehood clone()
	{
		return new FullFalsehood(contents, metadata.clone(), verdicts, events);
	}

	/**
	 * 
	 */
	public FullFalsehood() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}

	/**
	 * @return the metadata
	 */
	public com.trecapps.falsehoodsearch.models.Falsehood getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(com.trecapps.falsehoodsearch.models.Falsehood metadata) {
		this.metadata = metadata;
	}

	/**
	 * @return the keywords
	 */
	public List<VerdictObj> getVerdicts() {
		return verdicts;
	}

	/**
	 * @param keywords the keywords to set
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
