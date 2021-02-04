package com.trecapps.userservice.repositories.primary;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.trecapps.userservice.models.primary.TrecAccount;

@Repository
public interface TrecAccountRepo extends JpaRepository<TrecAccount, Long> {

	@Query("select t from TrecAccount t where t.username = ?1")
	TrecAccount getTrecAccountByUsername(String username);
	
	@Query("select t from TrecAccount t where t.mainEmail = ?1")
	TrecAccount getTrecAccountByMainEmail(String mainEmail);
}
