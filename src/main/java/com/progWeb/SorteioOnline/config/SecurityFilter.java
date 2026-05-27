package com.progWeb.SorteioOnline.config;
import com.progWeb.SorteioOnline.DTO.JWTUserData;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private TokenConfig tonkenConfig;

    public SecurityFilter(TokenConfig tonkenConfig) {
        this.tonkenConfig = tonkenConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizedheader = request.getHeader("Authorization");

        if (authorizedheader != null && authorizedheader.startsWith("Bearer ")){
            String token = authorizedheader.substring(7);
            Optional<JWTUserData> optUser = tonkenConfig.validateToken(token);

            if(optUser.isPresent()){
                JWTUserData userData = optUser.get();

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userData, null, null);

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}