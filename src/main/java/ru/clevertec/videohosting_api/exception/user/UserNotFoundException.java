package ru.clevertec.videohosting_api.exception.user;

import ru.clevertec.videohosting_api.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
