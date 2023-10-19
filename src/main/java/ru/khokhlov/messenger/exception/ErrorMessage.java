package ru.khokhlov.messenger.exception;

public record ErrorMessage(
        String fieldName,
        String message
) {
}
