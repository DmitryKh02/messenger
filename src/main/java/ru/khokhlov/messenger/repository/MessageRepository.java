package ru.khokhlov.messenger.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.khokhlov.messenger.entity.Message;
import ru.khokhlov.messenger.entity.User;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> getMessagesBySenderAndRecipientOrderBySentAt(User sender, User recipient);
}
