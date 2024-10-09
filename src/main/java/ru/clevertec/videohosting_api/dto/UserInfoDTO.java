package ru.clevertec.videohosting_api.dto;

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
