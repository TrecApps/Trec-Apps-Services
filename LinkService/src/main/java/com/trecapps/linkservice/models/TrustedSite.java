package com.trecapps.linkservice.models;

import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
@Component
public class TrustedSite {

    @Id
    String siteId;

    @Column
    String url;

    public TrustedSite() {
    }

    public TrustedSite(String siteId, String url) {
        this.siteId = siteId;
        this.url = url;
    }

    public String getSiteId() {
        return siteId;
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
