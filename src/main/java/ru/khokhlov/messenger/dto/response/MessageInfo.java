package ru.khokhlov.messenger.dto.response;

import java.sql.Timestamp;

public record MessageInfo(
        String content,
        Timestamp sentAt
) {
}
