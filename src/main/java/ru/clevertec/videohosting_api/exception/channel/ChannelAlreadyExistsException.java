package ru.clevertec.videohosting_api.exception.channel;

import ru.clevertec.videohosting_api.exception.AlreadyExistsException;

public class ChannelAlreadyExistsException extends AlreadyExistsException {
    public ChannelAlreadyExistsException(String message) {
        super(message);
    }
}
