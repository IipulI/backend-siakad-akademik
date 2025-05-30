package com.siakad.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class PindahKelasReqDto {
    private UUID kelasId;
    private List<UUID> mahasiswaIds;
}
