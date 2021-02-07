package com.trecapps.falsehoodauth.repos;

import com.trecapps.falsehoodauth.models.PublicFigure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PublicFigureRepo extends JpaRepository<PublicFigure, Long>
{
}
