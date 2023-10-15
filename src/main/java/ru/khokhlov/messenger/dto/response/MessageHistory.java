package ru.khokhlov.messenger.dto.response;

import java.util.List;

public record MessageHistory(
        String biba,
        String boba,
        List<MessageInfo> messageList
) {
}
