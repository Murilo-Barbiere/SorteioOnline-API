package com.progWeb.SorteioOnline.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.progWeb.SorteioOnline.DTO.JWTUserData;
import com.progWeb.SorteioOnline.model.UsuarioModel;
import org.springframework.stereotype.Component;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

@Component
public class TokenConfig {

    private String secret = "secret";
    Algorithm algorithm = Algorithm.HMAC256(secret);

    public  String generateToken(UsuarioModel user){
        return JWT.create()
                .withClaim("userId", user.getId())
                .withClaim("role", String.valueOf(user.getRole()))
                .withSubject(user.getEmail())
                .withExpiresAt(Instant.now().plusSeconds(1800))
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

    public String validarTokenGoogle(String token) {
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance()
            ).setAudience(Collections.singletonList("923197901865-70b6fpl6pou975uejhsq187bgecgia5v.apps.googleusercontent.com")).build();

            GoogleIdToken idToken = verifier.verify(token);

            if (idToken == null) {
                throw new RuntimeException("Token inválido");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();

            return payload.getEmail();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}