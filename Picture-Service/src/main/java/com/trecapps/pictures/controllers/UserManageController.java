package com.trecapps.pictures.controllers;

import com.trecapps.pictures.models.PictureEntry;
import com.trecapps.pictures.models.PictureModel;
import com.trecapps.pictures.security.UserInfo;
import com.trecapps.pictures.services.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Calendar;

@Controller
@RequestMapping("/User")
public class UserManageController {

    PictureService service;

    @Autowired
    UserManageController(PictureService service1)
    {
        service = service1;
    }

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

    private static String imageTypes[] = {
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE
    };

    @PostMapping(value = "/add", consumes = {
            MediaType.IMAGE_GIF_VALUE,
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE
    }, produces = MediaType.TEXT_PLAIN_VALUE)
    String addImage(@RequestBody byte[] data, HttpServletRequest req, HttpServletResponse resp, Authentication auth,
                  @RequestParam(defaultValue = "0", name = "adult")byte adult)
    {
        byte isAdult = 0;
        switch(adult)
        {
            case 18:
                isAdult = 0b00000001;
                break;
            case -18:
                isAdult = 0b00010001;
                break;
            case 21:
                isAdult = 0b00000010;
                break;
            case -21:
                isAdult = 0b00010010;
            case 0:
                break;
            default:
                resp.setStatus(HttpStatus.BAD_REQUEST.value());
                return "Adult attribute not set properly!";
        }
        try
        {
            UserInfo info = (UserInfo) auth.getPrincipal();
            long userId = info.getSub();


            PictureEntry ent = new PictureEntry();
            PictureModel mod = new PictureModel();

            mod.setSafe((byte)0);
            mod.setExtension(req.getContentType());
            mod.setSubmitted(new Date(Calendar.getInstance().getTime().getTime()));
            mod.setAdultStatus(isAdult);
            mod.setSubmitter(userId);

            ent.setModel(mod);

            ent.setData(Base64.getEncoder().encodeToString(data));

            String ret = service.submitPicture(ent);

            if(!"".equals(ret))
                resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ret;
        }catch(Exception e)
        {
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            return "Needs Authentication!";
        }

    }

    @DeleteMapping("/Remove")
    String remove(@RequestParam("id") String id, HttpServletResponse resp, Authentication auth)
    {
        PictureEntry ent = service.getPicture(id);
        if(ent == null || ent.getModel() == null)
        {
            resp.setStatus(HttpStatus.NOT_FOUND.value());
            return "Picture not found!";
        }
        PictureModel mod = ent.getModel();
        try {
            UserInfo info = (UserInfo) auth.getPrincipal();
            long userId = info.getSub();

            if(userId != mod.getSubmitter())
            throw new Exception("Wrong User!");

            service.removePicture(userId, id);
        }
        catch(Exception e)
        {
            resp.setStatus(HttpStatus.UNAUTHORIZED.value());
            return "Needs Authentication!";
        }

        resp.setStatus(HttpStatus.NO_CONTENT.value());
        return null;
    }
}
