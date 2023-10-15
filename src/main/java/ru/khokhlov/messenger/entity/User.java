package ru.khokhlov.messenger.entity;

import jakarta.persistence.*;
import lombok.Data;
import ru.khokhlov.messenger.enums.Status;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "birthday")
    private Timestamp birthday;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "is_account_active")
    private boolean accountActivity;

    @Column(name = "delete_timestamp")
    private Timestamp deleteTimestamp;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @ManyToMany
    @JoinTable(
            name = "friends",
            joinColumns = @JoinColumn(name = "friend_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> friends = new HashSet<>();


    public void addFriends(User friend) {
        if (friends == null) {
            friends = new HashSet<>();
        }

        if (friend != null) {
            friends.add(friend);
        }
    }
}
