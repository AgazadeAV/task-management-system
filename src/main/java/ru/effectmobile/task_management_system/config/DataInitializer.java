package ru.effectmobile.task_management_system.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.model.entity.User;
import ru.effectmobile.task_management_system.model.enums.Role;
import ru.effectmobile.task_management_system.repository.UserRepository;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (userRepository.existsByEmail("admin@example.com")) {
            return;
        }

        User admin = User.builder()
                .username("admin")
                .firstName("admin")
                .lastName("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("YourPassword123!"))
                .role(Role.ADMIN)
                .birthDate(LocalDate.of(1985, 10, 20))
                .phoneNumber("+71234567890")
                .build();

        userRepository.save(admin);
        System.out.println("✅ Admin user created: admin@example.com / YourPassword123!");
    }
}
