package ru.khokhlov.messenger.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.khokhlov.messenger.dto.request.BasicUserInformation;
import ru.khokhlov.messenger.dto.request.NewPassword;
import ru.khokhlov.messenger.dto.request.RegistrationFormDTO;
import ru.khokhlov.messenger.dto.request.UserDTO;
import ru.khokhlov.messenger.dto.response.UserResponse;

public interface UserService {
    UserResponse createUser(RegistrationFormDTO userRegistrationInfo);

    boolean isEmailActivate(String code);

    UserResponse updateUserInformation(BasicUserInformation userInfo);

    UserResponse updateUserPassword(NewPassword password);

    UserResponse deleteUser(UserDTO user);

    UserResponse restoreUser(UserDTO user);

    int deleteConfirmation();

    UserDetails loadUserByUsername(String email);
}
