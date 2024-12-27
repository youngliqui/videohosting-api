package ru.clevertec.videohosting_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.videohosting_api.dto.security.JwtAuthenticationResponse;
import ru.clevertec.videohosting_api.dto.security.SignInRequest;
import ru.clevertec.videohosting_api.dto.security.SignUpRequest;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.service.security.AuthenticationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public UserInfoDTO signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }
}
