package ru.khokhlov.messenger.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public record UserDTO(
        @Schema(description = "Имя пользователя")
        @NotBlank(message = "Nickname is mandatory")
        String nickname,

        @Schema(description = "Пароль")
        @Pattern(regexp = "^(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid password format")
        @NotBlank(message = "Password is mandatory")
        String password
) {
}
