package ru.khokhlov.messenger.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record MessageDTO(
        @Schema(description = "Имя пользователя")
        @NotBlank(message = "Nickname is mandatory")
        String recipient,

        @Schema(description = "Сообщение")
        @NotBlank(message = "Text is mandatory")
        String content
) {
}

