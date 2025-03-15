package ru.effectmobile.task_management_system.service.base;

public interface CipherService {

    String encrypt(String plainText);

    String decrypt(String encryptedText);
}
