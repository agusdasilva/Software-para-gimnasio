package com.example.gymweb.Auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret:}")
    private String secret;

    private SecretKey getKey() {
        if (secret == null || secret.isBlank()) {
            throw new IllegalStateException("Falta configurar JWT_SECRET (jwt.secret)");
        }
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generarToken(String subject) {
        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + 86400000L); // 1 día

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(getKey())
                .compact();
    }

    public String extraerSubject(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    public boolean esTokenValido(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token);
    }
}
