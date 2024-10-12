package ru.clevertec.videohosting_api.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import ru.clevertec.videohosting_api.dto.channel.ChannelCreateDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelExtendedInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelInfoDTO;
import ru.clevertec.videohosting_api.model.User;

import java.io.IOException;

public interface ChannelService {
    Page<ChannelInfoDTO> getAllChannels(String name, String language, String categoryName, int page, int size);

    ChannelExtendedInfoDTO create(ChannelCreateDTO channelCreateDTO, User author) throws IOException;

    ChannelExtendedInfoDTO changeAvatar(Long channelId, MultipartFile avatar) throws IOException;

    ChannelExtendedInfoDTO getChannelInfoById(Long channelId);

    boolean canUserChange(Long channelId, User currentUser);

    ChannelExtendedInfoDTO applyPatchToChannel(Long channelId, JsonPatch patch) throws JsonPatchException, JsonProcessingException;
}
