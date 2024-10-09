package ru.clevertec.videohosting_api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.clevertec.videohosting_api.dto.UserInfoDTO;
import ru.clevertec.videohosting_api.exception.UserNotFoundException;
import ru.clevertec.videohosting_api.model.User;

public interface UserService {
    Page<UserInfoDTO> getAllUsers(Pageable pageable);

    UserInfoDTO getUserInfoById(Long userId) throws UserNotFoundException;

    User save(User user);

    User create(User user);

    User getByUsername(String username);

    User getCurrentUser();

    UserDetailsService userDetailsService();
}
