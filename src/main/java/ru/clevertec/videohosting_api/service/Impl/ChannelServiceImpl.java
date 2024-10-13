package ru.clevertec.videohosting_api.service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.clevertec.videohosting_api.dto.channel.ChannelCreateDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelExtendedInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelInfoDTO;
import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.exception.ChannelAlreadyExistsException;
import ru.clevertec.videohosting_api.exception.ChannelNotFoundException;
import ru.clevertec.videohosting_api.exception.SubscribedAlreadyException;
import ru.clevertec.videohosting_api.exception.UnauthorizedActionException;
import ru.clevertec.videohosting_api.mapper.ChannelMapper;
import ru.clevertec.videohosting_api.model.Category;
import ru.clevertec.videohosting_api.model.Channel;
import ru.clevertec.videohosting_api.model.ChannelSpecification;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.ChannelRepository;
import ru.clevertec.videohosting_api.service.AvatarService;
import ru.clevertec.videohosting_api.service.CategoryService;
import ru.clevertec.videohosting_api.service.ChannelService;
import ru.clevertec.videohosting_api.service.UserService;
import ru.clevertec.videohosting_api.updater.channel.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper;
    private final AvatarService avatarService;
    private final UserService userService;

    @Autowired
    public ChannelServiceImpl(
            ChannelRepository channelRepository,
            ChannelMapper channelMapper,
            CategoryService categoryService,
            ObjectMapper objectMapper,
            AvatarService avatarService,
            UserService userService
    ) {
        this.channelRepository = channelRepository;
        this.channelMapper = channelMapper;
        this.categoryService = categoryService;
        this.objectMapper = objectMapper;
        this.avatarService = avatarService;
        this.userService = userService;
    }

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
    public ChannelExtendedInfoDTO getChannelInfoById(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        return channelMapper.channelToChannelExtendedInfoDTO(channel);
    }

    @Override
    public boolean canUserChange(Long channelId, User currentUser) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        return !currentUser.getRole().name().equals("ADMIN") &&
                !currentUser.getId().equals(channel.getAuthor().getId());
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
    @Transactional
    public void subscribeToChannel(Long channelId, Long userId) {
        User currentUser = userService.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {
            throw new UnauthorizedActionException("User is not authorized to subscribe to this channel");
        }

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        if (channel.getSubscribers().contains(currentUser)) {
            throw new SubscribedAlreadyException("User is already subscribed to this channel");
        }

        channel.getSubscribers().add(currentUser);
        currentUser.getSubscriptions().add(channel);

        channelRepository.save(channel);
    }

    @Override
    public void unsubscribeFromChannel(Long channelId, Long userId) {
        User currentUser = userService.getCurrentUser();

        if (!currentUser.getId().equals(userId)) {
            throw new UnauthorizedActionException("User is not authorized to subscribe to this channel");
        }

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        if (!channel.getSubscribers().contains(currentUser)) {
            throw new SubscribedAlreadyException("The user has not subscribed to this channel yet");
        }

        channel.getSubscribers().remove(currentUser);
        currentUser.getSubscriptions().remove(channel);

        channelRepository.save(channel);
    }
}
