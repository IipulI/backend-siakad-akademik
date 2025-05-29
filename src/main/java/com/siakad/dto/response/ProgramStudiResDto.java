package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ProgramStudiResDto {
    private UUID id;
    private String namaProgramStudi;
    private JenjangResDto jenjang;
}
