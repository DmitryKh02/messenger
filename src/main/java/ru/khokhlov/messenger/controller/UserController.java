package ru.khokhlov.messenger.controller;


import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import ru.khokhlov.messenger.dto.Token;
import ru.khokhlov.messenger.dto.request.BasicUserInformation;
import ru.khokhlov.messenger.dto.request.NewPassword;
import ru.khokhlov.messenger.dto.request.RegistrationFormDTO;
import ru.khokhlov.messenger.dto.request.UserDTO;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.service.UserService;
import ru.khokhlov.messenger.utils.JwtTokenUtils;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(name = "Контроллер пользователя", description = "Всё, что связанно с клиентами")
public class UserController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    @Operation(
            summary = "Регистрация пользователя",
            description = "Позволяет зарегистрировать уникального пользователя")
    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Parameter(description = "Информация о клиенте") @Valid @RequestBody RegistrationFormDTO userInfo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userInfo));
    }

    @Operation(
            summary = "Обновление данных пользователя",
            description = "Позволяет изменить почту, логи, имя, фамили, отчество, дату рождения и статус")
    @PutMapping(value = "/update-info")
    public ResponseEntity<UserResponse> updateUserInformation(@Parameter(description = "Информация о клиенте") @Valid @RequestBody BasicUserInformation userInfo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUserInformation(userInfo));
    }

    @Operation(
            summary = "Обновление пароля пользователя",
            description = "Позволяет изменить пароль пользователя")
    @PutMapping(value = "/update-password")
    public ResponseEntity<UserResponse> updateUserPassword(@Parameter(description = "Информация о клиенте") @Valid @RequestBody NewPassword password) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.updateUserPassword(password));
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Присвоение пользователю статуса неактивного")
    @DeleteMapping(value = "/delete")
    public ResponseEntity<UserResponse> deleteUser(@Parameter(description = "Информация о клиенте") @Valid @RequestBody UserDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.deleteUser(user));
    }

    @Operation(
            summary = "Удаление пользователя",
            description = "Присвоение пользователю статуса неактивного")
    @PutMapping(value = "/restore")
    public ResponseEntity<UserResponse> restoreUser(@Parameter(description = "Информация о клиенте") @Valid @RequestBody UserDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.restoreUser(user));
    }


    @Hidden
    @Operation(
            summary = "Получение токена авторизации",
            description = "Позволяет получить токен для взаимодействия с системой")
    @PostMapping("/auth")
    public ResponseEntity<Token> createAuthToken(@Parameter(description = "Форма авторизации") @Valid @RequestBody RegistrationFormDTO registrationFormDTO) {
        //TODO понять, как работает эта функция
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(registrationFormDTO.email(), registrationFormDTO.password()));

        UserDetails userDetails = userService.loadUserByUsername(registrationFormDTO.email());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new Token("Bearer", token));
    }

    @Hidden
    @GetMapping("/activate/{code}")
    public String activationEmail(@PathVariable String code) {
        boolean isActivated = userService.isEmailActivate(code);
        String message = "Такого кода активации не существует!";

        if (isActivated) {
            message = "Вы подтвердили свой e-mail и можете закрывать эту страницу";
        }

        return message;
    }
}