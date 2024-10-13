package ru.clevertec.videohosting_api.service.channel.information;

import org.springframework.data.domain.Page;
import ru.clevertec.videohosting_api.dto.channel.ChannelExtendedInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelInfoDTO;

public interface ChannelInformationService {
    Page<ChannelInfoDTO> getAllChannels(String name, String language, String categoryName, int page, int size);

    ChannelExtendedInfoDTO getChannelInfoById(Long channelId);
}
