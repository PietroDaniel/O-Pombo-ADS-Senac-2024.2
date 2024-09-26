package com.pombo.pombo.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.pombo.pombo.model.entity.Denuncia;
import com.pombo.pombo.model.entity.Pruu;

import java.util.List;
import java.util.UUID;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, UUID> {

    List<Denuncia> findByPruu_Uuid(UUID pruuUuid);
    List<Denuncia> findByPruuAndSituacao(Pruu pruu, Denuncia.SituacaoDenuncia situacao);
}