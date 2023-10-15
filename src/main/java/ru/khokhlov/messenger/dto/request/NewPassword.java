package ru.khokhlov.messenger.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public record NewPassword(
        @Schema(description = "Старый пароль")
        @Pattern(regexp = "^(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid old password format")
        @NotBlank(message = "Password is mandatory")
        @JsonProperty("old_password")
        String oldPassword,

        @Schema(description = "Новый пароль")
        @Pattern(regexp = "^(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid new password format")
        @NotBlank(message = "New password is mandatory")
        @JsonProperty("new_password")
        String newPassword,

        @Schema(description = "Подтверждение нового пароля")
        @Pattern(regexp = "^(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid new confirmed password format")
        @NotBlank(message = "Password is mandatory")
        @JsonProperty("new_confirmed_password")
        String newConfirmedPassword
) {
}
