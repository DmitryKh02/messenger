package ru.khokhlov.messenger.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.khokhlov.messenger.dto.response.FriendsList;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.entity.User;
import ru.khokhlov.messenger.mapper.UserMapper;
import ru.khokhlov.messenger.repository.UserRepository;
import ru.khokhlov.messenger.service.FriendsService;

@Service
@RequiredArgsConstructor
public class FriendsServiceImpl implements FriendsService {
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

    @Override
    public FriendsList getAllFriends(String nickname) throws EntityNotFoundException {
        User user = userRepository.findByNickname(nickname);
        return new FriendsList(user.getFriends().stream().map(User::getNickname).toList());
    }
}
