package com.trecapps.linkservice.services;

import com.trecapps.linkservice.models.Link;
import com.trecapps.linkservice.models.TrustedSite;
import com.trecapps.linkservice.repos.LinkRepo;
import com.trecapps.linkservice.repos.TrustedSiteRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service
public class LinkService {

    LinkRepo linkRepo;

    TrustedSiteRepo trustedSiteRepo;

    @Autowired
    public LinkService(LinkRepo linkRepo, TrustedSiteRepo trustedSiteRepo)
    {
        this.linkRepo = linkRepo;
        this.trustedSiteRepo = trustedSiteRepo;
    }

    public String AddLink(String link)
    {
        URL jUrl = null;
        try {
            jUrl = new URL(link);
        } catch (MalformedURLException e) {
            return "Malformed URL provided!";
        }

        jUrl.getProtocol();

        if(!"https".equals(jUrl.getProtocol())) {
            return "URL needs to be HTTPS!";
        }

        List<TrustedSite> ts = trustedSiteRepo.getSiteByUrl(jUrl.getHost());
        if(ts.size() == 0)
            return "Base Url "+ jUrl.getHost() +" provided is not listed as a Trusted site!";

        TrustedSite trustedSite = ts.get(0);

        List<Link> links = linkRepo.getLinkByEndpoint(jUrl.getFile(), trustedSite);
        if(links.size() > 0)
            return "URL has already been added!";

        Link newLink = new Link(null, jUrl.getFile(),trustedSite);

        linkRepo.save(newLink);
        return "";
    }

    public Link getLink(String linkKey)
    {
        if(!linkRepo.existsById(linkKey))
            return null;
        return linkRepo.getOne(linkKey);
    }

    public String addTrustedSite(String url, String title)
    {
        if(trustedSiteRepo.existsById(title))
            return "Site ID already taken!";

        URL jUrl = null;
        try
        {
            jUrl = new URL(url);
        }
        catch(MalformedURLException e)
        {
            return "Malformed URL detected!";
        }

        if(!"https://".equals(jUrl.getProtocol()))
            return "Trusted Site must have HTTPS as the Protocol!";

        List<TrustedSite> ts = trustedSiteRepo.getSiteByUrl(jUrl.getHost());
        if(ts.size() > 0)
            return "Base Url "+ jUrl.getHost() +" provided is already listed as a Trusted site!";

        TrustedSite trustedSite = new TrustedSite(title, jUrl.getProtocol());

        trustedSiteRepo.save(trustedSite);
        return "";
    }

    public List<TrustedSite> getTrustedLists(int page, int size)
    {
        return trustedSiteRepo.findAll(PageRequest.of(page, size)).toList();
    }

}
