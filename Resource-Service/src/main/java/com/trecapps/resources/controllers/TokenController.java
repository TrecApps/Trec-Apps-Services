package com.trecapps.resources.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletResponse;

@Controller
public class TokenController
{
    @GetMapping("/tokenize")
    public void tokenize(@RequestParam(value = "code", required = true) String code,
                         @RequestParam(value = "redirect_url", required = true)String redirectUrl,
                         HttpServletResponse resp)
    {

    }
}
