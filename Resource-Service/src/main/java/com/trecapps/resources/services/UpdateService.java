package com.trecapps.resources.services;

import com.trecapps.resources.models.*;
import com.trecapps.resources.repos.InstitutionRepo;
import com.trecapps.resources.repos.MediaOutletRepo;
import com.trecapps.resources.repos.PublicFigureRepo;
import com.trecapps.resources.repos.RegionRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UpdateService {

    FalsehoodStorageHolder falsehoodStorageHolder;

    PublicFigureRepo publicFigureRepo;
    InstitutionRepo institutionRepo;
    RegionRepo regionRepo;
    MediaOutletRepo mediaOutletRepo;

    @Autowired
    public UpdateService(FalsehoodStorageHolder falsehoodStorageHolder, PublicFigureRepo publicFigureRepo,
                         InstitutionRepo institutionRepo, RegionRepo regionRepo, MediaOutletRepo mediaOutletRepo)
    {
        this.falsehoodStorageHolder = falsehoodStorageHolder;
        this.publicFigureRepo = publicFigureRepo;
        this.institutionRepo = institutionRepo;
        this.mediaOutletRepo = mediaOutletRepo;
        this.regionRepo = regionRepo;
    }

    public String submitPublicFigure(PublicFigureEntry publicFigure, boolean isUpdate)
    {
        if(publicFigure == null)
        {
            return "Public Figure Entry was null";
        }
        PublicFigure pFigure = publicFigure.getFigure();
        if(pFigure == null)
        {
            return "Public Figure Metadata was null";
        }

        if(publicFigure.getText() == null)
        {
            return "Public Figure text was null";
        }

        if(isUpdate)
        {
            if(pFigure.getId() == null)
                return "Update Requires ID";
        }
        else
        {
            pFigure.setId(null);
        }

        pFigure = publicFigureRepo.save(pFigure);

        if(!"Success".equals(falsehoodStorageHolder.addNewFile("PublicFigure-" + pFigure.getId(), publicFigure.getText())))
        {
            if(!isUpdate)
               publicFigureRepo.delete(pFigure);
            return "Failed to Save Public Figure to Storage!";
        }

        return "";
    }

    public String submitInstitution(InstitutionEntry i, boolean isUpdate)
    {
        if(i == null)
            return "Null value detected";

        if(i.getContents() == null)
            return "Null contents detected";
        Institution inst = i.getInstitution();
        if(inst == null)
            return "Null metadata detected";

        if(isUpdate)
        {
            if(inst.getId() == null)
                return "Update Requires ID";
        }
        else
        {
            inst.setId(null);
        }

        inst = institutionRepo.save(inst);

        if(!"Success".equals(falsehoodStorageHolder.addNewFile("Institution-" + inst.getId(), i.getContents())))
        {
            if(!isUpdate)
                institutionRepo.delete(inst);
            return "Failed to save to storage!";
        }
        return "";
    }

    public String submitRegion(RegionEntry r, boolean isUpdate)
    {
        if(r == null)
            return "Null value detected";
        Region reg = r.getRegion();
        if(r.getContents() == null)
            return "Null contents detected";
        if(reg == null)
            return "Null metadata detected";

        if(isUpdate)
        {
            if(reg.getId() == null)
                return "Update Requires ID";
        }
        else
        {
            reg.setId(null);
        }

        reg = regionRepo.save(r.getRegion());

        if(!"Success".equals(falsehoodStorageHolder.addNewFile("Region-" + reg.getId(), r.getContents())))
        {
            if(!isUpdate)
                regionRepo.delete(reg);
            return "Failed to save to storage!";
        }
        return "";
    }

    public String submitMediaOutlet(MediaOutletEntry outletEntry, boolean isUpdate)
    {
        if(outletEntry == null)
        {
            return "Outlet Entry was null";
        }
        MediaOutlet outlet = outletEntry.getOutlet();
        if(outlet == null)
        {
            return "Outlet Metadata was null";
        }

        if(outletEntry.getText() == null)
        {
            return "Outlet text was null";
        }

        if(isUpdate)
        {
            if(outlet.getOutletId() == null)
                return "Update Requires ID";
        }
        else
        {
            outlet.setOutletId(null);
        }
        outlet = mediaOutletRepo.save(outlet);

        if(!"Success".equals(falsehoodStorageHolder.addNewFile("MediaOutlet-" + outlet.getOutletId(), outletEntry.getText())))
        {
            if(!isUpdate)
                mediaOutletRepo.delete(outlet);
            return "Failed to Save Media Outlet to Storage!";
        }

        return "";
    }

}
