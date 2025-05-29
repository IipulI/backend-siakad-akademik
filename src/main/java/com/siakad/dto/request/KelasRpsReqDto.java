package com.siakad.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class KelasRpsReqDto {
    private UUID rpsId;
    private List<UUID> kelasIds;
}
