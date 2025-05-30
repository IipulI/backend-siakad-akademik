    package com.siakad.dto.request;
    import lombok.Data;
    import java.time.LocalDate;
    import java.util.UUID;

    @Data
    public class PeriodeAkademikReqDto {
        private UUID siakTahunAjaranId;
        private String namaPeriode;
        private String kodePeriode;
        private LocalDate tanggalMulai;
        private LocalDate tanggalSelesai;
    }
