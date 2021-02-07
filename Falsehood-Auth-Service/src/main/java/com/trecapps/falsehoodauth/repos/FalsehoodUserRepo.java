package com.trecapps.falsehoodauth.repos;

import com.trecapps.falsehoodauth.models.FalsehoodUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface FalsehoodUserRepo extends JpaRepository<FalsehoodUser, Long> {

    @Query("select count(u) from FalsehoodUser u where u.credit > ?1")
    Long getUsersAboveCredit(Integer credit);

}
