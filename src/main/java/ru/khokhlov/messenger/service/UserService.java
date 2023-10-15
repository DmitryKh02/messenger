package ru.khokhlov.messenger.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.khokhlov.messenger.dto.request.BasicUserInformation;
import ru.khokhlov.messenger.dto.request.NewPassword;
import ru.khokhlov.messenger.dto.request.RegistrationFormDTO;
import ru.khokhlov.messenger.dto.request.UserDTO;
import ru.khokhlov.messenger.dto.response.ExitResponse;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.entity.User;

public interface UserService {
    /**
     * Creates a new user based on the provided registration information.
     *
     * @param userRegistrationInfo The registration information for the new user.
     * @return A UserResponse containing the details of the newly created user.
     */
    UserResponse createUser(RegistrationFormDTO userRegistrationInfo);

    /**
     * Retrieves a user by their nickname.
     *
     * @param nickname The nickname of the user to retrieve.
     * @return The user with the specified nickname.
     */
    User getUserByNickname(String nickname);

    /**
     * Checks if a user's email has been activated using a code.
     *
     * @param code The activation code to verify.
     * @return true if the email is activated, false otherwise.
     */
    boolean isEmailActivate(String code);

    /**
     * Updates a user's basic information.
     *
     * @param nickname user login
     * @param userInfo The updated basic user information.
     * @return A UserResponse containing the updated user details.
     */
    UserResponse updateUserInformation(String nickname, BasicUserInformation userInfo);

    /**
     * Updates a user's password.
     *
     * @param nickname user login
     * @param password The new password for the user.
     * @return A UserResponse indicating the success of the password update.
     */
    UserResponse updateUserPassword(String nickname, NewPassword password);

    /**
     * Deletes a user's account.
     *
     * @param user The user to be deleted.
     * @return A UserResponse indicating the success of the user deletion.
     */
    UserResponse deleteUser(UserDTO user);

    /**
     * Restores a user's account.
     *
     * @param user The user to be restored.
     * @return A UserResponse indicating the success of the user restoration.
     */
    UserResponse restoreUser(UserDTO user);

    /**
     * Initiates a user account deletion confirmation process.
     *
     * @return An integer representing the result of the deletion confirmation process.
     */
    int deleteConfirmation();

    /**
     * Loads user details for authentication based on their email.
     *
     * @param email The email of the user for authentication.
     * @return UserDetails for the specified user.
     */
    UserDetails loadUserByUsername(String email);
}
