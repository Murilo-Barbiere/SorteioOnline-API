package com.progWeb.SorteioOnline.controller;

import com.progWeb.SorteioOnline.DTO.Response.LoginResponseDTO;
import com.progWeb.SorteioOnline.DTO.Response.RegisterResponseDTO;
import com.progWeb.SorteioOnline.DTO.request.GoogleTokenRequest;
import com.progWeb.SorteioOnline.DTO.request.LoginRequestDTO;
import com.progWeb.SorteioOnline.DTO.request.RegisterRequestDTO;
import com.progWeb.SorteioOnline.service.AuthService;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request){
        LoginResponseDTO response = authService.validaUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request){
        RegisterResponseDTO response = authService.newUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody GoogleTokenRequest request) {
        String jwt = authService.getTokenGoogleUserIsPresent(request.token());

        return ResponseEntity.ok(Map.of(
                "token", jwt
        ));
    }
}