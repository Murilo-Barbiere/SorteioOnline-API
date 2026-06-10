package com.progWeb.SorteioOnline.service;

import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.DTO.Response.UsuarioResposeDTO;
import com.progWeb.SorteioOnline.DTO.StatusSorteio;
import com.progWeb.SorteioOnline.DTO.request.SorteioRequestDTO;
import com.progWeb.SorteioOnline.model.SorteioModel;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import com.progWeb.SorteioOnline.repository.SorteioRepository;
import com.progWeb.SorteioOnline.repository.UsuarioRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class SorteioService {

    private SorteioRepository sorteioRepository;
    private UsuarioRepository userRepository;
    private EmailService emailService;

    public SorteioService(SorteioRepository sorteioRepository,
                          UsuarioRepository userRepository,
                          EmailService emailService) {
        this.sorteioRepository = sorteioRepository;
        this.userRepository    = userRepository;
        this.emailService      = emailService;
    }

    public SorteioModel addSorteio(JWTUserData jwtUserData, SorteioRequestDTO dadosSorteio) {
        UsuarioModel criador = userRepository.findById(jwtUserData.userId())
                .orElseThrow(() -> new RuntimeException("user nao encontrado"));

        SorteioModel sorteio = new SorteioModel();
        sorteio.setNomeSorteio(dadosSorteio.nome());
        sorteio.setStatusSorteio(dadosSorteio.status());
        sorteio.setCriador(criador);
        sorteio.setDescricao(dadosSorteio.descricao());

        if (dadosSorteio.dataEncerramento() != null) {
            sorteio.setDataEncerramento(dadosSorteio.dataEncerramento());
        }

        return sorteioRepository.save(sorteio);
    }

    public List<SorteioModel> listSorteios() {
        return sorteioRepository.findAll();
    }

    public Optional<SorteioModel> getSorteio(Long id) {
        return sorteioRepository.findById(id);
    }

    public List<SorteioModel> SorteiosParticipando(JWTUserData userData) {
        UsuarioModel user = userRepository.findById(userData.userId())
                .orElseThrow(() -> new RuntimeException("user nao existente"));
        return user.getSorteiosParticipando();
    }

    public List<SorteioModel> sorteiosCriados(JWTUserData userData) {
        return sorteioRepository.findByCriadorId(userData.userId());
    }

    // ── Deletar / Atualizar ───────────────────────────────────────────────────

    @Transactional
    public boolean deleteSorteio(Long idSorteio, JWTUserData jwtUserData) {
        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("sorteio nao existente"));

        if (!(jwtUserData.userId().equals(sorteio.getCriador().getId())
                || jwtUserData.role().equals("ROLE_ADMIN"))) {
            return false;
        }

        sorteioRepository.deleteParticipacoes(idSorteio);
        sorteioRepository.deleteById(idSorteio);
        return true;
    }

    public boolean atualiza(Long id, SorteioRequestDTO novoSorteio, JWTUserData jwtUserData) {
        SorteioModel sorteio = sorteioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("user nao existente"));

        if(sorteio.getStatusSorteio().equals(StatusSorteio.encerrado)){
            throw new RuntimeException("Edição não permitida: o sorteio já foi encerrado.");
        }

        if (!(jwtUserData.userId().equals(sorteio.getCriador().getId())
                || jwtUserData.role().equals("ROLE_ADMIN"))) {
            return false;
        }

        sorteio.setNomeSorteio(novoSorteio.nome());
        sorteio.setStatusSorteio(novoSorteio.status());
        sorteio.setDescricao(novoSorteio.descricao());

        if (novoSorteio.dataEncerramento() != null) {
            sorteio.setDataEncerramento(novoSorteio.dataEncerramento());
        }

        sorteioRepository.save(sorteio);
        return true;
    }

    public void encerrar(Long idSorteio, JWTUserData userData) {
        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("user nao existente"));

        if (!(userData.userId().equals(sorteio.getCriador().getId())
                || userData.role().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("nao autorizado");
        }

        sorteio.setStatusSorteio(StatusSorteio.encerrado);
        sorteioRepository.save(sorteio);
    }


    public void participar(Long idSorteio, JWTUserData userData) {
        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("sorteio nao existente"));

        if (sorteio.getStatusSorteio() == StatusSorteio.encerrado) {
            throw new RuntimeException("Sorteio encerrado. Nao e possivel participar.");
        }

        UsuarioModel novoParticipante = userRepository.findById(userData.userId())
                .orElseThrow(() -> new RuntimeException("user nao existente"));

        boolean jaParticipa = novoParticipante.getSorteiosParticipando()
                .stream()
                .anyMatch(s -> s.getId().equals(idSorteio));
        if (jaParticipa) {
            throw new RuntimeException("user ja participa do sorteio");
        }

        novoParticipante.getSorteiosParticipando().add(sorteio);
        userRepository.save(novoParticipante);
    }

    @Transactional
    public void removerParticipacao(Long idSorteio, Long idUserRemove, JWTUserData userData) {
        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("sorteio nao existente"));

        boolean ehOProprioUsuario = userData.userId().equals(idUserRemove);
        boolean ehCriador         = userData.userId().equals(sorteio.getCriador().getId());
        boolean ehAdmin            = userData.role().equals("ROLE_ADMIN");

        if (!ehOProprioUsuario && !ehCriador && !ehAdmin) {
            throw new RuntimeException("nao autorizado");
        }

        UsuarioModel usuarioRemover = userRepository.findById(idUserRemove)
                .orElseThrow(() -> new RuntimeException("usuario nao encontrado"));

        usuarioRemover.getSorteiosParticipando()
                .removeIf(s -> s.getId().equals(idSorteio));

        userRepository.save(usuarioRemover);
    }

    public UsuarioResposeDTO sortear(Long idSorteio, JWTUserData userData) {
        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("Sorteio nao existente"));

        if (sorteio.getGanhador() != null) {
            throw new RuntimeException("Este sorteio ja possui um ganhador.");
        }
        if (sorteio.getStatusSorteio() == StatusSorteio.encerrado) {
            throw new RuntimeException("Sorteio encerrado");
        }
        if (sorteio.getParticipantes().isEmpty()) {
            throw new RuntimeException("Sorteio sem participantes");
        }

        boolean isCriador = userData.userId().equals(sorteio.getCriador().getId());
        boolean isAdmin   = userData.role().equals("ROLE_ADMIN");
        if (!isCriador && !isAdmin) {
            throw new RuntimeException("nao autorizado");
        }

        return executarSorteio(sorteio);
    }

    public UsuarioResposeDTO getGanhador(Long idSorteio) {
        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("Sorteio nao encontrado"));

        if (sorteio.getGanhador() == null) {
            throw new RuntimeException("Este sorteio ainda nao possui um ganhador.");
        }

        UsuarioModel g = sorteio.getGanhador();
        return new UsuarioResposeDTO(g.getId(), g.getNome(), g.getEmail());
    }

    public List<UsuarioResposeDTO> getParticipantes(Long idSorteio, JWTUserData userData) {
        SorteioModel sorteio = sorteioRepository.findById(idSorteio)
                .orElseThrow(() -> new RuntimeException("Sorteio nao existente"));

        boolean isCriador = userData.userId().equals(sorteio.getCriador().getId());
        boolean isAdmin   = userData.role().equals("ROLE_ADMIN");

        if (!isCriador && !isAdmin) {
            throw new RuntimeException("nao autorizado");
        }

        return sorteioRepository.findParticipantes(idSorteio);
    }

    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void encerrarSorteiosAutomaticamente() {
        List<SorteioModel> sorteiosExpirados =
                sorteioRepository.findSorteiosParaEncerrarAutomaticamente(LocalDateTime.now());

        for (SorteioModel sorteio : sorteiosExpirados) {
            try {
                if (sorteio.getParticipantes().isEmpty()) {
                    sorteio.setStatusSorteio(StatusSorteio.encerrado);
                    sorteioRepository.save(sorteio);
                } else {
                    executarSorteio(sorteio);   // envia e-mail internamente
                }
            } catch (Exception e) {
                System.err.println(
                        "[Scheduler] Erro ao encerrar sorteio id=" + sorteio.getId()
                                + ": " + e.getMessage()
                );
            }
        }
    }

    private UsuarioResposeDTO executarSorteio(SorteioModel sorteio) {
        Random random = new Random();

        UsuarioModel userGanhador = sorteio.getParticipantes()
                .get(random.nextInt(sorteio.getParticipantes().size()));

        sorteio.setGanhador(userGanhador);
        sorteio.setStatusSorteio(StatusSorteio.encerrado);
        sorteioRepository.save(sorteio);

        emailService.enviarEmailVencedor(
                userGanhador.getEmail(),
                userGanhador.getNome(),
                sorteio.getId(),
                sorteio.getNomeSorteio()
        );

        return new UsuarioResposeDTO(
                userGanhador.getId(),
                userGanhador.getNome(),
                userGanhador.getEmail()
        );
    }
}