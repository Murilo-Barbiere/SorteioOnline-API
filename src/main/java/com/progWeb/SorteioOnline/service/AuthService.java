package com.progWeb.SorteioOnline.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.progWeb.SorteioOnline.DTO.Response.LoginResponseDTO;
import com.progWeb.SorteioOnline.DTO.Response.RegisterResponseDTO;
import com.progWeb.SorteioOnline.DTO.request.LoginRequestDTO;
import com.progWeb.SorteioOnline.DTO.request.RegisterRequestDTO;
import com.progWeb.SorteioOnline.config.TokenConfig;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import com.progWeb.SorteioOnline.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UsuarioRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private TokenConfig tokenConfig;

    public AuthService(UsuarioRepository userRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, TokenConfig tokenConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    public LoginResponseDTO validaUser(LoginRequestDTO request){
        UsernamePasswordAuthenticationToken userPass = new UsernamePasswordAuthenticationToken(request.email(), request.senha());
        Authentication authentication = authenticationManager.authenticate(userPass);

        UsuarioModel user = (UsuarioModel) authentication.getPrincipal();
        String token = tokenConfig.generateToken(user);
        return new LoginResponseDTO(token);
    }

    public RegisterResponseDTO newUser(RegisterRequestDTO request){
        UsuarioModel user = new UsuarioModel();

        user.setNome(request.nome());
        user.setEmail(request.email());
        user.setSenha(passwordEncoder.encode(request.senha()));

        userRepository.save(user);

        return new RegisterResponseDTO(user.getNome(), user.getEmail());
    }

    public String getTokenGoogleUserIsPresent(String token){
        var payload = tokenConfig.validarTokenGoogle(token);
        String email = payload.getEmail();

        UsuarioModel user = (UsuarioModel) userRepository.findUserByEmail(email)
                .orElseThrow(()-> new RuntimeException("user nao registrado"));

        return tokenConfig.generateToken(user);
    }

    public String cadastraTokenGoogleUser(String token){
        var payload = tokenConfig.validarTokenGoogle(token);
        String email = payload.getEmail();
        String nome = (String) payload.get("name");

        UsuarioModel newUser = new UsuarioModel();
        newUser.setNome(nome);
        newUser.setEmail(email);

        userRepository.save(newUser);

        return tokenConfig.generateToken(newUser);
    }
}
