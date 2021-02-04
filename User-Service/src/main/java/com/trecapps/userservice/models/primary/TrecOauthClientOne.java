package com.trecapps.userservice.models.primary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class TrecOauthClientOne {

	@Id
	short clientId;
	
	@Column
	String name;
	
	@Column
	String publicId;

	/**
	 * @param clientId
	 * @param name
	 * @param publicKey
	 */
	public TrecOauthClientOne(short clientId, String name, String publicId) {
		super();
		this.clientId = clientId;
		this.name = name;
		this.publicId = publicId;
	}

	/**
	 * 
	 */
	public TrecOauthClientOne() {
		super();
		// TODO Auto-generated constructor stub
	}




	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the clientId
	 */
	public short getClientId() {
		return clientId;
	}

	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(short clientId) {
		this.clientId = clientId;
	}

	/**
	 * @return the publicId
	 */
	public String getPublicId() {
		return publicId;
	}

	/**
	 * @param publicId the publicId to set
	 */
	public void setPublicId(String publicId) {
		this.publicId = publicId;
	}


	
}
