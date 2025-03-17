package ru.effectmobile.task_management_system.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.repository.UserRepository;
import ru.effectmobile.task_management_system.service.base.CipherService;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CipherService cipherService;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.first-name}")
    private String adminFirstName;

    @Value("${admin.last-name}")
    private String adminLastName;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.birth-date}")
    private String adminBirthDate;

    @Value("${admin.phone-number}")
    private String adminPhoneNumber;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail(cipherService.encrypt(adminEmail))) return;

        User admin = User.builder()
                .username(cipherService.encrypt(adminUsername))
                .firstName(adminFirstName)
                .lastName(adminLastName)
                .email(cipherService.encrypt(adminEmail))
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ROLE_ADMIN)
                .birthDate(LocalDate.parse(adminBirthDate))
                .phoneNumber(cipherService.encrypt(adminPhoneNumber))
                .build();

        userRepository.save(admin);
    }
}
