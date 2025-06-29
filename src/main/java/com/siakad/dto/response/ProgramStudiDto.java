package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ProgramStudiDto {
    private UUID id;
    private String namaProgramStudi;

    public ProgramStudiDto(UUID id, String namaProgramStudi) {
        this.id = id;
        this.namaProgramStudi = namaProgramStudi;
    }

    public ProgramStudiDto() {}
}
