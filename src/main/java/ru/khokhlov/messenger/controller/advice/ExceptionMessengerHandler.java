package ru.khokhlov.messenger.controller.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.khokhlov.messenger.exception.ErrorMessage;
import ru.khokhlov.messenger.exception.ErrorResponse;
import ru.khokhlov.messenger.exception.InvalidDataException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@ControllerAdvice
public class ExceptionMessengerHandler {
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErrorMessage> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return e.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorMessage(error.getField(), error.getDefaultMessage())).toList();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidDataException.class)
    public ErrorResponse onInvalidDataException(InvalidDataException e) {
        return createResponseException(HttpStatus.BAD_REQUEST, e.getErrorMessage().fieldName(), e.getErrorMessage().message());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(InvalidFormatException.class)
    public ErrorResponse onInvalidFormatException(InvalidFormatException e) {
        return createResponseException(HttpStatus.BAD_REQUEST, e.getTargetType().toString(), e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityExistsException.class)
    public ErrorResponse onInvalidFormatException(EntityExistsException e) {
        return createResponseException(HttpStatus.BAD_REQUEST, "Entity already exists", e.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErrorResponse onInvalidApplicationId(EntityNotFoundException ex) {
        return createResponseException(HttpStatus.NOT_FOUND, "Entity not found", ex.getMessage());
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthenticationException.class)
    public ErrorResponse onAuthenticationException(AuthenticationException ex) {
        return createResponseException(HttpStatus.UNAUTHORIZED, "Uncorrected login or password", ex.getMessage());
    }

    private ErrorResponse createResponseException(HttpStatus status, String error, String message) {
        return new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                error,
                message
        );
    }
}
