package ru.clevertec.videohosting_api.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoDTO {
    private Long id;
    private String nickname;
    private String name;
    private String email;
}
