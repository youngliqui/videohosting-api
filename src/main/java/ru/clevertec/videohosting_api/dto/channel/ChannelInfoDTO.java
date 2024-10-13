package ru.clevertec.videohosting_api.dto.channel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelInfoDTO {
    private Long id;
    private String name;
    private String language;
    private String categoryName;
    private Long subscribersCount;
    private String avatar;
}
