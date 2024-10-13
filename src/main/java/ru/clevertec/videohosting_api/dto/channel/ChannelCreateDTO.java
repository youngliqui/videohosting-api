package ru.clevertec.videohosting_api.dto.channel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChannelCreateDTO {
    @NotBlank(message = "Name should not be empty")
    @Size(min = 3, max = 100, message = "The name must be between 3 and 100 characters long")
    private String name;

    @NotBlank(message = "Language should not be empty")
    @Size(max = 50)
    private String language;

    @NotBlank(message = "Category should not be empty")
    @Size(min = 4, max = 50, message = "The category name must be between 4 and 50 characters long")
    private String categoryName;
}
