package ru.effectmobile.task_management_system.repository;

import javax.crypto.SecretKey;

public interface KeyStorage {
    SecretKey getAesKey();
}
