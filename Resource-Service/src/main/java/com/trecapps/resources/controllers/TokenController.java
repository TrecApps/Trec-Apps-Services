package com.trecapps.resources.controllers;

import com.trecapps.resources.security.OauthToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

@Controller
public class TokenController
{
    String clientId;
    String clientSecret;

    RestTemplate template;

    @Autowired
    public TokenController(@Value("spring.security.oauth2.client.registration.custom-client.client-id") String clientId,
                           @Value("spring.security.oauth2.client.registration.custom-client.client-secret") String clientSecret,
                           RestTemplate template)
    {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.template = template;
    }


    @PostMapping("/tokenize")
    public String tokenize(@RequestBody String code,
                         HttpServletResponse resp) throws IOException {
        Map<String, String> requestBody = new TreeMap<>();
        requestBody.put("code", code);
        requestBody.put("client_id", clientId);
        requestBody.put("client_secret", clientSecret);


        ResponseEntity<OauthToken> response = template.postForEntity("/oauth2/token", requestBody,OauthToken.class);
        var status = response.getStatusCode();
        if(status.is2xxSuccessful())
        {
            OauthToken token = response.getBody();
            Cookie cook = new Cookie("JSESSIONID", token.getAccess_token());
            cook.setMaxAge(0);
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
