package com.trecapps.linkservice.models;

import org.springframework.stereotype.Component;

import javax.persistence.*;

@Table
@Entity
@Component
public class Link
{
    @Id
    String urlCode;

    @Column
    String endpoint;

    @ManyToOne
    TrustedSite site;

    public Link() {
    }

    public Link(String urlCode, String endpoint, TrustedSite site) {
        this.urlCode = urlCode;
        this.endpoint = endpoint;
        this.site = site;
    }

    public String getUrlCode() {
        return urlCode;
    }

    public void setUrlCode(String urlCode) {
        this.urlCode = urlCode;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public TrustedSite getSite() {
        return site;
    }

    public void setSite(TrustedSite site) {
        this.site = site;
    }
}
