package ru.clevertec.videohosting_api.util;

import org.springframework.web.multipart.MultipartFile;
import ru.clevertec.videohosting_api.exception.avatar.AvatarEncodeException;

import java.io.IOException;
import java.util.Base64;

public class ImageUtils {

    public static String encodeAvatar(MultipartFile multipartFile) {
        try {
            byte[] avatarBytes = multipartFile.getBytes();
            return Base64.getEncoder().encodeToString(avatarBytes);
        } catch (IOException e) {
            throw new AvatarEncodeException("Failed to encode avatar: " + e.getMessage());
        }
    }
}
