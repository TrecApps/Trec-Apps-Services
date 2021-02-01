package com.trecapps.resources.services;

import com.trecapps.resources.models.*;
import com.trecapps.resources.repos.InstitutionRepo;
import com.trecapps.resources.repos.MediaOutletRepo;
import com.trecapps.resources.repos.PublicFigureRepo;
import com.trecapps.resources.repos.RegionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class RetrievalService {

    InstitutionRepo iRepo;

    RegionRepo rRepo;

    MediaOutletRepo oRepo;

    PublicFigureRepo pRepo;

    FalsehoodStorageHolder storage;

    @Autowired
    RetrievalService(InstitutionRepo iRepo, RegionRepo rRepo, MediaOutletRepo oRepo, PublicFigureRepo pRepo, FalsehoodStorageHolder storage)
    {
        this.iRepo = iRepo;
        this.oRepo = oRepo;
        this.pRepo = pRepo;
        this.rRepo = rRepo;
        this.storage = storage;
    }

    public List<Institution> getInstitutionList(String name)
    {
        return iRepo.getLikeName(name);
    }
    public InstitutionEntry getInstitution(long id)
    {
        if(!iRepo.existsById(id))
        {
            System.out.println("Repository " + id + " Doesn't Exist!");
            return null;
        }
        Institution i = iRepo.getOne(id);
        String s;
        try {
            s = storage.retrieveContents("Institution-" + id);
        } catch (IOException e) {
            s = "ERROR: " + e.getMessage();
        }
        return new InstitutionEntry(i,s);
    }

    public List<Region> getRegionList(String name)
    {
        return rRepo.getLikeName(name);
    }

    public RegionEntry getRegion(long id)
    {
        if(!rRepo.existsById(id))
            return null;

        Region i = rRepo.getOne(id);
        String s;
        try {
            s = storage.retrieveContents("Region-" + id);
        } catch (IOException e) {
            s = "ERROR: " + e.getMessage();
        }
        return new RegionEntry(i,s);
    }

    public List<PublicFigure> getPublicFigure(String entry)
    {
        String names[] = entry.replace('_', ' ').trim().split(" ");

        if(names.length == 0)
        {
            return null;
        }
        if(names.length == 1)
        {
            return pRepo.findLikeName(names[0]);
        }
        if(names.length == 2)
        {
            return pRepo.findLikeName(names[0], names[1]);
        }
        String middle = "";
        for(int rust = 1; rust < names.length -1; rust++)
        {
            middle += names[rust] + " ";
        }

        return pRepo.findLikeName(names[0], middle.trim(), names[names.length-1]);
    }

    public PublicFigureEntry getPublicFigure(Long id)
    {
        try {
            return new PublicFigureEntry(pRepo.getOne(id),storage.retrieveContents("PublicFigure-" + id));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public List<MediaOutlet> getMediaOutlets(String name) {
        return oRepo.getOutletLikeName(name);
    }
    public MediaOutletEntry getMediaOutlet(Integer id)
    {
        try {
            return new MediaOutletEntry( oRepo.getOne(id),storage.retrieveContents("MediaOutlet-" + id));
        } catch(IOException e)
        {
            return null;
        }
    }
}
