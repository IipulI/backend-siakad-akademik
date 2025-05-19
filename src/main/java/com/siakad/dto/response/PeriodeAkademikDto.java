package com.siakad.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class PeriodeAkademikDto {
    private UUID id;
    private String namaPeriode;
    private String kodePeriode;
}
