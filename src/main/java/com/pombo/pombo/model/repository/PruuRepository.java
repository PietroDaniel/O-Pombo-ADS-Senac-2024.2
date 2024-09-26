package com.pombo.pombo.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pombo.pombo.model.entity.Pruu;

@Repository
public interface PruuRepository extends JpaRepository<Pruu, UUID>, JpaSpecificationExecutor<Pruu> {

    Optional<Pruu> findByUuid(UUID pruuUuid);

    List<Pruu> findAll(Object object);

    Optional<Pruu> findById(String uuid);
}
