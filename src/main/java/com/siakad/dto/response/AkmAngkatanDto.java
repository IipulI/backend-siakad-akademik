package com.siakad.dto.response;

import lombok.Data;

@Data
public class AkmAngkatanDto {
    private String angkatan;
    private Integer aktif;
    private Integer cuti;
    private Integer nonAktif;
    private Integer total;
}
