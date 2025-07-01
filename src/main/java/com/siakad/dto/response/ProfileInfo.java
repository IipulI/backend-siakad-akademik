package com.siakad.dto.response;

import lombok.Data;

@Data
public class ProfileInfo {
    private String nim;
    private String namaMahasiswa;
    private String programStudi;
    private String statusMahasiwa;
    private String angkatan;
    private String tahunKurikulum;
    private Integer semester;
    private String pembimbingAkademik;
    private Integer sksLulus;
    private Integer totalSks;
    private Double ipkLulus;
    private Double ipk;
}
