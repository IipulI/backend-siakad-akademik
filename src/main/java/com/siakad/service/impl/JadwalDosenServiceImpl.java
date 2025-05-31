package com.siakad.service.impl;

import com.siakad.dto.request.*;
import com.siakad.dto.response.JadwalDosenResDto;
import com.siakad.dto.response.JadwalDto;
import com.siakad.dto.response.JadwalKuliahResDto;
import com.siakad.dto.transform.JadwalDosenTransform;
import com.siakad.entity.Dosen;
import com.siakad.entity.JadwalKuliah;
import com.siakad.entity.KelasKuliah;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.DosenRepository;
import com.siakad.repository.JadwalKuliahRepository;
import com.siakad.repository.KelasKuliahRepository;
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
        kelasKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

        List<JadwalKuliah> all = jadwalKuliahRepository.findAllByKelasKuliahIdAndIsDeletedFalse(id);
        return mapper.toDto(all);
    }

    @Override
    public List<JadwalDto> getByDosenId(UUID id, UUID dosenId) {
        kelasKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan"));

        List<JadwalKuliah> byDosen = jadwalKuliahRepository.findJadwalKuliahBySiakDosenIdAndIsDeletedFalse(id, dosenId);

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


}
