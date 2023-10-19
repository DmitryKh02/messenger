package ru.khokhlov.messenger.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.khokhlov.messenger.dto.request.MessageDTO;
import ru.khokhlov.messenger.dto.response.*;
import ru.khokhlov.messenger.entity.Message;
import ru.khokhlov.messenger.service.MessagesService;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessagesControllerTest {

    @InjectMocks
    private MessagesController messagesController;

    @Mock
    private MessagesService messagesService;

    private  Principal principal;

    @BeforeEach
    public void setUp(){
        principal = () -> "CurrentUser";
    }

    @Test
    void testSendMessage_Success() {
        // Create test data
        MessageDTO messageDTO = new MessageDTO("CompanionUser", "Hello, how are you?");

        // Mock MessagesService behavior
        Message message = new Message();
        SuccessfulSubmission successfulSubmission = new SuccessfulSubmission(message);
        when(messagesService.sendMessage(principal.getName(), messageDTO)).thenReturn(successfulSubmission);

        // Call the sendMessage method
        ResponseEntity<SuccessfulSubmission> result = messagesController.sendMessage(principal, messageDTO);

        // Assertions
        verify(messagesService).sendMessage(principal.getName(), messageDTO); // Verify sendMessage call
        assertNotNull(result); // Verify that a response is returned
        assertEquals(HttpStatus.CREATED, result.getStatusCode()); // Verify the HTTP status code
        assertEquals(successfulSubmission, result.getBody()); // Verify the response body
    }

    @Test
    void testGetMessageStory_Success() {
        // Create test data
        String companionNickname = "CompanionUser";
        MessageHistory messageHistory = new MessageHistory("CurrentUser", "CompanionUser", List.of(
                new MessageInfo("Hello", Timestamp.valueOf(LocalDateTime.now())),
                new MessageInfo("Hi there!", Timestamp.valueOf(LocalDateTime.now()))
        ));

        // Mock MessagesService behavior
        when(messagesService.getMessageStory(principal.getName(), companionNickname)).thenReturn(messageHistory);

        // Call the getMessageStory method
        ResponseEntity<MessageHistory> result = messagesController.getMessageStory(principal, companionNickname);

        // Assertions
        verify(messagesService).getMessageStory(principal.getName(), companionNickname); // Verify getMessageStory call
        assertNotNull(result); // Verify that a response is returned
        assertEquals(HttpStatus.CREATED, result.getStatusCode()); // Verify the HTTP status code
        assertEquals(messageHistory, result.getBody()); // Verify the response body
    }
}