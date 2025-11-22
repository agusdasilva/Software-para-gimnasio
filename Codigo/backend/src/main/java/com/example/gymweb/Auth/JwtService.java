package com.example.gymweb.Auth;

import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.util.Date;
import javax.crypto.SecretKey;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;


@Service
public class JwtService {

    private static final String SECRET =
            "esta_es_una_clave_super_larga_para_jwt_que_debe_tener_muchos_caracteres_1234567890";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    // GENERAR TOKEN
    public String generarToken(String subject) {

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hs
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // EXTRAER EMAIL
    public String extraerSubject(String token) {
        return parseClaims(token).getBody().getSubject();
    }

    // VALIDAR TOKEN
    public boolean esTokenValido(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // PARSE INTERNO
    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
}
