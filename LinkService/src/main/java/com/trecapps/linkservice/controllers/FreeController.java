package com.trecapps.linkservice.controllers;

import com.trecapps.linkservice.models.Link;
import com.trecapps.linkservice.models.TrustedSite;
import com.trecapps.linkservice.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class FreeController {

    LinkService linkService;

    @Autowired
    public FreeController(LinkService linkService)
    {
        this.linkService = linkService;
    }

    @GetMapping("/link/{link}")
    ResponseEntity<String> getLink(@PathVariable("link")String link)
    {
        Link link1 = linkService.getLink(link);
        if(link == null)
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.FOUND);

        responseEntity.getHeaders().add("Location",
                link1.getSite().getUrl() + link1.getEndpoint());
        return responseEntity;
    }

    @GetMapping("/trustedSites")
    ResponseEntity<List<TrustedSite>> getTrustedSites(
            @RequestParam(name = "page", required = false, defaultValue = "0") int page,
            @RequestParam(name = "size", required = false, defaultValue = "20") int size)
    {
        return new ResponseEntity<>(linkService.getTrustedLists(page, size), HttpStatus.OK);
    }
}
