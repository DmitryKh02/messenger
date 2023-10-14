package ru.khokhlov.messenger.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.khokhlov.messenger.entity.User;
import ru.khokhlov.messenger.service.MailSender;
import ru.khokhlov.messenger.utils.MessageCreator;

//TODO переделать sender писем
@Service
@RequiredArgsConstructor
public class MailSenderImpl implements MailSender {
    private final MessageCreator messageCreator;
    private final JavaMailSender mailSender;

    @Value(value = "${application.email.address}")
    private String organization;
    @Value(value = "${application.server.address.auth}")
    private String auth;

    @Override
    public void activateEmail(User user) {
        String subject = "Подтверждение E-mail";
        String text = messageCreator.createWelcomeMessage(
                user.getNickname(),
                auth,
                user.getActivationCode());

        send(user.getEmail(), subject,text);
    }

    private void send(String destination, String subject, String message) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(organization);
        mailMessage.setTo(destination);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }
}
