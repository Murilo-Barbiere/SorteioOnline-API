package com.progWeb.SorteioOnline.service;

import com.progWeb.SorteioOnline.model.ImagemModel;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import com.progWeb.SorteioOnline.repository.ImagemRepository;
import com.progWeb.SorteioOnline.repository.SorteioRepository;
import com.progWeb.SorteioOnline.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ImagemService{

    private SorteioRepository sorteioRepository;
    private UsuarioRepository usuarioRepository;
    private ImagemRepository imagemRepository;

    public ImagemService(SorteioRepository sorteioRepository, UsuarioRepository usuarioRepository,
                         ImagemRepository imagemRepository) {
        this.sorteioRepository = sorteioRepository;
        this.usuarioRepository = usuarioRepository;
        this.imagemRepository = imagemRepository;
    }

    public void saveFotoPerfil (Long id, MultipartFile arquivo) throws IOException {
        UsuarioModel usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user nao existente"));

        ImagemModel imagem = new ImagemModel();

        imagem.setNomeArquivo(arquivo.getOriginalFilename());
        imagem.setTipoArquivo(arquivo.getContentType());
        imagem.setDados(arquivo.getBytes());

        usuario.setFotoPerfil(imagem);

        usuarioRepository.save(usuario);
    }

    public ImagemModel retornaImagem(Long id){
        return imagemRepository.findById(id).orElseThrow();
    }
}
