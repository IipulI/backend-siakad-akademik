package com.siakad.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class KeluargaMahasiswaReqDto {
    private String hubungan;
    private String nama;
    private String nik;
    private LocalDate tanggalLahir;
    private String statusHidup;
    private String statusKerabat;
    private String pendidikan;
    private String pekerjaan;
    private String penghasilan;
    private String alamat;
    private String noTelepon;
    private String email;
}
