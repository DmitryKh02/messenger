package ru.khokhlov.messenger.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khokhlov.messenger.dto.request.MessageDTO;
import ru.khokhlov.messenger.dto.response.MessageHistory;
import ru.khokhlov.messenger.dto.response.SuccessfulSubmission;
import ru.khokhlov.messenger.service.MessagesService;

import java.security.Principal;


@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
@Tag(name = "Messages Controller", description = "Controller for sending and receiving messages for a specific user")
public class MessagesController {
    private final MessagesService messagesService;

    /**
     * Send a message from the current user.
     *
     * @param principal The principal user.
     * @param messageDTO Information about the message to be sent.
     * @return A SuccessfulSubmission indicating the success of sending the message.
     */
    @Operation(
            summary = "Send Message",
            description = "Send a message from the current user."
    )
    @PostMapping
    public ResponseEntity<SuccessfulSubmission> sendMessage(
            @Parameter(description = "The current user") Principal principal,
            @Parameter(description = "Message information") @Valid @RequestBody MessageDTO messageDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messagesService.sendMessage(principal.getName(), messageDTO));
    }

    /**
     * Retrieve the message history for a specific user and companion.
     *
     * @param principal The principal user.
     * @param companionNickname The nickname of the conversation companion.
     * @return A MessageHistory containing the message history.
     */
    @Operation(
            summary = "Retrieve Message History",
            description = "Retrieve the message history for a specific user and companion."
    )
    @GetMapping(value = "/restore/{companionNickname}")
    public ResponseEntity<MessageHistory> getMessageStory(
            @Parameter(description = "The current user") Principal principal,
            @Parameter(description = "User and companion information") @PathVariable String companionNickname) {
        return ResponseEntity.status(HttpStatus.CREATED).body(messagesService.getMessageStory(principal.getName(), companionNickname));
    }
}
