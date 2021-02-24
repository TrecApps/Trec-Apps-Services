package com.trecapps.resources.controllers;

import com.trecapps.resources.models.InstitutionEntry;
import com.trecapps.resources.models.MediaOutletEntry;
import com.trecapps.resources.models.PublicFigureEntry;
import com.trecapps.resources.models.RegionEntry;
import com.trecapps.resources.services.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/Update")
public class UpdateController {

    UpdateService updateService;

    @Autowired
    public UpdateController(UpdateService updateService)
    {
        this.updateService = updateService;
    }

    @PostMapping("/PublicFigure")
    ResponseEntity<String> addPublicFigure(PublicFigureEntry entry)
    {
        String ret = updateService.submitPublicFigure(entry, false);
        if("".equals(ret))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/PublicFigure")
    ResponseEntity<String> updatePublicFigure(PublicFigureEntry entry)
    {
        String ret = updateService.submitPublicFigure(entry, true);
        if("".equals(ret))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/MediaOutlet")
    ResponseEntity<String> addMediaOutlet(MediaOutletEntry entry)
    {
        String ret = updateService.submitMediaOutlet(entry, false);
        if("".equals(ret))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/MediaOutlet")
    ResponseEntity<String> updateMediaOutlet(MediaOutletEntry entry)
    {
        String ret = updateService.submitMediaOutlet(entry, true);
        if("".equals(ret))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/Region")
    ResponseEntity<String> addRegion(RegionEntry entry)
    {
        String ret = updateService.submitRegion(entry, false);
        if("".equals(ret))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/Region")
    ResponseEntity<String> updateRegion(RegionEntry entry)
    {
        String ret = updateService.submitRegion(entry, true);
        if("".equals(ret))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/Institution")
    ResponseEntity<String> addInstitution(InstitutionEntry entry)
    {
        String ret = updateService.submitInstitution(entry, false);
        if("".equals(ret))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/Institution")
    ResponseEntity<String> updateInstitution(InstitutionEntry entry)
    {
        String ret = updateService.submitInstitution(entry, true);
        if("".equals(ret))
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        return new ResponseEntity<>(ret, HttpStatus.BAD_REQUEST);
    }
}
