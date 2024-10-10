package ru.clevertec.videohosting_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.clevertec.videohosting_api.dto.ChangePasswordDTO;
import ru.clevertec.videohosting_api.dto.UserInfoDTO;
import ru.clevertec.videohosting_api.dto.UserSubscriptionDTO;
import ru.clevertec.videohosting_api.dto.UserUpdateDTO;
import ru.clevertec.videohosting_api.exception.UserNotFoundException;
import ru.clevertec.videohosting_api.model.User;

import java.util.List;

public interface UserService {
    Page<UserInfoDTO> getAllUsers(Pageable pageable);

    UserInfoDTO getUserInfoById(Long userId) throws UserNotFoundException;

    User save(User user);

    User create(User user);

    User getByUsername(String username);

    User getCurrentUser();

    UserDetailsService userDetailsService();

    User updateUser(Long userId, UserUpdateDTO updateDTO);

    User applyPatchToUser(Long userId, JsonPatch patch) throws JsonPatchException, JsonProcessingException;

    User changePassword(Long userId, ChangePasswordDTO changePasswordDTO);

    List<UserSubscriptionDTO> getUserSubscriptions(Long userId);
}
