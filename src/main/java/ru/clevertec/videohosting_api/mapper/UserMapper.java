package ru.clevertec.videohosting_api.mapper;

import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.dto.user.UserUpdateDTO;
import ru.clevertec.videohosting_api.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserInfoDTO userToUserInfoDTO(User user);

    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            ignoreByDefault = true
    )
    @Mapping(source = "userUpdateDTO.nickname", target = "nickname")
    @Mapping(source = "userUpdateDTO.name", target = "name")
    @Mapping(source = "userUpdateDTO.email", target = "email")
    User patchUser(@MappingTarget User user, UserUpdateDTO userUpdateDTO);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "userUpdateDTO.nickname", target = "nickname")
    @Mapping(source = "userUpdateDTO.name", target = "name")
    @Mapping(source = "userUpdateDTO.email", target = "email")
    User updateUser(@MappingTarget User user, UserUpdateDTO userUpdateDTO);
}
