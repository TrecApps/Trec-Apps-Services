package com.trecapps.linkservice.controllers;

import com.trecapps.linkservice.services.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Auth")
public class AuthController
{
    LinkService linkService;

    @Autowired
    public AuthController(LinkService linkService)
    {
        this.linkService = linkService;
    }

    @PostMapping
    ResponseEntity<String> addLink(RequestEntity<String> entity)
    {
        String result = linkService.AddLink(entity.getBody());
        if("".equals(result))
            return new ResponseEntity<String>(HttpStatus.OK);
        return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
    }
}
