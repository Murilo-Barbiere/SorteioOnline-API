package com.progWeb.SorteioOnline.service;

import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.DTO.Response.UsuarioResposeDTO;
import com.progWeb.SorteioOnline.DTO.Role;
import com.progWeb.SorteioOnline.DTO.request.RegisterRequestDTO;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import com.progWeb.SorteioOnline.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UsuarioResposeDTO> getAll(){
        return usuarioRepository.findAllByUsuarioRespose();

    }

    public Optional<UsuarioResposeDTO> getUser(Long id){
        return usuarioRepository.findByUsuarioRespose(id);
    }

    public boolean deletaUsuario(Long id, JWTUserData jwtUserData){
        if(!(jwtUserData.userId().equals(id) || jwtUserData.role().equals("ROLE_ADMIN"))) return false;

        usuarioRepository.deleteById(id);
        return true;
    }

    public void atualiza(Long id, JWTUserData jwtUserData, RegisterRequestDTO novosDados){
        if(!jwtUserData.userId().equals(id)) throw new RuntimeException("Nao autorizado");

        UsuarioModel newUser = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User nao encontrado"));
        newUser.setNome(novosDados.nome());
        newUser.setEmail(novosDados.email());
        newUser.setSenha(passwordEncoder.encode(novosDados.senha()));

        usuarioRepository.save(newUser);
    }
}
