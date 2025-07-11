package com.siakad.service;

import com.siakad.dto.request.*;
import com.siakad.dto.response.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface KrsService {
    // Mahasiswa
    KrsInfoResDto infoKrs(UUID mahasiswaID);

    void save(KrsReqDto dto, HttpServletRequest servletRequest);
    void update(KrsReqDto dto, HttpServletRequest servletRequest);
    void deleteMultiple(KrsReqDto reqDto, HttpServletRequest servletRequest);

    Page<KrsResDto> getPaginated(String kelas, Pageable pageable);

    Page<KrsResDto> getPaginatedKelas(String mataKuliah, Pageable pageable);

    KrsMenungguResDto getAllKrsByStatusMenunggu();
    void updateStatus(HttpServletRequest servletRequest);

    // Admin Akademik
    List<PesertaKelas> getPesertaKelas(UUID kelasId);
    List<EligiblePesertaKelasDto> getEligiblePesertaKelas(UUID kelasId, String nama, String periodeMasuk, String sistemKuliah);
    void addPesertaKelas(UUID id, PesertaKelasReqDto request, HttpServletRequest servletRequest);
    void deletePesertaKelas(UUID id, PesertaKelasReqDto request, HttpServletRequest servletRequest);
    void pindahKelasPeserta(UUID id, PindahKelasReqDto request, HttpServletRequest servletRequest);
    List<MengulangResDto> getAllMengulang(UUID mahasiswaId);
    List<MengulangResDto> getAllMengulangByPeriode(UUID mahasiswaId, UUID periodeId);
    void deleteKrs(UUID id, HttpServletRequest servletRequest);
    List<FinalisasiMkDto> getAllFinalisasiMk(UUID mahasiswaId);
    List<StatusSemesterDto> getStatusSemester(UUID mahasiswaId);
    RiwayatKrsDto getRiwayatKrs(UUID mahasiswaId, String periodeAkademik);
    List<SuntingKrsResDto> getSuntingKrs(UUID mahasiswaId, String namaPeriode);
    void updateSuntingKrs(UUID krsId, EditSuntingDto dto, HttpServletRequest servletRequest);

    // Dosen Service
    void updateStatusKrsSetuju(UpdateStatusKrsReqDto request, HttpServletRequest servletRequest);
    void updateStatusKrsTolak(UpdateStatusKrsReqDto request, HttpServletRequest servletRequest);
    KrsMenungguResDto getDetailKrsMahasiswa(UUID mahasiswaId);
}
