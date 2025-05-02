package com.siakad.service.impl;

import com.siakad.dto.request.KomponenPenilaianReqDto;
import com.siakad.dto.response.KomponenPenilaianResDto;
import com.siakad.dto.transform.KomponenPenilaianTransform;
import com.siakad.entity.KomponenPenilaian;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.KomponenPenilaianRepository;
import com.siakad.service.KomponenPenilaianService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KomponenPenilaianServiceImpl implements KomponenPenilaianService {

    private final KomponenPenilaianRepository repository;
    private final KomponenPenilaianTransform mapper;
    private final UserActivityService service;

    @Override
    public KomponenPenilaianResDto create(KomponenPenilaianReqDto request, HttpServletRequest httpServletRequest) {
        KomponenPenilaian komponenPenilaian = mapper.toEntity(request);
        komponenPenilaian.setIsDeleted(false);

        repository.save(komponenPenilaian);
        service.saveUserActivity(httpServletRequest, MessageKey.CREATE_KOMPONEN_PENILAIAN);
        return mapper.toDto(komponenPenilaian);
    }

    @Override
    public Page<KomponenPenilaianResDto> getPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, 10);

        Page<KomponenPenilaian>  all = repository.findAllNotDeleted(pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public KomponenPenilaianResDto getOne(UUID id) {
        KomponenPenilaian komponenPenilaian = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.USER_NOT_FOUND, "komponenPenilaian  tidak ditemukan :" + id));
        return mapper.toDto(komponenPenilaian);
    }

    @Override
    public KomponenPenilaianResDto update(UUID id, KomponenPenilaianReqDto request, HttpServletRequest httpServletRequest) {
        KomponenPenilaian komponenPenilaian = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "komponenPenilaian  tidak ditemukan :" + id));

        mapper.toEntity(request, komponenPenilaian);
        komponenPenilaian.setUpdatedAt(LocalDateTime.now());
        repository.save(komponenPenilaian);

        service.saveUserActivity(httpServletRequest, MessageKey.UPDATE_KOMPONEN_PENILAIAN);

        return mapper.toDto(komponenPenilaian);
    }

    @Override
    public void delete(UUID id, HttpServletRequest httpServletRequest) {
        KomponenPenilaian komponenPenilaian = repository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "komponenPenilaian  tidak ditemukan :" + id));

        komponenPenilaian.setIsDeleted(true);
        repository.save(komponenPenilaian);

        service.saveUserActivity(httpServletRequest, MessageKey.DELETE_KOMPONEN_PENILAIAN);

        mapper.toDto(komponenPenilaian);
    }
}
