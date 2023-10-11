package ru.khokhlov.messenger.dto;

public record Token(
        String type,
        String accessToken
) {
}
