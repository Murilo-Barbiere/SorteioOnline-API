package com.progWeb.SorteioOnline.model;

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
    private UserModel criador;

    @ManyToMany(mappedBy = "sorteiosParticipando")
    private List<UserModel> participante = new ArrayList<>();
}