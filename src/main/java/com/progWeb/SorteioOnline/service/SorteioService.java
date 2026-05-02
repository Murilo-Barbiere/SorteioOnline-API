package com.progWeb.SorteioOnline.service;

import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.DTO.request.SorteioRequestDTO;
import com.progWeb.SorteioOnline.model.SorteioModel;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import com.progWeb.SorteioOnline.repository.SorteioRepository;
import com.progWeb.SorteioOnline.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SorteioService {

    private SorteioRepository sorteioRepository;
    private UsuarioRepository userRepository;

    public SorteioService(SorteioRepository sorteioRepository, UsuarioRepository userRepository) {
        this.sorteioRepository = sorteioRepository;
        this.userRepository = userRepository;
    }

    //refatorar isso
    public SorteioModel addSorteio(SorteioRequestDTO dadosSorteio){
        UsuarioModel criador = userRepository.findById(dadosSorteio.criadorId())
                .orElseThrow(() -> new RuntimeException("user nao encontrado"));

        SorteioModel sorteio = new SorteioModel();
        sorteio.setNomeSorteio(dadosSorteio.nome());
        sorteio.setStatusSorteio(dadosSorteio.status());
        sorteio.setCriador(criador);

        return sorteioRepository.save(sorteio);
    }

    public List<SorteioModel> listSorteios(){
        return sorteioRepository.findAll();
    }

    public Optional<SorteioModel> getSorteio(Long id){
        return sorteioRepository.findById(id);
    }

    //testa isso dps
    public boolean deleteSorteio(Long idSorteio, JWTUserData jwtUserData){
        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("user nao nao existente"));

        if(!(jwtUserData.userId().equals(sorteio.getCriador().getId()) || jwtUserData.role().equals("ROLE_ADMIN"))){
            return false;
        }
        sorteioRepository.deleteById(idSorteio);
        return true;
    }

    //alterar se mais atributos em sorteioModel
    public boolean atualiza(Long id, SorteioRequestDTO novoSorteio, JWTUserData jwtUserData){
        SorteioModel sorteio = sorteioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user nao nao existente"));

        if(!(jwtUserData.userId().equals(sorteio.getCriador().getId()) || jwtUserData.role().equals("ROLE_ADMIN"))){
            return false;
        }

        sorteio.setNomeSorteio(novoSorteio.nome());
        sorteio.setStatusSorteio(novoSorteio.status());

        sorteioRepository.save(sorteio);
        return true;
    }
}