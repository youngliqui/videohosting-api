package ru.clevertec.videohosting_api.service.avatar;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.clevertec.videohosting_api.exception.avatar.AvatarEncodeException;

import java.io.IOException;
import java.util.Base64;

@Service
public class AvatarServiceImpl implements AvatarService {
    @Override
    public String encodeAvatar(MultipartFile multipartFile) {
        try {
            byte[] avatarBytes = multipartFile.getBytes();
            return Base64.getEncoder().encodeToString(avatarBytes);
        } catch (IOException e) {
            throw new AvatarEncodeException("Failed to encode avatar: " + e.getMessage());
        }
    }
}
