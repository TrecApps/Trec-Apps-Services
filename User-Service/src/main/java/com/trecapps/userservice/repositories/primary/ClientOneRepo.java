package com.trecapps.userservice.repositories.primary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.trecapps.userservice.models.primary.TrecOauthClientOne;

public interface ClientOneRepo  extends JpaRepository<TrecOauthClientOne, Short>{

}
