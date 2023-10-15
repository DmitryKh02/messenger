package ru.khokhlov.messenger.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.entity.User;
import ru.khokhlov.messenger.mapper.UserMapper;
import ru.khokhlov.messenger.repository.UserRepository;
import ru.khokhlov.messenger.service.FriendsServer;

@Service
@RequiredArgsConstructor
public class FriendsServerImpl implements FriendsServer {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse addFriend(String biba, String boba) throws EntityNotFoundException {

        User userBiba = userRepository.findByNickname(biba);
        User userBoba = userRepository.findByNickname(boba);

        if (userBiba == null || userBoba == null) {
            throw new EntityNotFoundException("User with nickname " + biba + "not found");
        }

        if (!(userBiba.getFriends().contains(userBoba) || userBoba.getFriends().contains(userBiba))) {
            userBiba.addFriends(userBoba);
            userRepository.saveAndFlush(userBiba);
        }

        return userMapper.fromUserToUSerResponse(userBiba);
    }
}
