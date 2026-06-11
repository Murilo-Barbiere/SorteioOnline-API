package com.progWeb.SorteioOnline.controller;

import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.DTO.Response.UsuarioResponseDTO;
import com.progWeb.SorteioOnline.DTO.request.RegisterRequestDTO;
import com.progWeb.SorteioOnline.service.UsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponseDTO> listaUsuarios(){
        return usuarioService.getAll();
    }

    @GetMapping("/{id}")
    public UsuarioResponseDTO mostraUsuario(@PathVariable Long id){
        return usuarioService.getUser(id).orElseThrow(() -> new RuntimeException("erro"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletaUsuario(@PathVariable Long id, @AuthenticationPrincipal JWTUserData jwt){
        boolean deletado = usuarioService.deletaUsuario(id, jwt);

        if(!deletado){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User nao encontrado");
        }
        return ResponseEntity.ok("User deletado");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> alteraDados(@PathVariable Long id,
                                         @AuthenticationPrincipal JWTUserData jwt,
                                         @RequestBody RegisterRequestDTO request) {
        usuarioService.atualiza(id, jwt, request);
        return ResponseEntity.ok("atualizado");
    }
}