package com.siakad.service.impl;

import com.siakad.dto.request.*;
import com.siakad.dto.response.GetDosenDto;
import com.siakad.dto.response.JadwalDto;
import com.siakad.dto.response.RuanganResDto;
import com.siakad.dto.response.JadwalUjianResDto;
import com.siakad.dto.transform.JadwalDosenTransform;
import com.siakad.entity.Dosen;
import com.siakad.entity.JadwalKuliah;
import com.siakad.entity.KelasKuliah;
import com.siakad.entity.Ruangan;
import com.siakad.dto.transform.KelasKuliahTranform;
import com.siakad.entity.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.*;
import com.siakad.service.JadwalDosenService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class JadwalDosenServiceImpl implements JadwalDosenService {
    private final JadwalKuliahRepository jadwalKuliahRepository;
    private final DosenRepository dosenRepository;
    private final KelasKuliahRepository kelasKuliahRepository;
    private final UserActivityService service;
    private final JadwalDosenTransform mapper;
    private final JadwalUjianRepository jadwalUjianRepository;
    private final RuanganRepository ruanganRepository;
    private final KelasKuliahTranform kelasKuliahTranform;

    @Override
    public void save(UUID id, JadwalDosenReqDto request, HttpServletRequest servletRequest) {

        KelasKuliah kelasKuliah = kelasKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

        List<UUID> updatedJadwalIds = new ArrayList<>();

        for (JadwalDosenDto item : request.getJadwal()) {
            UUID dosenId = item.getDosenId();

            Dosen dosen = dosenRepository.findByIdAndIsDeletedFalse(dosenId)
                    .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Dosen tidak ditemukan"));

            List<UUID> jadwalIds = item.getJadwalIds();

            if (jadwalIds != null && !jadwalIds.isEmpty()) {
                for (UUID idJadwal : jadwalIds) {
                    JadwalKuliah jadwalKuliah = jadwalKuliahRepository.findByIdAndIsDeletedFalse(idJadwal)
                            .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Jadwal tidak ditemukan"));

                    jadwalKuliah.setSiakDosen(dosen);
                    jadwalKuliah.setSiakKelasKuliah(kelasKuliah);

                    jadwalKuliahRepository.save(jadwalKuliah);
                    updatedJadwalIds.add(idJadwal);
                }
            }
        }
        service.saveUserActivity(servletRequest, MessageKey.UPDATE_JADWAL_KULIAH);
    }

    @Override
    public List<JadwalDto> getAll(UUID id) {
        // Validasi kelas kuliah
        kelasKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

        // Ambil semua jadwal berdasarkan kelas
        List<JadwalKuliah> all = jadwalKuliahRepository.findAllByKelasKuliahIdAndIsDeletedFalse(id);

        // Manual mapping ke JadwalDto
        return all.stream().map(jadwal -> {
            return JadwalDto.builder()
                    .id(jadwal.getId())
                    .hari(jadwal.getHari())
                    .jamMulai(jadwal.getJamMulai().toString())
                    .jamSelesai(jadwal.getJamSelesai().toString())
                    .jenisPertemuan(jadwal.getJenisPertemuan())
                    .metodePembelajaran(jadwal.getMetodePembelajaran())
                    .siakRuangan(mapRuangan(jadwal.getSiakRuangan()))
                    .siakDosen(mapDosen(jadwal.getSiakDosen()))
                    .build();
        }).toList();
    }

    private RuanganResDto mapRuangan(Ruangan ruangan) {
        if (ruangan == null) return null;

        RuanganResDto dto = new RuanganResDto();
        dto.setId(ruangan.getId());
        dto.setNamaRuangan(ruangan.getNamaRuangan());
        dto.setKapasitas(ruangan.getKapasitas());
        dto.setLantai(ruangan.getLantai());
        return dto;
    }

    private GetDosenDto mapDosen(Dosen dosen) {
        if (dosen == null) return null;

        GetDosenDto dto = new GetDosenDto();
        dto.setId(dosen.getId());
        dto.setNamaDosen(dosen.getNama());
        dto.setNidn(dosen.getNidn());
        return dto;
    }



    @Override
    public List<JadwalDto> getByDosenId(UUID id, UUID dosenId) {
        kelasKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

        List<JadwalKuliah> byDosen = jadwalKuliahRepository.findJadwalKuliahByKelasIdAndSiakDosenIdAndIsDeletedFalse(id, dosenId);

        return mapper.toDto(byDosen);
    }

    @Override
    public List<GetJadwalResDto> getJadwalHarian(GetJadwalReqDto reqDto) {
        LocalDate tanggal = reqDto.getTanggal();
        String hari = tanggal.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("id", "ID"));
        List<JadwalKuliah> jadwalKuliahList = jadwalKuliahRepository
                .findByHariIgnoreCaseAndSiakKelasKuliah_SiakPeriodeAkademik_IdAndIsDeletedFalse(hari, reqDto.getSiakPeriodeAkademikId());
        return mapper.toGetJadwalResDtoList(jadwalKuliahList);
    }

    @Override
    public List<JadwalUjianResDto> getAllJadwalUjian(UUID id) {
        kelasKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

        List<JadwalUjian> jadwalUjians = jadwalUjianRepository.findAllByKelasKuliahIdAndIsDeletedFalse(id);
        return mapper.toJadwalUjianDto(jadwalUjians);
    }

    @Override
    public void saveJadwalUjian(UUID id, JadwalUjianReqDto request, HttpServletRequest servletRequest){
        KelasKuliah kelasKuliah = kelasKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

        Ruangan ruangan = ruanganRepository.findByIdAndIsDeletedFalse(request.getSiakRuanganId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Ruangan tidak ditemukan"));

        Dosen dosen = dosenRepository.findByIdAndIsDeletedFalse(request.getSiakDosenId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Dosen tidak ditemukan"));

        JadwalUjian jadwalUjian = new JadwalUjian();
        jadwalUjian.setSiakKelasKuliah(kelasKuliah);
        jadwalUjian.setSiakRuangan(ruangan);
        jadwalUjian.setSiakDosen(dosen);
        jadwalUjian.setJenisUjian(request.getJenisUjian());
        jadwalUjian.setJamMulai(request.getJamMulai());
        jadwalUjian.setJamSelesai(request.getJamSelesai());
        jadwalUjian.setTanggal(request.getTanggalJadwal());
        jadwalUjianRepository.save(jadwalUjian);
    }

    @Override
    public boolean deleteJadwalUjian(UUID kelasId, UUID id, HttpServletRequest servletRequest) {
        kelasKuliahRepository.findByIdAndIsDeletedFalse(kelasId)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

        JadwalUjian jadwalUjian = jadwalUjianRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Jadwal ujian tidak ditemukkan"));

        jadwalUjian.setIsDeleted(false);
        jadwalUjianRepository.save(jadwalUjian);

        service.saveUserActivity(servletRequest, MessageKey.DELETE_JADWAL_KULIAH);
        return true;
    }
}
