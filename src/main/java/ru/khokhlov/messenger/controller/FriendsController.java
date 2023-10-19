package ru.khokhlov.messenger.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khokhlov.messenger.dto.request.FriendDTO;
import ru.khokhlov.messenger.dto.response.FriendsList;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.service.FriendsService;

import java.security.Principal;

@RestController
@RequestMapping("/friends")
@RequiredArgsConstructor
@Tag(name = "Friends Interaction Controller", description = "Friends and related actions")
public class FriendsController {
    private final FriendsService friendsService;

    /**
     * Adds a user to the friend list by nickname.
     *
     * @param principal The principal user.
     * @param friendDTO Information about the friend to be added.
     * @return A UserResponse indicating the success of adding the friend.
     */
    @Operation(
            summary = "Add Friend",
            description = "Allows the user to add another user as a friend by providing their nickname."
    )
    @PostMapping(value = "/add")
    public ResponseEntity<UserResponse> addFriend(
            @Parameter(description = "The principal user") Principal principal,
            @Parameter(description = "Friend information") @Valid @RequestBody FriendDTO friendDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(friendsService.addFriend(principal.getName(), friendDTO.friend()));
    }

    @Operation(
            summary = "Add Friend",
            description = "Allows the user to add another user as a friend by providing their nickname."
    )
    @GetMapping(value = "/get")
    public ResponseEntity<FriendsList> getFriends(
            @Parameter(description = "The principal user") Principal principal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(friendsService.getAllFriends(principal.getName()));
    }
}