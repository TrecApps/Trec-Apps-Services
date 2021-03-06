package com.trecapps.falsehoodauth.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class TrecOauthClientRepository  implements SecurityContextRepository {

    String cookieName;

    RestTemplate template;

    String userInfoUrl;

    AuthoritiesExtractor authExtractor;

    PrincipalExtractor principalExtractor;

    @Autowired
    TrecOauthClientRepository(@Value("${trec.session.cookie:JSESSIONID}")String cookieName,
                              @Value("${trec.session.auth.base}")String authBase,
                              @Value("${trec.session.auth.userinfo:auth/oauth2/userinfo}")String authEnd,
                              RestTemplate template,
                              AuthoritiesExtractor authExtractor,
                              PrincipalExtractor principalExtractor)
    {
        this.cookieName = cookieName;
        this.template = template;
        if(!authBase.endsWith("/") && !authEnd.startsWith("/"))
            this.userInfoUrl = authBase + "/" + authEnd;
        else
            this.userInfoUrl = authBase + authEnd;
        this.authExtractor = authExtractor;
        this.principalExtractor = principalExtractor;
    }


    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest req = requestResponseHolder.getRequest();
        SecurityContext ret = getContextFromCookie(req);
        if(ret == null)
            return SecurityContextHolder.createEmptyContext();
        return ret;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {

    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return false;
    }

    SecurityContext getContextFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        if (cookies == null) {
            return context;
        }
        for (Cookie c : cookies) {
            if (c.getName().equals(cookieName))
            {
                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(c.getValue());
                HttpEntity entity = new HttpEntity(headers);
                try
                {
                    ResponseEntity<UserInfo> response = template.exchange(this.userInfoUrl, HttpMethod.GET, entity, UserInfo.class );

                    UserInfo obj = response.getBody();

                    Map<String, Object> userAtts = new TreeMap<>();

                    userAtts.put("email", obj.email);
                    userAtts.put("verified",obj.email_verified);
                    userAtts.put("roles",obj.roles);
                    userAtts.put("last_name", obj.family_name);
                    userAtts.put("first_name", obj.given_name);
                    userAtts.put("sub", obj.sub);
                    userAtts.put("preferred_username", obj.name);

                    Object user = principalExtractor.extractPrincipal(userAtts);
                    List<GrantedAuthority> roles = authExtractor.extractAuthorities(userAtts);
                    if(user instanceof UserDetails && roles != null)
                    {
                        UserDetails userDetails = (UserDetails) user;
                        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, null, roles);
                        context.setAuthentication(token);
                    }


                }catch(RestClientResponseException ex)
                {
                    System.out.println(ex.getRawStatusCode() + " " + ex.getMessage());
                }
            }
        }

        return context;
    }
}
