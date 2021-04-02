package com.trecapps.pictures.services;

import com.trecapps.pictures.models.PictureEntry;
import com.trecapps.pictures.models.PictureModel;
import com.trecapps.pictures.repos.PictureModelRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Date;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Service
public class PictureService
{
    PictureModelRepo repo;

    PictureStorageHolder storage;

    @Autowired
    public PictureService(PictureModelRepo repo, PictureStorageHolder storage)
    {
        this.repo = repo;
        this.storage = storage;
    }

    List<PictureModel> getPictures(boolean safe, long id, Date before, Date after, int pageNum, int size)
    {
        Pageable page = PageRequest.of(pageNum, size);
        List<PictureModel> ret;
        if(safe)
        {
            if(before == null)
            {
                if(after == null)
                    ret = repo.getSafePicturesBySubmitter(id, page);
                else
                    ret = repo.getSafePicturesBySubmitterAndDate(id, after, new Date(Calendar.getInstance().getTime().getTime()), page);
            }
            else
            {
                if(after == null)
                    ret = repo.getSafePicturesBySubmitterAndBefore(id, before, page);
                else ret = repo.getSafePicturesBySubmitterAndDate(id, after, before, page);
            }
        }
        else
        {
            if(before == null)
            {
                if(after == null)
                    ret = repo.getPicturesBySubmitter(id, page);
                else
                    ret = repo.getPicturesBySubmitterAndDate(id, after, new Date(Calendar.getInstance().getTime().getTime()), page);
            }
            else
            {
                if(after == null)
                    ret = repo.getPicturesBySubmitterAndBefore(id, before, page);
                else ret = repo.getPicturesBySubmitterAndDate(id, after, before, page);
            }
        }
        return ret;
    }

    PictureEntry getBaseImage(String id)
    {
        PictureEntry ret = new PictureEntry();
        Optional<PictureModel> oMod = repo.findById(id);

        if(oMod.isEmpty())
        {
            ret.setData("ERROR: Not Found!");
            return ret;
        }

        try
        {
            ret.setData(storage.retrieveContents(id));
            ret.setModel(oMod.get());
        }
        catch(IOException e)
        {
            ret.setData(e.getMessage());
        }
        return ret;
    }

    String submitPicture(PictureEntry entry)
    {
        PictureModel mod = entry.getModel();

        if(mod == null)
            return "CLIENT: Null model detected!";
        if(entry.getModel() == null)
            return "CLIENT: No Picture Data Provided!";

        // To-Do: Once picture scanning software is available, scan the data

        mod = repo.save(mod);

        if(mod.getId() == null)
            return "Failed to properly save picture model!";

        String result = storage.addNewFile(mod.getId(), entry.getData());


        if("Success".equalsIgnoreCase(result))
            return "";

        repo.delete(mod);
        return result;
    }

    String removePicture(long id, String pictureId)
    {
        if(pictureId == null)
            return "Null Picture Id Provided!";

        storage.removeFile(pictureId);
        Optional<PictureModel> pic = repo.findById(pictureId);
        pic.ifPresent((PictureModel mod) -> repo.delete(mod));
        return "";
    }
}
