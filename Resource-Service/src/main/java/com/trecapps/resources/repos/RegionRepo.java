package com.trecapps.resources.repos;

import java.util.List;

import com.trecapps.resources.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface RegionRepo extends JpaRepository<Region, Long>
{
	@Query("select r from Region r where r.name like %:name%")
	List<Region> getLikeName(String name);
}
