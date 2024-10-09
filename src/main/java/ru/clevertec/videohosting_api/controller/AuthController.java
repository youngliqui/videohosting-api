package ru.clevertec.videohosting_api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.videohosting_api.dto.JwtAuthenticationResponse;
import ru.clevertec.videohosting_api.dto.SignInRequest;
import ru.clevertec.videohosting_api.dto.SignUpRequest;
import ru.clevertec.videohosting_api.service.AuthenticationService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/sign-up")
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request,
                                            BindingResult bindingResult) {
        return authenticationService.signUp(request);
    }

    @PostMapping("/sign-in")
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {

        }

        return authenticationService.signIn(request);
    }
}
