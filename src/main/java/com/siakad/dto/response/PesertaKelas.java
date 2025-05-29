package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PesertaKelas {
    private UUID id;
    private String npm;
    private String nama;
    private String angkatan;

    private String kategori;
    private String status; // This status is for the KrsRincianMahasiswa entry
    private BigDecimal kehadiran;
    private BigDecimal tugas;
    private BigDecimal uas;
    private BigDecimal uts;
    private BigDecimal nilai;
    private String hurufMutu;
    private BigDecimal angkaMutu;
    private BigDecimal nilaiAkhir;

    private ProgramStudiResDto programStudiResDto;
}
