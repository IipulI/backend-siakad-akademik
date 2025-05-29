package com.siakad.service.impl;

import com.siakad.dto.request.JenjangReqDto;
import com.siakad.dto.response.JenjangResDto;
import com.siakad.dto.transform.JenjangTransform;
import com.siakad.entity.Jenjang;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.JenjangRepository;
import com.siakad.service.JenjangService;
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
public class JenjangServiceImpl implements JenjangService {

    private final JenjangRepository jenjangRepository;
    private final JenjangTransform mapper;
    private final UserActivityService service;

    @Override
    public JenjangResDto save(JenjangReqDto dto, HttpServletRequest servletRequest) {
        Jenjang jenjang = mapper.toEntity(dto);
        jenjang.setIsDeleted(false);
        Jenjang saved = jenjangRepository.save(jenjang);
        service.saveUserActivity(servletRequest, MessageKey.CREATE_JENJANG);
        return mapper.toDto(saved);
    }

    @Override
    public List<JenjangResDto> getAll() {
        return jenjangRepository.findAllByIsDeletedFalse().stream().map(mapper::toDto).collect(Collectors.toList());
    }

    @Override
    public JenjangResDto getOne(UUID id) {
        Jenjang jenjang = jenjangRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Jenjang tidak ditemukkkan : " + id));

        return mapper.toDto(jenjang);
    }

    @Override
    public JenjangResDto update(UUID id, JenjangReqDto dto, HttpServletRequest servletRequest) {
        Jenjang jenjang = jenjangRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Jenjang tidak ditemukkkan : " + id));

        mapper.toEntity(dto, jenjang);
        jenjang.setIsDeleted(false);
        jenjang.setUpdatedAt(LocalDateTime.now());
        Jenjang saved = jenjangRepository.save(jenjang);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_JENJANG);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        Jenjang jenjang = jenjangRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Jenjang tidak ditemukkan : " + id));

        jenjang.setIsDeleted(true);
        Jenjang saved = jenjangRepository.save(jenjang);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_JENJANG);

        mapper.toDto(saved);
    }
}
