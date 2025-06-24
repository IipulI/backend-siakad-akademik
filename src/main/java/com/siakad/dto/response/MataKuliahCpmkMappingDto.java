package com.siakad.dto.response;

import java.util.UUID;

public interface MataKuliahCpmkMappingDto {
    UUID getMataKuliahId();
    String getKodeMataKuliah();
    String getNamaMataKuliah();
    String getTahunKurikulum();
    String getNamaProgramStudi();
    Boolean getHasCpmk();
}