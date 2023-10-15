package ru.khokhlov.messenger.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MessageHistory(
        @Schema(description = "First writer")
        String biba,
        @Schema(description = "Second writer")
        String boba,
        @Schema(description = "List of messages")
        List<MessageInfo> messageList
) {
}
