package ru.khokhlov.messenger.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MessageHistory(
        @Schema(description = "First writer")
        String sender,
        @Schema(description = "Second writer")
        String recipient,
        @Schema(description = "List of messages")
        List<MessageInfo> messageList
) {
}
