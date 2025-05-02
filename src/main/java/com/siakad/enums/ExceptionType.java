package com.siakad.enums;

import lombok.Getter;

@Getter
public enum ExceptionType {

    RESOURCE_NOT_FOUND("Sumber daya tidak ditemukan", 404),
    USER_NOT_FOUND("Pengguna tidak ditemukan", 404),
    ROLE_NOT_FOUND("Peran default tidak ditemukan", 404),
    BAD_REQUEST("Permintaan tidak valid", 400),
    EMAIL_ALREADY_EXISTS("Email sudah digunakan", 409),
    USERNAME_ALREADY_EXISTS("Username sudah digunakan", 409),
    NPM_ALREADY_EXISTS("NPM sudah digunakan", 409),
    INVALID_PASSWORD("Username atau kata sandi salah", 401),
    FORBIDDEN("Akses ditolak", 403),
    INTERNAL_SERVER_ERROR("Terjadi kesalahan pada server", 500),
    APPOINTMENT_CONFLICT("Terjadi konflik jadwal", 409),
    RESOURCE_CONFLICT("Terjadi konflik pada sumber daya", 409);

    private final String message;
    private final int httpCode;

    ExceptionType(String message, int httpCode) {
        this.message = message;
        this.httpCode = httpCode;
    }

    public String getFormattedMessage(String context) {
        return String.format("%s: %s", message, context);
    }
}