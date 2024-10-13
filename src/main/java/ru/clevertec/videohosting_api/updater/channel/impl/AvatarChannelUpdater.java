package ru.clevertec.videohosting_api.updater.channel.impl;

import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.model.Channel;
import ru.clevertec.videohosting_api.service.avatar.AvatarService;
import ru.clevertec.videohosting_api.updater.channel.ChannelUpdater;

public class AvatarChannelUpdater implements ChannelUpdater {
    private final AvatarService avatarService;

    public AvatarChannelUpdater(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @Override
    public void update(Channel channel, ChannelUpdateDTO channelUpdateDTO) {
        if (channelUpdateDTO.getAvatar() != null) {
            String encodedAvatar = avatarService.encodeAvatar(channelUpdateDTO.getAvatar());
            channel.setAvatar(encodedAvatar);
        }
    }
}
