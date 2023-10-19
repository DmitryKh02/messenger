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
    public UserResponse addFriend(String sender, String recipient) throws EntityNotFoundException {

        User senderUser = userRepository.findByNickname(sender);
        User recipientUser = userRepository.findByNickname(recipient);

        if (senderUser == null) {
            throw new EntityNotFoundException("User with nickname " + sender + "not found");
        }

        if (recipientUser == null) {
            throw new EntityNotFoundException("User with nickname " + recipient + "not found");
        }

        if (!(senderUser.getFriends().contains(recipientUser) || recipientUser.getFriends().contains(senderUser))) {
            senderUser.addFriends(recipientUser);
            userRepository.saveAndFlush(senderUser);
        }

        return userMapper.fromUserToUSerResponse(senderUser);
    }

    @Override
    public FriendsList getAllFriends(String nickname) throws EntityNotFoundException {
        User user = userRepository.findByNickname(nickname);
        return new FriendsList(user.getFriends().stream().map(User::getNickname).toList());
    }
}
