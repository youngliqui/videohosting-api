package ru.clevertec.videohosting_api.service.channel.management;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
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
import ru.clevertec.videohosting_api.service.avatar.AvatarService;
import ru.clevertec.videohosting_api.service.category.CategoryService;
import ru.clevertec.videohosting_api.updater.channel.ChannelUpdater;
import ru.clevertec.videohosting_api.updater.channel.impl.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class ChannelManagementServiceImpl implements ChannelManagementService {
    private final ChannelRepository channelRepository;
    private final CategoryService categoryService;
    private final ChannelMapper channelMapper;
    private final AvatarService avatarService;
    private final ObjectMapper objectMapper;

    public ChannelManagementServiceImpl(
            ChannelRepository channelRepository,
            CategoryService categoryService,
            ChannelMapper channelMapper,
            AvatarService avatarService, ObjectMapper objectMapper) {
        this.channelRepository = channelRepository;
        this.categoryService = categoryService;
        this.channelMapper = channelMapper;
        this.avatarService = avatarService;
        this.objectMapper = objectMapper;
    }

    @Override
    public ChannelExtendedInfoDTO create(ChannelCreateDTO channelCreateDTO, User author) {
        if (channelRepository.existsByAuthorId(author.getId())) {
            throw new ChannelAlreadyExistsException("Author already has a channel");
        }

        Category category = categoryService.getCategoryByName(channelCreateDTO.getCategoryName());
        Channel newChannel = Channel.builder()
                .name(channelCreateDTO.getName())
                .author(author)
                .subscribers(new HashSet<>())
                .language(channelCreateDTO.getLanguage())
                .category(category)
                .build();

        return channelMapper.channelToChannelExtendedInfoDTO(channelRepository.save(newChannel));
    }

    @Override
    public ChannelExtendedInfoDTO changeAvatar(Long channelId, MultipartFile avatar) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        channel.setAvatar(avatarService.encodeAvatar(avatar));
        return channelMapper.channelToChannelExtendedInfoDTO(channelRepository.save(channel));
    }

    @Override
    public ChannelExtendedInfoDTO updateChannel(Long channelId, ChannelUpdateDTO channelUpdateDTO) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        List<ChannelUpdater> updaters = Arrays.asList(
                new NameChannelUpdater(),
                new DescriptionChannelUpdater(),
                new LanguageChannelUpdater(),
                new AvatarChannelUpdater(avatarService),
                new CategoryChannelUpdater(categoryService)
        );
        updaters.forEach(updater -> updater.update(channel, channelUpdateDTO));

        return channelMapper.channelToChannelExtendedInfoDTO(channelRepository.save(channel));
    }

    @Override
    public ChannelExtendedInfoDTO applyPatchToChannel(Long channelId, JsonPatch patch)
            throws JsonPatchException, JsonProcessingException {

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        JsonNode patchedChannelNode = patch.apply(objectMapper.convertValue(channel, JsonNode.class));
        Channel patchedChannel = objectMapper.treeToValue(patchedChannelNode, Channel.class);

        return channelMapper.channelToChannelExtendedInfoDTO(channelRepository.save(patchedChannel));
    }

    @Override
    public boolean canUserChange(Long channelId, User currentUser) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        return !currentUser.getRole().name().equals("ADMIN") &&
                !currentUser.getId().equals(channel.getAuthor().getId());
    }
}
