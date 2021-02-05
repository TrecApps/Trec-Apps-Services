package com.trecapps.userservice.controllers;

import com.trecapps.userservice.models.OauthToken;
import com.trecapps.userservice.models.primary.TrecAccount;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
@RequestMapping("/oauth2")
public class Oauth2Controller {

    @GetMapping("/login")
    public ModelAndView getAuthPage(@RequestParam Map<String, String> params)
    {
        return null;
    }

    @GetMapping("/authorize")
    public void doAuthorize(@RequestParam Map<String, String> params, HttpServletResponse response)
    {

    }

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public OauthToken getToken(RequestEntity<MultiValueMap<String, String>> request)
    {
        return null;
    }

    @GetMapping("/userinfo")
    public TrecAccount getUserInfo(HttpServletRequest req)
    {
        return null;
    }

    @GetMapping("/logout")
    public void doLogOut(@RequestParam Map<String, String> params, HttpServletResponse response)
    {

    }

}
