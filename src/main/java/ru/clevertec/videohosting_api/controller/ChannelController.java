package ru.clevertec.videohosting_api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.clevertec.videohosting_api.dto.channel.ChannelCreateDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelExtendedInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.exception.validation.CustomValidationException;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.service.channel.information.ChannelInformationService;
import ru.clevertec.videohosting_api.service.channel.management.ChannelManagementService;
import ru.clevertec.videohosting_api.service.channel.subscription.ChannelSubscriptionService;
import ru.clevertec.videohosting_api.service.user.authentication.UserAuthenticationService;

@RestController
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelInformationService channelInformationService;
    private final ChannelManagementService channelManagementService;
    private final ChannelSubscriptionService channelSubscriptionService;
    private final UserAuthenticationService userAuthenticationService;

    @Autowired
    public ChannelController(
            ChannelInformationService channelInformationService,
            ChannelManagementService channelManagementService,
            ChannelSubscriptionService channelSubscriptionService,
            UserAuthenticationService userAuthenticationService
    ) {
        this.channelInformationService = channelInformationService;
        this.channelManagementService = channelManagementService;
        this.channelSubscriptionService = channelSubscriptionService;
        this.userAuthenticationService = userAuthenticationService;
    }


    @GetMapping
    public ResponseEntity<Page<ChannelInfoDTO>> getAllChannels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Page<ChannelInfoDTO> channels = channelInformationService.getAllChannels(name, language, category, page, size);

        if (channels.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(channels);
    }

    @PostMapping
    public ResponseEntity<ChannelExtendedInfoDTO> createChannel(
            @RequestBody @Valid ChannelCreateDTO channelCreateDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }

        User author = userAuthenticationService.getCurrentUser();
        ChannelExtendedInfoDTO createdChannel =
                channelManagementService.create(channelCreateDTO, author);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelExtendedInfoDTO> getChannelInfoById(
            @PathVariable("channelId") Long channelId
    ) {
        ChannelExtendedInfoDTO channelInfo = channelInformationService.getChannelInfoById(channelId);

        return ResponseEntity.ok(channelInfo);
    }

    @PatchMapping(value = "/{channelId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ChannelExtendedInfoDTO> changeAvatar(
            @PathVariable("channelId") Long channelId,
            @RequestPart("avatar") MultipartFile avatar
    ) {
        if (channelManagementService.canUserChange(channelId, userAuthenticationService.getCurrentUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ChannelExtendedInfoDTO updatedChannel =
                channelManagementService.changeAvatar(channelId, avatar);
        return ResponseEntity.ok(updatedChannel);
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelExtendedInfoDTO> updateChannel(@PathVariable("channelId") Long channelId,
                                                                @RequestBody JsonPatch patch) {
        if (channelManagementService.canUserChange(channelId, userAuthenticationService.getCurrentUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            ChannelExtendedInfoDTO updatedChannel =
                    channelManagementService.applyPatchToChannel(channelId, patch);
            return ResponseEntity.ok(updatedChannel);

        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping(value = "/{channelId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ChannelExtendedInfoDTO> updateChannel(
            @PathVariable("channelId") Long channelId,
            @RequestPart(value = "name", required = false) String name,
            @RequestPart(value = "description", required = false) String description,
            @RequestPart(value = "language", required = false) String language,
            @RequestPart(value = "categoryName", required = false) String categoryName,
            @RequestPart(value = "avatar", required = false) MultipartFile avatar
    ) {
        if (channelManagementService.canUserChange(channelId, userAuthenticationService.getCurrentUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ChannelUpdateDTO channelUpdateDTO = ChannelUpdateDTO.builder()
                .name(name)
                .description(description)
                .language(language)
                .categoryName(categoryName)
                .avatar(avatar)
                .build();

        ChannelExtendedInfoDTO channel =
                channelManagementService.updateChannel(channelId, channelUpdateDTO);

        return ResponseEntity.ok(channel);
    }

    @PostMapping("/{channelId}/subscribers/{userId}")
    public ResponseEntity<Void> subscribeToChannel(@PathVariable("channelId") Long channelId,
                                                   @PathVariable("userId") Long userId) {
        channelSubscriptionService.subscribeToChannel(channelId, userId);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{channelId}/subscribers/{userId}")
    public ResponseEntity<Void> unsubscribeFromChannel(@PathVariable("channelId") Long channelId,
                                                       @PathVariable("userId") Long userId) {
        channelSubscriptionService.unsubscribeFromChannel(channelId, userId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
