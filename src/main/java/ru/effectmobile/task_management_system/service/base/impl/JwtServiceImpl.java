package ru.effectmobile.task_management_system.service.base.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.service.base.JwtService;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    private final Key signInKey;
    private final long jwtExpirationMs;
    private final String issuer;

    public JwtServiceImpl(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long jwtExpirationMs,
            @Value("${jwt.issuer}") String issuer,
            @Value("${hash.algorithm}") String hashAlgorithm
    ) {
        this.signInKey = new SecretKeySpec(Base64.getDecoder().decode(secretKey), hashAlgorithm);
        this.jwtExpirationMs = jwtExpirationMs;
        this.issuer = issuer;
        log.info("JWT Service initialized with expiration: {}ms, issuer: {}", jwtExpirationMs, issuer);
    }

    @Override
    public String extractUsername(String token) {
        String username = extractClaim(token, Claims::getSubject);
        log.debug("Extracted username from token: {}", username);
        return username;
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        log.debug("Extracted claims: {}", claims);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(User user) {
        String token = Jwts.builder()
                .setClaims(Map.of("role", user.getRole().name()))
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .setIssuer(issuer)
                .signWith(signInKey, SignatureAlgorithm.HS256)
                .compact();
        log.info("Generated JWT token for user: {}", user.getEmail());
        return token;
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        boolean valid = username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isIssuerValid(token);
        log.info("Token validation result for user {}: {}", username, valid);
        return valid;
    }

    private boolean isTokenExpired(String token) {
        boolean expired = extractExpiration(token).before(new Date());
        log.debug("Token expiration check: {}", expired);
        return expired;
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isIssuerValid(String token) {
        boolean valid = issuer.equals(extractIssuer(token));
        log.debug("Issuer validation result: {}", valid);
        return valid;
    }

    private String extractIssuer(String token) {
        return extractClaim(token, Claims::getIssuer);
    }

    private Claims extractAllClaims(String token) {
        log.debug("Parsing JWT token...");
        return Jwts.parserBuilder()
                .setSigningKey(signInKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
