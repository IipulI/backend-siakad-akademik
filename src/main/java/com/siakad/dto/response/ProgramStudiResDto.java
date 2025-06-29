package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class ProgramStudiResDto {
    private UUID id;
    private String namaProgramStudi;
    private JenjangResDto jenjang;

    // Specific constructor for direct DTO projection from query results
    public ProgramStudiResDto(UUID id, String namaProgramStudi,
                              UUID jenjangId, String jenjangNama, String jenjangJenjang) {
        this.id = id;
        this.namaProgramStudi = namaProgramStudi;
        // Construct the nested JenjangResDto
        this.jenjang = new JenjangResDto(jenjangId, jenjangNama, jenjangJenjang);
    }

    public ProgramStudiResDto() {} // Default constructor
}
