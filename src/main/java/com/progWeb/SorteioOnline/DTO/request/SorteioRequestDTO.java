package com.progWeb.SorteioOnline.DTO.request;

import com.progWeb.SorteioOnline.DTO.StatusSorteio;
import java.time.LocalDateTime;

public record SorteioRequestDTO(String nome,
                                StatusSorteio status,
                                String descricao,
                                LocalDateTime dataEncerramento) {}