package com.siakad.service.impl;

import com.siakad.dto.request.JadwalKuliahReqDto;
import com.siakad.dto.request.KelasKuliahReqDto;
import com.siakad.dto.response.KelasKuliahResDto;
import com.siakad.dto.transform.helper.KelasKuliahMapperHelper;
import com.siakad.dto.transform.KelasKuliahTranform;
import com.siakad.entity.*;
import com.siakad.entity.service.KelasKuliahSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.*;
import com.siakad.service.KelasKuliahService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KelasKuliahServiceImpl implements KelasKuliahService {

    private final KelasKuliahRepository kelasKuliahRepository;
    private final ProgramStudiRepository programStudiRepository;
    private final PeriodeAkademikRepository periodeAkademikRepository;
    private final MataKuliahRepository mataKuliahRepository;
    private final JadwalKuliahRepository jadwalKuliahRepository;
    private final RuanganRepository ruanganRepository;
    private final UserActivityService service;
    private final KelasKuliahTranform mapper;

    @Override
    public KelasKuliahResDto create(KelasKuliahReqDto request, HttpServletRequest servletRequest) {
        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(request.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + request.getSiakProgramStudiId()));

        PeriodeAkademik periodeAkademik = periodeAkademikRepository.findByIdAndIsDeletedFalse(request.getSiakPeriodeAkademikId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Periode Akademik tidak ditemukan : " + request.getSiakPeriodeAkademikId()));

        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(request.getSiakMataKuliahId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Mata kuliah tidak ditemukan : " + request.getSiakMataKuliahId()));

        KelasKuliah kelasKuliah = mapper.toEntity(request);
        kelasKuliah.setSiakMataKuliah(mataKuliah);
        kelasKuliah.setSiakPeriodeAkademik(periodeAkademik);
        kelasKuliah.setSiakProgramStudi(programStudi);
        kelasKuliah.setStatusKelas("aktif");
        kelasKuliah.setIsDeleted(false);
        KelasKuliah kelas = kelasKuliahRepository.save(kelasKuliah);

        if(request.getJadwalKuliah() != null) {
            for (JadwalKuliahReqDto jadwalKuliahReqDto : request.getJadwalKuliah()) {
                Ruangan ruangan = ruanganRepository.findByIdAndIsDeletedFalse(jadwalKuliahReqDto.getSiakRuanganId())
                        .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Ruangan tidak ditemukkan : " + jadwalKuliahReqDto.getSiakRuanganId()));

                JadwalKuliah jadwalKuliah = mapper.toEntity(jadwalKuliahReqDto);
                jadwalKuliah.setSiakKelasKuliah(kelas);
                jadwalKuliah.setSiakRuangan(ruangan);
                jadwalKuliah.setIsDeleted(false);
                jadwalKuliahRepository.save(jadwalKuliah);
            }
        }

        service.saveUserActivity(servletRequest, MessageKey.CREATE_KELAS_KULIAH);
        KelasKuliahMapperHelper helper = new KelasKuliahMapperHelper(jadwalKuliahRepository);
        return mapper.toDto(kelas, helper);
    }

    @Override
    public Page<KelasKuliahResDto> search(String keyword, String periodeAkademik, String tahunKurikulum, String programStudi, String sistemKuliah, Pageable pageable) {
        KelasKuliahSpecification specBuilder = new KelasKuliahSpecification();
        Specification<KelasKuliah> spec = specBuilder.entitySearch(keyword, periodeAkademik, tahunKurikulum, programStudi, sistemKuliah);
        Page<KelasKuliah> all = kelasKuliahRepository.findAll(spec, pageable);
        KelasKuliahMapperHelper helper = new KelasKuliahMapperHelper(jadwalKuliahRepository);
        return all.map(entity -> mapper.toDto(entity, helper));
    }

    @Override
    public KelasKuliahResDto getOne(UUID id) {
        KelasKuliah kelasKuliah = kelasKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas Kuliah tidak ditemukan : " + id));
        KelasKuliahMapperHelper helper = new KelasKuliahMapperHelper(jadwalKuliahRepository);
        return mapper.toDto(kelasKuliah, helper);
    }

    @Override
    public KelasKuliahResDto update(KelasKuliahReqDto request, UUID id, HttpServletRequest servletRequest) {
        KelasKuliah kelasKuliah = kelasKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Mata Kuliah tidak ditemukan : " + id));

        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(request.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + request.getSiakProgramStudiId()));

        PeriodeAkademik periodeAkademik = periodeAkademikRepository.findByIdAndIsDeletedFalse(request.getSiakPeriodeAkademikId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Periode Akademik tidak ditemukan : " + request.getSiakPeriodeAkademikId()));

        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(request.getSiakMataKuliahId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Mata kuliah tidak ditemukan : " + request.getSiakMataKuliahId()));

        mapper.toEntity(request);
        kelasKuliah.setId(id);
        kelasKuliah.setSiakMataKuliah(mataKuliah);
        kelasKuliah.setSiakPeriodeAkademik(periodeAkademik);
        kelasKuliah.setSiakProgramStudi(programStudi);
        kelasKuliah.setStatusKelas("aktif");
        kelasKuliah.setUpdatedAt(LocalDateTime.now());
        KelasKuliah kelas = kelasKuliahRepository.save(kelasKuliah);

        if(request.getJadwalKuliah() != null) {
            for (JadwalKuliahReqDto jadwalKuliahReqDto : request.getJadwalKuliah()) {

                Ruangan ruangan = ruanganRepository.findByIdAndIsDeletedFalse(jadwalKuliahReqDto.getSiakRuanganId())
                        .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Ruangan tidak ditemukkan : " + jadwalKuliahReqDto.getSiakRuanganId()));

                JadwalKuliah jadwalKuliah = mapper.toEntity(jadwalKuliahReqDto);
                jadwalKuliah.setId(jadwalKuliah.getId());
                jadwalKuliah.setSiakKelasKuliah(kelas);
                jadwalKuliah.setSiakRuangan(ruangan);
                jadwalKuliah.setUpdatedAt(LocalDateTime.now());
                jadwalKuliahRepository.save(jadwalKuliah);
            }
        }

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_KELAS_KULIAH);
        KelasKuliahMapperHelper helper = new KelasKuliahMapperHelper(jadwalKuliahRepository);
        return mapper.toDto(kelas, helper);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        KelasKuliah kelasKuliah = kelasKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Kelas kuliah tidak ditemukan : " + id));

        kelasKuliah.setIsDeleted(true);
        kelasKuliahRepository.save(kelasKuliah);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_KELAS_KULIAH);
    }
}
