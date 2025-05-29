package com.siakad.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class JadwalDosenDto {
    private UUID dosenId;
    private List<UUID> jadwalIds;
}
