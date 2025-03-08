package org.quizzcloud.backend.auth.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtUtil.class);
    private final Key signingKey;
    private final long expirationTime;

    /**
     * Constructor: Decodifica la clave secreta y la valida.
     */
    public JwtUtil(@Value("${jwt.secret}") String secretKey,
                   @Value("${jwt.expiration}") long expirationMillis) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secretKey);

            // 🔹 Verifica que la clave tenga al menos 32 bytes para HS256
            if (decodedKey.length < 32) {
                throw new IllegalArgumentException("Clave JWT inválida. Debe tener al menos 32 bytes.");
            }

            this.signingKey = Keys.hmacShaKeyFor(decodedKey);
            this.expirationTime = expirationMillis;
            LOGGER.info("Clave JWT cargada correctamente.");
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Error al decodificar la clave JWT. Asegúrate de que esté en Base64.", e);
        }
    }

    /**
     * Genera un JWT con la información del usuario.
     */
    public String generateToken(String userId, String email, String role) {
        Instant now = Instant.now();

        String token = Jwts.builder()
                .setSubject(email)
                .claim("id", userId)
                .claim("role", role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plusMillis(expirationTime)))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();

        LOGGER.info("JWT generado exitosamente: {}", token);
        return token;
    }

    /**
     * Extrae el email desde un token JWT.
     */
    public String extractEmail(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (Exception e) {
            LOGGER.error("Error al extraer el email del JWT: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Valida un JWT y maneja diferentes tipos de excepciones.
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(signingKey)
                    .build()
                    .parseClaimsJws(token);

            LOGGER.info("JWT válido. Expira en: {}", claims.getBody().getExpiration());
            return true;

        } catch (ExpiredJwtException e) {
            LOGGER.error("Token expirado: {}", token);
        } catch (UnsupportedJwtException e) {
            LOGGER.error("Token no soportado: {}", token);
        } catch (MalformedJwtException e) {
            LOGGER.error("Token malformado: {}", token);
        } catch (SignatureException e) {
            LOGGER.error("Firma del token inválida: {}", token);
        } catch (Exception e) {
            LOGGER.error("Error al validar token: {}", token, e);
        }
        return false;
    }
}
