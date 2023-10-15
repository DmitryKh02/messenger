package ru.khokhlov.messenger.service;

import ru.khokhlov.messenger.dto.request.MessageDTO;
import ru.khokhlov.messenger.dto.response.MessageHistory;
import ru.khokhlov.messenger.dto.response.SuccessfulSubmission;

public interface MessagesService {
    /**
     * Sends a message from the specified sender to the given recipient.
     *
     * @param senderName The name of the sender.
     * @param messageDTO The message data to be sent.
     * @return A SuccessfulSubmission object indicating the success of the message sending.
     */
    SuccessfulSubmission sendMessage(String senderName, MessageDTO messageDTO);

    /**
     * Retrieves the message history for a user with the given name and their conversation with a specific companion.
     *
     * @param name The name of the user for whom the message history is requested.
     * @param companionNickname The nickname of the conversation companion.
     * @return A MessageHistory object containing the message history for the specified user and companion.
     */
    MessageHistory getMessageStory(String name, String companionNickname);
}
