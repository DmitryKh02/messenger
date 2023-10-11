package ru.khokhlov.messenger.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import ru.khokhlov.messenger.enums.Status;

import java.time.LocalDate;

@Validated
public record BasicUserInformation(
        @Schema(description = "Id пользователя")
        @NotNull(message = "Id is mandatory")
        @JsonProperty("user_id")
        Long userId,

        @Email
        @Schema(description = "Email")
        @NotBlank(message = "Email is mandatory")
        String email,

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
        String lastName,

        @Schema(description = "День рождения")
        @NotNull(message = "Birthdate cannot be null")
        @Past(message = "Birthdate must be a past date")
        LocalDate birthday,

        @Schema(description = "Фамилия")
        Status status
) {
}
