package com.trecapps.linkservice.repos;

import com.trecapps.linkservice.models.TrustedSite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrustedSiteRepo extends JpaRepository<TrustedSite, String>
{
}
