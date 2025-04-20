package com.siakad.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResDto<T> {

    private String status;
    private String message;
    private T data;
    private PaginationDto pagination;

    public ApiResDto(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResDto(String status, String message, T data, PaginationDto pagination) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.pagination = pagination;
    }
}
