package com.siakad.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProgramStudiStatusDto {
    private String kodeProgramStudi;
    private String ketuaProgramStudi;
    private String programStudi;
    private JenjangDto jenjang;
    private boolean statusPl;
    private boolean statusCpl;
    private boolean statusPlCpl;
    private boolean statusCpmk;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class JenjangDto {
        private String nama;
        private String jenjang;
    }
}
