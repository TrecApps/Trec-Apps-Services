package com.trecapps.userservice.controllers;

import com.trecapps.userservice.models.OauthToken;
import com.trecapps.userservice.models.UserInfo;
import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.models.primary.TrecOauthClient;
import com.trecapps.userservice.security.TrecAuthentication;
import com.trecapps.userservice.services.JwtTokenService;
import com.trecapps.userservice.services.TrecAccountService;
import com.trecapps.userservice.services.TrecOauthClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
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
        response.setStatus(HttpServletResponse.SC_FOUND);
    }

    @GetMapping("/authorize")
    public void doAuthorize(@RequestParam Map<String, String> params, HttpServletResponse response, Authentication user)
    {
        TrecAccount account = ((TrecAuthentication)user).getAccount();

        String clientId = params.get("client_id");
        String redirectUrl = URLDecoder.decode(params.get("redirect_url"), StandardCharsets.UTF_8);
        TrecOauthClient client = validateClient(clientId, redirectUrl, null, false);

        String code = jwtService.generateOneTimeCode(account, client);
        if(code == null)
        {
            // To-Do: Handle Error
            return;
        }

        char appender = redirectUrl.indexOf('?') == -1 ? '?' : '&';

        response.addHeader("Location", String.format("%s%ccode=%s", redirectUrl, appender, code));
        response.setStatus(HttpServletResponse.SC_FOUND);
    }

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public OauthToken getToken(@RequestBody MultiValueMap<String, String> request)
    {
        OauthToken ret = jwtService.verifyOneTimeCode(
                request.getFirst("code"),
                request.getFirst("client_id"),
                request.getFirst("client_secret"));
        if(ret == null)
        {
            // handle error
        }
        return ret;
    }

    @GetMapping("/userinfo")
    public UserInfo getUserInfo(HttpServletRequest req)
    {
        String auth = req.getHeader("Authorization");
        if(auth.startsWith("Bearer"))
            auth = auth.substring(6).trim();
        TrecAccount acc = jwtService.verifyToken(auth);
        if(acc == null)
            return null;

        UserInfo ret = new UserInfo();

        ret.setEmail(acc.getMainEmail());
        ret.setFamily_name(acc.getLastName());
        ret.setGiven_name(acc.getFirstName());
        ret.setName(acc.getFirstName() + " " + acc.getLastName());
        ret.setPreferred_username(acc.getUsername());
        ret.setEmail_verified((acc.getIsValidated() % 2) > 0);
        ret.setSub(acc.getAccountId());

        var auths = acc.getAuthorities();

        String roles = "";
        boolean added = false;
        for(GrantedAuthority ga: auths)
        {
            if(added)
                roles += ";";
            roles += ga.getAuthority();
        }
        ret.setRoles(roles);
        return ret;

    }

    @GetMapping("/logout")
    public void doLogOut(@RequestParam Map<String, String> params, HttpServletResponse response)
    {
        Cookie cook = new Cookie("JSESSIONID", "");
        cook.setMaxAge(0);
        cook.setHttpOnly(true);
        response.addCookie(cook);
    }

}
