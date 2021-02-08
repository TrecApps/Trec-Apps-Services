package com.trecapps.userservice.controllers;

import com.trecapps.userservice.models.OauthToken;
import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.models.primary.TrecOauthClient;
import com.trecapps.userservice.services.JwtTokenService;
import com.trecapps.userservice.services.TrecAccountService;
import com.trecapps.userservice.services.TrecOauthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Controller
@RequestMapping("/oauth2")
public class Oauth2Controller {

    @Autowired
    TrecOauthClientService oauthService;

    @Autowired
    TrecAccountService accountService;

    @Autowired
    JwtTokenService jwtService;

    private TrecOauthClient validateClient(String clientId, String url, String sec, boolean needSecret)
    {
        TrecOauthClient client;
        if(clientId == null || (client = oauthService.loadClientByClientId(clientId)) == null)
        {
            throw new RuntimeException("Null Client Detected!");
        }

        // To-Do: Proper input validation of redirectUrl
        if(url == null)
        {
            throw new RuntimeException("Null RedirectUrl Detected!");
        }

        if(needSecret && !client.getClientSecret().equals(sec))
        {
            // To-Do: This indicates a potential greater danger that requires a different response

            // For now, just throw a Runtime Exception
            throw new RuntimeException("Secrets of the Sith");
        }

        return client;
    }

    @GetMapping("/login")
    public ModelAndView getAuthPage(@RequestParam Map<String, String> params)
    {
        String clientId = params.get("client_id");
        String redirectUrl = params.get("redirect_url");
        TrecOauthClient client = validateClient(clientId, redirectUrl, null, false);

        ModelAndView view = new ModelAndView();
        view.setViewName("login");

        ModelMap models = view.getModelMap();
        models.addAttribute("formAction", "/auth/oauth2/login");
        models.addAttribute("message", client.getName() + " Log In");
        models.addAttribute("redirect_url", redirectUrl);
        models.addAttribute("client_id", clientId);
        return view;
    }

    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void attemptLogin(RequestEntity<MultiValueMap<String, String>> request, HttpServletResponse response)
    {
        MultiValueMap<String, String> vars = request.getBody();

        String clientId = vars.getFirst("client_id");
        String redirectUrl = vars.getFirst("redirect_url");
        String username = vars.getFirst("username");
        String password = vars.getFirst("password");

        TrecOauthClient client = validateClient(clientId, redirectUrl, null, false);

        TrecAccount  account = accountService.logInEmail(username, password);
        if(account == null)
            account = accountService.logInUsername(username, password);

        if(account == null)
        {
            // To-Do: authentication failed. Handle it

            return;
        }
        Cookie cook = new Cookie("JSESSIONID", jwtService.generateSession(account, client, null));
        cook.setHttpOnly(true);         // Not accessibe via JavaScript
        cook.setMaxAge(-1);             // Make it a Session Cookie
        response.addCookie(cook);

        response.addHeader("Location", String.format("/auth/oauth2/authorize?client_id=%s&redirect_url=%s",
                URLEncoder.encode(clientId, StandardCharsets.UTF_8),
                URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8)));
    }

    @GetMapping("/authorize")
    public void doAuthorize(@RequestParam Map<String, String> params, HttpServletResponse response)
    {
        String clientId = params.get("client_id");
        String redirectUrl = URLDecoder.decode(params.get("redirect_url"), StandardCharsets.UTF_8);
        TrecOauthClient client = validateClient(clientId, redirectUrl, null, false);

        String codeParam = redirectUrl.indexOf('?') == -1 ?

        response.addHeader("Location", String.format("%s%s", redirectUrl)));

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
