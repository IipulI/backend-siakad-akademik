package com.siakad.service;

import com.siakad.dto.request.PenilaianKelasKuliahReqDto;
import com.siakad.dto.response.PenilaianKelasResDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface PenilaianKelasService {

    void updateNilaiKelas(UUID kelasId, PenilaianKelasKuliahReqDto dto, HttpServletRequest servletRequest);

    PenilaianKelasResDto getAllPenilaianKelas(UUID kelasId);
}
