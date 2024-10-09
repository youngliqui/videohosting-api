package ru.clevertec.videohosting_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignUpRequest {
    @Size(min = 5, max = 50, message = "The nickname must contain from 5 to 50 characters")
    @NotBlank(message = "The nickname cannot be empty")
    private String nickname;

    @Size(min = 5, max = 255, message = "The password must be between 5 and 255 characters long")
    @NotBlank(message = "The password cannot be empty")
    private String password;

    @Size(max = 255, message = "The name must be no more than 255 characters long")
    @NotBlank(message = "The name cannot be empty")
    private String name;

    @Size(min = 5, max = 255, message = "The email address must contain from 5 to 255 characters")
    @NotBlank(message = "The email address cannot be empty")
    @Email(message = "The email address must be in the format user@example.com")
    private String email;
}
