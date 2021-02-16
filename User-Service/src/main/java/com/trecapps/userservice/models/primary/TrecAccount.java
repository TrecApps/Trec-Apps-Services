package com.trecapps.userservice.models.primary;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.trecapps.userservice.models.TrecAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

// import com.trecapps.userservice.security.TrecAuthority;

@Component
@Table
@Entity
public class TrecAccount implements UserDetails // implements UserDetails
{

	@Transient
	private static final int MINUTE_LENGTH = 100000;
	@Transient
	private static final int HOUR_LENGTH = 60;
	@Transient
	private static final int DAY_LENGTH = 24;
	@Transient
	private static final int MONTH_LENGTH = 30;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1456689615303218227L;
	String firstName;
	String lastName;
	
	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="TrecSequence")
	@SequenceGenerator(name="TrecSequence", allocationSize=1, sequenceName="SQ_TREC_PK")
	@NotNull
	long accountId;
	
	@Column
	@Size(min=6, max = 30)
	@NotNull
	String username;
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	String mainEmail;
	String trecEmail;
	String backupEmail;
	String token;
	Date birthday;
	int isValidated;
	String color;
	String validationToken;

	int oauthUse;

	byte priveledges;

	public int getOauthUse() {
		return oauthUse;
	}

	public void setOauthUse(int oauthUse) {
		this.oauthUse = oauthUse;
	}

	// Security Attributes
	byte passwordMonthReset; // How many months before Password needs to be Changed
	Date passwordChanged; // When the Password was last set
	
	byte timeForValidToken; // How long after login that the token should last (by 10 minutes)
	byte validTimeFromActivity; // Whether apps should update the token every activity
	
	byte maxLoginAttempts; // How many login attempts per hour 
	Date recentFailedLogin; 
	byte failedLoginAttempts; // Set this to 0 initially
	
	byte lockTime;  // Time, in 10 minutes, to lock an account after max_login_attempts 
	Date lockInit;  // When the account was locked (if NULL, assume unlocked)
	
	public TrecAccount(long accountId, String firstName, String lastName, String username, String mainEmail, String trecEmail,
			String backupEmail, String token, Date birthday, int isValidated, String color, String validationToken,
			
			byte passwordMonthReset, Date passwordChanged, byte timeForValidToken, byte validTimeFromActivity,
			byte maxLoginAttempts,Date recentFailedLogin, byte failedLoginAttempts, byte lockTime, Date lockInit) {
		super();
		this.accountId = accountId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.mainEmail = mainEmail;
		this.trecEmail = trecEmail;
		this.backupEmail = backupEmail;
		this.token = token;
		this.birthday = birthday;
		this.isValidated = isValidated;
		this.color = color;
		this.validationToken = validationToken;
		
		// Set Security Attributes
		this.passwordMonthReset = passwordMonthReset;
		this.passwordChanged=passwordChanged;
		this.timeForValidToken=timeForValidToken;
		this.validTimeFromActivity=validTimeFromActivity;
		this.maxLoginAttempts=maxLoginAttempts;
		this.recentFailedLogin=recentFailedLogin;
		this.failedLoginAttempts=failedLoginAttempts;
		this.lockTime=lockTime;
		this.lockInit=lockInit;

		this.priveledges = 0;
		// Used to aid in generating one time codes
		oauthUse = 0;

	}
	public TrecAccount() {
		super();
	}


	public byte getPriveledges() {
		return priveledges;
	}

	public void setPriveledges(byte priveledges) {
		this.priveledges = priveledges;
	}

	@Override
	public String toString() {
		return "TrecAccount [firstName=" + firstName + ", lastName=" + lastName + ", accountId=" + accountId
				+ ", username=" + username + ", mainEmail=" + mainEmail + ", trecEmail=" + trecEmail + ", backupEmail="
				+ backupEmail + ", token=" + token + ", birthday=" + birthday + ", isValidated=" + isValidated
				+ ", color=" + color + ", validationToken=" + validationToken + ", passwordMonthReset="
				+ passwordMonthReset + ", passwordChanged=" + passwordChanged + ", timeForValidToken="
				+ timeForValidToken + ", validTimeFromActivity=" + validTimeFromActivity + ", maxLoginAttempts="
				+ maxLoginAttempts + ", recentFailedLogin=" + recentFailedLogin + ", failedLoginAttempts="
				+ failedLoginAttempts + ", lockTime=" + lockTime + ", lockInit=" + lockInit + "]";
	}
	
	
	
	/**
	 * @return the passwordMonthReset
	 */
	public byte getPasswordMonthReset() {
		return passwordMonthReset;
	}
	/**
	 * @param passwordMonthReset the passwordMonthReset to set
	 */
	public void setPasswordMonthReset(byte passwordMonthReset) {
		this.passwordMonthReset = passwordMonthReset;
	}
	/**
	 * @return the passwordChanged
	 */
	public Date getPasswordChanged() {
		return passwordChanged;
	}
	/**
	 * @param passwordChanged the passwordChanged to set
	 */
	public void setPasswordChanged(Date passwordChanged) {
		this.passwordChanged = passwordChanged;
	}
	/**
	 * @return the timeForValidToken
	 */
	public byte getTimeForValidToken() {
		return timeForValidToken;
	}
	/**
	 * @param timeForValidToken the timeForValidToken to set
	 */
	public void setTimeForValidToken(byte timeForValidToken) {
		this.timeForValidToken = timeForValidToken;
	}
	/**
	 * @return the validTimeFromActivity
	 */
	public byte getValidTimeFromActivity() {
		return validTimeFromActivity;
	}
	/**
	 * @param validTimeFromActivity the validTimeFromActivity to set
	 */
	public void setValidTimeFromActivity(byte validTimeFromActivity) {
		this.validTimeFromActivity = validTimeFromActivity;
	}
	/**
	 * @return the maxLoginAttempts
	 */
	public byte getMaxLoginAttempts() {
		return maxLoginAttempts;
	}
	/**
	 * @param maxLoginAttempts the maxLoginAttempts to set
	 */
	public void setMaxLoginAttempts(byte maxLoginAttempts) {
		this.maxLoginAttempts = maxLoginAttempts;
	}
	/**
	 * @return the recentFailedLogin
	 */
	public Date getRecentFailedLogin() {
		return recentFailedLogin;
	}
	/**
	 * @param recentFailedLogin the recentFailedLogin to set
	 */
	public void setRecentFailedLogin(Date recentFailedLogin) {
		this.recentFailedLogin = recentFailedLogin;
	}
	/**
	 * @return the failedLoginAttempts
	 */
	public byte getFailedLoginAttempts() {
		return failedLoginAttempts;
	}
	/**
	 * @param failedLoginAttempts the failedLoginAttempts to set
	 */
	public void setFailedLoginAttempts(byte failedLoginAttempts) {
		this.failedLoginAttempts = failedLoginAttempts;
	}
	/**
	 * @return the lockTime
	 */
	public byte getLockTime() {
		return lockTime;
	}
	/**
	 * @param lockTime the lockTime to set
	 */
	public void setLockTime(byte lockTime) {
		this.lockTime = lockTime;
	}
	/**
	 * @return the lockInit
	 */
	public Date getLockInit() {
		return lockInit;
	}
	/**
	 * @param lockInit the lockInit to set
	 */
	public void setLockInit(Date lockInit) {
		this.lockInit = lockInit;
	}
	public long getAccountId() {
		return accountId;
	}
	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return new ArrayList<TrecAuthority>();
	}

	@Override
	public String getPassword() {
		return this.token;
	}

	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return false;
	}

	@Override
	public boolean isAccountNonLocked() {
		if(lockInit == null)
			return true;

		// Currently seen as locked,
		Date now = new Date(Calendar.getInstance().getTime().getTime());
		long diff = now.getTime() - lockInit.getTime();

		if(diff < (lockTime * 600000))
			return false;

		lockInit = null;
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		if(passwordMonthReset == 0)
			return true;

		Date now = new Date(Calendar.getInstance().getTime().getTime());
		long diff = now.getTime() - this.passwordChanged.getTime();

		return diff < (this.passwordMonthReset * MINUTE_LENGTH * HOUR_LENGTH * DAY_LENGTH * MONTH_LENGTH);
	}

	@Override
	public boolean isEnabled() {
		return false;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	public String getMainEmail() {
		return mainEmail;
	}
	public void setMainEmail(String mainEmail) {
		this.mainEmail = mainEmail;
	}
	public String getTrecEmail() {
		return trecEmail;
	}
	public void setTrecEmail(String trecEmail) {
		this.trecEmail = trecEmail;
	}
	public String getBackupEmail() {
		return backupEmail;
	}
	public void setBackupEmail(String backupEmail) {
		this.backupEmail = backupEmail;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	public int getIsValidated() {
		return isValidated;
	}
	public void setIsValidated(int isValidated) {
		this.isValidated = isValidated;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getValidationToken() {
		return validationToken;
	}
	public void setValidationToken(String validationToken) {
		this.validationToken = validationToken;
	}

}
