package ru.clevertec.videohosting_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {
    @Size(min = 5, max = 50, message = "The nickname must contain from 5 to 50 characters")
    @NotBlank(message = "The nickname cannot be empty")
    private String nickname;

    @Size(max = 255, message = "The name must be no more than 255 characters long")
    @NotBlank(message = "The name cannot be empty")
    private String name;

    @Size(min = 5, max = 255, message = "The email address must contain from 5 to 255 characters")
    @NotBlank(message = "The email address cannot be empty")
    @Email(message = "The email address must be in the format user@example.com")
    private String email;
}
