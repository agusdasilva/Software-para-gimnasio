package com.example.gymweb.Auth;

<<<<<<< Updated upstream
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.util.Date;
import javax.crypto.SecretKey;
=======

>>>>>>> Stashed changes
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
<<<<<<< Updated upstream

=======
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;
>>>>>>> Stashed changes

@Service
public class JwtService {

    private static final String SECRET =
            "esta_es_una_clave_super_larga_para_jwt_que_debe_tener_muchos_caracteres_1234567890";

<<<<<<< Updated upstream
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
=======
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    public String generarToken(String subject) {

        Date ahora = new Date();
        Date expiracion = new Date(ahora.getTime() + 86400000L); // 1 día

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(ahora)
                .setExpiration(expiracion)
                .signWith(key)
                .compact();
    }

>>>>>>> Stashed changes
    public String extraerSubject(String token) {
        return parseClaims(token).getBody().getSubject();
    }

<<<<<<< Updated upstream
    // VALIDAR TOKEN
=======
>>>>>>> Stashed changes
    public boolean esTokenValido(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

<<<<<<< Updated upstream
    // PARSE INTERNO
=======
>>>>>>> Stashed changes
    private Jws<Claims> parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }
<<<<<<< Updated upstream
}
=======
}
>>>>>>> Stashed changes
