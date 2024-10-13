package ru.clevertec.videohosting_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.videohosting_api.dto.ChangePasswordDTO;
import ru.clevertec.videohosting_api.dto.user.UserInfoDTO;
import ru.clevertec.videohosting_api.dto.user.UserSubscriptionDTO;
import ru.clevertec.videohosting_api.dto.user.UserUpdateDTO;
import ru.clevertec.videohosting_api.exception.validation.CustomValidationException;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.service.user.authentication.UserAuthenticationService;
import ru.clevertec.videohosting_api.service.user.information.UserInformationService;
import ru.clevertec.videohosting_api.service.user.management.UserManagementService;
import ru.clevertec.videohosting_api.service.user.subscription.UserSubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserAuthenticationService userAuthenticationService;
    private final UserInformationService userInformationService;
    private final UserManagementService userManagementService;
    private final UserSubscriptionService userSubscriptionService;

    @Autowired
    public UserController(
            UserAuthenticationService userAuthenticationService,
            UserInformationService userInformationService,
            UserManagementService userManagementService,
            UserSubscriptionService userSubscriptionService
    ) {
        this.userAuthenticationService = userAuthenticationService;
        this.userInformationService = userInformationService;
        this.userManagementService = userManagementService;
        this.userSubscriptionService = userSubscriptionService;
    }

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<UserInfoDTO>> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserInfoDTO> users = userInformationService.getAllUsers(pageable);

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/me")
    public ResponseEntity<User> authenticatedUser() {
        User currentUser = userAuthenticationService.getCurrentUser();

        return ResponseEntity.ok(currentUser);
    }

    @GetMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    public ResponseEntity<UserInfoDTO> getUserInfoById(@PathVariable("userId") Long userId) {
        UserInfoDTO userInfo = userInformationService.getUserInfoById(userId);

        return ResponseEntity.ok(userInfo);
    }

    @PatchMapping(path = "/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    public ResponseEntity<User> updateUser(@PathVariable Long userId,
                                           @RequestBody JsonPatch patch) {
        try {
            User updatedUser = userManagementService.applyPatchToUser(userId, patch);
            return ResponseEntity.ok(updatedUser);

        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{userId}")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    public ResponseEntity<User> updateUser(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid UserUpdateDTO updateDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        User updatedUser = userManagementService.updateUser(userId, updateDTO);

        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping("/{userId}/password")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    public ResponseEntity<User> updateUserPassword(
            @PathVariable("userId") Long userId,
            @RequestBody @Valid ChangePasswordDTO changePasswordDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }
        User updatedUser = userAuthenticationService.changePassword(userId, changePasswordDTO);

        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{userId}/subscription")
    @PreAuthorize("#userId == authentication.principal.id or hasAnyAuthority('ADMIN')")
    public ResponseEntity<List<UserSubscriptionDTO>> getUserSubscriptions(@PathVariable("userId") Long userId) {
        List<UserSubscriptionDTO> subscriptions = userSubscriptionService.getUserSubscriptions(userId);

        return ResponseEntity.ok(subscriptions);
    }
}
