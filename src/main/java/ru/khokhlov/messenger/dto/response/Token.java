package ru.khokhlov.messenger.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record Token(
        @Schema(description = "Bearer type of token")
        String type,
        @Schema(description = "JWT Token")
        String accessToken
) {
}
