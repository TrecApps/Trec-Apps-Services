package com.trecapps.userservice.models;

public class OauthClient {

	short id;
	
	String name;
	
	String idStr;
	
	byte clientLevel;
	
	String publicKey;
	
	String privateKey;

	/**
	 * @param id
	 * @param name
	 * @param idStr
	 * @param clientLevel
	 * @param publicKey
	 * @param privateKey
	 */
	public OauthClient(short id, String name, String idStr, byte clientLevel, String publicKey, String privateKey) {
		super();
		this.id = id;
		this.name = name;
		this.idStr = idStr;
		this.clientLevel = clientLevel;
		this.publicKey = publicKey;
		this.privateKey = privateKey;
	}

	/**
	 * @return the id
	 */
	public short getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(short id) {
		this.id = id;
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
	 * @return the idStr
	 */
	public String getIdStr() {
		return idStr;
	}

	/**
	 * @param idStr the idStr to set
	 */
	public void setIdStr(String idStr) {
		this.idStr = idStr;
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

	/**
	 * @return the privateKey
	 */
	public String getPrivateKey() {
		return privateKey;
	}

	/**
	 * @param privateKey the privateKey to set
	 */
	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}
	
	
}
