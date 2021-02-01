package com.trecapps.resources.controllers;

import com.trecapps.resources.models.*;
import com.trecapps.resources.services.RetrievalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class RetrievalController {

    RetrievalService service;

    @Autowired
    RetrievalController(RetrievalService service)
    {
        this.service = service;
    }

    // Public Figures
    @GetMapping("/publicFigures/{name}")
    public List<PublicFigure> getPublicFigures(@PathVariable("name")String name)
    {
        return service.getPublicFigure(name);
    }

    @GetMapping("/publicFigure/{id}")
    public PublicFigureEntry getPublicFigure(@PathVariable("id")Long id,
                                             @RequestParam(defaultValue = "0", required = false)int page,
                                             @RequestParam(defaultValue = "20", required = false)int pageSize)
    {
        return service.getPublicFigure(id);
    }

    // Media Outlets
    @GetMapping("/mediaOutlets/{name}")
    public List<MediaOutlet> getMediaOutlets(@PathVariable("name")String name)
    {
        return service.getMediaOutlets(name);
    }

    @GetMapping("/mediaOutlet/{id}")
    public MediaOutletEntry getMediaOutlet(@PathVariable("id")Integer id,
                                            @RequestParam(defaultValue = "0", required = false)int page,
                                            @RequestParam(defaultValue = "20", required = false)int pageSize)
    {
        return service.getMediaOutlet(id);
    }

    // Regions
    @GetMapping("/regions/{name}")
    public List<Region> getRegions(@PathVariable("name")String name)
    {
        return service.getRegionList(name);
    }

    @GetMapping("/region/{id}")
    public RegionEntry getRegion(@PathVariable("id")Integer id,
                                           @RequestParam(defaultValue = "0", required = false)int page,
                                           @RequestParam(defaultValue = "20", required = false)int pageSize)
    {
        return service.getRegion(id);
    }

    // Institution
    @GetMapping("/institutions/{name}")
    public List<Institution> getInstitutions(@PathVariable("name")String name)
    {
        return service.getInstitutionList(name);
    }

    @GetMapping("/institution/{id}")
    public InstitutionEntry getIntstitution(@PathVariable("id")Integer id,
                                 @RequestParam(defaultValue = "0", required = false)int page,
                                 @RequestParam(defaultValue = "20", required = false)int pageSize)
    {
        return service.getInstitution(id);
    }
}
