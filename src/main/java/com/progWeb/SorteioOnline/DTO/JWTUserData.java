package com.progWeb.SorteioOnline.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public record JWTUserData(Long userId, String email, String role) {
    public JWTUserData(Long userId, String email, Role role) {
        this.userId = userId;
        this.email = email;
        this.role = String.valueOf(role);
    }
}