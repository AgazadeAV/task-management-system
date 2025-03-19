package ru.effectmobile.task_management_system.service.base.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.service.base.CipherService;
import ru.effectmobile.task_management_system.service.base.UserService;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final CipherService cipherService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.debug("Attempting to load user by email: {}", cipherService.decrypt(email));

        User user = userService.findByEmail(email);

        if (user == null) {
            log.warn("User not found with email: {}", cipherService.decrypt(email));
            throw new UsernameNotFoundException("User not found with email: " + cipherService.decrypt(email));
        }

        log.info("User loaded successfully: {}", cipherService.decrypt(email));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRole().name())
                .build();
    }
}
