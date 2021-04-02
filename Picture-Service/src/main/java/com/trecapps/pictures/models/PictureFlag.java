package com.trecapps.pictures.models;

import javax.persistence.*;

@Table
@Entity
public class PictureFlag {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    String id;

    byte type;

    byte severity;

    String details;

    byte resolved;

    public byte isResolved() {
        return resolved;
    }

    public void setResolved(byte resolved) {
        this.resolved = resolved;
    }

    public PictureFlag() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public byte getSeverity() {
        return severity;
    }

    public void setSeverity(byte severity) {
        this.severity = severity;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
