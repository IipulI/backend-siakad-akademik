package com.siakad.service.impl;

import com.siakad.dto.request.SkalaPenilaianReqDto;
import com.siakad.dto.response.SkalaPenilaianResDto;
import com.siakad.dto.transform.SkalaPenilaianTransform;
import com.siakad.entity.ProgramStudi;
import com.siakad.entity.SkalaPenilaian;
import com.siakad.entity.TahunAjaran;
import com.siakad.entity.service.SkalaPenilaianSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.ProgramStudiRepository;
import com.siakad.repository.SkalaPenilaianRepository;
import com.siakad.repository.TahunAjaranRepository;
import com.siakad.service.SkalaPenilaianService;
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
public class SkalaPenilaianServiceImpl implements SkalaPenilaianService {

    private final SkalaPenilaianRepository skalaPenilaianRepository;
    private final TahunAjaranRepository tahunAjaranRepository;
    private final ProgramStudiRepository programStudiRepository;
    private final UserActivityService service;
    private final SkalaPenilaianTransform mapper;

    @Override
    public SkalaPenilaianResDto save(SkalaPenilaianReqDto reqDto, HttpServletRequest servletRequest) {
        TahunAjaran tahunAjaran = tahunAjaranRepository.findByIdAndIsDeletedFalse(reqDto.getSiakTahunAjaranId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun Ajaran tidak ditemukan : " + reqDto.getSiakTahunAjaranId()));

        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(reqDto.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + reqDto.getSiakProgramStudiId()));

        SkalaPenilaian skalaPenilaian = mapper.toEntity(reqDto);
        skalaPenilaian.setSiakTahunAjaran(tahunAjaran);
        skalaPenilaian.setSiakProgramStudi(programStudi);
        skalaPenilaian.setIsDeleted(false);
        SkalaPenilaian saved = skalaPenilaianRepository.save(skalaPenilaian);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_SKALA_PENILAIAN);
        return mapper.toDto(saved);
    }

    @Override
    public Page<SkalaPenilaianResDto> getPaginate(String tahunAjaran, String programStudi, Pageable pageable) {
        SkalaPenilaianSpecification specBuilder = new SkalaPenilaianSpecification();
        Specification<SkalaPenilaian> spec = specBuilder.entitySearch(tahunAjaran, programStudi);
        Page<SkalaPenilaian> all = skalaPenilaianRepository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public SkalaPenilaianResDto getOne(UUID id) {
        SkalaPenilaian skalaPenilaian = skalaPenilaianRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Skala penilaian tidak ditemukan : " + id));
        return mapper.toDto(skalaPenilaian);
    }

    @Override
    public SkalaPenilaianResDto update(UUID id, SkalaPenilaianReqDto reqDto, HttpServletRequest servletRequest) {
        SkalaPenilaian skalaPenilaian = skalaPenilaianRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Skala penilaian tidak ditemukan : " + id));

        TahunAjaran tahunAjaran = tahunAjaranRepository.findByIdAndIsDeletedFalse(reqDto.getSiakTahunAjaranId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Tahun Ajaran tidak ditemukan : " + reqDto.getSiakTahunAjaranId()));

        ProgramStudi programStudi = programStudiRepository.findByIdAndIsDeletedFalse(reqDto.getSiakProgramStudiId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Program Studi tidak ditemukan : " + reqDto.getSiakProgramStudiId()));


        mapper.toEntity(reqDto, skalaPenilaian);
        skalaPenilaian.setSiakProgramStudi(programStudi);
        skalaPenilaian.setSiakTahunAjaran(tahunAjaran);
        skalaPenilaian.setUpdatedAt(LocalDateTime.now());

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_SKALA_PENILAIAN);
        return mapper.toDto(skalaPenilaian);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        SkalaPenilaian skalaPenilaian = skalaPenilaianRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Skala penilaian tidak ditemukan : " + id));

        skalaPenilaian.setIsDeleted(true);
        SkalaPenilaian saved = skalaPenilaianRepository.save(skalaPenilaian);

        service.saveUserActivity(servletRequest, MessageKey.DELETE_SKALA_PENILAIAN);
        mapper.toDto(saved);
    }
}
