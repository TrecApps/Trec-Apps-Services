package com.trecapps.falsehoodsearch.controllers;

import com.trecapps.falsehoodsearch.models.Falsehood;
import com.trecapps.falsehoodsearch.models.FullFalsehood;
import com.trecapps.falsehoodsearch.models.FullPublicFalsehood;
import com.trecapps.falsehoodsearch.models.PublicFalsehood;
import com.trecapps.falsehoodsearch.services.FalsehoodService;
import com.trecapps.falsehoodsearch.services.PublicFalsehoodService;
import com.trecapps.falsehoodsearch.services.SearchFalsehood;
import com.trecapps.falsehoodsearch.services.SearchPublicFalsehood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/Falsehoods")
public class FalsehoodController
{
    PublicFalsehoodService pfService;

    FalsehoodService mfService;

    @Autowired
    public FalsehoodController(PublicFalsehoodService pfService, FalsehoodService mfService)
    {
        this.pfService = pfService;
        this.mfService = mfService;
    }

    @PostMapping("/Public/searchConfirmed")
    public List<PublicFalsehood> searchFalsehoodByParams(@RequestBody SearchPublicFalsehood searchObj)
    {
        return pfService.searchConfirmedFalsehoodsByAttribute(searchObj);
    }

    @PostMapping("/Public/searchRejected")
    public List<PublicFalsehood> searchRFalsehoodByParams(@RequestBody SearchPublicFalsehood searchObj)
    {
        return pfService.searchRejectedFalsehoodsByAttribute(searchObj);
    }

    @GetMapping("/Public/searchSubmitted")
    public List<PublicFalsehood> searchSubmittedPublicFalsehoods(@RequestParam(value="size", defaultValue="20", required=false)int size,
                                                           @RequestParam(value="page", defaultValue="0", required=false)int page)
    {
        return pfService.getSubmittedFalsehoods(size, page);
    }

    @GetMapping("/Public/id/{id}")
    public FullPublicFalsehood GetPublicFalsehood(@PathVariable("id") BigInteger id)
    {
        return pfService.getFalsehoodById(id);
    }

    @PostMapping("/Media/searchConfirmed")
    public List<Falsehood> searchFalsehoodByParams(@RequestBody SearchFalsehood searchObj)
    {
        return mfService.getConfirmedFalsehoodsBySearchFeatures(searchObj);
    }

    @PostMapping("/Media/searchRejected")
    public List<Falsehood> searchRFalsehoodByParams(@RequestBody SearchFalsehood searchObj)
    {
        return mfService.getRejectedFalsehoodsBySearchFeatures(searchObj);
    }

    @GetMapping("/Media/searchSubmitted")
    public List<Falsehood> searchSubmittedFalsehoods(@RequestParam(value="size", defaultValue="20", required=false)int size,
                                                           @RequestParam(value="page", defaultValue="0", required=false)int page)
    {
        return mfService.getSubmittedFalsehoods(size, page);
    }

    @GetMapping("/Media/id/{id}")
    public FullFalsehood GetFalsehood(@PathVariable("id") BigInteger id)
    {
        return mfService.getFalsehoodById(id);
    }

}
