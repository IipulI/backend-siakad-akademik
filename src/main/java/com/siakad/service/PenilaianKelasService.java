package com.siakad.service;

import com.siakad.dto.request.PenilaianKelasKuliahReqDto;
import jakarta.servlet.http.HttpServletRequest;

import java.util.UUID;

public interface PenilaianKelasService {

    void updateNilaiKelas(PenilaianKelasKuliahReqDto dto, HttpServletRequest servletRequest);

}
