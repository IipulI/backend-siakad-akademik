package com.siakad.service.impl;

import com.siakad.dto.request.ProfilLulusanReqDto;
import com.siakad.dto.response.ProfilLulusanResDto;
import com.siakad.dto.transform.ProfilLulusanTransform;
import com.siakad.entity.ProfilLulusan;
import com.siakad.entity.ProgramStudi;
import com.siakad.entity.TahunKurikulum;
import com.siakad.entity.service.ProfilLulusanSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.ProfilLulusanRepository;
import com.siakad.repository.ProgramStudiRepository;
import com.siakad.repository.TahunKurikulumRepository;
import com.siakad.service.ProfilLulusanService;
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
public class ProfilLulusanServiceImpl implements ProfilLulusanService {
    private final ProfilLulusanRepository profilLulusanRepository;
    private final ProgramStudiRepository programStudiRepository;
    private final TahunKurikulumRepository tahunKurikulumRepository;
    private final UserActivityService service;
    private final ProfilLulusanTransform mapper;

    @Override
    public ProfilLulusanResDto save(ProfilLulusanReqDto reqDto, HttpServletRequest servletRequest) {
        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(reqDto.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + reqDto.getSiakProgramStudiId()));

        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(reqDto.getSiakTahunKurikulumId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun Kurikulum tidak ditemukan : " + reqDto.getSiakTahunKurikulumId()));

        ProfilLulusan profilLulusan = mapper.toEntity(reqDto);
        profilLulusan.setSiakProgramStudi(programStudi);
        profilLulusan.setSiakTahunKurikulum(tahunKurikulum);
        profilLulusan.setIsDeleted(false);
        ProfilLulusan saved = profilLulusanRepository.save(profilLulusan);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_PROFIL_LULUSAN);
        return mapper.toDto(saved);
    }

    @Override
    public Page<ProfilLulusanResDto> getPaginate(String tahunKurkulum, Pageable pageable) {
        ProfilLulusanSpecification specBuilder = new ProfilLulusanSpecification();
        Specification<ProfilLulusan> spec = specBuilder.entitySearch(tahunKurkulum);
        Page<ProfilLulusan> all = profilLulusanRepository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public ProfilLulusanResDto getOne(UUID id) {
        ProfilLulusan profilLulusan = profilLulusanRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Profil Lulusan tidak ditemukan : " + id));

        return mapper.toDto(profilLulusan);
    }

    @Override
    public ProfilLulusanResDto update(UUID id, ProfilLulusanReqDto reqDto, HttpServletRequest servletRequest) {
        ProfilLulusan profilLulusan = profilLulusanRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Profil Lulusan tidak ditemukan : " + id));

        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(reqDto.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + reqDto.getSiakProgramStudiId()));

        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(reqDto.getSiakTahunKurikulumId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun Kurikulum tidak ditemukan : " + reqDto.getSiakTahunKurikulumId()));

        mapper.toEntity(reqDto, profilLulusan);
        profilLulusan.setSiakProgramStudi(programStudi);
        profilLulusan.setSiakTahunKurikulum(tahunKurikulum);
        profilLulusan.setUpdatedAt(LocalDateTime.now());
        ProfilLulusan saved = profilLulusanRepository.save(profilLulusan);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_PROFIL_LULUSAN);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        ProfilLulusan profilLulusan = profilLulusanRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Profil Lulusan tidak ditemukan : " + id));

        profilLulusan.setIsDeleted(true);
        ProfilLulusan saved = profilLulusanRepository.save(profilLulusan);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_PROFIL_LULUSAN);
        mapper.toDto(saved);
    }
}
