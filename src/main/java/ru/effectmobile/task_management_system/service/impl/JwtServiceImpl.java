package ru.effectmobile.task_management_system.service.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.effectmobile.task_management_system.model.User;
import ru.effectmobile.task_management_system.service.JwtService;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private final Key signInKey;
    private final long jwtExpirationMs;
    private final String issuer;

    public JwtServiceImpl(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration}") long jwtExpirationMs,
            @Value("${jwt.issuer}") String issuer
    ) {
        this.signInKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        this.jwtExpirationMs = jwtExpirationMs;
        this.issuer = issuer;
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(User user) {
        return Jwts.builder()
                .setClaims(Map.of("role", user.getRole().name()))
                .setSubject(user.getEmail())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .setIssuer(issuer)
                .signWith(signInKey, SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token) && isIssuerValid(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private boolean isIssuerValid(String token) {
        return issuer.equals(extractIssuer(token));
    }

    private String extractIssuer(String token) {
        return extractClaim(token, Claims::getIssuer);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signInKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
