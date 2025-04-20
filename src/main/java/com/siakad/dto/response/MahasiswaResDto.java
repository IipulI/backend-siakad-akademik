package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class MahasiswaResDto {

    private UUID id;
    private String nama;
    private String npm;
    private String angkatan;
    private String status;
    private String jenisKelamin;
    private String tempatLahir;
    private LocalDate tanggalLahir;
    private String email;
    private String noTelepon;
    private String alamat;
    private String agama;
    private String statusNikah;
    private String nik;
    private String noKk;
    private String pendidikanAsal;
    private String namaPendidikanAsal;
    private String nisn;
    private String pekerjaan;
    private String instansiPekerjaan;
    private String penghasilan;
}
