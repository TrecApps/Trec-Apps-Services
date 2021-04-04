package com.trecapps.pictures.services;

import com.trecapps.pictures.models.PictureEntry;
import com.trecapps.pictures.models.PictureFlag;
import com.trecapps.pictures.models.PictureModel;
import com.trecapps.pictures.repos.PictureFlagRepo;
import com.trecapps.pictures.repos.PictureModelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PictureFlagService {

    PictureModelRepo mRepo;
    PictureFlagRepo fRepo;
    PictureStorageHolder storage;

    @Autowired
    public PictureFlagService(PictureModelRepo mRepo, PictureFlagRepo fRepo, PictureStorageHolder storage)
    {
        this.storage = storage;
        this.mRepo = mRepo;
        this.fRepo = fRepo;
    }

    public String submitFlag(PictureFlag flag, String picId)
    {
        Optional<PictureModel> oMod = mRepo.findById(picId);

        if(oMod.isEmpty())
            return "Picture Not found!";

        PictureModel mod = oMod.get();

        flag = fRepo.save(flag);

        if(flag.getId() == null)
            return "Failed to process flag!";

        mod.setFlagId(flag.getId());

        mRepo.save(mod);

        // To-Do: Add means of alerting moderators once such a system becomes available

        return "";
    }

    public List<PictureFlag> getUnresolvedFlags(int pageNum, int size)
    {
        Pageable page = PageRequest.of(pageNum,size);

        return fRepo.getUnresolvedFlags(page);
    }

    public PictureEntry getFlaggedImage(String id)
    {
        PictureModel mod = mRepo.getPictureByFlagId(id);
        try
        {
            PictureEntry ent = new PictureEntry();
            ent.setData(storage.retrieveContents(mod.getId()));
            ent.setModel(mod);
            return ent;
        }
        catch(Exception e)
        {
            return null;
        }
    }

    public String resolveFlag(String flagId, boolean affirm)
    {
        Optional<PictureFlag> oFlag = fRepo.findById(flagId);

        if(oFlag.isEmpty())
            return "CLIENT ERROR: Flag Entry not found!";

        PictureFlag flag = oFlag.get();
        flag.setResolved((byte)1);
        fRepo.save(flag);

        if(!affirm)
        {
            PictureModel mod = mRepo.getPictureByFlagId(flagId);
            if(mod != null)
            {
                mod.setFlagId(null);
                mod.setSafe((byte)1);
                mRepo.save(mod);
            }
        }
        return "";
    }
}
