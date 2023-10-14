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
import ru.khokhlov.messenger.dto.request.RegistrationFormDTO;
import ru.khokhlov.messenger.dto.request.UserDTO;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.service.UserService;
import ru.khokhlov.messenger.utils.JwtTokenUtils;
@RestController
@RequestMapping("/secure")
@RequiredArgsConstructor
@Tag(name = "Контроллер пользователя", description = "Всё, что связанно с клиентами")
public class AuthController {
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


    @Hidden
    @Operation(
            summary = "Получение токена авторизации",
            description = "Позволяет получить токен для взаимодействия с системой")
    @PostMapping("/auth")
    public ResponseEntity<Token> createAuthToken(@Parameter(description = "Форма авторизации") @Valid @RequestBody UserDTO userDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.nickname(), userDTO.password()));

        UserDetails userDetails = userService.loadUserByUsername(userDTO.nickname());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(new Token("Bearer", token));
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