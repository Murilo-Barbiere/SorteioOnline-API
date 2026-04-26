package com.progWeb.SorteioOnline.DTO.request;

import jakarta.validation.constraints.NotEmpty;

public record ResgisterRequestDTO(@NotEmpty String nome,
                                  @NotEmpty String email,
                                  @NotEmpty String senha){}
