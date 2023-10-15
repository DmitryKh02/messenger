package ru.khokhlov.messenger.service;

import ru.khokhlov.messenger.dto.response.UserResponse;

public interface FriendsServer {
    /**
     * Adds a friend connection between two users.
     *
     * @param biba The nickname of the first user.
     * @param boba The nickname of the second user.
     * @return A UserResponse indicating the success of adding the friend connection.
     */
    UserResponse addFriend(String biba, String boba);
}
