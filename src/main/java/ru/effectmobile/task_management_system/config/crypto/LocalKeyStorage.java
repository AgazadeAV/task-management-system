package ru.effectmobile.task_management_system.config.crypto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
public class LocalKeyStorage {

    @Bean
    public KeyStorage keyStorage(@Value("${crypto.keys.AES}") String aesKeyBase64) {
        byte[] decodedKey = Base64.getDecoder().decode(aesKeyBase64);
        SecretKey secretKey = new SecretKeySpec(decodedKey, "AES");

        return () -> secretKey;
    }
}
