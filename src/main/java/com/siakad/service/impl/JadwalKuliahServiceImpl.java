package com.siakad.service.impl;

import com.siakad.dto.request.JadwalKuliahReqDto;
import com.siakad.dto.response.DetailKelasDosenPengajarResDto;
import com.siakad.dto.response.DosenDto;
import com.siakad.dto.response.JadwalKuliahResDto;
import com.siakad.dto.transform.JadwalKuliahTransform;
import com.siakad.entity.*;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.DosenRepository;
import com.siakad.repository.JadwalKuliahRepository;
import com.siakad.repository.KelasKuliahRepository;
import com.siakad.repository.RuanganRepository;
import com.siakad.service.JadwalKuliahService;
import com.siakad.service.UserActivityService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class JadwalKuliahServiceImpl implements JadwalKuliahService {

    private final KelasKuliahRepository kelasKuliahRepository;
    private final DosenRepository dosenRepository;
    private final RuanganRepository ruanganRepository;
    private final JadwalKuliahRepository jadwalKuliahRepository;
    private final JadwalKuliahTransform mapper;
    private final UserActivityService service;

    @Override
    public void create(JadwalKuliahReqDto request, HttpServletRequest httpServletRequest) {
        
    }

    @Override
    public List<JadwalKuliahResDto> getAll() {
        List<JadwalKuliah> data = jadwalKuliahRepository.findAllByIsDeletedFalse();
        return data.stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public DetailKelasDosenPengajarResDto getDetailKelasDosenPengajar(UUID kelasId) {
        List<JadwalKuliah> jadwals = jadwalKuliahRepository.findAllByKelasKuliahIdAndIsDeletedFalse(kelasId);

        if (jadwals.isEmpty()) {
            throw new EntityNotFoundException("Data tidak ditemukan");
        }

        KelasKuliah kelas = jadwals.get(0).getSiakKelasKuliah();

        // Kelompokkan jadwal berdasarkan dosen
        Map<Dosen, List<JadwalKuliah>> grouped = jadwals.stream()
                .collect(Collectors.groupingBy(JadwalKuliah::getSiakDosen));

        List<DosenDto> dosenDtos = grouped.entrySet().stream()
                .map(entry -> mapper.toDosenPengajarDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return new DetailKelasDosenPengajarResDto(
                kelas.getId(),
                kelas.getNama(),
                kelas.getSiakMataKuliah().getNamaMataKuliah(),
                kelas.getSiakPeriodeAkademik().getNamaPeriode(),
                dosenDtos
        );
    }


    @Override
    public JadwalKuliahResDto update(JadwalKuliahReqDto request, UUID id, HttpServletRequest httpServletRequest) {
//        JadwalKuliah jadwalKuliah = jadwalKuliahRepository.findByIdAndIsDeletedFalse(id)
//                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan : " + id));
//
//        KelasKuliah kelasKuliah = kelasKuliahRepository.findByIdAndIsDeletedFalse(request.getSiakKelasKuliahId())
//                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan : " + request.getSiakKelasKuliahId()));
//
//        Dosen dosen = dosenRepository.findByIdAndIsDeletedFalse(request.getSiakDosenId())
//                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Dosen tidak ditemukan : " + request.getSiakDosenId()));
//
//        Ruangan ruangan = ruanganRepository.findByIdAndIsDeletedFalse(request.getSiakRuanganId())
//                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Ruangan tidak ditemukan : " + request.getSiakRuanganId()));
//
//        mapper.toEntity(request, jadwalKuliah);
//        jadwalKuliah.setSiakDosen(dosen);
//        jadwalKuliah.setSiakRuangan(ruangan);
//        jadwalKuliah.setSiakKelasKuliah(kelasKuliah);
//        jadwalKuliah.setUpdatedAt(LocalDateTime.now());
//        jadwalKuliahRepository.save(jadwalKuliah);
//
//        service.saveUserActivity(httpServletRequest, MessageKey.UPDATE_JADWAL_KULIAH);
//        return mapper.toDto(jadwalKuliah);
        return null;
    }

    @Override
    public void delete(UUID id, HttpServletRequest httpServletRequest) {
        JadwalKuliah jadwalKuliah = jadwalKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan : " + id));

        jadwalKuliah.setIsDeleted(true);
        JadwalKuliah saved = jadwalKuliahRepository.save(jadwalKuliah);
        service.saveUserActivity(httpServletRequest, MessageKey.DELETE_JADWAL_KULIAH);
        mapper.toDto(saved);
    }
}
