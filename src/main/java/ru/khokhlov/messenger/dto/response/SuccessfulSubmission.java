package ru.khokhlov.messenger.dto.response;

import ru.khokhlov.messenger.entity.Message;

public record SuccessfulSubmission(
        Message message
) {
}
