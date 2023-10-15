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
import ru.khokhlov.messenger.dto.response.ExitResponse;
import ru.khokhlov.messenger.dto.response.UserResponse;
import ru.khokhlov.messenger.service.UserService;
import ru.khokhlov.messenger.utils.JwtTokenUtils;

import java.security.Principal;

@RestController
@RequestMapping("/security")
@RequiredArgsConstructor
@Tag(name = "User Controller", description = "Everything related to clients")
public class SecurityController {
    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    /**
     * Allows the registration of a unique user.
     *
     * @param userInfo Information about the client to register.
     * @return A UserResponse indicating the success of user registration.
     */
    @Operation(
            summary = "User Registration",
            description = "Allows the registration of a unique user."
    )
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Parameter(description = "Client information") @Valid @RequestBody RegistrationFormDTO userInfo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(userInfo));
    }

    /**
     * Allows obtaining an authorization token for interacting with the system.
     *
     * @param userDTO Form for user authentication.
     * @return A Token containing the authorization token.
     */
    @Operation(
            summary = "Create Authorization Token",
            description = "Allows obtaining an authorization token for interacting with the system."
    )
    @PostMapping("/auth")
    public ResponseEntity<Token> createAuthToken(
            @Parameter(description = "Authentication form") @Valid @RequestBody UserDTO userDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.nickname(), userDTO.password()));

        UserDetails userDetails = userService.loadUserByUsername(userDTO.nickname());
        String token = jwtTokenUtils.generateToken(userDetails);

        return ResponseEntity.status(HttpStatus.OK).body(new Token("Bearer", token));
    }

    /**
     * Marks a token as invalid, making it no longer usable.
     *
     * @param authorizationHeader The authorization header with the token to invalidate.
     * @param principal The principal user.
     * @return An ExitResponse indicating the success of token invalidation.
     */
    @Operation(
            summary = "Invalidate Authorization Token",
            description = "Marks a token as invalid, making it no longer usable."
    )
    @PutMapping("/invalidation")
    public ResponseEntity<ExitResponse> invalidateToken(
            @RequestHeader("Authorization") String authorizationHeader,
            @Parameter(description = "The current user") Principal principal) {
        ExitResponse exitResponse = new ExitResponse(principal.getName());
        jwtTokenUtils.invalidateToken(authorizationHeader);

        return ResponseEntity.status(HttpStatus.OK).body(exitResponse);
    }

    @Hidden
    @GetMapping("/activate/{code}")
    public String activationEmail(@PathVariable String code) {
        boolean isActivated = userService.isEmailActivate(code);
        String message = "This activation code does not exist!";

        if (isActivated) {
            message = "You have confirmed your email, and you can close this page.";
        }

        return message;
    }
}