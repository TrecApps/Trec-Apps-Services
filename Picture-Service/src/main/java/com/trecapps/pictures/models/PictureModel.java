package com.trecapps.pictures.models;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.sql.Date;

@Table
@Entity
public class PictureModel
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    String id;          // Id, used to identify the actual file

    byte safe;          // Whether it has been cleared of any issue by a TrecApps employee

    String flagId;      // Id of flag based info (if null or empty, then no one has flagged this image)

    byte adultStatus;   // Whether this image is SFW or not.

    Date submitted;     // When this picture was added

    @Length(max = 6)
    String extension;   // the file extension of the image

    long submitter;     // TrecAccount Id of the account that added the image

    public PictureModel() {
        safe = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public byte getSafe() {
        return safe;
    }

    public void setSafe(byte safe) {
        this.safe = safe;
    }

    public String getFlagId() {
        return flagId;
    }

    public void setFlagId(String flagId) {
        this.flagId = flagId;
    }

    public byte getAdultStatus() {
        return adultStatus;
    }

    public void setAdultStatus(byte adultStatus) {
        this.adultStatus = adultStatus;
    }

    public Date getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    @Override
    public String toString() {
        return "PictureModel{" +
                "id='" + id + '\'' +
                ", safe=" + safe +
                ", flagId='" + flagId + '\'' +
                ", adultStatus=" + adultStatus +
                ", submitted=" + submitted +
                ", extension='" + extension + '\'' +
                '}';
    }
}
