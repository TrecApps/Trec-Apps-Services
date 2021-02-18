package com.trecapps.userservice.security;

import com.trecapps.userservice.models.primary.TrecAccount;
import com.trecapps.userservice.services.JwtTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
            System.out.println("Not TrecAuthentication");
            cook = new Cookie("JSESSIONID", null);
            cook.setHttpOnly(true);
            cook.setMaxAge(0);
        }
        else
        {
            System.out.println("TrecAccount Detected!");
            TrecAuthentication trecAuth = (TrecAuthentication) context.getAuthentication();

            if (trecAuth == null) {
                System.out.println("TrecAccount was NULL");
                cook = new Cookie("JSESSIONID", null);
                cook.setHttpOnly(true);
                cook.setMaxAge(0);
            } else {
                cook = new Cookie("JSESSIONID", jwtService.generateToken(trecAuth.getAccount()));
                cook.setHttpOnly(true);
                cook.setMaxAge(-1);
            }
        }
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
            System.out.println("Null Cookies detected");
            return context;
        }
        for(Cookie c: cookies)
        {
            if(c.getName().equals("JSESSIONID"))
            {
                String data = c.getValue();
                TrecAccount acc = jwtService.verifyToken(data);
                if(acc == null)
                    return context;


                context.setAuthentication(new TrecAuthentication(acc));
                return context;
            }
        }

        return context;
    }
}
