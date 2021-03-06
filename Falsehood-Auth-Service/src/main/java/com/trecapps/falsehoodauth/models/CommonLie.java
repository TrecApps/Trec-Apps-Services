package com.trecapps.falsehoodauth.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class CommonLie implements Comparable<CommonLie>{

	@Id
	Long id;
	
	@Column
	String title;
	
	

	/**
	 * 
	 */
	public CommonLie() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CommonLie(Long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int compareTo(CommonLie o) {
		return id.compareTo(o.getId());
	}
	
	
}
