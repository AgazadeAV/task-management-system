package ru.effectmobile.task_management_system.config.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
import ru.effectmobile.task_management_system.repository.CommentRepository;
import ru.effectmobile.task_management_system.repository.TaskRepository;

import static ru.effectmobile.task_management_system.controller.AuthController.AUTH_API_URI;
import static ru.effectmobile.task_management_system.controller.CommentController.COMMENT_API_URI;
import static ru.effectmobile.task_management_system.controller.TaskController.GET_ALL_TASKS;
import static ru.effectmobile.task_management_system.controller.TaskController.GET_TASKS_WITH_FILTERS;
import static ru.effectmobile.task_management_system.controller.TaskController.GET_TASK_BY_ID;
import static ru.effectmobile.task_management_system.controller.TaskController.TASK_API_URI;
import static ru.effectmobile.task_management_system.controller.TaskController.UPDATE_TASK_BY_ID;
import static ru.effectmobile.task_management_system.controller.UserController.USER_API_URI;

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
    private static final String ROLE_USER = Role.ROLE_USER.name();

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
                    auth.requestMatchers(getUserEndpoints()).hasAnyAuthority(ROLE_ADMIN, ROLE_USER);
                    auth.anyRequest().authenticated();
                })
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

    @Bean
    public TaskSecurityService taskSecurityService(TaskRepository taskRepository) {
        log.info("Creating TaskSecurityService bean...");
        return new TaskSecurityService(taskRepository);
    }

    @Bean
    public CommentSecurityService commentSecurityService(CommentRepository commentRepository) {
        log.info("Creating CommentSecurityService bean...");
        return new CommentSecurityService(commentRepository);
    }

    private String[] getPublicEndpoints() {
        String[] endpoints = new String[]{
                apiBaseUrl + AUTH_API_URI + "/**",
                apiDocsPath + "/**",
                swaggerUiPath,
                swaggerUiPath.replaceAll("\\.\\w+$", "") + "/**"
        };
        log.debug("Public endpoints configured: {}", (Object) endpoints);
        return endpoints;
    }

    private String[] getUserEndpoints() {
        String[] endpoints = new String[]{
                apiBaseUrl + COMMENT_API_URI + "/**",
                apiBaseUrl + TASK_API_URI + GET_ALL_TASKS,
                apiBaseUrl + TASK_API_URI + GET_TASK_BY_ID,
                apiBaseUrl + TASK_API_URI + UPDATE_TASK_BY_ID,
                apiBaseUrl + TASK_API_URI + GET_TASKS_WITH_FILTERS
        };
        log.debug("User endpoints configured: {}", (Object) endpoints);
        return endpoints;
    }

    private String[] getAdminEndpoints() {
        String[] endpoints = new String[]{
                apiBaseUrl + USER_API_URI + "/**",
                apiBaseUrl + TASK_API_URI + "/**"
        };
        log.debug("Admin endpoints configured: {}", (Object) endpoints);
        return endpoints;
    }
}
