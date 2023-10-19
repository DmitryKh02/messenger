package ru.khokhlov.messenger.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.khokhlov.messenger.dto.request.BasicUserInformation;
import ru.khokhlov.messenger.dto.request.FriendDTO;
import ru.khokhlov.messenger.dto.request.NewPassword;
import ru.khokhlov.messenger.dto.request.UserDTO;
import ru.khokhlov.messenger.dto.response.FriendsList;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.enums.Status;
import ru.khokhlov.messenger.service.FriendsService;
import ru.khokhlov.messenger.service.UserService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private  Principal principal;

    @BeforeEach
    public void setUp(){
        principal = () -> "CurrentUser";
    }

    @Test
    void testUpdateUserInformation_Success() {
        // Create test data
        BasicUserInformation userInfo = new BasicUserInformation(
                "UpdatedEmail@example.com",
                "UpdatedUsername",
                "UpdatedFirstName",
                "UpdatedMiddleName",
                "UpdatedLastName",
                LocalDate.of(2000, 1, 1),
                Status.FREE
        );

        // Mock UserService behavior
        UserResponse userResponse = new UserResponse(1L, userInfo.email(), userInfo.nickname(), userInfo.firstName(), userInfo.middleName(), userInfo.lastName(), userInfo.birthday(), userInfo.status());
        when(userService.updateUserInformation(principal.getName(), userInfo)).thenReturn(userResponse);

        // Call the updateUserInformation method
        ResponseEntity<UserResponse> result = userController.updateUserInformation(principal, userInfo);

        // Assertions
        verify(userService).updateUserInformation(principal.getName(), userInfo); // Verify updateUserInformation call
        assertNotNull(result); // Verify that a response is returned
        assertEquals(HttpStatus.CREATED, result.getStatusCode()); // Verify the HTTP status code
        assertEquals(userResponse, result.getBody()); // Verify the response body
    }

    @Test
    void testUpdateUserPassword_Success() {
        // Create test data
        NewPassword newPassword = new NewPassword("OldPassword", "NewPassword", "NewPassword");

        // Mock UserService behavior
        UserResponse userResponse = new UserResponse(1L, "user@example.com", "CurrentUser", "John", "Doe", "Doe", LocalDate.now(), Status.FREE);
        when(userService.updateUserPassword(principal.getName(), newPassword)).thenReturn(userResponse);

        // Call the updateUserPassword method
        ResponseEntity<UserResponse> result = userController.updateUserPassword(principal, newPassword);

        // Assertions
        verify(userService).updateUserPassword(principal.getName(), newPassword); // Verify updateUserPassword call
        assertNotNull(result); // Verify that a response is returned
        assertEquals(HttpStatus.CREATED, result.getStatusCode()); // Verify the HTTP status code
        assertEquals(userResponse, result.getBody()); // Verify the response body
    }

    @Test
    void testDeleteUser_Success() {
        // Create test data
        UserDTO userDTO = new UserDTO("CurrentUser", "Password");

        // Mock UserService behavior
        UserResponse userResponse = new UserResponse(1L, "user@example.com", "CurrentUser", "John", "Doe", "Doe", LocalDate.now(), Status.FREE);
        when(userService.deleteUser(userDTO)).thenReturn(userResponse);

        // Call the deleteUser method
        ResponseEntity<UserResponse> result = userController.deleteUser(userDTO);

        // Assertions
        verify(userService).deleteUser(userDTO); // Verify deleteUser call
        assertNotNull(result); // Verify that a response is returned
        assertEquals(HttpStatus.CREATED, result.getStatusCode()); // Verify the HTTP status code
        assertEquals(userResponse, result.getBody()); // Verify the response body
    }

    @Test
    void testRestoreUser_Success() {
        // Create test data
        UserDTO userDTO = new UserDTO("CurrentUser", "Password");

        // Mock UserService behavior
        UserResponse userResponse = new UserResponse(1L, "user@example.com", "CurrentUser", "John", "Doe", "Doe", LocalDate.now(), Status.FREE);
        when(userService.restoreUser(userDTO)).thenReturn(userResponse);

        // Call the restoreUser method
        ResponseEntity<UserResponse> result = userController.restoreUser(userDTO);

        // Assertions
        verify(userService).restoreUser(userDTO); // Verify restoreUser call
        assertNotNull(result); // Verify that a response is returned
        assertEquals(HttpStatus.CREATED, result.getStatusCode()); // Verify the HTTP status code
        assertEquals(userResponse, result.getBody()); // Verify the response body
    }
}
