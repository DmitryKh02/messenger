package ru.khokhlov.messenger.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private User recipient;

    @Column(name = "content", length = 1024, nullable = false)
    private String content;

    @Column(name = "sent_at")
    private Timestamp sentAt;

//    public Message(User sender, User recipient, String content, Timestamp sentAt){
//        this.sender = sender;
//        this.recipient = recipient;
//        this.content = content;
//        this.sentAt = sentAt;
//    }
}
