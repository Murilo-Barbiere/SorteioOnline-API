package com.progWeb.SorteioOnline.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Optional;

@Component
public class TonkenConfig {

    private String secret = "secret";
    Algorithm algorithm = Algorithm.HMAC256(secret);

    public  String generateToken(UsuarioModel user){
        return JWT.create()
                .withClaim("userId", user.getId())
                .withClaim("role", String.valueOf(user.getRole()))
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(86400))
                .withIssuedAt(Instant.now())
                .sign(algorithm)
        ;
    }

    public Optional<JWTUserData> validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            DecodedJWT deCode = JWT.require(algorithm).build().verify(token);

            return Optional.of(new JWTUserData(
                deCode.getClaim("userId").asLong(),
                deCode.getSubject(),
                deCode.getClaim("role").asString()
                ));

        }catch (JWTVerificationException e){
            return  Optional.empty();
        }
    }
}