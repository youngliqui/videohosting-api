package ru.clevertec.videohosting_api.exception;

public class CustomValidationException extends RuntimeException {
    public CustomValidationException(String message) {
        super(message);
    }
}
