package ru.clevertec.videohosting_api.service.user.management;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.dto.user.UserUpdateDTO;
import ru.clevertec.videohosting_api.exception.user.EmailAlreadyExistsException;
import ru.clevertec.videohosting_api.exception.user.UserNotFoundException;
import ru.clevertec.videohosting_api.exception.user.UsernameAlreadyExistsException;
import ru.clevertec.videohosting_api.mapper.UserMapper;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    private final UserRepository userRepository;

    @Override
    public UserInfoDTO save(User user) {
        User savedUser = userRepository.save(user);
        return UserMapper.INSTANCE.userToUserInfoDTO(savedUser);
    }

    @Override
    public UserInfoDTO create(User user) {
        if (userRepository.existsByNickname(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username = " + user.getUsername() + " already exists");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new EmailAlreadyExistsException("Email = " + user.getEmail() + " already exists");
        }

        return save(user);
    }

    @Override
    public UserInfoDTO updateUser(Long userId, UserUpdateDTO updateDTO) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = " + userId + " was not found"));
        user = UserMapper.INSTANCE.updateUser(user, updateDTO);

        return save(user);
    }

    @Override
    public UserInfoDTO patchUser(Long userId, UserUpdateDTO updateDTO) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = " + userId + " was not found"));
        user = UserMapper.INSTANCE.patchUser(user, updateDTO);

        return save(user);
    }
}
