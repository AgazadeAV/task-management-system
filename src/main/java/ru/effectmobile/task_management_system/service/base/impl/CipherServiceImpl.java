package ru.effectmobile.task_management_system.service.base.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.repository.KeyStorage;
import ru.effectmobile.task_management_system.service.base.CipherService;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@Component
@RequiredArgsConstructor
public class CipherServiceImpl implements CipherService {

    private final KeyStorage keyStorage;

    @Value("${crypto.cipher-algorithm}")
    private String cipherAlgorithm;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = keyStorage.getAesKey();
        log.info("CipherService initialized with algorithm: {}", cipherAlgorithm);
    }

    @SneakyThrows
    @Override
    public String encrypt(String data) {
        log.debug("Encrypting data: {}", data);
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        String encryptedString = Base64.getEncoder().encodeToString(encryptedData);
        log.debug("Encryption successful");
        return encryptedString;
    }

    @SneakyThrows
    @Override
    public String decrypt(String encryptedData) {
        log.debug("Decrypting data");
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        String decryptedString = new String(decryptedData);
        log.debug("Decryption successful");
        return decryptedString;
    }
}
