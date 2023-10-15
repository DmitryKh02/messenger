package ru.khokhlov.messenger.service;

import ru.khokhlov.messenger.entity.User;

public interface MailSender {
    /**
     * Activates the email for a specific user.
     *
     * @param user The user whose email is to be activated.
     */
    void activateEmail(User user);
}
