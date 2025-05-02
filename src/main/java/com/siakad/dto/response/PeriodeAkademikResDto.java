package com.siakad.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.UUID;

@Data
public class PeriodeAkademikResDto {
    private UUID id;
    private UUID siakTahunAjaranId;
    private String namaPeriode;
    private String kodePeriode;
    private String jenis;
    private Date tanggalMulai;
    private Date tanggalSelesai;
}
