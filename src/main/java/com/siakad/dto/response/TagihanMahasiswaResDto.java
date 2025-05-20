package com.siakad.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class TagihanMahasiswaResDto {
    private UUID id;
    private String tanggal;
    private String kodeTagihan;
    private String npm;
    private String nama;
    private String jenisTagihan;
    private BigDecimal nominal;
    private BigDecimal bayar;
    private Boolean lunas;
    private LocalDate tanggalTenggat;
    private LocalDate tanggalBayar;
    @JsonIgnore
    private String periodeAkademik;
    @JsonIgnore
    private Integer semester;
    @JsonIgnore
    private String angkatan;
    @JsonIgnore
    private String programStudi;
    @JsonIgnore
    private String fakultas;
    @JsonIgnore
    private String kelasKuliah;
}
