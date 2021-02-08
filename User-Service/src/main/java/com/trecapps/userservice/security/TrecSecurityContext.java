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

        Cookie[] cookies = request.getCookies();

        if(cookies == null)
            return ;
        Cookie cook = null;
        if(cookies != null)
            for(Cookie c: cookies)
            {
                if(c.getName().equals("JSESSIONID"))
                {
                    cook = c;
                    break;
                }
            }

        try {
            TrecAuthentication trecAuth = (TrecAuthentication) context.getAuthentication();
            if(cook != null)
            {
                cook.setValue(jwtService.generateSession(trecAuth.getAccount(), null, jwtService.getSessionId(cook.getValue())));

            }
            else
            {
                cook = new Cookie("JSESSIONID", jwtService.generateSession(trecAuth.getAccount(), null, null));
            }

            response.addCookie(cook);
        } catch(Exception e)
        {
            // To-Do: Log Exception
            if(cook != null)
                cook.setMaxAge(0);

            return;
        }


    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return getContextFromCookie(request) != null;
    }

    SecurityContext getContextFromCookie(HttpServletRequest request)
    {
        Cookie[] cookies = request.getCookies();

        if(cookies == null)
            return null;

        for(Cookie c: cookies)
        {
            if(c.getName().equals("JSESSIONID"))
            {
                String data = c.getValue();
                TrecAccount acc = jwtService.verifyToken(data);
                if(acc == null)
                    return null;
                SecurityContext context = SecurityContextHolder.createEmptyContext();

                context.setAuthentication(new TrecAuthentication(acc));
                return context;
            }
        }

        return null;
    }
}
