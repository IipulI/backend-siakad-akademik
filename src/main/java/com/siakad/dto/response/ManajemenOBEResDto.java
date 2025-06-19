package com.siakad.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class ManajemenOBEResDto {
    private UUID id;
    private String kodeProgramStudi;
    private String programStudi;
    private JenjangResDto jenjang;
    private boolean statusPl;
    private boolean statusCpl;
    private boolean statusPlCpl;
    private boolean statusCpmk;
}
