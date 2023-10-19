package ru.khokhlov.messenger.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import ru.khokhlov.messenger.dto.request.RegistrationFormDTO;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.entity.Role;
import ru.khokhlov.messenger.entity.User;
import ru.khokhlov.messenger.mapper.UserMapper;
import ru.khokhlov.messenger.repository.UserRepository;
import ru.khokhlov.messenger.service.impl.UserServiceImpl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleService roleService;

    @Mock
    private MailSender mailSender;

    @Test
    void testCreateUser_Success() {
        // Create test data
        RegistrationFormDTO registrationFormDTO = new RegistrationFormDTO(
                "test@example.com",
                "StrongestPassword1!",
                "testUser",
                "Ivan",
                "Ivanovich",
                "Ivanov");

        User user = new User();
        user.setEmail("test@example.com");
        user.setEmail(registrationFormDTO.email());
        user.setNickname(registrationFormDTO.nickname());
        when(userMapper.fromRegistrationDTOToUser(registrationFormDTO)).thenReturn(user);

        // Mock UserRepository behavior
        when(userRepository.findByEmail(registrationFormDTO.email())).thenReturn(null);
        when(userRepository.findByNickname(registrationFormDTO.nickname())).thenReturn(null);

        // Mock RoleService to return a role
        Role userRole = new Role();
        when(roleService.getRoleByName("USER")).thenReturn(userRole);

        // Mock UserRepository to capture the saved user
        when(userRepository.saveAndFlush(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setUserId(1L); // Simulate a saved user with an ID
            return savedUser;
        });

        UserResponse userResponse = new UserResponse(
                1L,
                "test@example.com",
                "testUser",
                "Ivan",
                "Ivanovich",
                "Ivanov",
                null,
                null);

        when(userMapper.fromUserToUSerResponse(user)).thenReturn(userResponse);

        // Call the createUser method
        UserResponse result = userService.createUser(registrationFormDTO);

        // Assertions
        verify(roleService).getRoleByName("USER"); // Verify role retrieval
        verify(userRepository).saveAndFlush(any(User.class)); // Verify user save
        assertNotNull(result); // Verify that a UserResponse is returned
        assertEquals(1L, result.userId()); // Verify the user ID in the result
    }

    @Test
    void testGetUserByNickname_Success() {
        // Create test data
        String nickname = "testUser";
        User user = new User();
        user.setNickname(nickname);

        // Mock UserRepository behavior
        when(userRepository.findByNickname(nickname)).thenReturn(user);

        // Call the getUserByNickname method
        User result = userService.getUserByNickname(nickname);

        // Assertions
        verify(userRepository).findByNickname(nickname); // Verify user lookup
        assertNotNull(result); // Verify that a User is returned
        assertEquals(nickname, result.getNickname()); // Verify the user's nickname in the result
    }

    @Test
    void testLoadUserByUsername_Success() {
        // Create test data
        String nickname = "testUser";
        User user = new User();
        user.setNickname(nickname);
        user.setPassword("StrongestPassword1!");

        // Mock UserRepository behavior
        when(userRepository.findByNickname(nickname)).thenReturn(user);

        // Mock RoleService to return a role
        Role userRole = new Role();
        userRole.setRoleId(1L);
        userRole.setName("USER");
        user.setRole(userRole);

        // Call the loadUserByUsername method
        UserDetails result = userService.loadUserByUsername(nickname);

        // Assertions
        verify(userRepository).findByNickname(nickname); // Verify user lookup
        assertNotNull(result); // Verify that UserDetails is returned
        assertEquals(nickname, result.getUsername()); // Verify the username in the result
    }

    // Add tests for other methods as needed

    @Test
    void testDeleteConfirmation_Success() {
        // Create test data
        List<User> userList = new ArrayList<>();
        User user = new User();
        user.setAccountActivity(false);
        userList.add(user);

        // Mock UserRepository behavior
        when(userRepository.findAllByAccountActivity(false)).thenReturn(userList);

        // Mock LocalDateTime
        LocalDateTime deleteDate = LocalDateTime.now().minusDays(20);
        user.setDeleteTimestamp(Timestamp.valueOf(deleteDate));

        // Call the deleteConfirmation method
        int result = userService.deleteConfirmation();

        // Assertions
        verify(userRepository).findAllByAccountActivity(false); // Verify inactive users lookup
        verify(userRepository).deleteAll(userList); // Verify user deletion
        assertEquals(1, result); // Verify the number of users deleted
    }
}
