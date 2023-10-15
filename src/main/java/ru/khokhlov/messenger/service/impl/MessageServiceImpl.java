package ru.khokhlov.messenger.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.khokhlov.messenger.dto.request.MessageDTO;
import ru.khokhlov.messenger.dto.response.MessageHistory;
import ru.khokhlov.messenger.dto.response.MessageInfo;
import ru.khokhlov.messenger.dto.response.SuccessfulSubmission;
import ru.khokhlov.messenger.entity.Message;
import ru.khokhlov.messenger.entity.User;
import ru.khokhlov.messenger.repository.MessageRepository;
import ru.khokhlov.messenger.service.MessagesService;
import ru.khokhlov.messenger.service.UserService;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessagesService {
    private final MessageRepository messageRepository;
    private final UserService userService;


    @Override
    public SuccessfulSubmission sendMessage(String senderName, MessageDTO messageDTO) {
        User sender = userService.getUserByNickname(senderName);
        User recipient = userService.getUserByNickname(messageDTO.recipient());

        Message message = new Message();
        message.setSender(sender);
        message.setRecipient(recipient);
        message.setContent(messageDTO.content());
        message.setSentAt(Timestamp.valueOf(LocalDateTime.now()));
        messageRepository.saveAndFlush(message);

        return new SuccessfulSubmission(message);
    }

    @Override
    public MessageHistory getMessageStory(String name, String companionNickname) {
        User sender = userService.getUserByNickname(name);
        User recipient = userService.getUserByNickname(companionNickname);
        List<Message> messageList = messageRepository.getMessagesBySenderAndRecipientOrderBySentAt(sender,recipient);

        List<MessageInfo> messageInfoList = new ArrayList<>();

        for (Message message : messageList) {
            MessageInfo messageInfo = new MessageInfo(message.getContent(), message.getSentAt());
            messageInfoList.add(messageInfo);
        }
        return new MessageHistory(sender.getNickname(), recipient.getNickname(),messageInfoList);
    }
}
