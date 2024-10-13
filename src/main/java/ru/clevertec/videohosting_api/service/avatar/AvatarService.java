package ru.clevertec.videohosting_api.service.avatar;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {
    String encodeAvatar(MultipartFile multipartFile);
}
