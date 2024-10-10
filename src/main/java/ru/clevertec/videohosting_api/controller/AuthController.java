package ru.clevertec.videohosting_api.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.clevertec.videohosting_api.dto.JwtAuthenticationResponse;
import ru.clevertec.videohosting_api.dto.SignInRequest;
import ru.clevertec.videohosting_api.dto.SignUpRequest;
import ru.clevertec.videohosting_api.exception.CustomValidationException;
import ru.clevertec.videohosting_api.model.User;
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
    public ResponseEntity<User> signUp(@RequestBody @Valid SignUpRequest request,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }

        User createdUser = authenticationService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<JwtAuthenticationResponse> signIn(@RequestBody @Valid SignInRequest request,
                                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }

        JwtAuthenticationResponse response = authenticationService.signIn(request);

        return ResponseEntity.ok(response);
    }
}
