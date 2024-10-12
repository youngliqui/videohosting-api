package ru.clevertec.videohosting_api.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.dto.ChangePasswordDTO;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.dto.user.UserSubscriptionDTO;
import ru.clevertec.videohosting_api.dto.user.UserUpdateDTO;
import ru.clevertec.videohosting_api.exception.EmailAlreadyExistsException;
import ru.clevertec.videohosting_api.exception.UserNotFoundException;
import ru.clevertec.videohosting_api.exception.UsernameAlreadyExistsException;
import ru.clevertec.videohosting_api.mapper.UserMapper;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.UserRepository;
import ru.clevertec.videohosting_api.service.UserService;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            UserMapper userMapper,
            ObjectMapper jacksonObjectMapper,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.objectMapper = jacksonObjectMapper;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public User updateUser(Long userId, UserUpdateDTO updateDTO) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = " + userId + " was not found"));

        user.setNickname(updateDTO.getNickname());
        user.setName(updateDTO.getName());
        user.setEmail(updateDTO.getEmail());

        return userRepository.save(user);
    }

    @Override
    public User applyPatchToUser(Long userId, JsonPatch patch) throws JsonPatchException, JsonProcessingException {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = " + userId + " was not found"));

        JsonNode patchedUserNode = patch.apply(objectMapper.convertValue(user, JsonNode.class));
        User patchedUser = objectMapper.treeToValue(patchedUserNode, User.class);

        return userRepository.save(patchedUser);
    }

    @Override
    public User changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = " + userId + " was not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new RuntimeException("New password and confirmation do not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        return userRepository.save(user);
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
