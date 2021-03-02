package com.trecapps.resources.security;

import org.springframework.boot.autoconfigure.security.oauth2.resource.AuthoritiesExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class TrecAuthorityExtractor implements AuthoritiesExtractor
{
    @Override
    public List<GrantedAuthority> extractAuthorities(Map<String, Object> map) {

        List<GrantedAuthority> ret = new ArrayList<>();

        String roles = map.get("roles").toString();

        for(String role: roles.split(";"))
        {
            ret.add(new TrecAuthority(role));
        }

        return ret;
    }
}
