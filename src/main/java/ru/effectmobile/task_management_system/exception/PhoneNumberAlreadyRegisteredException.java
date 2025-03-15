package ru.effectmobile.task_management_system.exception;

public class PhoneNumberAlreadyRegisteredException extends AlreadyRegisteredException{

    public PhoneNumberAlreadyRegisteredException(String message) {
        super(message);
    }
}
