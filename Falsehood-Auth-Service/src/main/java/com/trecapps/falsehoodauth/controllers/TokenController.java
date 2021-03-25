package com.trecapps.falsehoodauth.controllers;

import com.trecapps.falsehoodauth.security.OauthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class TokenController
{
    String clientId;
    String clientSecret;

    RestTemplate template;

    @Autowired
    public TokenController(@Value("${spring.security.oauth2.client.registration.custom-client.client-id}") String clientId,
                           @Value("${spring.security.oauth2.client.registration.custom-client.client-secret}") String clientSecret,
                           RestTemplate template)
    {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.template = template;
    }


    @PostMapping("/tokenize")
    public String tokenize(@RequestBody String code,
                         HttpServletResponse resp) throws IOException {
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("code", code);
        requestBody.add("client_id", clientId);
        requestBody.add("client_secret", clientSecret);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<OauthToken> response = template.postForEntity("http://localhost:8082/auth/oauth2/token", entity ,OauthToken.class);
        var status = response.getStatusCode();
        if(status.is2xxSuccessful())
        {
            OauthToken token = response.getBody();
            Cookie cook = new Cookie("JSESSIONID", token.getAccess_token());
            cook.setMaxAge(-1);
            cook.setHttpOnly(true);
            resp.addCookie(cook);
            return token.getRefresh_token();
        }
        else
        {
            resp.setStatus(status.value());
            return null;
        }
    }
}
