package com.siakad.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailKelasDosenPengajarResDto {
    private UUID kelasKuliahId;
    private String namaKelas;
    private String mataKuliah;
    private String periode;
    private List<DosenDto> dosenPengajar;
}
