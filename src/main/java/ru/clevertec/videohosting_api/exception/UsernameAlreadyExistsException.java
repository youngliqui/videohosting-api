package ru.clevertec.videohosting_api.exception;

public class UsernameAlreadyExistsException extends AlreadyExistsException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
