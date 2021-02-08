package com.trecapps.userservice.models.secondary;

import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.Calendar;

@Component
@Table
@Entity
public class InvalidSession {

    @Id
    long accountId;

    @Column(length = 220)
    String invalidSessions;

    @Column
    Date lastInvalidation;

    public InvalidSession() {
        invalidSessions = "";
    }

    public InvalidSession(long accountId, String invalidSessions, Date lastInvalidation) {
        this.accountId = accountId;
        setInvalidSessions(invalidSessions);
        this.lastInvalidation = lastInvalidation;
    }

    public long getAccountId() {
        return accountId;
    }

    public void setAccountId(long accountId) {
        this.accountId = accountId;
    }

    public String getInvalidSessions() {
        return invalidSessions;
    }

    public void setInvalidSessions(String invalidSessions) {
        this.invalidSessions = invalidSessions;
        if(this.invalidSessions == null)
            this.invalidSessions = "";
    }

    public Date getLastInvalidation() {
        return lastInvalidation;
    }

    public void setLastInvalidation(Date lastInvalidation) {
        this.lastInvalidation = lastInvalidation;
    }

    public boolean addSession(String session)
    {
        if(session.length() != 10)
            throw new IllegalArgumentException("Parameter must be a string of ten characters");

        Date now = new Date(Calendar.getInstance().getTime().getTime());

        if(lastInvalidation == null || (now.getTime() - lastInvalidation.getTime()) > 600000)
        {
            // Here, last Validation has never been set or it has been over tem minutes ago, so we can clear the session
            // Data, as now any invalid sessions will be caught by expiration date
            lastInvalidation = now;
            invalidSessions = session;
            return true;
        }

        // 220 is the maximum length of the string we allow
        if(invalidSessions.length() >= 210)
        {
            // If we reach this point, way too many sessions have been created and the account should be locked
            return false;
        }

        invalidSessions += ";" + session;
        lastInvalidation = now;
        return true;
    }

    public boolean hasSession(String session)
    {
        return invalidSessions.indexOf(session) != -1;
    }
}
