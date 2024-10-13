package ru.clevertec.videohosting_api.updater.channel;

import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.model.Category;
import ru.clevertec.videohosting_api.model.Channel;
import ru.clevertec.videohosting_api.service.CategoryService;

public class CategoryChannelUpdater implements ChannelUpdater {
    private final CategoryService categoryService;

    public CategoryChannelUpdater(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public void update(Channel channel, ChannelUpdateDTO channelUpdateDTO) {
        if (channelUpdateDTO.getCategoryName() != null) {
            Category category = categoryService.getCategoryByName(channelUpdateDTO.getCategoryName());

            channel.setCategory(category);
        }
    }
}
