package ru.clevertec.videohosting_api.service.user.authentication;

import org.springframework.security.core.userdetails.UserDetailsService;
import ru.clevertec.videohosting_api.dto.security.ChangePasswordDTO;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.model.User;

public interface UserAuthenticationService {
    UserInfoDTO changePassword(Long userId, ChangePasswordDTO changePasswordDTO);

    User getCurrentUser();

    UserDetailsService userDetailsService();
}
