package com.progWeb.SorteioOnline.controller;

import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.model.ImagemModel;
import com.progWeb.SorteioOnline.service.ImagemService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/imagem")
public class ImagemController {

    private final ImagemService imagemService;

    public ImagemController(ImagemService imagemService) {
        this.imagemService = imagemService;
    }

    @PostMapping("/usuario/{id}/foto-perfil")
    public ResponseEntity<?> definirFotoPerfil(
            @PathVariable Long id,
            @RequestParam MultipartFile arquivo,
            @AuthenticationPrincipal JWTUserData userData
    ) throws IOException {

        imagemService.definirFotoPerfil(id, arquivo, userData);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sorteio/{id}/foto-capa")
    public ResponseEntity<?> definirFotoCapa(
            @PathVariable Long id,
            @RequestParam MultipartFile arquivo,
            @AuthenticationPrincipal JWTUserData userData
    ) throws IOException {

        imagemService.definirFotoCapaSorteio(id, arquivo, userData);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/sorteio/{id}/fotos")
    public ResponseEntity<?> adicionarFotoSorteio(
            @PathVariable Long id,
            @RequestParam MultipartFile arquivo,
            @AuthenticationPrincipal JWTUserData userData
    ) throws IOException {

        imagemService.adicionarFotoSorteio(id, arquivo, userData);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/usuario/{id}/foto-perfil")
    @Transactional
    public ResponseEntity<byte[]> retornarFotoPerfil(@PathVariable Long id) {

        ImagemModel imagem = imagemService.retornarFotoPerfil(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imagem.getTipoArquivo()))
                .body(imagem.getDados());
    }

    @GetMapping("/sorteio/{id}/foto-capa")
    @Transactional
    public ResponseEntity<byte[]> retornarFotoCapa(@PathVariable Long id) {

        ImagemModel imagem = imagemService.retornarFotoCapa(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imagem.getTipoArquivo()))
                .body(imagem.getDados());
    }

    @GetMapping("/sorteio/{id}/fotos")
    @Transactional
    public ResponseEntity<List<Long>> retornarFotosSorteio(@PathVariable Long id) {
        List<ImagemModel> fotos = imagemService.retornarFotosSorteio(id);
        List<Long> ids = fotos.stream().map(ImagemModel::getId).toList();
        return ResponseEntity.ok(ids);
    }

    // RETORNAR IMAGEM POR ID

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> retornarImagem(@PathVariable Long id) {

        ImagemModel imagem = imagemService.retornarImagem(id);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(imagem.getTipoArquivo()))
                .body(imagem.getDados());
    }
}