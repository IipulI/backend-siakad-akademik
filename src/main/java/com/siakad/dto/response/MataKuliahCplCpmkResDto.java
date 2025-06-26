package com.siakad.dto.response;

import lombok.Data;

import java.util.List;

@Data
public class MataKuliahCplCpmkResDto {
    private List<CapaianMataKuliahResDto> capaianMataKuliah;
    private List<CapaianPembelajaranLulusanResDto> capaianPembelajaranLulusan;
    private List<ProfilLulusanResDto> profilLulusan;
}
