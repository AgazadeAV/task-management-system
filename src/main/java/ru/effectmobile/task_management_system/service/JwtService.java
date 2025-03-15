package ru.effectmobile.task_management_system.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;
import ru.effectmobile.task_management_system.model.User;

import java.util.function.Function;

public interface JwtService {

    String extractUsername(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    String generateToken(User user);

    boolean isTokenValid(String token, UserDetails userDetails);
}
