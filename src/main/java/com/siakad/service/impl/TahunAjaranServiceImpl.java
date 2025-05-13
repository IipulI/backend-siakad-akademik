package com.siakad.service.impl;

import com.siakad.dto.request.TahunAjaranReqDto;
import com.siakad.dto.response.TahunAjaranResDto;
import com.siakad.dto.transform.TahunAjaranTransform;
import com.siakad.entity.TahunAjaran;
import com.siakad.entity.service.TahunAjaranSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.TahunAjaranRepository;
import com.siakad.service.TahunAjaranService;
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
public class TahunAjaranServiceImpl implements TahunAjaranService {

    private final TahunAjaranRepository repository;
    private final TahunAjaranTransform mapper;
    private final UserActivityService service;

    @Override
    public TahunAjaranResDto create(TahunAjaranReqDto request, HttpServletRequest servletRequest) {
        TahunAjaran tahunAjaran = mapper.toEntity(request);
        tahunAjaran.setIsDeleted(false);
        repository.save(tahunAjaran);

        service.saveUserActivity(servletRequest, MessageKey.CREATE_TAHUN_AJARAN);
        return mapper.toDto(tahunAjaran);
    }

    @Override
    public Page<TahunAjaranResDto> search(String keyword, Pageable pageable) {
        TahunAjaranSpecification specBuilder = new TahunAjaranSpecification();
        Specification<TahunAjaran> spec = specBuilder.entitySearch(keyword);
        Page<TahunAjaran> all = repository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }


    @Override
    public TahunAjaranResDto getOne(UUID id) {
        TahunAjaran tahunAjaran = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(
                        ExceptionType.RESOURCE_NOT_FOUND, "Tahun ajaran tidak ditemukan : "+ id));

        return mapper.toDto(tahunAjaran);
    }

    @Override
    public TahunAjaranResDto update(UUID id, TahunAjaranReqDto request, HttpServletRequest servletRequest) {
        TahunAjaran tahunAjaran = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(
                        ExceptionType.RESOURCE_NOT_FOUND, "Tahun ajaran tidak ditemukan"+ id));
        mapper.toEntity(request, tahunAjaran);
        tahunAjaran.setUpdatedAt(LocalDateTime.now());
        repository.save(tahunAjaran);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_TAHUN_AJARAN);

        return mapper.toDto(tahunAjaran);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        TahunAjaran tahunAjaran = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(
                        ExceptionType.RESOURCE_NOT_FOUND, "Tahun ajaran tidak ditemukan"+ id));

        tahunAjaran.setIsDeleted(true);
        TahunAjaran saved = repository.save(tahunAjaran);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_TAHUN_AJARAN);
        mapper.toDto(saved);
    }
}
