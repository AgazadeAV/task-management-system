package ru.effectmobile.task_management_system.config.crypto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableConfigurationProperties(LocalKeyStorage.KeyStorageProperties.class)
public class LocalKeyStorage {

    @Bean
    public KeyStorage keyStorage(KeyStorageProperties properties) {
        Map<String, SecretKey> aesStorage = new HashMap<>();
        for (Map.Entry<String, String> entry : properties.getKeys().entrySet()) {
            String alias = entry.getKey();
            byte[] decodedKey = Base64.getDecoder().decode(entry.getValue());
            SecretKey secretKey = new SecretKeySpec(decodedKey, alias);
            aesStorage.put(alias, secretKey);
        }

        return () -> aesStorage.get("AES");
    }

    @Setter
    @Getter
    @ConfigurationProperties(prefix = "crypto")
    public static class KeyStorageProperties {

        private Map<String, String> keys = new HashMap<>();
    }
}
