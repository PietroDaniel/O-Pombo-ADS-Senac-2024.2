package com.pombo.pombo.service;

import com.pombo.pombo.exception.PomboException;
import com.pombo.pombo.model.dto.PruuDTO;
import com.pombo.pombo.model.entity.Pruu;
import com.pombo.pombo.model.entity.Usuario;
import com.pombo.pombo.model.repository.PruuRepository;
import com.pombo.pombo.model.repository.UsuarioRepository;
import com.pombo.pombo.model.seletor.PruuSeletor;
import com.pombo.pombo.utils.RSAEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PruuService {

    @Autowired
    private PruuRepository pruuRepository;

    @Autowired
    private RSAEncoder rsaEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ImagemService imagemService;


    public void salvarFotoPruu(MultipartFile foto, String pruuId, Long usuarioId) throws PomboException {

        Pruu pruu = pruuRepository.findById(pruuId).orElseThrow(() -> new PomboException("Pruu não encontrado"));

        if (!pruu.getUsuario().getId().equals(usuarioId)) {
            throw new PomboException("Você não pode alterar a imagem do Pruu de outro usuário!");
        }

        String imagemBase64 = imagemService.processarImagem(foto);
        pruu.setFoto(imagemBase64);
        pruuRepository.save(pruu);

    }

    public Pruu criarPruu(Pruu novoPruu) throws PomboException {
    	if (novoPruu.getTexto() == null) {
            throw new PomboException("Seja criativo, digite algo!");
        }

        if (novoPruu.getTexto().isBlank()) {
            throw new PomboException("O texto do Pruu não pode estar vazio ou conter apenas espaços.");
        }

        if (novoPruu.getTexto().length() < 1 || novoPruu.getTexto().length() > 300) {
            throw new PomboException("O texto deve ter entre 1 e 300 caracteres.");
        }
        
       // String textoCriptografado = rsaEncoder.encode(novoPruu.getTexto());

       // novoPruu.setTexto(textoCriptografado);

        return pruuRepository.save(novoPruu);
    }
    
    public void atualizarPruu(String id, String novoConteudo) throws PomboException {
        Pruu pruu = pruuRepository.findById(id)
            .orElseThrow(() -> new PomboException("Pruu não encontrado"));
        pruu.setTexto(novoConteudo);
        pruuRepository.save(pruu);
    }


    public void darLike(Long usuarioID, String pruuID) throws PomboException {

        Pruu pruu = pruuRepository.findById(pruuID).orElseThrow(() -> new PomboException("Pruu não encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioID).orElseThrow(() -> new PomboException("Usuário não encontrado"));

        List<Usuario> likes = pruu.getLikedByUsers();

        if (likes.contains(usuario)) {
            likes.remove(usuario);
        } else {
            likes.add(usuario);
        }

        pruu.setLikedByUsers(likes);

        pruuRepository.save(pruu);

    }

    public List<Usuario> buscarLikesPruu(String pruuId) throws PomboException {

        Pruu pruu = pruuRepository.findById(pruuId).orElseThrow(() -> new PomboException("Pruu não encontrado"));

        return pruu.getLikedByUsers();
    }

    public PruuDTO buscarPorId(String uuid) throws PomboException {
        Pruu pruu = pruuRepository.findById(uuid).orElseThrow(() -> new PomboException("Pruu não encontrado"));
        //pruu.setTexto(rsaEncoder.decode(pruu.getTexto()));

        Integer quantidadeLikes = pruu.getLikedByUsers().size();
        Integer quantidadeDenuncias = pruu.getDenuncias().size();

        return Pruu.paraDTO(pruu, quantidadeLikes, quantidadeDenuncias);
    }
    
    
  
   
    public Page<PruuDTO> listarComFiltros(PruuSeletor seletor, Usuario usuarioAutenticado) {
        // Configuração da paginação e ordenação
        Pageable pageable = PageRequest.of(
            seletor.getPagina() - 1, 
            seletor.getLimite(), 
            Sort.by("dataHoraCriacao").descending() // Ordenação por data
        );
        
        if (seletor.isCurtidosPeloUsuario()) {
            seletor.setCurtidoPorUsuarioId(usuarioAutenticado.getId());
        }
        
        // Busca paginada utilizando Specification
        Page<Pruu> paginaPruus = pruuRepository.findAll(seletor, pageable);

        // Mapeia para DTO
        return paginaPruus.map(pruu -> Pruu.paraDTO(
            pruu,
            pruu.getLikedByUsers().size(),
            pruu.getDenuncias().size(),
            usuarioAutenticado
        ));
    }
    
//    public List<PruuDTO> listarComFiltros(PruuSeletor seletor, Usuario usuarioAutenticado) {
//
//        List<Pruu> pruus = new ArrayList<>();
//
//        if (seletor.temPaginacao()) {
//            int pageNumber = seletor.getPagina();
//            int pageSize = seletor.getLimite();
//
//            PageRequest page = PageRequest.of(pageNumber - 1, pageSize);
//            pruus = pruuRepository.findAll(seletor, page).toList();
//        }
//
////        if (seletor.isEstaCurtido()) {
////            pruus = pruus.stream()
////                    .filter(pruu -> pruu.getLikedByUsers().stream()
////                            .anyMatch(usuario -> usuario.getId().equals(subjectId)))
////                    .collect(Collectors.toList());
////        }
//
//        pruus = pruuRepository.findAllOrderedByDataHora(seletor);
//        return converterParaDTO(pruus, usuarioAutenticado);
//    }

    public void excluirPruu(String pruuid, Long usuarioID) throws PomboException {

        Pruu pruu = pruuRepository.findById(pruuid).orElseThrow(() -> new PomboException("Pruu não encontrada!"));

        if (!pruu.getUsuario().getId().equals(usuarioID)) {
            throw new PomboException("Você não pode excluir um Pruu de outro usuário!");
        }

        pruu.setExcluido(true);
        pruuRepository.save(pruu);

    }

    public List<Pruu> removerPruusDeletadosBloqueados(List<Pruu> pruus) {
        return pruus.stream()
                .filter(pruu -> !pruu.isBloqueado() && !pruu.isExcluido())
                .collect(Collectors.toList());
    }

    public List<PruuDTO> converterParaDTO(List<Pruu> pruus, Usuario usuarioAutenticado) {

        List<PruuDTO> dtos = new ArrayList<>();

        for (Pruu p : pruus) {
           // p.setTexto(rsaEncoder.decode(p.getTexto()));

            Integer quantidadeLikes = p.getLikedByUsers().size();
            Integer quantidadeDenuncias = p.getDenuncias().size();

            PruuDTO dto = Pruu.paraDTO(p, quantidadeLikes, quantidadeDenuncias, usuarioAutenticado);
            dtos.add(dto);
        }
        return dtos;
    }
    
    public List<PruuDTO> converterParaDTO(List<Pruu> pruus) {

        List<PruuDTO> dtos = new ArrayList<>();

        for (Pruu p : pruus) {
           // p.setTexto(rsaEncoder.decode(p.getTexto()));

            Integer quantidadeLikes = p.getLikedByUsers().size();
            Integer quantidadeDenuncias = p.getDenuncias().size();

            PruuDTO dto = Pruu.paraDTO(p, quantidadeLikes, quantidadeDenuncias);
            dtos.add(dto);
        }
        return dtos;
    }
}