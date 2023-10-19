package ru.khokhlov.messenger.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import ru.khokhlov.messenger.entity.User;
import ru.khokhlov.messenger.service.impl.MailSenderImpl;
import ru.khokhlov.messenger.utils.MessageCreator;


@ExtendWith(MockitoExtension.class)
class MailSenderImplTest {
    @InjectMocks
    private MailSenderImpl mailSenderService;

    @Mock
    private MessageCreator messageCreator;

    @Mock
    private JavaMailSender mailSender;

    @Test
    void testActivateEmail() {
        // Create a user for testing
        User user = new User();
        user.setNickname("testuser");
        user.setEmail("testuser@example.com");
        user.setActivationCode("activationCode123");

        // Mock MessageCreator to capture the email message
        Mockito.when(messageCreator.createWelcomeMessage("testuser", null , "activationCode123"))
                .thenReturn("Привет, testuser!\nДля подтверждения почты перейдите по ссылке: \nhttp:localhost:8080/security/activate/activationCode123");

        // Call the activateEmail method
        mailSenderService.activateEmail(user);

        // Assertions
        // Verify that mailSender.send was called
        Mockito.verify(mailSender).send(Mockito.any(SimpleMailMessage.class));
    }
}
