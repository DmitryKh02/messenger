package ru.khokhlov.messenger.dto.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.validation.annotation.Validated;

@Validated
public record RegistrationFormDTO(
        @Email
        @Schema(description = "Email")
        @NotBlank(message = "Email is mandatory")
        String email,

        /*
         *     Минимум 8 символов.
         *     Содержит хотя бы одну строчную букву (a-z).
         *     Содержит хотя бы одну заглавную букву (A-Z).
         *     Содержит хотя бы одну цифру (0-9).
         *     Содержит хотя бы один специальный символ из [@ $!%*?&].
         */
        @Schema(description = "Пароль")
        @Pattern(regexp = "^(?=.*[a-zа-я])(?=.*[A-ZА-Я])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid password format")
        @NotBlank(message = "Password is mandatory")
        String password,

        @Schema(description = "Имя пользователя")
        @NotBlank(message = "Nickname is mandatory")
        String nickname,

        @Schema(description = "Имя")
        @Pattern(regexp = "^[A-Za-zА-Яа-я]+$", message = "Invalid first name format")
        @JsonProperty("first_name")
        String firstName,

        @Schema(description = "Отчество")
        @Pattern(regexp = "^[A-Za-zА-Яа-я]+$", message = "Invalid middle name format")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @JsonProperty("middle_name")
        String middleName,
        @Schema(description = "Фамилия")
        @Pattern(regexp = "^[A-Za-zА-Яа-я]+$", message = "Invalid last name format")
        @JsonProperty("last_name")
        String lastName
) {
}
