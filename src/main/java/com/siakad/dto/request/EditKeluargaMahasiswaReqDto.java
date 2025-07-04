package com.siakad.dto.request;

import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
public class EditKeluargaMahasiswaReqDto {
    private UUID id;
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
    private Boolean isDeleted;
}
