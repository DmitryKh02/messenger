package ru.khokhlov.messenger.config;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.khokhlov.messenger.utils.JwtTokenUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenUtils jwtTokenUtils;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String nickname = null;
        String jwt = null;

        if(authHeader != null && authHeader.startsWith("Bearer ")){
            jwt = authHeader.substring(7);
            try {
                nickname = jwtTokenUtils.getUsernameFromToken(jwt);
            } catch (ExpiredJwtException exception) {
                log.error("JwtRequestFilter.doFilterInternal - Token lifetime is over");
            }
            catch (SignatureException e){
                log.error("Wrong signature");
            }
        }

        if(nickname!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    nickname,
                    null,
                    jwtTokenUtils.getRolesFromToken(jwt).stream().map(SimpleGrantedAuthority::new).toList()
            );

            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request,response);
    }
}
