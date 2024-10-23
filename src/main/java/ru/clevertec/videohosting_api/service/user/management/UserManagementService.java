package ru.clevertec.videohosting_api.service.user.management;

import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.dto.user.UserUpdateDTO;
import ru.clevertec.videohosting_api.model.User;

public interface UserManagementService {
    UserInfoDTO save(User user);

    UserInfoDTO create(User user);

    UserInfoDTO updateUser(Long userId, UserUpdateDTO updateDTO);

    UserInfoDTO patchUser(Long userId, UserUpdateDTO userUpdateDTO);
}
