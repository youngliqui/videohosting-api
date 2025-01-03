package ru.clevertec.videohosting_api.service.user.information;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.dto.user.UserSubscriptionDTO;
import ru.clevertec.videohosting_api.exception.user.UserNotFoundException;
import ru.clevertec.videohosting_api.model.User;

import java.util.List;

public interface UserInformationService {
    Page<UserInfoDTO> getAllUsers(Pageable pageable);

    UserInfoDTO getUserInfoById(Long userId) throws UserNotFoundException;

    User getByUsername(String username);

    List<UserSubscriptionDTO> getUserSubscriptions(Long userId);
}
