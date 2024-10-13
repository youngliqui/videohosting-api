package ru.clevertec.videohosting_api.updater.channel;

import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.model.Channel;

public interface ChannelUpdater {
    void update(Channel channel, ChannelUpdateDTO channelUpdateDTO);
}
