package com.progWeb.SorteioOnline.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "imagem")
public class ImagemModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeArquivo;

    private String tipoArquivo;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private byte[] dados;
}