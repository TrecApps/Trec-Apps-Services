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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Controller
@RequestMapping("/oauth2")
public class Oauth2Controller {

    TrecOauthClientService oauthService;

    TrecAccountService accountService;

    JwtTokenService jwtService;

    /**
     * Serves as the Representation of the login page
     */
    StringBuilder loginContents;

    @Autowired
    public Oauth2Controller(TrecOauthClientService oauthService, TrecAccountService accountService,
                            JwtTokenService jwtService) throws IOException {
        this.oauthService = oauthService;
        this.accountService = accountService;
        this.jwtService = jwtService;

        // need to read it into memory
        InputStream inStream = getClass().getResourceAsStream("/templates/login.jsp");


        // File file = ResourceUtils.getFile("classpath:templates/login.jsp");
        BufferedReader br = new BufferedReader(new InputStreamReader(inStream));
        loginContents = new StringBuilder();
        char[] buffer = new char[100];
        int ret = 0;
        do {
            ret = br.read(buffer);

            for(int rust = 0; rust < ret; rust++)
            {
                loginContents.append(buffer[rust]);
            }
        }
        while(ret != -1);

    }

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
    public void getLoginPage(@RequestParam Map<String, String> params, HttpServletResponse response) throws IOException {

        String copy = new String(loginContents);

        String clientId = params.get("client_id");
        String redirectUrl = params.get("redirect_url");
        String redirectUrl2 = params.get("redirect_url2");
        TrecOauthClient client = null;

        try {
            client = validateClient(clientId, redirectUrl, null, false);
        } catch(Exception e)
        {
            response.setStatus(404);
            response.setContentType("text/plain");
            response.getWriter().write("Could not find client with ID: " + clientId);
            return;
        }
        copy = copy.replace("${jsp.formAction}", "/api/auth/oauth2/login");
        copy = copy.replace("${jsp.message}", client.getName() + " Log In");
        copy = copy.replace("${jsp.client_id}", clientId);
        copy = copy.replace("${jsp.redirect_url}", URLEncoder.encode(redirectUrl,StandardCharsets.UTF_8));

        response.setStatus(200);
        response.setContentType("text/html; charset=UTF-8");
        response.getWriter().write(copy);

    }


    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public void attemptLogin(@RequestBody MultiValueMap<String, String> vars, HttpServletResponse response)
    {


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
            System.out.println("In login endpoint, auth failed!");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Cookie cook = new Cookie("OSESSIONID", jwtService.generateSession(account, client, null));
        cook.setHttpOnly(true);         // Not accessibe via JavaScript
        cook.setMaxAge(-1);             // Make it a Session Cookie
        response.addCookie(cook);

        System.out.println("Set cookie in post login oauth endpoint!");

        response.addHeader("Location", String.format("/api/auth/oauth2/authorize?client_id=%s&redirect_url=%s",
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
            System.out.println("FAILED to validate client in /authorize endpoint!");
            // To-Do: Handle Error
            return;
        }

        String appender = redirectUrl.indexOf('?') == -1 ? "?" : "&";

        response.addHeader("Location", String.format("%s%scode=%s",
                redirectUrl,
                appender,
                code));
        response.setStatus(HttpServletResponse.SC_FOUND);
    }

    @PostMapping(value = "/token", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces="application/json")
    public @ResponseBody OauthToken getToken(@RequestBody MultiValueMap<String, String> request)
    {
        System.out.println("Token Endpoint called!");
        OauthToken ret = jwtService.verifyOneTimeCode(
                request.getFirst("code"),
                request.getFirst("client_id"),
                request.getFirst("client_secret"));
        if(ret == null)
        {
            System.out.println("Error Detected in token endpoint!");
            // handle error

            //maybe it was a refresh token
            ret = jwtService.verifyAuthToken(request.getFirst("code"));
        }
        return ret;
    }

    @GetMapping("/userinfo")
    public @ResponseBody UserInfo getUserInfo(HttpServletRequest req, HttpServletResponse resp)
    {
        System.out.println("In UserInfo Endpoint!");
        String auth = req.getHeader("Authorization");
        if(auth.startsWith("Bearer"))
            auth = auth.substring(6).trim();
        TrecAccount acc = jwtService.verifyToken(auth);
        if(acc == null) {
            System.out.println("Failed to verify token!");
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            return null;
        }
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
        resp.setStatus(HttpStatus.OK.value());
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
