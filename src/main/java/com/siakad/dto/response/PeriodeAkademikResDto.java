package com.siakad.dto.response;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class PeriodeAkademikResDto {
    private UUID id;
    private String tahun;
    private String namaPeriode;
    private String kodePeriode;
    private String status;
    private LocalDate tanggalMulai;
    private LocalDate tanggalSelesai;
}
