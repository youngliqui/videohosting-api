package ru.clevertec.videohosting_api.service.channel.information;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.dto.channel.ChannelExtendedInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelInfoDTO;
import ru.clevertec.videohosting_api.exception.channel.ChannelNotFoundException;
import ru.clevertec.videohosting_api.mapper.ChannelMapper;
import ru.clevertec.videohosting_api.model.Channel;
import ru.clevertec.videohosting_api.model.ChannelSpecification;
import ru.clevertec.videohosting_api.repository.ChannelRepository;

@Service
@RequiredArgsConstructor
public class ChannelInformationServiceImpl implements ChannelInformationService {
    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;

    @Override
    public Page<ChannelInfoDTO> getAllChannels(String name, String language, String categoryName, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Channel> specification =
                Specification.where(ChannelSpecification.hasName(name))
                        .and(ChannelSpecification.hasLanguage(language))
                        .and(ChannelSpecification.hasCategory(categoryName));

        return channelRepository.findAll(specification, pageable)
                .map(channelMapper::channelToChannelInfoDTO);
    }

    @Override
    public ChannelExtendedInfoDTO getChannelInfoById(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        return channelMapper.channelToChannelExtendedInfoDTO(channel);
    }
}
