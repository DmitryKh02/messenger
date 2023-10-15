package ru.khokhlov.messenger.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import ru.khokhlov.messenger.entity.Message;

public record SuccessfulSubmission(
        @Schema(description = "message")
        Message message
) {
}
