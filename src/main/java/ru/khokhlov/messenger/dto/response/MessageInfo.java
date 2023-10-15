package ru.khokhlov.messenger.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.sql.Timestamp;

public record MessageInfo(
        @Schema(description = "Some text")
        String content,

        @Schema(description = "Time of send message")
        Timestamp sentAt
) {
}
