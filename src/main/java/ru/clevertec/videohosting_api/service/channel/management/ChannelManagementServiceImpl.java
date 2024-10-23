package ru.clevertec.videohosting_api.service.channel.management;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.clevertec.videohosting_api.dto.channel.ChannelCreateDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelExtendedInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.exception.channel.ChannelAlreadyExistsException;
import ru.clevertec.videohosting_api.exception.channel.ChannelNotFoundException;
import ru.clevertec.videohosting_api.mapper.ChannelMapper;
import ru.clevertec.videohosting_api.model.Category;
import ru.clevertec.videohosting_api.model.Channel;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.ChannelRepository;
import ru.clevertec.videohosting_api.service.category.CategoryService;
import ru.clevertec.videohosting_api.util.ImageUtils;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChannelManagementServiceImpl implements ChannelManagementService {
    private final ChannelRepository channelRepository;
    private final CategoryService categoryService;
    private final ChannelMapper channelMapper;

    @Override
    public ChannelExtendedInfoDTO create(ChannelCreateDTO channelCreateDTO, User author) {
        if (channelRepository.existsByAuthorId(author.getId())) {
            throw new ChannelAlreadyExistsException("Author already has a channel");
        }

        Category category = categoryService.getCategoryByName(channelCreateDTO.getCategoryName());
        Channel newChannel = channelMapper.channelCreateDTOToChannel(channelCreateDTO, author, category);

        return channelMapper.channelToChannelExtendedInfoDTO(channelRepository.save(newChannel));
    }

    @Override
    public ChannelExtendedInfoDTO changeAvatar(Long channelId, MultipartFile avatar) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        channel.setAvatar(ImageUtils.encodeAvatar(avatar));
        return channelMapper.channelToChannelExtendedInfoDTO(channelRepository.save(channel));
    }

    @Override
    public ChannelExtendedInfoDTO patchChannel(Long channelId, ChannelUpdateDTO channelUpdateDTO) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        Category category = Optional.ofNullable(channelUpdateDTO.getCategoryName())
                .map(categoryService::getCategoryByName)
                .orElse(null);

        Channel updatedChannel = channelMapper.patchChannel(channel, channelUpdateDTO, category);

        return channelMapper.channelToChannelExtendedInfoDTO(updatedChannel);
    }

    @Override
    public ChannelExtendedInfoDTO updateChannel(Long channelId, ChannelUpdateDTO channelUpdateDTO) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));
        Category category = categoryService.getCategoryByName(channelUpdateDTO.getCategoryName());

        Channel updatedChannel = channelMapper.updateChannel(channel, channelUpdateDTO, category);

        return channelMapper.channelToChannelExtendedInfoDTO(updatedChannel);
    }
}
