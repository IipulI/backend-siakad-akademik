package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class RpsDosenResDto {
    private UUID id;
    private String nama;
    private String nidn;
}
