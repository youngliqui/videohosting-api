package ru.clevertec.videohosting_api.dto.channel;

import lombok.Getter;
import lombok.Setter;
import ru.clevertec.videohosting_api.dto.category.CategoryInfoDTO;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;

import java.util.Date;

@Getter
@Setter
public class ChannelExtendedInfoDTO {
    private Long id;
    private String name;
    private String description;
    private UserInfoDTO author;
    private String language;
    private CategoryInfoDTO category;
    private Long subscribersCount;
    private Date creationDate;
    private String avatar;
}
