package com.trecapps.userservice.repositories.secondary;

import com.trecapps.userservice.models.secondary.InvalidSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidSessionRepo extends JpaRepository<InvalidSession, Long> {
}
