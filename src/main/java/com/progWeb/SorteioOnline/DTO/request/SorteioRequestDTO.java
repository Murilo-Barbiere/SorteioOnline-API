package com.progWeb.SorteioOnline.DTO.request;

import com.progWeb.SorteioOnline.DTO.StatusSorteio;
import jakarta.validation.constraints.NotEmpty;

public record SorteioRequestDTO(@NotEmpty String nome,
                                @NotEmpty StatusSorteio status,
                                @NotEmpty Long criadorId) {}
