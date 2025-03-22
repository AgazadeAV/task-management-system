package ru.effectmobile.task_management_system.config.crypto;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.effectmobile.task_management_system.exception.custom.encryption.EncryptionException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import java.util.Base64;

import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.DECRYPTION_FAILED_MESSAGE;
import static ru.effectmobile.task_management_system.exception.util.ExceptionMessageUtil.Messages.ENCRYPTION_FAILED_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class CipherService {

    private final KeyStorage keyStorage;

    @Value("${crypto.cipher-algorithm}")
    private String cipherAlgorithm;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = keyStorage.getAesKey();
        log.info("CipherService initialized with algorithm: {}", cipherAlgorithm);
    }

    public String encrypt(String data) {
        try {
            log.debug("Encrypting data: {}", data);
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] encryptedData = cipher.doFinal(data.getBytes());
            String encryptedString = Base64.getEncoder().encodeToString(encryptedData);
            log.debug("Encryption successful");
            return encryptedString;
        } catch (Exception e) {
            log.error("Encryption failed for data: {}", data, e);
            throw new EncryptionException(ENCRYPTION_FAILED_MESSAGE, e);
        }
    }

    public String decrypt(String encryptedData) {
        try {
            log.debug("Decrypting data");
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedData = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
            String decryptedString = new String(decryptedData);
            log.debug("Decryption successful");
            return decryptedString;
        } catch (Exception e) {
            log.error("Decryption failed for input", e);
            throw new EncryptionException(DECRYPTION_FAILED_MESSAGE, e);
        }
    }
}
