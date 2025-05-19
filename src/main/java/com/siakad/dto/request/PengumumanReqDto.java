package com.siakad.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class PengumumanReqDto {
    private UUID siakUserId;
    private String judul;
    private String isi;
    private Boolean isActive;
    private Boolean isPriority;
}
