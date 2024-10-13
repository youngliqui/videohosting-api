package ru.clevertec.videohosting_api.dto.channel;

import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@Setter
public class ChannelUpdateDTO {
    @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters long")
    private String name;

    @Size(max = 500, message = "The description should be no more than 500 characters long")
    private String description;

    @Size(max = 50)
    private String language;

    @Size(min = 4, max = 50, message = "The category name must be between 4 and 50 characters long")
    private String categoryName;

    private MultipartFile avatar;
}
