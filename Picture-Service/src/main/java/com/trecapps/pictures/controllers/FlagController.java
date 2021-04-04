package com.trecapps.pictures.controllers;

import com.netflix.discovery.converters.Auto;
import com.trecapps.pictures.models.PictureEntry;
import com.trecapps.pictures.models.PictureFlag;
import com.trecapps.pictures.services.PictureFlagService;
import com.trecapps.pictures.services.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/Flag")
public class FlagController {

    PictureFlagService fService;
    PictureService service;

    @Autowired
    FlagController(PictureFlagService fService,PictureService service)
    {
        this.service = service;
        this.fService = fService;
    }

    @PostMapping(value = "/submit", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void submitFlag(MultiValueMap<String, String> map, HttpServletResponse resp)
    {
        String details = map.getFirst("details");
        String type = map.getFirst("type");
        String severity = map.getFirst("severity");
        String picture = map.getFirst("picture");

        if(service.getPicture(picture) == null)
        {
            resp.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        byte bType = 0;
        byte bSev = severity == null ? 0 : Byte.parseByte(severity);

        switch(type.toLowerCase())
        {
            case "1":
            case "threat":
                bType = 1;
                break;
            case "2":
            case "harassment":
                bType = 2;
                break;
            case "3":
            case "violence":
                bType = 3;
                break;
            case "4":
            case "porn":
                bType = 4;
                break;
            case "5":

                bType = 5;
        }

        if(bSev > 5)
            bSev = 5;

        PictureFlag flag = new PictureFlag();
        flag.setResolved((byte)0);
        flag.setDetails(details);
        flag.setSeverity(bSev);
        flag.setType(bType);

        if(!"".equals(fService.submitFlag(flag, picture)))
            resp.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @GetMapping("/Auth/List")
    List<PictureFlag> getFlags(@RequestParam(defaultValue = "0", name = "page")int page,
                               @RequestParam(defaultValue = "20", name = "size")int size)
    {
        return fService.getUnresolvedFlags(page, size);
    }

    @GetMapping("/Auth/Image")
    PictureEntry getFlaggedImage(@RequestParam("id") String id)
    {
        return fService.getFlaggedImage(id);
    }

    @PutMapping(value = "/Auth/Resolve", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    void resolveFlag(MultiValueMap<String, String> map, HttpServletResponse resp)
    {
        String flag = map.getFirst("Flag");
        boolean uphold = Boolean.getBoolean(map.getFirst("Uphold"));

        String res = fService.resolveFlag(flag,uphold);

        if(!"".equals(res))
            resp.setStatus(HttpStatus.NOT_FOUND.value());
    }
}
