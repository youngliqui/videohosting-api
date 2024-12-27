package ru.clevertec.videohosting_api.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.exception.channel.ChannelNotFoundException;
import ru.clevertec.videohosting_api.model.Channel;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.ChannelRepository;

@Service
@RequiredArgsConstructor
public class AccessServiceImpl implements AccessService {
    private final ChannelRepository channelRepository;

    @Override
    public boolean canUserChangeChannel(Long channelId, User currentUser) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        return currentUser.getRole().name().equals("ADMIN") ||
                currentUser.getId().equals(channel.getAuthor().getId());
    }
}
