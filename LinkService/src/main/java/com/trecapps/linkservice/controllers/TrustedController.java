package com.trecapps.linkservice.controllers;

import com.trecapps.linkservice.models.TrustedSite;
import com.trecapps.linkservice.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Restricted")
public class TrustedController
{
    LinkService linkService;

    @Autowired
    public TrustedController(LinkService linkService)
    {
        this.linkService = linkService;
    }

    @PostMapping("/AddSite")
    ResponseEntity<String> addSite(TrustedSite site)
    {
        String response = linkService.addTrustedSite(site.getUrl(), site.getSiteId());
        if("".equals(response))
            return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
