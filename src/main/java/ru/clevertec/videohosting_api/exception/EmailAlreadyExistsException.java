package ru.clevertec.videohosting_api.exception;

public class EmailAlreadyExistsException extends AlreadyExistsException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
