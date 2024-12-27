package ru.clevertec.videohosting_api.service.channel.management;

import org.springframework.web.multipart.MultipartFile;
import ru.clevertec.videohosting_api.dto.channel.ChannelCreateDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelExtendedInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.model.User;

public interface ChannelManagementService {
    ChannelExtendedInfoDTO create(ChannelCreateDTO channelCreateDTO, User author);

    ChannelExtendedInfoDTO updateChannel(Long channelId, ChannelUpdateDTO channelUpdateDTO);

    ChannelExtendedInfoDTO changeAvatar(Long channelId, MultipartFile avatar);

    ChannelExtendedInfoDTO patchChannel(Long channelId, ChannelUpdateDTO channelUpdateDTO);
}
