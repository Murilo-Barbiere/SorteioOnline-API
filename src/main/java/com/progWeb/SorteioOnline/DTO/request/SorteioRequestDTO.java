package com.progWeb.SorteioOnline.DTO.request;

import com.progWeb.SorteioOnline.DTO.StatusSorteio;
import jakarta.validation.constraints.NotEmpty;

public record SorteioRequestDTO(String nome,
                                StatusSorteio status,
                                String descricao) {}