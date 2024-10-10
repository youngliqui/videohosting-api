package ru.clevertec.videohosting_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDTO {
    @Size(min = 5, max = 255, message = "The password must be between 5 and 255 characters long")
    @NotBlank(message = "The current password cannot be empty")
    private String currentPassword;

    @Size(min = 5, max = 255, message = "The password must be between 5 and 255 characters long")
    @NotBlank(message = "The new password cannot be empty")
    private String newPassword;

    @Size(min = 5, max = 255, message = "The password must be between 5 and 255 characters long")
    @NotBlank(message = "The confirm password cannot be empty")
    private String confirmPassword;
}
