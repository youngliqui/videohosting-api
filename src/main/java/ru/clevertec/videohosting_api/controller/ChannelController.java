package ru.clevertec.videohosting_api.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.clevertec.videohosting_api.dto.channel.ChannelCreateDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelExtendedInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.exception.user.UnauthorizedActionException;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.service.channel.information.ChannelInformationService;
import ru.clevertec.videohosting_api.service.channel.management.ChannelManagementService;
import ru.clevertec.videohosting_api.service.channel.subscription.ChannelSubscriptionService;
import ru.clevertec.videohosting_api.service.security.AccessService;
import ru.clevertec.videohosting_api.service.user.authentication.UserAuthenticationService;

@RestController
@RequestMapping("/channels")
@RequiredArgsConstructor
public class ChannelController {
    private final ChannelInformationService channelInformationService;
    private final ChannelManagementService channelManagementService;
    private final ChannelSubscriptionService channelSubscriptionService;
    private final UserAuthenticationService userAuthenticationService;
    private final AccessService accessService;


    @GetMapping
    public Page<ChannelInfoDTO> getAllChannels(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String category,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        return channelInformationService.getAllChannels(name, language, category, page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ChannelExtendedInfoDTO createChannel(
            @RequestBody @Valid ChannelCreateDTO channelCreateDTO
    ) {
        User author = userAuthenticationService.getCurrentUser();

        return channelManagementService.create(channelCreateDTO, author);
    }

    @GetMapping("/{channelId}")
    public ChannelExtendedInfoDTO getChannelInfoById(
            @PathVariable Long channelId
    ) {
        return channelInformationService.getChannelInfoById(channelId);
    }

    @PatchMapping(value = "/{channelId}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ChannelExtendedInfoDTO changeAvatar(
            @PathVariable Long channelId,
            @RequestPart MultipartFile avatar
    ) {
        checkUserCanChangeChannel(channelId);

        return channelManagementService.changeAvatar(channelId, avatar);
    }

    @PatchMapping("/{channelId}")
    public ChannelExtendedInfoDTO patchChannel(
            @PathVariable Long channelId,
            @RequestBody ChannelUpdateDTO channelUpdateDTO
    ) {
        checkUserCanChangeChannel(channelId);

        return channelManagementService.patchChannel(channelId, channelUpdateDTO);
    }

    @PutMapping(value = "/{channelId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ChannelExtendedInfoDTO updateChannel(
            @PathVariable Long channelId,
            @ModelAttribute ChannelUpdateDTO channelUpdateDTO
    ) {
        checkUserCanChangeChannel(channelId);

        return channelManagementService.updateChannel(channelId, channelUpdateDTO);
    }

    @PostMapping("/{channelId}/subscribers/{userId}")
    public void subscribeToChannel(@PathVariable Long channelId,
                                   @PathVariable Long userId) {
        channelSubscriptionService.subscribeToChannel(channelId, userId);
    }

    @DeleteMapping("/{channelId}/subscribers/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unsubscribeFromChannel(@PathVariable Long channelId,
                                       @PathVariable Long userId) {
        channelSubscriptionService.unsubscribeFromChannel(channelId, userId);
    }

    private void checkUserCanChangeChannel(Long channelId) {
        if (!accessService.canUserChangeChannel(channelId, userAuthenticationService.getCurrentUser())) {
            throw new UnauthorizedActionException("You do not have access rights to change the channel");
        }
    }
}
