package com.trecapps.userservice.repositories.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.trecapps.userservice.models.secondary.UserSalt;

@Repository
public interface UserSaltRepo extends JpaRepository<UserSalt, Long> {

}
