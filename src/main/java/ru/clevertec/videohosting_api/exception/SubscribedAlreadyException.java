package ru.clevertec.videohosting_api.exception;

public class SubscribedAlreadyException extends AlreadyExistsException {
    public SubscribedAlreadyException(String message) {
        super(message);
    }
}
