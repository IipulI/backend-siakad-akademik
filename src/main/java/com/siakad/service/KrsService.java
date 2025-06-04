package com.siakad.service;

import com.siakad.dto.request.JadwalDosenReqDto;
import com.siakad.dto.request.KrsReqDto;
import com.siakad.dto.request.PesertaKelasReqDto;
import com.siakad.dto.request.PindahKelasReqDto;
import com.siakad.dto.response.KrsMenungguResDto;
import com.siakad.dto.response.KrsResDto;
import com.siakad.dto.response.PesertaKelas;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface KrsService {
    void save(KrsReqDto dto, HttpServletRequest servletRequest);
    void update(KrsReqDto dto, HttpServletRequest servletRequest);
    Page<KrsResDto> getPaginated(String kelas, Pageable pageable);
    KrsMenungguResDto getAllKrsByStatusMenunggu();
    void updateStatus(HttpServletRequest servletRequest);
    List<PesertaKelas> getPesertaKelas(UUID kelasId);
    void addPesertaKelas(UUID id, PesertaKelasReqDto request, HttpServletRequest servletRequest);
    void deletePesertaKelas(UUID id, PesertaKelasReqDto request, HttpServletRequest servletRequest);
    void pindahKelasPeserta(UUID id, PindahKelasReqDto request, HttpServletRequest servletRequest);
}
