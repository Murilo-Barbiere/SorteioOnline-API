package com.progWeb.SorteioOnline.service;

import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.DTO.Response.UsuarioResponseDTO;
import com.progWeb.SorteioOnline.DTO.request.RegisterRequestDTO;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import com.progWeb.SorteioOnline.repository.SorteioRepository;
import com.progWeb.SorteioOnline.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private SorteioRepository sorteioRepository;
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, SorteioRepository sorteioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.sorteioRepository = sorteioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResponseDTO> getAll(){
        return usuarioRepository.findAllByUsuarioRespose();

    }

    public Optional<UsuarioResponseDTO> getUser(Long id){
        return usuarioRepository.findByUsuarioRespose(id);
    }

    @Transactional
    public boolean deletaUsuario(Long id, JWTUserData jwtUserData){
        if(!(jwtUserData.userId().equals(id) || jwtUserData.role().equals("ROLE_ADMIN"))){
            throw new RuntimeException("Nao autorizado");
        }

        sorteioRepository.nullifyGanhador(id);
        sorteioRepository.deleteByCriadorId(id);

        usuarioRepository.deleteById(id);
        return true;
    }

    public void atualiza(Long id, JWTUserData jwtUserData, RegisterRequestDTO novosDados){
        if(!(jwtUserData.userId().equals(id) || jwtUserData.role().equals("ROLE_ADMIN"))){
            throw new RuntimeException("Nao autorizado");
        }

        UsuarioModel newUser = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User nao encontrado"));
        newUser.setNome(novosDados.nome());
        newUser.setEmail(novosDados.email());
        newUser.setSenha(passwordEncoder.encode(novosDados.senha()));

        usuarioRepository.save(newUser);
    }
}