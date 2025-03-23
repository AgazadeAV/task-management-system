package ru.effectmobile.task_management_system.config.crypto;

import javax.crypto.SecretKey;

@FunctionalInterface
public interface KeyStorage {
    SecretKey getAesKey();
}
