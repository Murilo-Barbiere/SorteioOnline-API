package com.progWeb.SorteioOnline.config;

import com.progWeb.SorteioOnline.DTO.Role;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import com.progWeb.SorteioOnline.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminConfig {

    @Value("${admin.email}")
    private String admEmail;

    @Value("${admin.password}")
    private String admPassword;

    @Bean
    public CommandLineRunner primeiroADM(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        return args -> {
            if(usuarioRepository.findUserByEmail(admEmail).isEmpty()){
                UsuarioModel adm = new UsuarioModel();
                adm.setNome("adm");
                adm.setEmail(admEmail);
                adm.setSenha(passwordEncoder.encode(admPassword));
                adm.setRole(Role.ROLE_ADMIN);

                usuarioRepository.save(adm);
            }
        };
    }
}