package com.trecapps.linkservice.repos;

import com.trecapps.linkservice.models.Link;
import com.trecapps.linkservice.models.TrustedSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepo extends JpaRepository<Link, String> {
    @Query("select l from Link l where l.endpoint = :endpoint and l.site = :trustedSite")
    List<Link> getLinkByEndpoint(@Param("endpoint")String endpoint,
                                 @Param("trustedSite")TrustedSite trustedSite);
}
