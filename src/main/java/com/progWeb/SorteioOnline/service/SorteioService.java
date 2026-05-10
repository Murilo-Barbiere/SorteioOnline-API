package com.progWeb.SorteioOnline.service;

import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.DTO.Response.UsuarioResposeDTO;
import com.progWeb.SorteioOnline.DTO.StatusSorteio;
import com.progWeb.SorteioOnline.DTO.request.SorteioRequestDTO;
import com.progWeb.SorteioOnline.model.SorteioModel;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import com.progWeb.SorteioOnline.repository.SorteioRepository;
import com.progWeb.SorteioOnline.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public SorteioModel addSorteio(JWTUserData jwtUserData, SorteioRequestDTO dadosSorteio){
        UsuarioModel criador = userRepository.findById(jwtUserData.userId())
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

    public boolean deleteSorteio(Long idSorteio, JWTUserData jwtUserData){
        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("user nao existente"));

        if(!(jwtUserData.userId().equals(sorteio.getCriador().getId()) || jwtUserData.role().equals("ROLE_ADMIN"))){
            return false;
        }
        sorteioRepository.deleteById(idSorteio);
        return true;
    }

    public boolean atualiza(Long id, SorteioRequestDTO novoSorteio, JWTUserData jwtUserData){
        SorteioModel sorteio = sorteioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user nao existente"));

        if(!(jwtUserData.userId().equals(sorteio.getCriador().getId()) || jwtUserData.role().equals("ROLE_ADMIN"))){
            return false;
        }

        sorteio.setNomeSorteio(novoSorteio.nome());
        sorteio.setStatusSorteio(novoSorteio.status());

        sorteioRepository.save(sorteio);
        return true;
    }

    public void encerrar(Long idSorteio, JWTUserData userData){
        SorteioModel sorteio = sorteioRepository.findById(idSorteio).
                orElseThrow(() -> new RuntimeException("user nao existente"));

        if(!(userData.userId().equals(sorteio.getCriador().getId()) || userData.role().equals("ROLE_ADMIN"))){
            throw new RuntimeException("nao autorizado");
        }

        sorteio.setStatusSorteio(StatusSorteio.encerrado);
        sorteioRepository.save(sorteio);
    }

    public void participar(Long idSorteio, JWTUserData userData){
        SorteioModel sorteio = sorteioRepository.findById(idSorteio).
                orElseThrow(() -> new RuntimeException("sorteio nao existente"));

        UsuarioModel novoParticipante = userRepository.findById(userData.userId())
                .orElseThrow(() -> new RuntimeException("user nao existente"));

        boolean jaParticipa = novoParticipante.getSorteiosParticipando()
                .stream()
                .anyMatch(s -> s.getId().equals(idSorteio));
        if(jaParticipa){
            throw new RuntimeException("user ja participa do sorteio");
        }

        novoParticipante.getSorteiosParticipando().add(sorteio);
        userRepository.save(novoParticipante);
    }

    public List<UsuarioResposeDTO> getParticipantes(Long idSorteio, JWTUserData userData){
        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("Sorteio nao existente"));

        boolean isCriador = userData.userId().equals(sorteio.getCriador().getId());
        boolean isAdmin   = userData.role().equals("ROLE_ADMIN");

        if(!isCriador && !isAdmin){
            throw new RuntimeException("nao autorizado");
        }

        return sorteioRepository.findParticipantes(idSorteio);
    }
}