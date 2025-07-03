package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class GetDosenDto {
    private UUID id;
    private String nidn;
    private String namaDosen;
}
