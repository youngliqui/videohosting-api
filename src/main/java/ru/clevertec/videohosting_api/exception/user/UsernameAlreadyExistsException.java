package ru.clevertec.videohosting_api.exception.user;

import ru.clevertec.videohosting_api.exception.AlreadyExistsException;

public class UsernameAlreadyExistsException extends AlreadyExistsException {
    public UsernameAlreadyExistsException(String message) {
        super(message);
    }
}
