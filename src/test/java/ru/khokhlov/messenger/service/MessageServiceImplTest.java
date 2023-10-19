package ru.khokhlov.messenger.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.khokhlov.messenger.dto.request.MessageDTO;
import ru.khokhlov.messenger.dto.response.MessageHistory;
import ru.khokhlov.messenger.dto.response.MessageInfo;
import ru.khokhlov.messenger.dto.response.SuccessfulSubmission;
import ru.khokhlov.messenger.entity.Message;
import ru.khokhlov.messenger.entity.User;
import ru.khokhlov.messenger.repository.MessageRepository;
import ru.khokhlov.messenger.service.impl.MessageServiceImpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @InjectMocks
    private MessageServiceImpl messageService;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserService userService;

    @Test
    void testSendMessage_Success() {
        // Create test data
        String senderName = "senderUser";
        String recipientName = "recipientUser";
        User sender = new User();
        User recipient = new User();
        sender.setNickname(senderName);
        recipient.setNickname(recipientName);

        MessageDTO messageDTO = new MessageDTO(recipientName, "Hello, recipient!");

        Message expectedMessage = new Message();
        expectedMessage.setSender(sender);
        expectedMessage.setRecipient(recipient);
        expectedMessage.setContent(messageDTO.content());
        expectedMessage.setSentAt(Timestamp.valueOf(LocalDateTime.now()));

        // Mock UserService behavior
        when(userService.getUserByNickname(senderName)).thenReturn(sender);
        when(userService.getUserByNickname(recipientName)).thenReturn(recipient);

        // Mock MessageRepository to capture the saved message
        when(messageRepository.saveAndFlush(any(Message.class))).thenAnswer(invocation -> {
            Message savedMessage = invocation.getArgument(0);
            savedMessage.setMessageId(1L); // Simulate a saved message with an ID
            return savedMessage;
        });

        // Call the sendMessage method
        SuccessfulSubmission result = messageService.sendMessage(senderName, messageDTO);

        // Assertions
        verify(userService, times(2)).getUserByNickname(anyString()); // Verify user lookup
        verify(messageRepository).saveAndFlush(any(Message.class)); // Verify message save
        assertEquals(1L, result.message().getMessageId()); // Verify the message ID in the result
    }

    @Test
    void testGetMessageStory_Success() {
        // Create test data
        String senderName = "senderUser";
        String recipientName = "recipientUser";
        User sender = new User();
        User recipient = new User();
        sender.setNickname(senderName);
        recipient.setNickname(recipientName);

        MessageInfo messageInfo1 = new MessageInfo("Hello, recipient!", Timestamp.valueOf(LocalDateTime.now()));
        MessageInfo messageInfo2 = new MessageInfo("Hi, sender!", Timestamp.valueOf(LocalDateTime.now()));

        List<Message> messageList = new ArrayList<>();
        Message message1 = new Message();
        message1.setSender(sender);
        message1.setRecipient(recipient);
        message1.setContent(messageInfo1.content());
        message1.setSentAt(messageInfo1.sentAt());

        Message message2 = new Message();
        message2.setSender(recipient);
        message2.setRecipient(sender);
        message2.setContent(messageInfo2.content());
        message2.setSentAt(messageInfo2.sentAt());

        messageList.add(message1);
        messageList.add(message2);

        // Mock UserService behavior
        when(userService.getUserByNickname(senderName)).thenReturn(sender);
        when(userService.getUserByNickname(recipientName)).thenReturn(recipient);

        // Mock MessageRepository to return the list of messages
        when(messageRepository.getMessagesBySenderAndRecipientOrderBySentAt(sender, recipient))
                .thenReturn(messageList);

        // Call the getMessageStory method
        MessageHistory result = messageService.getMessageStory(senderName, recipientName);

        // Assertions
        verify(userService, times(2)).getUserByNickname(anyString()); // Verify user lookup
        verify(messageRepository).getMessagesBySenderAndRecipientOrderBySentAt(sender, recipient); // Verify message retrieval
        assertEquals(senderName, result.sender()); // Verify sender's nickname
        assertEquals(recipientName, result.recipient()); // Verify recipient's nickname
        assertEquals(2, result.messageList().size()); // Verify the number of messages
    }
}
