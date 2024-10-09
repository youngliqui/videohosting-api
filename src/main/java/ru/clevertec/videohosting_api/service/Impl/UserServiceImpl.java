package ru.clevertec.videohosting_api.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.dto.UserInfoDTO;
import ru.clevertec.videohosting_api.exception.EmailAlreadyExistsException;
import ru.clevertec.videohosting_api.exception.UserNotFoundException;
import ru.clevertec.videohosting_api.exception.UsernameAlreadyExistsException;
import ru.clevertec.videohosting_api.mapper.UserMapper;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.UserRepository;
import ru.clevertec.videohosting_api.service.UserService;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
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
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User create(User user) {
        if (userRepository.existsByNickname(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username = " + user.getUsername() + " already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email = " + user.getEmail() + " already exists");
        }

        return save(user);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.findByNickname(username)
                .orElseThrow(() ->
                        new UserNotFoundException("User with nickname = " + username + " was not found"));
    }

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
}
