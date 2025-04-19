package com.siakad.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MahasiswaReqDto {

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
