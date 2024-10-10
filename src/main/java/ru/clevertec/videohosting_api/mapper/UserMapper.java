package ru.clevertec.videohosting_api.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.videohosting_api.dto.UserInfoDTO;
import ru.clevertec.videohosting_api.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserInfoDTO userToUserInfoDTO(User user);

    User userInfoDTOToUser(UserInfoDTO userInfoDTO);
}