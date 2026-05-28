package com.progWeb.SorteioOnline.controller;

import com.progWeb.SorteioOnline.model.ImagemModel;
import com.progWeb.SorteioOnline.service.ImagemService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/imagem")
public class ImagemController {

    ImagemService imagemService;

    public ImagemController(ImagemService imagemService) {
        this.imagemService = imagemService;
    }

    @PostMapping("/{id}/foto-perfil")
    public ResponseEntity<?> uploadFotoPerfil(@PathVariable Long id, @RequestParam MultipartFile arquivo)
            throws IOException {

        imagemService.saveFotoPerfil(id, arquivo);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/imagem/{id}")
    public ResponseEntity<byte[]> buscarImagem(@PathVariable Long id) {

        ImagemModel imagem = imagemService.retornaImagem(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imagem.getTipoArquivo()))
                .body(imagem.getDados());
    }
}