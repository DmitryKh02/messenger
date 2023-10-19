package ru.khokhlov.messenger.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record FriendsList(
        @Schema(description = "List of friends nicknames")
        List<String> friendsList
) {
}
