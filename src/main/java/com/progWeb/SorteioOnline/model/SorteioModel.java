package com.progWeb.SorteioOnline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.progWeb.SorteioOnline.DTO.StatusSorteio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "sorteio")
public class SorteioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeSorteio;

    private String descricao;

    private StatusSorteio statusSorteio;

    private LocalDateTime dataEncerramento;

    @ManyToOne
    @JoinColumn(name = "criador_id")
    @JsonIgnore
    private UsuarioModel criador;

    @ManyToOne
    @JoinColumn(name = "ganhador_id")
    @JsonIgnore
    private UsuarioModel ganhador;

    @ManyToMany(mappedBy = "sorteiosParticipando")
    @JsonIgnore
    private List<UsuarioModel> participantes = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "foto_capa_id")
    @JsonIgnore
    private ImagemModel fotoCapa;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "sorteio_id")
    @JsonIgnore
    private List<ImagemModel> fotos = new ArrayList<>();
}