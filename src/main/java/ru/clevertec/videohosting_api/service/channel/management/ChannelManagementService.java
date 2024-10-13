package ru.clevertec.videohosting_api.service.channel.management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.web.multipart.MultipartFile;
import ru.clevertec.videohosting_api.dto.channel.ChannelCreateDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelExtendedInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.model.User;

public interface ChannelManagementService {
    ChannelExtendedInfoDTO create(ChannelCreateDTO channelCreateDTO, User author);

    ChannelExtendedInfoDTO updateChannel(Long channelId, ChannelUpdateDTO channelUpdateDTO);

    ChannelExtendedInfoDTO changeAvatar(Long channelId, MultipartFile avatar);

    ChannelExtendedInfoDTO applyPatchToChannel(Long channelId, JsonPatch patch) throws JsonPatchException, JsonProcessingException;

    boolean canUserChange(Long channelId, User currentUser);
}
