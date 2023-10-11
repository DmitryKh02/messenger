package ru.khokhlov.messenger.exception;

import lombok.Getter;

@Getter
public class InvalidDataException extends RuntimeException{
    private final ErrorMessage errorMessage;

    public InvalidDataException(ErrorMessage errorMessage) {
        super();
        this.errorMessage = errorMessage;
    }
}
