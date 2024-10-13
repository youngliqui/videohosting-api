package ru.clevertec.videohosting_api.service.user.management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import ru.clevertec.videohosting_api.dto.user.UserUpdateDTO;
import ru.clevertec.videohosting_api.model.User;

public interface UserManagementService {
    User save(User user);

    User create(User user);

    User updateUser(Long userId, UserUpdateDTO updateDTO);

    User applyPatchToUser(Long userId, JsonPatch patch) throws JsonPatchException, JsonProcessingException;
}
