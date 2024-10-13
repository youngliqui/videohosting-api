package ru.clevertec.videohosting_api.service.user.subscription;

import ru.clevertec.videohosting_api.dto.user.UserSubscriptionDTO;

import java.util.List;

public interface UserSubscriptionService {
    List<UserSubscriptionDTO> getUserSubscriptions(Long userId);
}
