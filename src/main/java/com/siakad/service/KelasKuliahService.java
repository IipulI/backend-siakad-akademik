package com.siakad.service;

import com.siakad.dto.request.KelasKuliahReqDto;
import com.siakad.dto.response.KelasKuliahResDto;
import com.siakad.entity.KelasKuliah;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface KelasKuliahService {
    KelasKuliahResDto create(KelasKuliahReqDto request, HttpServletRequest servletRequest);
    Page<KelasKuliahResDto> search(
            String keyword,
            String periodeAkademik,
            String tahunKurikulum,
            String programStudi,
            String sistemKuliah,
            Pageable pageable);
    KelasKuliahResDto getOne(UUID id);
    KelasKuliahResDto update(KelasKuliahReqDto request, UUID id, HttpServletRequest servletRequest);
    void delete(UUID id, HttpServletRequest servletRequest);
}
