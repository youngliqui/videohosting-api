package ru.clevertec.videohosting_api.service.category;

import ru.clevertec.videohosting_api.model.Category;

public interface CategoryService {
    Category getCategoryByName(String name);
}
