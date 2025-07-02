package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class PeriodeAkademikDto {
    private UUID id;
    private String namaPeriode;
    private String kodePeriode;

    public PeriodeAkademikDto(UUID id, String namaPeriode, String kodePeriode) {
        this.id = id;
        this.namaPeriode = namaPeriode;
        this.kodePeriode = kodePeriode;
    }

    public PeriodeAkademikDto() {}
}
