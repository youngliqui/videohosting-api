package ru.clevertec.videohosting_api.service.security;

import ru.clevertec.videohosting_api.model.User;

public interface AccessService {
    boolean canUserChangeChannel(Long channelId, User currentUser);
}
