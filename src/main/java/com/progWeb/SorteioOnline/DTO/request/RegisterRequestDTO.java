package com.progWeb.SorteioOnline.DTO.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record RegisterRequestDTO(@NotEmpty String nome,
                                 @NotEmpty @Email String email,
                                 String senha){}
