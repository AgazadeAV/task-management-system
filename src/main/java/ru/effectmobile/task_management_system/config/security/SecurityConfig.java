package ru.effectmobile.task_management_system.config.security;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.effectmobile.task_management_system.model.enums.Role;

import java.time.LocalDateTime;

import static ru.effectmobile.task_management_system.controller.AuthController.AUTH_API_URL;
import static ru.effectmobile.task_management_system.controller.UserController.USER_API_URL;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.ADMIN_ACCESS_DENIED_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.AUTHENTICATION_REQUIRED_MESSAGE;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Value("${api.base.url}")
    private String apiBaseUrl;

    @Value("${springdoc.api-docs.path}")
    private String apiDocsPath;

    @Value("${springdoc.swagger-ui.path}")
    private String swaggerUiPath;

    private static final String ROLE_ADMIN = Role.ROLE_ADMIN.name();

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        log.info("Configuring security filter chain...");
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers(getPublicEndpoints()).permitAll();
                    auth.requestMatchers(getAdminEndpoints()).hasAuthority(ROLE_ADMIN);
                    auth.anyRequest().authenticated();
                })
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            log.warn("Access denied: {}", request.getRequestURI());
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(getJsonResponse(HttpServletResponse.SC_FORBIDDEN, ADMIN_ACCESS_DENIED_MESSAGE, request.getRequestURI()));
                        })
                        .authenticationEntryPoint((request, response, authException) -> {
                            log.warn("Unauthorized access attempt: {}", request.getRequestURI());
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write(getJsonResponse(HttpServletResponse.SC_UNAUTHORIZED, AUTHENTICATION_REQUIRED_MESSAGE, request.getRequestURI()));
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        log.info("Security filter chain configured successfully.");
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        log.info("Configuring AuthenticationManager with DaoAuthenticationProvider...");
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        log.info("AuthenticationManager configured successfully.");
        return new ProviderManager(authProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        log.debug("Creating BCryptPasswordEncoder bean...");
        return new BCryptPasswordEncoder();
    }

    private String[] getPublicEndpoints() {
        String[] endpoints = new String[]{
                apiBaseUrl + AUTH_API_URL + "/**",
                apiDocsPath + "/**",
                swaggerUiPath,
                swaggerUiPath.replaceAll("\\.\\w+$", "") + "/**",
                "/actuator/**"
        };
        log.debug("Public endpoints configured: {}", (Object) endpoints);
        return endpoints;
    }

    private String[] getAdminEndpoints() {
        String[] endpoints = new String[]{
                apiBaseUrl + USER_API_URL + "/**"
        };
        log.debug("Admin endpoints configured: {}", (Object) endpoints);
        return endpoints;
    }

    private String getJsonResponse(int status, String message, String path) {
        return String.format("""
        {
            "timestamp": "%s",
            "status": %d,
            "error": "%s",
            "message": "%s",
            "path": "%s"
        }
        """,
                LocalDateTime.now(),
                status,
                HttpStatus.valueOf(status).getReasonPhrase(),
                message,
                path
        );
    }
}
