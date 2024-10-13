package ru.clevertec.videohosting_api.updater.channel.impl;

import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.model.Channel;
import ru.clevertec.videohosting_api.updater.channel.ChannelUpdater;

public class LanguageChannelUpdater implements ChannelUpdater {
    @Override
    public void update(Channel channel, ChannelUpdateDTO channelUpdateDTO) {
        if (channelUpdateDTO.getLanguage() != null) {
            channel.setLanguage(channelUpdateDTO.getLanguage());
        }
    }
}
