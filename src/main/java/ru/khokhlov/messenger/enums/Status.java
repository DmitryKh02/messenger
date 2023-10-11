package ru.khokhlov.messenger.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Status {
    FREE("Free"),
    MARRIED("Married"),
    DIVORCED("Divorced"),
    IT_IS_COMPLICATED("It's complicated");

    public final String value;
}
