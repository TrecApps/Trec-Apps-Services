package com.trecapps.linkservice.repos;

import com.trecapps.linkservice.models.Link;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepo extends JpaRepository<Link, String> {
}
