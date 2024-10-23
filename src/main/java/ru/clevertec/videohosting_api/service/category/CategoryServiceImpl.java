package ru.clevertec.videohosting_api.service.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.videohosting_api.exception.category.CategoryNotFoundException;
import ru.clevertec.videohosting_api.model.Category;
import ru.clevertec.videohosting_api.repository.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

    @Override
    public Category getCategoryByName(String name) {
        return categoryRepository.findByName(name)
                .orElseThrow(() ->
                        new CategoryNotFoundException("Category with name = " + name + " was not found"));
    }
}
