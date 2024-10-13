package ru.clevertec.videohosting_api.dto.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserSubscriptionDTO {
    private Long channelId;
    private String channelName;
}
