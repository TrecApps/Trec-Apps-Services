package com.trecapps.pictures.repos;

import com.trecapps.pictures.models.PictureFlag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PictureFlagRepo extends JpaRepository<PictureFlag, String>
{
}
