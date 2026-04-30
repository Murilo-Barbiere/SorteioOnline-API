package com.progWeb.SorteioOnline.service;

import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.DTO.request.SorteioRequestDTO;
import com.progWeb.SorteioOnline.model.SorteioModel;
import com.progWeb.SorteioOnline.model.UserModel;
import com.progWeb.SorteioOnline.repository.SorteioRepository;
import com.progWeb.SorteioOnline.repository.UserRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SorteioService {

    private SorteioRepository sorteioRepository;
    private UserRepository userRepository;

    public SorteioService(SorteioRepository sorteioRepository, UserRepository userRepository) {
        this.sorteioRepository = sorteioRepository;
        this.userRepository = userRepository;
    }

    public SorteioModel addSorteio(SorteioRequestDTO dadosSorteio){
        UserModel criador = userRepository.findById(dadosSorteio.criadorId())
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

    public boolean deleteSorteio(Long idSorteio, JWTUserData userLogadoJWT){
        Optional<SorteioModel> optSorteio = sorteioRepository.findById(idSorteio);

        if(optSorteio.isEmpty() || !(optSorteio.get().getId().equals(userLogadoJWT.userId()))) {
            return false;
        }
        sorteioRepository.deleteById(idSorteio);
        return true;
    }
}