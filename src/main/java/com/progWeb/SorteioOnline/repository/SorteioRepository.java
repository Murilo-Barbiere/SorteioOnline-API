package com.progWeb.SorteioOnline.repository;

import com.progWeb.SorteioOnline.DTO.Response.UsuarioResposeDTO;
import com.progWeb.SorteioOnline.model.SorteioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface SorteioRepository extends JpaRepository<SorteioModel, Long> {

    @Query("SELECT new com.progWeb.SorteioOnline.DTO.Response.UsuarioResposeDTO(u.id, u.nome, u.email) " +
            "FROM sorteio s " +
            "JOIN s.participante u " +
            "WHERE s.id = :idSorteio")
    List<UsuarioResposeDTO> findParticipantes(@Param("idSorteio") Long idSorteio);
}