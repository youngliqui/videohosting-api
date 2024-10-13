package ru.clevertec.videohosting_api.service;

import org.springframework.web.multipart.MultipartFile;

public interface AvatarService {
    String encodeAvatar(MultipartFile multipartFile);
}
