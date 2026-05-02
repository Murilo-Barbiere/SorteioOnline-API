package com.progWeb.SorteioOnline.config;

import com.progWeb.SorteioOnline.DTO.Role;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import com.progWeb.SorteioOnline.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminConfig {
    @Bean
    public CommandLineRunner primeiroADM(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder){
        return args -> {
            String admEmail = "admin@gmail.com";

            if(usuarioRepository.findUserByEmail(admEmail).isEmpty()){
                UsuarioModel adm = new UsuarioModel();
                adm.setNome("adm");
                adm.setEmail("admin@gmail.com");
                adm.setSenha(passwordEncoder.encode("40028922"));
                adm.setRole(Role.ROLE_ADMIN);

                usuarioRepository.save(adm);
            }
        };
    }
}