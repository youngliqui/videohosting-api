package ru.clevertec.videohosting_api.exception.channel;

import ru.clevertec.videohosting_api.exception.AlreadyExistsException;

public class SubscribedAlreadyException extends AlreadyExistsException {
    public SubscribedAlreadyException(String message) {
        super(message);
    }
}
