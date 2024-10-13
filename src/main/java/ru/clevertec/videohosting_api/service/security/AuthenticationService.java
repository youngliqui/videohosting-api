package ru.clevertec.videohosting_api.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.dto.security.JwtAuthenticationResponse;
import ru.clevertec.videohosting_api.dto.security.SignInRequest;
import ru.clevertec.videohosting_api.dto.security.SignUpRequest;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.model.security.Role;
import ru.clevertec.videohosting_api.service.user.authentication.UserAuthenticationService;
import ru.clevertec.videohosting_api.service.user.management.UserManagementService;

import java.util.LinkedHashSet;

@Service
public class AuthenticationService {
    private final UserAuthenticationService userAuthenticationService;
    private final UserManagementService userManagementService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationService(
            UserAuthenticationService userAuthenticationService,
            UserManagementService userManagementService,
            JwtService jwtService,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userAuthenticationService = userAuthenticationService;
        this.userManagementService = userManagementService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User signUp(SignUpRequest request) {
        User user = User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .name(request.getName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .subscriptions(new LinkedHashSet<>())
                .build();

        userManagementService.create(user);

        return user;
    }

    public JwtAuthenticationResponse signIn(SignInRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getNickname(),
                request.getPassword()
        ));

        UserDetails user = userAuthenticationService
                .userDetailsService()
                .loadUserByUsername(request.getNickname());

        String jwt = jwtService.generateToken(user);
        return new JwtAuthenticationResponse(jwt);
    }
}
