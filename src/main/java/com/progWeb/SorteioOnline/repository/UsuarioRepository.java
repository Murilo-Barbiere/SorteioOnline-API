package com.progWeb.SorteioOnline.repository;

import com.progWeb.SorteioOnline.DTO.Response.UsuarioResponseDTO;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioModel, Long> {
    Optional<UserDetails> findUserByEmail(String username);

    @Query(value = "SELECT new com.progWeb.SorteioOnline.DTO.Response.UsuarioResponseDTO(id, nome, email, role) " +
                    "FROM usuario u " +
                    "WHERE u.role = 0")
    List<UsuarioResponseDTO> findAllByUsuarioRespose();

    @Query(value = "SELECT new com.progWeb.SorteioOnline.DTO.Response.UsuarioResponseDTO(u.id, u.nome, u.email, u.role) " +
                    "FROM usuario u WHERE u.id = :id")
    Optional<UsuarioResponseDTO> findByUsuarioRespose(@Param("id") Long id);
}