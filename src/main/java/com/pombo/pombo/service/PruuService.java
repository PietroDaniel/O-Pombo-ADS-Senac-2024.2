package com.pombo.pombo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.repository.PruuRepository;

@Service
public class PruuService {

    @Autowired
    private PruuRepository pruuRepository;

    public List<Pruu> listarTodos() {
        return pruuRepository.findAll();
    }

    public Pruu buscarPorId(Long id) throws PomboException {
        return pruuRepository.findById(id)
            .orElseThrow(() -> new PomboException("Pruu não encontrado"));
    }

    public Pruu criarPruu(Pruu novoPruu) {
        return pruuRepository.save(novoPruu);
    }

    public Pruu atualizarPruu(Long id, Pruu pruuAtualizado) throws PomboException {
        Pruu pruu = buscarPorId(id);
        pruu.setTexto(pruuAtualizado.getTexto());
        pruu.setImagem(pruuAtualizado.getImagem());
        pruu.setBloqueado(pruuAtualizado.isBloqueado());
        return pruuRepository.save(pruu);
    }

    public void excluirPruu(Long id) throws PomboException {
        Pruu pruu = buscarPorId(id);
        pruuRepository.delete(pruu);
    }
}
