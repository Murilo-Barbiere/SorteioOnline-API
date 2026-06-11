package com.progWeb.SorteioOnline.repository;

import com.progWeb.SorteioOnline.DTO.Response.UsuarioResponseDTO;
import com.progWeb.SorteioOnline.DTO.StatusSorteio;
import com.progWeb.SorteioOnline.model.SorteioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SorteioRepository extends JpaRepository<SorteioModel, Long> {

    @Query("SELECT new com.progWeb.SorteioOnline.DTO.Response.UsuarioResposeDTO(u.id, u.nome, u.email) " +
            "FROM sorteio s " +
            "JOIN s.participantes u " +
            "WHERE s.id = :idSorteio")
    List<UsuarioResponseDTO> findParticipantes(@Param("idSorteio") Long idSorteio);

    @Query("SELECT s FROM sorteio s WHERE s.criador.id = :userId")
    List<SorteioModel> findByCriadorId(@Param("userId") Long userId);

    void deleteByCriadorId(Long userId);

    @org.springframework.data.jpa.repository.Modifying
    @Query("UPDATE sorteio s SET s.ganhador = NULL WHERE s.ganhador.id = :userId")
    void nullifyGanhador(@Param("userId") Long userId);

    @org.springframework.data.jpa.repository.Modifying
    @Query(value = "DELETE FROM usuario_sorteio WHERE sorteio_id = :idSorteio", nativeQuery = true)
    void deleteParticipacoes(@Param("idSorteio") Long idSorteio);

    @Query("SELECT s FROM sorteio s " +
            "WHERE s.statusSorteio = :status " +
            "AND s.dataEncerramento IS NOT NULL " +
            "AND s.dataEncerramento <= :agora " +
            "AND s.ganhador IS NULL")
    List<SorteioModel> findSorteiosParaEncerrarAutomaticamente(@Param("agora") LocalDateTime agora,
                                                               @Param("status") StatusSorteio status);

    default List<SorteioModel> findSorteiosParaEncerrarAutomaticamente(LocalDateTime agora) {
        return findSorteiosParaEncerrarAutomaticamente(agora, StatusSorteio.ativo);
    }
}