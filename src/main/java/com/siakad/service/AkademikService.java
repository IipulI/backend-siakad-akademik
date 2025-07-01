package com.siakad.service;

import com.siakad.dto.response.ManajemenOBEResDto;

import java.util.List;
import java.util.UUID;

public interface AkademikService {
    List<ManajemenOBEResDto> getStatusOverview(String tahunKurikulum, String namaProdi, String namaJenjang);

    ManajemenOBEResDto getOneStatusOverview(UUID id, String tahunKurikulum);
}
