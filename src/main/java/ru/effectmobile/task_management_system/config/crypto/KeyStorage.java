package ru.effectmobile.task_management_system.config.crypto;

import javax.crypto.SecretKey;

public interface KeyStorage {
    SecretKey getAesKey();
}
