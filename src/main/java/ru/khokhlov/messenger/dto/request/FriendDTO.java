package ru.khokhlov.messenger.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;

@Validated
public record FriendDTO(
        @Schema(description = "Имя пользователя")
        @NotBlank(message = "Nickname is mandatory")
        String friend
) {
}
