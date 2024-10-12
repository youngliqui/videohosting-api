package ru.clevertec.videohosting_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.videohosting_api.dto.category.CategoryInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelExtendedInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelInfoDTO;
import ru.clevertec.videohosting_api.model.Category;
import ru.clevertec.videohosting_api.model.Channel;

import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ChannelMapper {
    @Mapping(target = "subscribersCount", expression = "java(mapSubscribers(channel))")
    @Mapping(target = "categoryName", expression = "java(channel.getCategory().getName())")
    ChannelInfoDTO channelToChannelInfoDTO(Channel channel);

    default Long mapSubscribers(Channel channel) {
        return channel.getSubscribers() != null ? (long) channel.getSubscribers().size() : 0;
    }

    @Mapping(target = "subscribersCount", expression = "java(mapSubscribers(channel))")
    @Mapping(target = "category", expression = "java(mapCategory(channel.getCategory()))")
    ChannelExtendedInfoDTO channelToChannelExtendedInfoDTO(Channel channel);

    default CategoryInfoDTO mapCategory(Category category) {
        return Optional.ofNullable(category)
                .map(c -> CategoryInfoDTO.builder()
                        .id(category.getId())
                        .name(category.getName())
                        .build())
                .orElse(null);
    }
}
