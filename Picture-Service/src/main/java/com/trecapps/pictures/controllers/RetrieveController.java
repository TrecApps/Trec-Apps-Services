package com.trecapps.pictures.controllers;

import com.trecapps.pictures.models.PictureEntry;
import com.trecapps.pictures.models.PictureModel;
import com.trecapps.pictures.security.UserInfo;
import com.trecapps.pictures.services.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.sql.Timestamp;

@RestController
@RequestMapping("/Retrieve")
public class RetrieveController {

    PictureService service;

    private static boolean isEighteen(Calendar cDate, Calendar bDate)
    {
        int years = cDate.get(Calendar.YEAR) - bDate.get(Calendar.YEAR);
        if(years < 18)
            return false;
        if (years > 18)
            return true;
        int months = cDate.get(Calendar.MONTH) - bDate.get(Calendar.MONTH);
        int days = cDate.get(Calendar.DAY_OF_MONTH) - bDate.get(Calendar.DAY_OF_MONTH);

        return (months > 0) || ((months == 0) && (days >= 0));
    }

    @Autowired
    RetrieveController(PictureService service1)
    {
        service = service1;
    }

    @GetMapping("/User/{id}")
    List<PictureModel> getPicturesByUser(@PathVariable("id")long userId,
                                         @RequestParam(required = false, defaultValue = "true",
                                                 name = "restricted") boolean restricted,
                                         @RequestParam(defaultValue = "0", name = "page")int page,
                                         @RequestParam(defaultValue = "20", name = "size")int size,
                                         @RequestParam(required = false, name = "before")java.sql.Date before,
                                         @RequestParam(required = false, name = "after")java.sql.Date after)
    {
        SecurityContext context = SecurityContextHolder.getContext();

        if(!restricted) {
            restricted = true;
            Authentication auth = context.getAuthentication();

            if (auth != null && auth.getPrincipal() instanceof UserInfo)
            {
                UserInfo uInfo = (UserInfo) auth.getPrincipal();

                Timestamp birthdate = uInfo.getBirthday();

                Calendar bDate = Calendar.getInstance();
                bDate.setTime(new Date(birthdate.getTime()));
                Calendar cDate = Calendar.getInstance();

                if(isEighteen(cDate, bDate))
                    restricted = false;
            }
        }
        return service.getPictures(restricted, userId, before, after, page, size);
    }

    String getPicture(String id, HttpServletResponse resp)
    {
        PictureEntry entry = service.getPicture(id);
        if(entry == null)
        {
            resp.setStatus(HttpStatus.NOT_FOUND.value());
            return null;
        }
        String ret = entry.getData();
        PictureModel mod = entry.getModel();
        byte adultStat = mod.getAdultStatus();
        if(adultStat > 0)
        {
            // To-Do: get information on the location of the user, if possible

            SecurityContext context = SecurityContextHolder.getContext();
            Authentication auth = context.getAuthentication();

            if (auth != null && auth.getPrincipal() instanceof UserInfo)
            {
                UserInfo uInfo = (UserInfo) auth.getPrincipal();

                Timestamp birthdate = uInfo.getBirthday();

                Calendar bDate = Calendar.getInstance();
                bDate.setTime(new Date(birthdate.getTime()));
                Calendar cDate = Calendar.getInstance();

                if(!isEighteen(cDate, bDate))
                {
                    ret = null;
                    resp.setStatus(HttpStatus.FORBIDDEN.value());
                }
            }
            else
            {
                ret = null;
                resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            }
        }

        return ret;
    }
}
