package ru.khokhlov.messenger.service;

import jakarta.persistence.EntityNotFoundException;
import ru.khokhlov.messenger.dto.response.FriendsList;
import ru.khokhlov.messenger.dto.response.UserResponse;

public interface FriendsService {
    /**
     * Adds a friend connection between two users.
     *
     * @param sender The nickname of the first user.
     * @param recipient The nickname of the second user.
     * @return A UserResponse indicating the success of adding the friend connection.
     */
    UserResponse addFriend(String sender, String recipient);

    /**
     * Retrieves the list of friends for the principal user.
     *
     * @param nickname The nickname of user.
     * @return A FriendsList containing the list of friends.
     */
    FriendsList getAllFriends(String nickname) throws EntityNotFoundException;
}
