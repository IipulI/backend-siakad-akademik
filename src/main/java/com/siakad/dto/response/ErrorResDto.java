package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@Data
public class ErrorResDto {

    private int code;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> errors;  // Add this field

}
