package ru.khokhlov.messenger.dto.response;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import ru.khokhlov.messenger.enums.Status;

import java.time.LocalDate;
public record UserResponse(
        @Schema(description = "Id пользователя")
        Long userId,

        @Schema(description = "Email")
        String email,

        @Schema(description = "Имя пользователя")
        String nickname,

        @Schema(description = "Имя")
        @JsonProperty("first_name")
        String firstName,

        @Schema(description = "Отчество")
        @JsonProperty("middle_name")
        String middleName,

        @Schema(description = "Фамилия")
        @JsonProperty("last_name")
        String lastName,

        @Schema(description = "Фамилия")
        LocalDate birthday,

        @Schema(description = "Статус")
        Status status

) {
}
