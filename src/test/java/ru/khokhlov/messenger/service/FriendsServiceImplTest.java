package ru.khokhlov.messenger.service;

import jakarta.persistence.EntityNotFoundException;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.entity.User;
import ru.khokhlov.messenger.mapper.UserMapper;
import ru.khokhlov.messenger.repository.UserRepository;
import ru.khokhlov.messenger.service.impl.FriendsServiceImpl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FriendsServiceImplTest {
    @InjectMocks
    private FriendsServiceImpl friendsService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    void testAddFriend_Success() throws EntityNotFoundException {
        // Create test data
        String bibaNickname = "Biba";
        String bobaNickname = "Boba";
        User userBiba = new User();
        userBiba.setNickname(bibaNickname);
        User userBoba = new User();
        userBoba.setNickname(bobaNickname);

        // Mock UserRepository behavior
        Mockito.when(userRepository.findByNickname(bibaNickname)).thenReturn(userBiba);
        Mockito.when(userRepository.findByNickname(bobaNickname)).thenReturn(userBoba);

        // Mock UserMapper
        UserResponse userResponse = new UserResponse(
                userBiba.getUserId(),
                userBiba.getEmail(),
                userBiba.getNickname(),
                userBiba.getFirstName(),
                userBiba.getMiddleName(),
                userBiba.getLastName(),
                LocalDate.now(),
                userBiba.getStatus()
        );
        Mockito.when(userMapper.fromUserToUSerResponse(userBiba)).thenReturn(userResponse);

        // Call the service method
        UserResponse result = friendsService.addFriend(bibaNickname, bobaNickname);

        // Assertions
        Mockito.verify(userRepository).saveAndFlush(userBiba); // Ensure saveAndFlush is called
        assert result != null;
    }

    @Test
    void testAddFriend_UserNotFound() {
        // Create test data
        String bibaNickname = "Biba";
        String bobaNickname = "Boba";

        // Call the service method and expect EntityNotFoundException
        assertThrows(EntityNotFoundException.class, () -> friendsService.addFriend(bibaNickname, bobaNickname));
    }
}
