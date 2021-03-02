package com.trecapps.falsehoodauth.security;

import com.trecapps.falsehoodauth.models.FalsehoodUser;
import com.trecapps.falsehoodauth.repos.FalsehoodUserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrecPrincipalExtractor implements PrincipalExtractor
{
    @Autowired
    FalsehoodUserRepo repo;

    @Override
    public Object extractPrincipal(Map<String, Object> map) {

        Object id = map.get("sub");

        Long lId;
        if(id instanceof Integer)
        {
            int iId = (Integer)id;
            lId = (long)iId;
        }
        else if(id instanceof Long)
        {
            lId = (Long)id;
        }
        else if(id instanceof String)
        {
            lId = Long.getLong(((String) id));
        }
        else
            return null;

        FalsehoodUser user = null;
        if(repo.existsById(lId))
            user = repo.getOne(lId);
        else
        {
            user = new FalsehoodUser();
            user.setUserId(lId);
            user.setCredit(5);
            user.setUsername((String)map.get("preferred_username"));
            user.setEmail((String)map.get("email"));
            user = repo.save(user);
        }

        return user;
    }
}
