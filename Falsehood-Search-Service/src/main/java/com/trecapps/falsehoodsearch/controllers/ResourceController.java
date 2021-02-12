package com.trecapps.falsehoodsearch.controllers;

import com.trecapps.falsehoodsearch.models.*;
import com.trecapps.falsehoodsearch.services.MediaOutletService;
import com.trecapps.falsehoodsearch.services.PublicAttributeService;
import com.trecapps.falsehoodsearch.services.PublicFalsehoodService;
import com.trecapps.falsehoodsearch.services.PublicFigureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Resources")
public class ResourceController
{
    PublicFalsehoodService service;

    PublicAttributeService attService;

    PublicFigureService pfService;

    MediaOutletService moService;

    @Autowired
    public ResourceController(@Autowired PublicFalsehoodService service,
                              @Autowired PublicAttributeService attService,
                              @Autowired PublicFigureService pfService,
                              @Autowired MediaOutletService moService)
    {
        this.service = service;
        this.attService = attService;
        this.pfService = pfService;
        this.moService = moService;
    }

    @GetMapping("/Regions/{name}")
    public List<Region> getRegionsBySearchTerm(@PathVariable("name") String name)
    {
        return attService.getRegionList(name.replace('_', ' ').trim());
    }

    @GetMapping("/Institutions/{name}")
    public List<Institution> getInstitutionBySearchTerm(@PathVariable("name") String name)
    {
        return attService.getInstitutionList(name.replace('_', ' ').trim());
    }

    @GetMapping("/Region/{id}")
    public RegionEntry getRegionsById(@PathVariable("id") Long id)
    {
        return attService.getRegion(id);
    }

    @GetMapping("/Institution/{id}")
    public InstitutionEntry getInstitutionById(@PathVariable("id") Long id)
    {
        return attService.getInstitution(id);
    }

    @GetMapping("/PublicFigures/{name}")
    public List<PublicFigure> getFigures(@PathVariable("name")String name)
    {
        return pfService.getPublicFigure(name);
    }

    @GetMapping("/PublicFigure/{id}")
    public PublicFigureEntry getPublicFigure(@PathVariable("id")Long id)
    {
        return pfService.getEntryById(id);

    }

    @GetMapping("/outlet/{name}")
    public List<MediaOutlet> searchOutlets(@PathVariable("name")String name)
    {
        return moService.SearchOutletByName(name.replace('_', ' ').trim());
    }

    @GetMapping("/outletId/{id}")
    public MediaOutletEntry getOutlet(@PathVariable("id")Integer id)
    {
        return moService.GetOutletEntry(id);
    }
}
