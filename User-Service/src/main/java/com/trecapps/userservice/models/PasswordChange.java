package com.trecapps.userservice.models;

import org.springframework.stereotype.Component;

@Component
public class PasswordChange {

	String username;
	String currentPassword;
	String newPassword;
	public PasswordChange() {
		super();
	}
	public PasswordChange(String username, String currentPassword, String newPassword) {
		super();
		this.username = username;
		this.currentPassword = currentPassword;
		this.newPassword = newPassword;
	}
	
	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}
	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
	
}
