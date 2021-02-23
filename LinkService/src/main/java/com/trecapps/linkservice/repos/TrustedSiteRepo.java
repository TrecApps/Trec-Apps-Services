package com.trecapps.linkservice.repos;

import com.trecapps.linkservice.models.TrustedSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrustedSiteRepo extends JpaRepository<TrustedSite, String>
{
    @Query("select ts from TrustedSite ts where ts.url = :url")
    List<TrustedSite> getSiteByUrl(@Param("url")String url);
}
