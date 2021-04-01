package com.trecapps.pictures.repos;

import com.trecapps.pictures.models.PictureModel;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface PictureModelRepo extends JpaRepository<PictureModel, String>
{
    @Query("select pm from PictureModel pm where pm.submitter = :submitter")
    List<PictureModel> getPicturesBySubmitter(@Param("submitter")long submitter, Pageable page);

    @Query("select pm from PictureModel pm where pm.submitter = :submitter and pm:submitted >= :after and pm.submitted <= :before")
    List<PictureModel> getPicturesBySubmitterAndDate(@Param("submitter")long submitter, @Param("after")Date after,
                                                     @Param("before")Date before, Pageable page);

    @Query("select pm from PictureModel pm where pm.submitter = :submitter and pm.submitted <= :before")
    List<PictureModel> getPicturesBySubmitterAndBefore(@Param("submitter")long submitter,
                                                       @Param("before")Date before, Pageable page);

    @Query("select pm from PictureModel pm where pm.flagId = :flagId")
    PictureModel getPictureByFlagId(@Param("flagId")String flagId);

    @Query("select pm from PictureModel pm where pm.submitter = :submitter and pm.adultStatus = 0")
    List<PictureModel> getSafePicturesBySubmitter(@Param("submitter")long submitter, Pageable page);

    @Query("select pm from PictureModel pm where pm.submitter = :submitter and pm:submitted >= :after and pm.submitted <= :before and pm.adultStatus = 0")
    List<PictureModel> getSafePicturesBySubmitterAndDate(@Param("submitter")long submitter, @Param("after")Date after,
                                                         @Param("before")Date before, Pageable page);

    @Query("select pm from PictureModel pm where pm.submitter = :submitter and pm.submitted <= :before and pm.adultStatus = 0")
    List<PictureModel> getSafePicturesBySubmitterAndBefore(@Param("submitter")long submitter,
                                                           @Param("before")Date before, Pageable page);
}
