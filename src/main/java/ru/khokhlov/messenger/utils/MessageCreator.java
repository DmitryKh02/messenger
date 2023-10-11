package ru.khokhlov.messenger.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageCreator {
    @Value(value = "${application.server.address}")
    private String severAddress;

    public String createWelcomeMessage(String username, String entity, String link) {
        log.debug("MessageCreator.createWelcomeMessage - username {}, entity {}, link {}", username, entity, link);
        return "Привет, " + username + "!\nДля подтверждения почты перейдите по ссылке: \n" + severAddress + entity + "/activate/" + link;
    }
}
