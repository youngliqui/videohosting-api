package ru.clevertec.videohosting_api.updater.channel.impl;

import ru.clevertec.videohosting_api.dto.channel.ChannelUpdateDTO;
import ru.clevertec.videohosting_api.model.Category;
import ru.clevertec.videohosting_api.model.Channel;
import ru.clevertec.videohosting_api.service.category.CategoryService;
import ru.clevertec.videohosting_api.updater.channel.ChannelUpdater;

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
