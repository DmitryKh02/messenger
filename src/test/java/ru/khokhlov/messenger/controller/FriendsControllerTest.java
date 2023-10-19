package ru.khokhlov.messenger.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.khokhlov.messenger.dto.request.FriendDTO;
import ru.khokhlov.messenger.dto.response.FriendsList;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.enums.Status;
import ru.khokhlov.messenger.service.FriendsService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FriendsControllerTest {

    @InjectMocks
    private FriendsController friendsController;

    @Mock
    private FriendsService friendsService;

    private  Principal principal;

    @BeforeEach
    public void setUp(){
        principal = () -> "CurrentUser";
    }

    @Test
    void testAddFriend_Success() {
        // Create test data
        FriendDTO friendDTO = new FriendDTO("NewFriend");

        // Mock FriendsService behavior
        UserResponse userResponse = new UserResponse(1L, "new@friend.com", "NewFriend", "New", null, "Friend", LocalDate.now(), Status.FREE);
        when(friendsService.addFriend(principal.getName(), friendDTO.friend())).thenReturn(userResponse);

        // Call the addFriend method
        ResponseEntity<UserResponse> result = friendsController.addFriend(principal, friendDTO);

        // Assertions
        verify(friendsService).addFriend(principal.getName(), friendDTO.friend()); // Verify addFriend call
        assertNotNull(result); // Verify that a response is returned
        assertEquals(HttpStatus.CREATED, result.getStatusCode()); // Verify the HTTP status code
        assertEquals(userResponse, result.getBody()); // Verify the response body
    }

    @Test
    void testGetFriends_Success() {
        // Create test data
        FriendsList friendsList = new FriendsList(List.of("Friend1", "Friend2"));

        // Mock FriendsService behavior
        when(friendsService.getAllFriends(principal.getName())).thenReturn(friendsList);

        // Call the getFriends method
        ResponseEntity<FriendsList> result = friendsController.getFriends(principal);

        // Assertions
        verify(friendsService).getAllFriends(principal.getName()); // Verify getAllFriends call
        assertNotNull(result); // Verify that a response is returned
        assertEquals(HttpStatus.CREATED, result.getStatusCode()); // Verify the HTTP status code
        assertEquals(friendsList, result.getBody()); // Verify the response body
    }
}
