package ru.khokhlov.messenger.service;

import jakarta.persistence.EntityNotFoundException;
import ru.khokhlov.messenger.dto.response.FriendsList;
import ru.khokhlov.messenger.dto.response.UserResponse;

public interface FriendsService {
    /**
     * Adds a friend connection between two users.
     *
     * @param biba The nickname of the first user.
     * @param boba The nickname of the second user.
     * @return A UserResponse indicating the success of adding the friend connection.
     */
    UserResponse addFriend(String biba, String boba);

    FriendsList getAllFriends(String nickname) throws EntityNotFoundException;
}
