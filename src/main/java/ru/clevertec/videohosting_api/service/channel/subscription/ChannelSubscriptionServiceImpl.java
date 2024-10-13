package ru.clevertec.videohosting_api.service.channel.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.clevertec.videohosting_api.exception.ChannelNotFoundException;
import ru.clevertec.videohosting_api.exception.SubscribedAlreadyException;
import ru.clevertec.videohosting_api.exception.UnauthorizedActionException;
import ru.clevertec.videohosting_api.model.Channel;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.ChannelRepository;
import ru.clevertec.videohosting_api.service.user.authentication.UserAuthenticationService;

@Service
public class ChannelSubscriptionServiceImpl implements ChannelSubscriptionService {
    private final ChannelRepository channelRepository;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public ChannelSubscriptionServiceImpl(
            ChannelRepository channelRepository,
            UserAuthenticationService userAuthenticationService
    ) {
        this.channelRepository = channelRepository;
        this.userAuthenticationService = userAuthenticationService;
    }

    @Override
    @Transactional
    public void subscribeToChannel(Long channelId, Long userId) {
        User currentUser = validateUser(userId);
        Channel channel = getChannelById(channelId);

        if (channel.getSubscribers().contains(currentUser)) {
            throw new SubscribedAlreadyException("User is already subscribed to this channel");
        }

        channel.getSubscribers().add(currentUser);
        currentUser.getSubscriptions().add(channel);

        channelRepository.save(channel);
    }

    @Override
    @Transactional
    public void unsubscribeFromChannel(Long channelId, Long userId) {
        User currentUser = validateUser(userId);
        Channel channel = getChannelById(channelId);

        if (!channel.getSubscribers().contains(currentUser)) {
            throw new SubscribedAlreadyException("The user has not subscribed to this channel yet");
        }

        channel.getSubscribers().remove(currentUser);
        currentUser.getSubscriptions().remove(channel);

        channelRepository.save(channel);
    }

    private User validateUser(Long userId) {
        User currentUser = userAuthenticationService.getCurrentUser();
        if (!currentUser.getId().equals(userId)) {
            throw new UnauthorizedActionException("User is not authorized to subscribe to this channel");
        }

        return currentUser;
    }

    private Channel getChannelById(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));
    }
}
