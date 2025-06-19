package com.siakad.dto.request;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class TandaiLunasReqDto {
    private List<UUID> invoiceMahasiswaIds;
}
