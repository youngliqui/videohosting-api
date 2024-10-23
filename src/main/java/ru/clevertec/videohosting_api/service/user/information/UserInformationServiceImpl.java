package ru.clevertec.videohosting_api.service.user.information;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.dto.user.UserSubscriptionDTO;
import ru.clevertec.videohosting_api.exception.user.UserNotFoundException;
import ru.clevertec.videohosting_api.mapper.UserMapper;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserInformationServiceImpl implements UserInformationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public Page<UserInfoDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::userToUserInfoDTO);
    }

    @Override
    public UserInfoDTO getUserInfoById(Long userId) throws UserNotFoundException {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = " + userId + " was not found"));

        return userMapper.userToUserInfoDTO(user);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByNickname(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User with nickname = " + username + " was not found"));
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
