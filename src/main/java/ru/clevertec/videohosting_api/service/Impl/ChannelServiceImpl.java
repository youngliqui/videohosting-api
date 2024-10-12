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
import ru.clevertec.videohosting_api.exception.ChannelAlreadyExistsException;
import ru.clevertec.videohosting_api.exception.ChannelNotFoundException;
import ru.clevertec.videohosting_api.mapper.ChannelMapper;
import ru.clevertec.videohosting_api.model.Category;
import ru.clevertec.videohosting_api.model.Channel;
import ru.clevertec.videohosting_api.model.ChannelSpecification;
import ru.clevertec.videohosting_api.model.User;
import ru.clevertec.videohosting_api.repository.ChannelRepository;
import ru.clevertec.videohosting_api.service.CategoryService;
import ru.clevertec.videohosting_api.service.ChannelService;

import java.io.IOException;
import java.util.Base64;
import java.util.HashSet;

@Service
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ChannelMapper channelMapper;
    private final CategoryService categoryService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ChannelServiceImpl(
            ChannelRepository channelRepository,
            ChannelMapper channelMapper,
            CategoryService categoryService, ObjectMapper objectMapper
    ) {
        this.channelRepository = channelRepository;
        this.channelMapper = channelMapper;
        this.categoryService = categoryService;
        this.objectMapper = objectMapper;
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
    public ChannelExtendedInfoDTO changeAvatar(Long channelId, MultipartFile avatar) throws IOException {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() ->
                        new ChannelNotFoundException("Channel with id = " + channelId + " was not found"));

        channel.setAvatar(encodeAvatar(avatar));
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

    private String encodeAvatar(MultipartFile multipartFile) throws IOException {
        byte[] avatarBytes = multipartFile.getBytes();
        return Base64.getEncoder().encodeToString(avatarBytes);
    }
}
