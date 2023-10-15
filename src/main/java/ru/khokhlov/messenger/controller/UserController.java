package ru.khokhlov.messenger.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.khokhlov.messenger.dto.request.BasicUserInformation;
import ru.khokhlov.messenger.dto.request.NewPassword;
import ru.khokhlov.messenger.dto.request.UserDTO;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.service.UserService;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Manage user-related operations")
public class UserController {
    private final UserService userService;

    /**
     * Updates user information, allowing changes to email, username, first name, last name, middle name, date of birth, and status.
     *
     * @param userInfo Information about the user to update.
     * @return A UserResponse containing updated user details.
     */
    @Operation(
            summary = "Update User Information",
            description = "Allows the user to update their personal information, including email, username, name, date of birth, and status."
    )
    @PutMapping(value = "/update-info")
    public ResponseEntity<UserResponse> updateUserInformation(
            @Parameter(description = "User information to update") @Valid @RequestBody BasicUserInformation userInfo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUserInformation(userInfo));
    }

    /**
     * Updates a user's password.
     *
     * @param password Information about the new password.
     * @return A UserResponse indicating the success of the password update.
     */
    @Operation(
            summary = "Update User Password",
            description = "Allows the user to change their password."
    )
    @PutMapping(value = "/update-password")
    public ResponseEntity<UserResponse> updateUserPassword(
            @Parameter(description = "New password information") @Valid @RequestBody NewPassword password) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUserPassword(password));
    }

    /**
     * Deletes a user account, marking the user as inactive.
     *
     * @param user Information about the user to delete.
     * @return A UserResponse indicating the success of the user deletion.
     */
    @Operation(
            summary = "Delete User",
            description = "Marks the user's account as inactive, effectively deleting the user."
    )
    @DeleteMapping(value = "/delete")
    public ResponseEntity<UserResponse> deleteUser(
            @Parameter(description = "User information to delete") @Valid @RequestBody UserDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.deleteUser(user));
    }

    /**
     * Restores an inactive user account, marking the user as active.
     *
     * @param user Information about the user to restore.
     * @return A UserResponse indicating the success of the user restoration.
     */
    @Operation(
            summary = "Restore User",
            description = "Marks the user's account as active, effectively restoring the user."
    )
    @PutMapping(value = "/restore")
    public ResponseEntity<UserResponse> restoreUser(
            @Parameter(description = "User information to restore") @Valid @RequestBody UserDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.restoreUser(user));
    }
}