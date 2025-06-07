package com.siakad.dto.response;

import lombok.Data;

@Data
public class KrsDto {
    private String kodeMataKuliah;
    private String namaMataKuliah;
    private String kelas;
    private Integer sks;
    private String hari;
    private String jam;
    private String ruangan;
    private String dosenPengajar;
}
