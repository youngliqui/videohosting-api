package ru.clevertec.videohosting_api.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.videohosting_api.dto.UserInfoDTO;
import ru.clevertec.videohosting_api.exception.UserNotFoundException;
import ru.clevertec.videohosting_api.service.UserService;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    //@PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Page<UserInfoDTO>> getAllUsers(
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserInfoDTO> users = userService.getAllUsers(pageable);

        if (users.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(users);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoDTO> getUserInfoById(@PathVariable("userId") Long userId) {
        try {
            UserInfoDTO userInfoDTO = userService.getUserInfoById(userId);
            return ResponseEntity.ok(userInfoDTO);

        } catch (UserNotFoundException e) {
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            log.trace(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
