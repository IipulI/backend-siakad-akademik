package com.siakad.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CapaianMataKuliahReqDto {
    private String kodeCpmk;
    private String deskripsiCpmk;
    private List<UUID> capaianPembelajaranIds;
}
