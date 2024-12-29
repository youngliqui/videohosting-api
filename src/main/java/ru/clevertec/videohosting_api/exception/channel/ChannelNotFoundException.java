package ru.clevertec.videohosting_api.exception.channel;

import ru.clevertec.videohosting_api.exception.NotFoundException;

public class ChannelNotFoundException extends NotFoundException {
    public ChannelNotFoundException(String message) {
        super(message);
    }
}
