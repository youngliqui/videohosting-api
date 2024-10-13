package ru.clevertec.videohosting_api.service.user.information;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.exception.UserNotFoundException;
import ru.clevertec.videohosting_api.mapper.UserMapper;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.UserRepository;

@Service
public class UserInformationServiceImpl implements UserInformationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserInformationServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }


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
}
