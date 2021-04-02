package com.trecapps.pictures.repos;

import com.trecapps.pictures.models.PictureFlag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PictureFlagRepo extends JpaRepository<PictureFlag, String>
{
    @Query("select pf from PictureFlag pf where pf.resolved = 0")
    List<PictureFlag> getUnresolvedFlags(Pageable page);
}
