package ru.clevertec.videohosting_api.exception.category;

import ru.clevertec.videohosting_api.exception.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(String message) {
        super(message);
    }
}
