package ru.effectmobile.task_management_system.service.base.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.service.base.CipherService;
import ru.effectmobile.task_management_system.service.base.UserService;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final CipherService cipherService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        String encryptedEmail = cipherService.encrypt(email);
        User user = userService.findByEmail(encryptedEmail);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
