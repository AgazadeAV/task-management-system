package ru.effectmobile.task_management_system.repository;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "crypto")
public class LocalKeyStorage implements KeyStorage {

    private Map<String, String> keys;

    private final Map<String, SecretKey> aesStorage = new HashMap<>();

    @Override
    public SecretKey getAesKey() {
        return aesStorage.get("AES");
    }

    public void setKeys(Map<String, String> keys) {
        this.keys = keys;
        loadAesKeys();
    }

    private void loadAesKeys() {
        for (Map.Entry<String, String> entry : keys.entrySet()) {
            String alias = entry.getKey();
            byte[] decodedKey = Base64.getDecoder().decode(entry.getValue());
            SecretKey secretKey = new SecretKeySpec(decodedKey, alias);
            aesStorage.put(alias, secretKey);
        }
    }
}
