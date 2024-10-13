package ru.clevertec.videohosting_api.service.user.subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.dto.user.UserSubscriptionDTO;
import ru.clevertec.videohosting_api.exception.user.UserNotFoundException;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.UserRepository;

import java.util.List;

@Service
public class UserSubscriptionServiceImpl implements UserSubscriptionService {
    private final UserRepository userRepository;

    @Autowired
    public UserSubscriptionServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<UserSubscriptionDTO> getUserSubscriptions(Long userId) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = " + userId + " was not found"));

        return user.getSubscriptions().stream()
                .map(channel -> UserSubscriptionDTO.builder()
                        .channelId(channel.getId())
                        .channelName(channel.getName())
                        .build())
                .toList();
    }
}
