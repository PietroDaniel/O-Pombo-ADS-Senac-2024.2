package com.pombo.pombo.model.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.seletor.PruuSeletor;

@Repository
public interface PruuRepository extends JpaRepository<Pruu, String>, JpaSpecificationExecutor<Pruu> {

	@Query("SELECT p FROM Pruu p WHERE p.usuario.id = :idUsuario ORDER BY p.dataHoraCriacao DESC")
	public List<Pruu> findbyIdUsuario(Long idUsuario);
	
	@Query("SELECT p FROM Pruu p ORDER BY p.dataHoraCriacao DESC")
	public List<Pruu> findAllOrderedByDataHora();
	
	@Query("SELECT p FROM Pruu p ORDER BY p.dataHoraCriacao DESC")
	public List<Pruu> findAllOrderedByDataHora(PruuSeletor seletor);


}
