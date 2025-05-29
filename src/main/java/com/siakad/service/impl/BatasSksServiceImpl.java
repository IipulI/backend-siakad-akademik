package com.siakad.service.impl;

import com.siakad.dto.request.BatasSksReqDto;
import com.siakad.dto.response.BatasSksResDto;
import com.siakad.dto.transform.BatasSksTransform;
import com.siakad.entity.BatasSks;
import com.siakad.entity.Jenjang;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.BatasSksRepository;
import com.siakad.repository.JenjangRepository;
import com.siakad.service.BatasSksService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BatasSksServiceImpl implements BatasSksService {

    private final BatasSksRepository batasSksRepository;
    private final JenjangRepository jenjangRepository;
    private final BatasSksTransform mapper;
    private final UserActivityService service;

    @Override
    public BatasSksResDto save(BatasSksReqDto dto, HttpServletRequest request) {
        Jenjang jenjang = jenjangRepository.findByIdAndIsDeletedFalse(dto.getSiakJenjangId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Jenjang tidak ditemukkan : " + dto.getSiakJenjangId()));

        BatasSks batasSks = mapper.toEntity(dto);
        batasSks.setSiakJenjang(jenjang);
        batasSks.setIsDeleted(false);
        BatasSks saved = batasSksRepository.save(batasSks);
        service.saveUserActivity(request, MessageKey.CREATE_BATAS_SKS);
        return mapper.toDto(saved);
    }

    @Override
    public List<BatasSksResDto> getAll() {
        return batasSksRepository.findAllByIsDeletedFalse().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public BatasSksResDto getOne(UUID id) {
        BatasSks batasSks = batasSksRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Batas Sks tidak ditemukkan :" + id));

        return mapper.toDto(batasSks);
    }

    @Override
    public BatasSksResDto update(UUID id, BatasSksReqDto dto, HttpServletRequest request) {
        BatasSks batasSks = batasSksRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Batas Sks tidak ditemukkan :" + id));

        Jenjang jenjang = jenjangRepository.findByIdAndIsDeletedFalse(dto.getSiakJenjangId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Jenjang tidak ditemukkan : " + dto.getSiakJenjangId()));

        mapper.toEntity(dto, batasSks);
        batasSks.setSiakJenjang(jenjang);
        batasSks.setIsDeleted(false);
        batasSks.setUpdatedAt(LocalDateTime.now());
        BatasSks saved = batasSksRepository.save(batasSks);
        service.saveUserActivity(request, MessageKey.UPDATE_BATAS_SKS);

        return mapper.toDto(saved);
    }

    @Override
    public void delete(UUID id, HttpServletRequest request) {
        BatasSks batasSks = batasSksRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Batas Sks tidak ditemukkan :" + id));

        batasSks.setIsDeleted(true);
        BatasSks saved = batasSksRepository.save(batasSks);

        service.saveUserActivity(request, MessageKey.DELETE_BATAS_SKS);
        mapper.toDto(saved);


    }
}
