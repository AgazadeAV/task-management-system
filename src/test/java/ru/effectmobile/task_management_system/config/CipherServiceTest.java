package ru.effectmobile.task_management_system.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.effectmobile.task_management_system.config.crypto.CipherService;
import ru.effectmobile.task_management_system.exception.custom.encryption.EncryptionException;
import ru.effectmobile.task_management_system.config.crypto.KeyStorage;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.lang.reflect.Field;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CipherServiceTest {

    private static final String CIPHER_ALGORITHM = "AES";

    private CipherService cipherService;

    @Mock
    private KeyStorage keyStorage;

    @BeforeEach
    void setUp() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(CIPHER_ALGORITHM);
        keyGen.init(128);
        SecretKey secretKey = keyGen.generateKey();

        when(keyStorage.getAesKey()).thenReturn(secretKey);

        cipherService = new CipherService(keyStorage);
        Field field = CipherService.class.getDeclaredField("cipherAlgorithm");
        field.setAccessible(true);
        field.set(cipherService, CIPHER_ALGORITHM);
        cipherService.init();
    }

    @Test
    void encrypt_ShouldReturnEncryptedData() {
        String plainText = "HelloWorld123";
        String encrypted = cipherService.encrypt(plainText);

        assertNotNull(encrypted);
        assertNotEquals(plainText, encrypted);
    }

    @Test
    void decrypt_ShouldReturnOriginalData() {
        String plainText = "SensitiveData";
        String encrypted = cipherService.encrypt(plainText);
        String decrypted = cipherService.decrypt(encrypted);

        assertEquals(plainText, decrypted);
    }

    @Test
    void encrypt_decrypt_ShouldBeReversible() {
        String original = "Test123456!@#";
        String encrypted = cipherService.encrypt(original);
        String decrypted = cipherService.decrypt(encrypted);

        assertEquals(original, decrypted);
    }

    @Test
    void encrypt_ShouldHandleEmptyString() {
        String encrypted = cipherService.encrypt("");
        assertNotNull(encrypted);
        assertFalse(encrypted.isBlank());

        String decrypted = cipherService.decrypt(encrypted);
        assertEquals("", decrypted);
    }

    @Test
    void encrypt_ShouldThrowException_WhenNullInput() {
        assertThrows(EncryptionException.class, () -> cipherService.encrypt(null));
    }

    @Test
    void decrypt_ShouldThrowException_WhenInputIsNotBase64() {
        String notBase64 = "%%%invalid-base64###";
        assertThrows(EncryptionException.class, () -> cipherService.decrypt(notBase64));
    }

    @Test
    void decrypt_ShouldThrowException_WhenInputIsInvalidEncryptedData() {
        String base64ButInvalid = Base64.getEncoder().encodeToString("invalid".getBytes());
        assertThrows(EncryptionException.class, () -> cipherService.decrypt(base64ButInvalid));
    }
}
