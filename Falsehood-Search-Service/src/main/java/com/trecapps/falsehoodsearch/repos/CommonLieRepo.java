package com.trecapps.falsehoodsearch.repos;

import java.util.List;

import com.trecapps.falsehoodsearch.models.CommonLie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CommonLieRepo extends JpaRepository<CommonLie, Long>
{
	@Query("select cl from CommonLie cl where ?1 like cl.title")
	List<CommonLie> getCommonLieByTitleStart(String title);
	
	
}
