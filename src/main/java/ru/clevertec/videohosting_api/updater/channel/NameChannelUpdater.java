package ru.clevertec.videohosting_api.updater.channel;

import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.model.Channel;

public class NameChannelUpdater implements ChannelUpdater {
    @Override
    public void update(Channel channel, ChannelUpdateDTO channelUpdateDTO) {
        if (channelUpdateDTO.getName() != null) {
            channel.setName(channelUpdateDTO.getName());
        }
    }
}
