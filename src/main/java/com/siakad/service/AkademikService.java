package com.siakad.service;

import com.siakad.dto.response.ManajemenOBEResDto;

import java.util.List;

public interface AkademikService {
    List<ManajemenOBEResDto> getStatusOverview(String tahunKurikulum, String namaProdi, String namaJenjang);
}
