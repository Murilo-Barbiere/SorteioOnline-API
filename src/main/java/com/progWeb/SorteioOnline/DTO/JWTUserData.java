package com.progWeb.SorteioOnline.DTO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


public record JWTUserData(Long userId, String email, String role) {}