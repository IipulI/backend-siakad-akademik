package com.siakad.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageKey {
    SUCCESS("success"),
    LOGIN_SUCCESS("Successfully logged in"),
    CREATED("Successfully created"),
    UPDATED("Successfully updated"),
    READ("Successfully read"),
    DELETED("Successfully deleted"),

    CREATE_MAHASISWA("Create data mahasiswa"),
    UPDATE_MAHASISWA("Update data mahasiswa"),
    DELETE_MAHASISWA("Delete data mahasiswa");

    private final String message;
}
