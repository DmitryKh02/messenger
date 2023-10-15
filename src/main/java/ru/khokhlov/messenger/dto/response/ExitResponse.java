package ru.khokhlov.messenger.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExitResponse(
        @Schema(description = "User nickname")
        String nickname
) {
}
