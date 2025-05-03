package com.siakad.service.impl;

import com.siakad.dto.request.TahunKurikulumReqDto;
import com.siakad.dto.response.TahunKurikulumResDto;
import com.siakad.dto.transform.TahunKurikulumTransform;
import com.siakad.entity.TahunKurikulum;
import com.siakad.entity.service.TahunKurikulumSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.PeriodeAkademikRepository;
import com.siakad.repository.TahunKurikulumRepository;
import com.siakad.service.TahunKurikulumService;
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
public class TahunKurikulumServiceImpl implements TahunKurikulumService {

    private final TahunKurikulumRepository tahunKurikulumRepository;
    private final PeriodeAkademikRepository periodeAkademikRepository;
    private final UserActivityServiceImpl service;
    private final TahunKurikulumTransform mapper;

    @Override
    public TahunKurikulumResDto create(TahunKurikulumReqDto dto, HttpServletRequest servletRequest) {
        var periodeAkademik = periodeAkademikRepository.findByIdAndIsDeletedFalse(dto.getSiakPeriodeAkademikId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Periode Akademik  tidak ditemukkan : " + dto.getSiakPeriodeAkademikId()));

        TahunKurikulum tahunKurikulum = mapper.toEntity(dto);
        tahunKurikulum.setSiakPeriodeAkademik(periodeAkademik);
        tahunKurikulum.setIsDeleted(false);
        tahunKurikulumRepository.save(tahunKurikulum);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_TAHUN_KURIKULUM);
        return mapper.toDto(tahunKurikulum);
    }

    @Override
    public Page<TahunKurikulumResDto> search(String keyword, Pageable pageable) {
        TahunKurikulumSpecification specBuilder = new TahunKurikulumSpecification();
        Specification<TahunKurikulum> spec = specBuilder.entitySearch(keyword);
        Page<TahunKurikulum> all = tahunKurikulumRepository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public TahunKurikulumResDto getOne(UUID id) {
        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "TahunKurikulum  tidak ditemukan : " + id));
        return mapper.toDto(tahunKurikulum);
    }

    @Override
    public TahunKurikulumResDto update(UUID id, TahunKurikulumReqDto dto, HttpServletRequest servletRequest) {

        var periodeAkademik = periodeAkademikRepository.findByIdAndIsDeletedFalse(dto.getSiakPeriodeAkademikId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Periode Akademik  tidak ditemukkan : " + dto.getSiakPeriodeAkademikId()));

        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "TahunKurikulum  tidak ditemukan : " + id));

        mapper.toEntity(dto, tahunKurikulum);
        tahunKurikulum.setIsDeleted(false);
        tahunKurikulum.setSiakPeriodeAkademik(periodeAkademik);
        tahunKurikulum.setUpdatedAt(LocalDateTime.now());

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_TAHUN_KURIKULUM);
        return mapper.toDto(tahunKurikulum);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        TahunKurikulum tahunKurikulum = tahunKurikulumRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "TahunKurikulum  tidak ditemukan : " + id));

        tahunKurikulum.setIsDeleted(true);
        TahunKurikulum saved = tahunKurikulumRepository.save(tahunKurikulum);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_TAHUN_KURIKULUM);
        mapper.toDto(saved);
    }
}
