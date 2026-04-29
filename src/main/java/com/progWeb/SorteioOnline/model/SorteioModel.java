package com.progWeb.SorteioOnline.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.progWeb.SorteioOnline.DTO.StatusSorteio;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    private StatusSorteio statusSorteio;

    @ManyToOne
    @JoinColumn(name = "criador_id")
    @JsonIgnore
    private UserModel criador;

    @ManyToMany(mappedBy = "sorteiosParticipando")
    @JsonIgnore
    private List<UserModel> participante = new ArrayList<>();
}