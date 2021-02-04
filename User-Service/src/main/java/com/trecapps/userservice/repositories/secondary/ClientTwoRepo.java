package com.trecapps.userservice.repositories.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trecapps.userservice.models.secondary.TrecOauthClientTwo;

public interface ClientTwoRepo extends JpaRepository<TrecOauthClientTwo, Short>{

}
