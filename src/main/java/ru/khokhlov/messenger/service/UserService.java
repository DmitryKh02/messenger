package ru.khokhlov.messenger.service;

import org.springframework.security.core.userdetails.UserDetails;
import ru.khokhlov.messenger.dto.request.BasicUserInformation;
import ru.khokhlov.messenger.dto.request.NewPassword;
import ru.khokhlov.messenger.dto.request.RegistrationFormDTO;
import ru.khokhlov.messenger.dto.request.UserDTO;
import ru.khokhlov.messenger.dto.response.UserResponse;

public interface UserService {
    /**
     * Функция создания пользователя
     *
     * @param userRegistrationInfo регистрационна информация
     * @return ответная информация
     */
    UserResponse createUser(RegistrationFormDTO userRegistrationInfo);

    /**
     * Аутентификация пользователя
     *
     * @param registrationFormDTO данные для аутентификации
     * @param token токен пользователя
     * @return ответ пользователю
     */
    UserResponse authUser(RegistrationFormDTO registrationFormDTO, String token);

    /**
     * Функция получения деталей пользователя
     *
     * @param username имя пользователя
     * @return детали
     */
    UserDetails loadUserByUsername(String username);

    /**
     * Функция подтверждения почты клиента
     *
     * @param code код активации
     * @return true - подтверждена почта
     */
    boolean isEmailActivate(String code);

    UserResponse updateUserInformation(BasicUserInformation userInfo);

    UserResponse updateUserPassword(NewPassword password);

    UserResponse deleteUser(UserDTO user);

    UserResponse restoreUser(UserDTO user);

    int deleteConfirmation();
}
