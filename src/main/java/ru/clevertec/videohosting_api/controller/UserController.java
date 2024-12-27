package ru.clevertec.videohosting_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.videohosting_api.dto.security.ChangePasswordDTO;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.dto.user.UserSubscriptionDTO;
import ru.clevertec.videohosting_api.dto.user.UserUpdateDTO;
import ru.clevertec.videohosting_api.service.user.authentication.UserAuthenticationService;
import ru.clevertec.videohosting_api.service.user.information.UserInformationService;
import ru.clevertec.videohosting_api.service.user.management.UserManagementService;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserAuthenticationService userAuthenticationService;
    private final UserInformationService userInformationService;
    private final UserManagementService userManagementService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Page<UserInfoDTO> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return userInformationService.getAllUsers(pageable);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    public UserInfoDTO getUserInfoById(@PathVariable Long userId) {
        return userInformationService.getUserInfoById(userId);
    }

    @PatchMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    public UserInfoDTO patchUser(
            @PathVariable Long userId,
            @RequestBody UserUpdateDTO userUpdateDTO
    ) {
        return userManagementService.patchUser(userId, userUpdateDTO);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    public UserInfoDTO updateUser(
            @PathVariable Long userId,
            @RequestBody @Valid UserUpdateDTO updateDTO
    ) {
        return userManagementService.updateUser(userId, updateDTO);
    }

    @PatchMapping("/{userId}/password")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    public UserInfoDTO updateUserPassword(
            @PathVariable Long userId,
            @RequestBody @Valid ChangePasswordDTO changePasswordDTO
    ) {
        return userAuthenticationService.changePassword(userId, changePasswordDTO);
    }

    @GetMapping("/{userId}/subscriptions")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    public List<UserSubscriptionDTO> getUserSubscriptions(@PathVariable Long userId) {
        return userInformationService.getUserSubscriptions(userId);
    }
}
