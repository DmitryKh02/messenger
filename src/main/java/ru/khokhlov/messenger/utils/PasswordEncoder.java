package ru.khokhlov.messenger.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordEncoder {
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String getEncryptedPassword(String password){
        return encoder.encode(password);
    }

    public static boolean isPasswordsAreEquals(String rawPassword, String encodePassword){
        return encoder.matches(rawPassword, encodePassword);
    }
}
