package com.pombo.pombo.model.repository;


import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pombo.pombo.model.entity.Pruu;

@Repository
public interface PruuRepository extends PagingAndSortingRepository<Pruu, String>, JpaRepository<Pruu, String>, JpaSpecificationExecutor<Pruu> {

	 Page<Pruu> findAll(Specification<Pruu> spec, Pageable pageable);


}
