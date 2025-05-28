package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class DosenResDto {
    private UUID id;
    private String nama;
    private String nidn;
}
