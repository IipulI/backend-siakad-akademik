package com.siakad.service;

import com.siakad.dto.request.KomposisiNilaiMataKuliahReqDto;
import com.siakad.dto.request.KomposisiPenilaianReqDto;
import com.siakad.dto.response.KomposisiNilaiMataKuliahResDto;
import com.siakad.dto.response.KomposisiPenilaianResDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.UUID;

public interface KomposisiPenilaianService {
    List<KomposisiPenilaianResDto> getAll();
    KomposisiPenilaianResDto save(KomposisiPenilaianReqDto request, HttpServletRequest servletRequest);
    KomposisiPenilaianResDto getOne(UUID id);
    KomposisiPenilaianResDto update(KomposisiPenilaianReqDto request, UUID id, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
    KomposisiNilaiMataKuliahResDto saveKomposisiNilaiMataKuliah(KomposisiNilaiMataKuliahReqDto request, HttpServletRequest servletRequest);
    List<KomposisiNilaiMataKuliahResDto> getAllKomposisiNilaiMataKuliah(UUID id);
}
