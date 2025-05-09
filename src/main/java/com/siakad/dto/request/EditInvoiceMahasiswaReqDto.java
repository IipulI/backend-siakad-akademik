package com.siakad.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class EditInvoiceMahasiswaReqDto {
    private UUID siakPeriodeAkademikId;
    private UUID siakMahasiswaId;
    private String tanggalTenggat;

}
