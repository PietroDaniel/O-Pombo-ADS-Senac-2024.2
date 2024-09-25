package com.pombo.pombo.model.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.model.entity.Pruu;

@Repository
public interface PruuRepository extends JpaRepository<Pruu, String>, JpaSpecificationExecutor<Pruu> {

    Optional<Denuncia> findByUuid(UUID pruuUuid);

    List<Pruu> findAll();

    Pruu save(Pruu novoPruu);

    List<Pruu> findAll(Object object);

    Optional<Pruu> findByUuid(String uuid);

    Optional<Pruu> findById(String uuid);
}
