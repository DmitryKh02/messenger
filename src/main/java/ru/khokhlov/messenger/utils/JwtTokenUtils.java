package ru.khokhlov.messenger.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class JwtTokenUtils {
    @Value("${app.secret-key}")
    private String secret;

    @Value("${app.key-lifetime-minutes}")
    private Duration jwtLifetime;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        List<String> rolesList =userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();

        claims.put("roles", rolesList);

        Date issuedDate = new Date();
        Date expiredDate = new Date(issuedDate.getTime() + jwtLifetime.toMillis());
        byte[] mas = secret.getBytes(StandardCharsets.UTF_8);

        return  Jwts.builder()
                    .setClaims(claims)
                    .setSubject(userDetails.getUsername())
                    .setIssuedAt(issuedDate)
                    .setExpiration(expiredDate)
                    .signWith(SignatureAlgorithm.HS512, mas)
                    .compact();

    }

    public String getUsernameFromToken(String token){
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRolesFromToken(String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> claims = getAllClaimsFromToken(token);
        return objectMapper.convertValue(claims.get("roles"), new TypeReference<List<String>>() {});
    }

    private Claims getAllClaimsFromToken(String token){
        return  Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }
}
