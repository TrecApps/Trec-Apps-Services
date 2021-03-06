package com.trecapps.userservice.security;

import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.services.JwtTokenService;
import com.trecapps.userservice.services.TrecAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TrecSecurityContext implements SecurityContextRepository {

    @Autowired
    JwtTokenService jwtService;

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
        Cookie cook;
        if(!(context.getAuthentication() instanceof TrecAuthentication))
        {
            cook = new Cookie("JSESSIONID", null);
            cook.setHttpOnly(true);
            cook.setMaxAge(0);
        }
        else
        {
            TrecAuthentication trecAuth = (TrecAuthentication) context.getAuthentication();

            // Cookie will have been set by the endpoint!
            if(!trecAuth.isRegularSession())
                return;

            if (trecAuth == null) {
                cook = new Cookie("JSESSIONID", null);
                cook.setMaxAge(0);
            } else {
                cook = new Cookie("JSESSIONID", jwtService.generateToken(trecAuth.getAccount()));
                cook.setMaxAge(-1);

            }
        }
        cook.setHttpOnly(true);
        cook.setPath("/");
        response.addCookie(cook);


    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return getContextFromCookie(request) != null;
    }

    SecurityContext getContextFromCookie(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        if(cookies == null) {
            return context;
        }
        for(Cookie c: cookies)
        {
            String name = c.getName();
            if(name.equals("JSESSIONID") || name.equals("OSESSIONID"))
            {
                String data = c.getValue();
                TrecAccount acc = jwtService.verifyToken(data);
                if(acc == null)
                    return context;

                TrecAuthentication tAuth = new TrecAuthentication(acc);
                if(name.equals("OSESSIONID")) {
                    tAuth.setRegularSession(false);
                }
                context.setAuthentication(tAuth);
                return context;
            }
        }

        return context;
    }

}
