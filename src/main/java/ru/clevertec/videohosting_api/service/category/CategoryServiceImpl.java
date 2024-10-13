package ru.clevertec.videohosting_api.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.exception.CategoryNotFoundException;
import ru.clevertec.videohosting_api.model.Category;
import ru.clevertec.videohosting_api.repository.CategoryRepository;
import ru.clevertec.videohosting_api.service.category.CategoryService;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category with name = " + name + " was not found"));
    }
}
