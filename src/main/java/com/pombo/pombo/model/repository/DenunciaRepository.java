package com.pombo.pombo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pombo.pombo.model.entity.Denuncia;

@Repository
public interface DenunciaRepository extends JpaRepository<Denuncia, String> {

    List<Denuncia> findByPruuUuid(String pruuId);  
    //List<Denuncia> findByPruuUuidSituacao(String pruuId, Denuncia.SituacaoDenuncia situacao);
}