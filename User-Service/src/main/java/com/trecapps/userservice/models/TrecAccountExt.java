package com.trecapps.userservice.models;

import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class TrecAccountExt
{
    String firstName;
    String lastName;
    long accountId;
    String username;
    String mainEmail;
    String trecEmail;
    String backupEmail;
    Timestamp birthday;
    int isValidated;
    String color;

    byte passwordMonthReset;
    Timestamp passwordChanged;

    byte timeForValidToken;

    byte maxLoginAttempts;
    Timestamp recentFailedLogin;

    byte lockTime;

    public TrecAccountExt(String firstName, String lastName, long accountId,
                          String username, String mainEmail, String trecEmail,
                          String backupEmail, Timestamp birthday, int isValidated,
                          String color, byte passwordMonthReset,
                          Timestamp passwordChanged, byte timeForValidToken,
                          byte maxLoginAttempts, Timestamp recentFailedLogin,
                          byte lockTime)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.accountId = accountId;
        this.username = username;
        this.mainEmail = mainEmail;
        this.trecEmail = trecEmail;
        this.backupEmail = backupEmail;
        this.birthday = birthday;
        this.isValidated = isValidated;
        this.color = color;
        this.passwordMonthReset = passwordMonthReset;
        this.passwordChanged = passwordChanged;
        this.timeForValidToken = timeForValidToken;
        this.maxLoginAttempts = maxLoginAttempts;
        this.recentFailedLogin = recentFailedLogin;
        this.lockTime = lockTime;
    }

    public TrecAccountExt() {
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

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
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

    public Timestamp getBirthday() {
        return birthday;
    }

    public void setBirthday(Timestamp birthday) {
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

    public byte getPasswordMonthReset() {
        return passwordMonthReset;
    }

    public void setPasswordMonthReset(byte passwordMonthReset) {
        this.passwordMonthReset = passwordMonthReset;
    }

    public Timestamp getPasswordChanged() {
        return passwordChanged;
    }

    public void setPasswordChanged(Timestamp passwordChanged) {
        this.passwordChanged = passwordChanged;
    }

    public byte getTimeForValidToken() {
        return timeForValidToken;
    }

    public void setTimeForValidToken(byte timeForValidToken) {
        this.timeForValidToken = timeForValidToken;
    }

    public byte getMaxLoginAttempts() {
        return maxLoginAttempts;
    }

    public void setMaxLoginAttempts(byte maxLoginAttempts) {
        this.maxLoginAttempts = maxLoginAttempts;
    }

    public Timestamp getRecentFailedLogin() {
        return recentFailedLogin;
    }

    public void setRecentFailedLogin(Timestamp recentFailedLogin) {
        this.recentFailedLogin = recentFailedLogin;
    }

    public byte getLockTime() {
        return lockTime;
    }

    public void setLockTime(byte lockTime) {
        this.lockTime = lockTime;
    }
}
