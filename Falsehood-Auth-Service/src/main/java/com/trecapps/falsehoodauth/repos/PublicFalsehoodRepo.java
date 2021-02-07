package com.trecapps.falsehoodauth.repos;

import com.trecapps.falsehoodauth.models.PublicFalsehood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface PublicFalsehoodRepo extends JpaRepository<PublicFalsehood, BigInteger> {
    

    
}
