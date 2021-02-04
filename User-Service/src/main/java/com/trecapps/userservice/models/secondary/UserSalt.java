package com.trecapps.userservice.models.secondary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class UserSalt {

	@Id
	long id;
	
	@Column
	String salt;

	/**
	 * 
	 */
	public UserSalt() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param salt
	 */
	public UserSalt(long id, String salt) {
		super();
		this.id = id;
		this.salt = salt;
	}
	
	

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * @return the salt
	 */
	public String getSalt() {
		return salt;
	}

	/**
	 * @param salt the salt to set
	 */
	public void setSalt(String salt) {
		this.salt = salt;
	}
	
	
}
