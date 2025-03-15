package ru.effectmobile.task_management_system.service.base.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.repository.KeyStorage;
import ru.effectmobile.task_management_system.service.base.CipherService;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

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
    }

    @SneakyThrows
    @Override
    public String encrypt(String data) {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedData = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedData);
    }

    @SneakyThrows
    @Override
    public String decrypt(String encryptedData) {
        Cipher cipher = Cipher.getInstance(cipherAlgorithm);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(decryptedData);
    }
}
