package ru.clevertec.videohosting_api.exception.user;

import ru.clevertec.videohosting_api.exception.AlreadyExistsException;

public class EmailAlreadyExistsException extends AlreadyExistsException {
    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}
