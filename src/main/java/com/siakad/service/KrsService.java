package com.siakad.service;

import com.siakad.dto.request.KrsReqDto;
import com.siakad.dto.request.PesertaKelasReqDto;
import com.siakad.dto.request.PindahKelasReqDto;
import com.siakad.dto.response.*;
import com.siakad.enums.StatusMahasiswa;
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
    List<MengulangResDto> getAllMengulang(UUID mahasiswaId);
    List<MengulangResDto> getAllMengulangByPeriode(UUID mahasiswaId, UUID periodeId);
    void deleteKrs(UUID id, HttpServletRequest servletRequest);
    List<FinalisasiMkDto> getAllFinalisasiMk(UUID mahasiswaId);
    List<StatusSemesterDto> getStatusSemester(UUID mahasiswaId);
    RiwayatKrsDto getRiwayatKrs(UUID mahasiswaId);
}
