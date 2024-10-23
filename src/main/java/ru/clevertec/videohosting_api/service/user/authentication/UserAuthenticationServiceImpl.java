package ru.clevertec.videohosting_api.service.user.authentication;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.dto.security.ChangePasswordDTO;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.exception.user.IncorrectPasswordException;
import ru.clevertec.videohosting_api.exception.user.PasswordMismatchException;
import ru.clevertec.videohosting_api.exception.user.UserNotFoundException;
import ru.clevertec.videohosting_api.mapper.UserMapper;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.UserRepository;
import ru.clevertec.videohosting_api.service.user.information.UserInformationService;

@Service
@RequiredArgsConstructor
public class UserAuthenticationServiceImpl implements UserAuthenticationService {
    private final UserInformationService userInformationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userInformationService.getByUsername(username);
    }

    @Override
    public UserDetailsService userDetailsService() {
        return userInformationService::getByUsername;
    }

    @Override
    public UserInfoDTO changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        User user = userRepository.findUserById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException("User with id = " + userId + " was not found"));

        if (!passwordEncoder.matches(changePasswordDTO.getCurrentPassword(), user.getPassword())) {
            throw new IncorrectPasswordException("Current password is incorrect");
        }

        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new PasswordMismatchException("New password and confirmation do not match");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        User savedUser = userRepository.save(user);

        return userMapper.userToUserInfoDTO(savedUser);
    }
}
