package ru.khokhlov.messenger.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.khokhlov.messenger.service.UserService;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private final UserService userService;

    @Scheduled(fixedRate = 86400000)
    public void deleteAllInactiveUsers() {
        int countOfDeleteUsers = userService.deleteConfirmation();
        log.info("ScheduledTasks.deleteAllInactiveUsers - Users deleted {}", countOfDeleteUsers);
    }
}