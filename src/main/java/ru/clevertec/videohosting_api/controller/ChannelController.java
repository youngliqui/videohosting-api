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
import ru.clevertec.videohosting_api.exception.CustomValidationException;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.service.ChannelService;
import ru.clevertec.videohosting_api.service.UserService;

import java.io.IOException;

@RestController
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;
    private final UserService userService;

    @Autowired
    public ChannelController(ChannelService channelService, UserService userService) {
        this.channelService = channelService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Page<ChannelInfoDTO>> getAllChannels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        Page<ChannelInfoDTO> channels = channelService.getAllChannels(name, language, category, page, size);

        if (channels.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(channels);
    }

    @PostMapping
    public ResponseEntity<ChannelExtendedInfoDTO> createChannel(
            @RequestBody @Valid ChannelCreateDTO channelCreateDTO,
            BindingResult bindingResult
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult.getAllErrors().toString());
        }

        User author = userService.getCurrentUser();
        ChannelExtendedInfoDTO createdChannel = channelService.create(channelCreateDTO, author);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdChannel);
    }

    @GetMapping("/{channelId}")
    public ResponseEntity<ChannelExtendedInfoDTO> getChannelInfoById(
            @PathVariable("channelId") Long channelId
    ) {
        ChannelExtendedInfoDTO channelInfo = channelService.getChannelInfoById(channelId);

        return ResponseEntity.ok(channelInfo);
    }

    @PatchMapping(value = "/{channelId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ChannelExtendedInfoDTO> changeAvatar(
            @PathVariable("channelId") Long channelId,
            @RequestPart("avatar") MultipartFile avatar
    ) {
        if (channelService.canUserChange(channelId, userService.getCurrentUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        try {
            ChannelExtendedInfoDTO updatedChannel = channelService.changeAvatar(channelId, avatar);
            return ResponseEntity.ok(updatedChannel);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{channelId}")
    public ResponseEntity<ChannelExtendedInfoDTO> updateChannel(@PathVariable("channelId") Long channelId,
                                                                @RequestBody JsonPatch patch) {

        if (channelService.canUserChange(channelId, userService.getCurrentUser())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            ChannelExtendedInfoDTO updatedChannel = channelService.applyPatchToChannel(channelId, patch);
            return ResponseEntity.ok(updatedChannel);

        } catch (JsonPatchException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
