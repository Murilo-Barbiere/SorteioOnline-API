package com.progWeb.SorteioOnline.service;

import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.model.ImagemModel;
import com.progWeb.SorteioOnline.model.SorteioModel;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import com.progWeb.SorteioOnline.repository.ImagemRepository;
import com.progWeb.SorteioOnline.repository.SorteioRepository;
import com.progWeb.SorteioOnline.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ImagemService {

    private final UsuarioRepository usuarioRepository;
    private final SorteioRepository sorteioRepository;
    private final ImagemRepository imagemRepository;

    public ImagemService(
            UsuarioRepository usuarioRepository,
            SorteioRepository sorteioRepository,
            ImagemRepository imagemRepository
    ) {
        this.usuarioRepository = usuarioRepository;
        this.sorteioRepository = sorteioRepository;
        this.imagemRepository = imagemRepository;
    }

    // FOTO PERFIL

    public void definirFotoPerfil(
            Long idUsuario,
            MultipartFile arquivo,
            JWTUserData userData
    ) throws IOException {

        UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("usuario nao encontrado"));

        if (!(userData.userId().equals(usuario.getId())
                || userData.role().equals("ROLE_ADMIN"))) {

            throw new RuntimeException("nao autorizado");
        }

        ImagemModel imagem = criarImagem(arquivo);

        usuario.setFotoPerfil(imagem);

        usuarioRepository.save(usuario);
    }

    // FOTO CAPA

    public void definirFotoCapaSorteio(
            Long idSorteio,
            MultipartFile arquivo,
            JWTUserData userData
    ) throws IOException {

        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("sorteio nao encontrado"));

        if (!(userData.userId().equals(sorteio.getCriador().getId())
                || userData.role().equals("ROLE_ADMIN"))) {

            throw new RuntimeException("nao autorizado");
        }

        ImagemModel imagem = criarImagem(arquivo);

        sorteio.setFotoCapa(imagem);

        sorteioRepository.save(sorteio);
    }

    // FOTOS SORTEIO

    public void adicionarFotoSorteio(
            Long idSorteio,
            MultipartFile arquivo,
            JWTUserData userData
    ) throws IOException {

        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("sorteio nao encontrado"));

        if (!(userData.userId().equals(sorteio.getCriador().getId())
                || userData.role().equals("ROLE_ADMIN"))) {

            throw new RuntimeException("nao autorizado");
        }

        if (sorteio.getFotos().size() >= 3) {
            throw new RuntimeException("maximo de 3 fotos");
        }

        ImagemModel imagem = criarImagem(arquivo);

        sorteio.getFotos().add(imagem);

        sorteioRepository.save(sorteio);
    }

    // RETORNOS

    public ImagemModel retornarFotoPerfil(Long idUsuario) {
        UsuarioModel usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("usuario nao encontrado"));

        if (usuario.getFotoPerfil() == null) {
            throw new RuntimeException("foto nao definida");
        }

        return usuario.getFotoPerfil();
    }

    public ImagemModel retornarFotoCapa(Long idSorteio) {

        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("sorteio nao encontrado"));

        return sorteio.getFotoCapa();
    }

    public List<ImagemModel> retornarFotosSorteio(Long idSorteio) {

        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("sorteio nao encontrado"));

        return sorteio.getFotos();
    }

    public ImagemModel retornarImagem(Long idImagem) {

        return imagemRepository.findById(idImagem)
                .orElseThrow(() -> new RuntimeException("imagem nao encontrada"));
    }

    // AUXILIAR
    private ImagemModel criarImagem(MultipartFile arquivo) throws IOException {
        ImagemModel imagem = new ImagemModel();
        imagem.setNomeArquivo(arquivo.getOriginalFilename());
        imagem.setTipoArquivo(arquivo.getContentType());
        imagem.setDados(arquivo.getBytes());

        return imagemRepository.save(imagem);
    }
}