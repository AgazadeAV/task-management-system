package ru.effectmobile.task_management_system.config.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.effectmobile.task_management_system.config.crypto.CipherService;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";
    private static final int TOKEN_PREFIX_LENGTH = TOKEN_PREFIX.length();

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final CipherService cipherService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith(TOKEN_PREFIX)) {
            log.debug("No valid Authorization header found in request to '{}'", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(TOKEN_PREFIX_LENGTH);
        log.debug("Extracted JWT token from request: {}", jwt);

        try {
            userEmail = jwtService.extractUsername(jwt).trim();
            log.debug("Extracted username from JWT: {}", cipherService.decrypt(userEmail));
        } catch (Exception e) {
            log.warn("Failed to extract username from JWT: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            log.debug("No existing authentication found in SecurityContext. Validating token...");

            UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
            if (jwtService.isTokenValid(jwt, userDetails)) {
                log.info("JWT token is valid for user: {}", cipherService.decrypt(userEmail));
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("User '{}' successfully authenticated and added to SecurityContext.", cipherService.decrypt(userEmail));
            } else {
                log.warn("JWT token is invalid for user: {}", cipherService.decrypt(userEmail));
            }
        } else {
            log.debug("User '{}' is already authenticated.", cipherService.decrypt(userEmail));
        }

        filterChain.doFilter(request, response);
    }
}
