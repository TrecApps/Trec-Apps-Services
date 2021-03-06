package com.trecapps.falsehoodauth.repos;

import com.trecapps.falsehoodauth.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepo extends JpaRepository<Region, Long>
{
	@Query("select r from Region r where r.name like %:name%")
	List<Region> getLikeName(String name);
}
