package com.trecapps.userservice.models.secondary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class TrecOauthClientTwo {

	@Id
	short clientId;
	
	@Column
	byte clientLevel; // If negative, then it's being managed by TrecApps itself
	
	@Column
	String publicKey;

	/**
	 * @param clientId
	 * @param clientLevel
	 * @param privateKey
	 */
	public TrecOauthClientTwo(short clientId, byte clientLevel, String publicKey) {
		super();
		this.clientId = clientId;
		this.clientLevel = clientLevel;
		this.publicKey = publicKey;
	}

	/**
	 * 
	 */
	public TrecOauthClientTwo() {
		super();
		// TODO Auto-generated constructor stub
	}


	/**
	 * @return the clientLevel
	 */
	public byte getClientLevel() {
		return clientLevel;
	}

	/**
	 * @param clientLevel the clientLevel to set
	 */
	public void setClientLevel(byte clientLevel) {
		this.clientLevel = clientLevel;
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
	 * @return the publicKey
	 */
	public String getPublicKey() {
		return publicKey;
	}

	/**
	 * @param publicKey the publicKey to set
	 */
	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}


	
}
