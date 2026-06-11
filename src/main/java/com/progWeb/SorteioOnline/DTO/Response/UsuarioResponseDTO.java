package com.progWeb.SorteioOnline.DTO.Response;

import com.progWeb.SorteioOnline.DTO.Role;

public record UsuarioResponseDTO(Long id,
                                 String nome,
                                 String email,
                                 Role role){}
