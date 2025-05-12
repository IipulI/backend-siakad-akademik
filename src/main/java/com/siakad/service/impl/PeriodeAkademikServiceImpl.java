package com.siakad.service.impl;

import com.siakad.dto.request.PeriodeAkademikReqDto;
import com.siakad.dto.response.PeriodeAkademikResDto;
import com.siakad.dto.transform.PeriodeAkademikTransform;
import com.siakad.entity.PeriodeAkademik;
import com.siakad.entity.service.PeriodeAkademikSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.enums.PeriodeAkademikStatus;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.PeriodeAkademikRepository;
import com.siakad.repository.TahunAjaranRepository;
import com.siakad.service.PeriodeAkademikService;
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
public class PeriodeAkademikServiceImpl implements PeriodeAkademikService {

    private final PeriodeAkademikRepository periodeAkademikRepository;
    private final TahunAjaranRepository tahunAjaranRepository;
    private final UserActivityService service;
    private final PeriodeAkademikTransform mapper;

    @Override
    public PeriodeAkademikResDto create(PeriodeAkademikReqDto request, HttpServletRequest servletRequest) {
        var tahunAjaran = tahunAjaranRepository.findByIdAndIsDeletedFalse(request.getSiakTahunAjaranId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                        "Tahun Ajaran tidak ditemukan : " + request.getSiakTahunAjaranId()));

        PeriodeAkademik periodeAkademik = mapper.toEntity(request);
        periodeAkademik.setSiakTahunAjaran(tahunAjaran);
        periodeAkademik.setStatus(PeriodeAkademikStatus.NONAKTIF.getLabel());
        periodeAkademik.setIsDeleted(false);
        periodeAkademikRepository.save(periodeAkademik);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_PERIODE_AKADEMIK);
        return mapper.toDto(periodeAkademik);
    }

    @Override
    public Page<PeriodeAkademikResDto> search(String keyword, Pageable pageable) {
        PeriodeAkademikSpecification specBuilder = new PeriodeAkademikSpecification();
        Specification<PeriodeAkademik> spec = specBuilder.entitySearch(keyword);
        Page<PeriodeAkademik> all = periodeAkademikRepository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public PeriodeAkademikResDto getOne(UUID id) {
        PeriodeAkademik periodeAkademik = periodeAkademikRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                        "Periode akademik tidak ditemukan : " + id));
        return mapper.toDto(periodeAkademik);
    }

    @Override
    public PeriodeAkademikResDto update(UUID id, PeriodeAkademikReqDto request, HttpServletRequest servletRequest) {
        PeriodeAkademik periodeAkademik = periodeAkademikRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                        "Periode akademik tidak ditemukan : " + id));

        mapper.toEntity(request, periodeAkademik);
        periodeAkademik.setStatus(PeriodeAkademikStatus.NONAKTIF.getLabel());
        periodeAkademik.setUpdatedAt(LocalDateTime.now());
        periodeAkademikRepository.save(periodeAkademik);
        service.saveUserActivity(servletRequest, MessageKey.UPDATE_PERIODE_AKADEMIK);
        return mapper.toDto(periodeAkademik);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        PeriodeAkademik periodeAkademik = periodeAkademikRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND,
                        "Periode akademik tidak ditemukan : " + id));

        periodeAkademik.setIsDeleted(true);
        PeriodeAkademik saved = periodeAkademikRepository.save(periodeAkademik);

        service.saveUserActivity(servletRequest, MessageKey.DELETE_PERIODE_AKADEMIK);
        mapper.toDto(saved);
    }
}
