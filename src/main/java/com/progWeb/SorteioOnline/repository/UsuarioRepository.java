package com.progWeb.SorteioOnline.repository;

import com.progWeb.SorteioOnline.DTO.Response.UsuarioResposeDTO;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    Optional<UserDetails> findUserByEmail(String username);

    @Query(value = "SELECT new com.progWeb.SorteioOnline.DTO.Response.UsuarioResposeDTO(id, nome, email) " +
                    "FROM usuario")
    List<UsuarioResposeDTO> findAllByUsuarioRespose();

    @Query(value = "SELECT new com.progWeb.SorteioOnline.DTO.Response.UsuarioResposeDTO(u.id, u.nome, u.email) " +
                    "FROM usuario u WHERE u.id = :id")
    Optional<UsuarioResposeDTO> findByUsuarioRespose(@Param("id") Long id);
}