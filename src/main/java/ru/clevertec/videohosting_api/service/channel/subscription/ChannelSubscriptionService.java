package ru.clevertec.videohosting_api.service.channel.subscription;

public interface ChannelSubscriptionService {
    void subscribeToChannel(Long channelId, Long userId);

    void unsubscribeFromChannel(Long channelId, Long userId);
}
