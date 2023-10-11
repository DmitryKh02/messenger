package ru.khokhlov.messenger.service;

import ru.khokhlov.messenger.entity.User;

public interface MailSender {
    void activateEmail(User user);
}
