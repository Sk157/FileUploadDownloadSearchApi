package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.model.Doc;

public interface DocRepository  extends JpaRepository<Doc,Integer>{
	@Query(value ="SELECT * FROM docs  where doc_name like %:keyword%",nativeQuery = true )
	  public List<Doc> findByKeyword(@Param("keyword")String keyword);

}
