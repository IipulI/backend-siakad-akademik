package com.siakad.enums;

import lombok.Getter;

@Getter
public enum ExceptionType {

    RESOURCE_NOT_FOUND("Resource not found", 404),
    USER_NOT_FOUND("User not found", 404),
    ROLE_NOT_FOUND("Default role not found", 404),
    BAD_REQUEST("Bad request", 400),
    EMAIL_ALREADY_EXISTS("Email is already taken", 409),
    USERNAME_ALREADY_EXISTS("Username is already taken", 409),
    NPM_ALREADY_EXISTS("NPM is already taken", 409),
    INVALID_PASSWORD("Current password is incorrect", 401),
    FORBIDDEN("Access denied", 403),
    INTERNAL_SERVER_ERROR("Internal server error", 500),
    APPOINTMENT_CONFLICT("Appointment conflict occurred", 409),
    RESOURCE_CONFLICT("Resource conflict occurred", 409);


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