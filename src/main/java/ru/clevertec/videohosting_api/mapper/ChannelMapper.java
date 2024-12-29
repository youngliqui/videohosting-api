package ru.clevertec.videohosting_api.mapper;

import org.mapstruct.*;
import org.springframework.web.multipart.MultipartFile;
import ru.clevertec.videohosting_api.dto.category.CategoryInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelCreateDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelExtendedInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.model.Category;
import ru.clevertec.videohosting_api.model.Channel;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.util.ImageUtils;

import java.util.Optional;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface ChannelMapper {

    @Mapping(source = "subscribers", target = "subscribersCount", qualifiedByName = "subscribersToSubscribersCount")
    @Mapping(source = "category.name", target = "categoryName")
    ChannelInfoDTO channelToChannelInfoDTO(Channel channel);

    @Mapping(source = "subscribers", target = "subscribersCount", qualifiedByName = "subscribersToSubscribersCount")
    @Mapping(target = "category", qualifiedByName = "categoryToCategoryInfoDTO")
    ChannelExtendedInfoDTO channelToChannelExtendedInfoDTO(Channel channel);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "channelCreateDTO.name", target = "name")
    @Mapping(source = "channelCreateDTO.name", target = "language")
    @Mapping(target = "subscribers", expression = "java( new java.util.HashSet() )")
    @Mapping(source = "category", target = "category")
    @Mapping(source = "author", target = "author")
    Channel channelCreateDTOToChannel(ChannelCreateDTO channelCreateDTO, User author, Category category);


    @BeanMapping(
            nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS,
            ignoreByDefault = true
    )
    @Mapping(source = "channelUpdateDTO.name", target = "name")
    @Mapping(source = "channelUpdateDTO.avatar", target = "avatar", qualifiedByName = "mapAvatar")
    @Mapping(source = "channelUpdateDTO.description", target = "description")
    @Mapping(source = "channelUpdateDTO.language", target = "language")
    @Mapping(source = "category", target = "category")
    Channel patchChannel(@MappingTarget Channel channel, ChannelUpdateDTO channelUpdateDTO, Category category);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "channelUpdateDTO.name", target = "name")
    @Mapping(source = "channelUpdateDTO.avatar", target = "avatar", qualifiedByName = "mapAvatar")
    @Mapping(source = "channelUpdateDTO.description", target = "description")
    @Mapping(source = "channelUpdateDTO.language", target = "language")
    @Mapping(source = "category", target = "category")
    Channel updateChannel(@MappingTarget Channel channel, ChannelUpdateDTO channelUpdateDTO, Category category);


    @Named("mapAvatar")
    static String mapAvatar(MultipartFile multipartFile) {
        return ImageUtils.encodeAvatar(multipartFile);
    }

    @Named("categoryToCategoryInfoDTO")
    static CategoryInfoDTO mapCategory(Category category) {
        return Optional.ofNullable(category)
                .map(c -> CategoryInfoDTO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .orElse(null);
    }

    @Named("subscribersToSubscribersCount")
    static Long mapSubscribers(Set<User> subscribers) {
        return Optional.ofNullable(subscribers)
                .map(subs -> (long) subs.size())
                .orElse(0L);
    }
}
