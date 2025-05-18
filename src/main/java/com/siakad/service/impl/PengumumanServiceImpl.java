package com.siakad.service.impl;

import com.siakad.dto.request.PengumumanReqDto;
import com.siakad.dto.response.PengumumanResDto;
import com.siakad.dto.transform.PengumumanTransform;
import com.siakad.entity.Pengumuman;
import com.siakad.entity.User;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.PengumumanRepository;
import com.siakad.repository.UserRepository;
import com.siakad.service.PengumumanService;
import com.siakad.service.UserActivityService;
import com.siakad.util.FileUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PengumumanServiceImpl implements PengumumanService {

    private final UserRepository userRepository;
    private final UserActivityService service;
    private final PengumumanRepository pengumumanRepository;
    private final PengumumanTransform mapper;

    @Override
    public PengumumanResDto save(PengumumanReqDto dto, MultipartFile file, HttpServletRequest servletRequest) throws IOException {
        User user = userRepository.findByIdAndIsDeletedFalse(dto.getSiakUserId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "User Not Found"));

        Pengumuman entity = mapper.toEntity(dto);
        entity.setSiakUser(user);
        entity.setIsDeleted(false);
        entity.setBanner(FileUtils.compress(file.getBytes()));

        Pengumuman saved = pengumumanRepository.save(entity);
        service.saveUserActivity(servletRequest, MessageKey.CREATE_PENGUMUMAN);
        return mapper.toDto(saved);
    }

    @Override
    public PengumumanResDto getOne(UUID id) {
        Pengumuman entity = pengumumanRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Pengumuman tidak ditemukan : " + id));
        return mapper.toDto(entity);
    }

    @Override
    public PengumumanResDto update(UUID id, PengumumanReqDto dto, MultipartFile file, HttpServletRequest servletRequest) throws IOException {
        Pengumuman entity = pengumumanRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Pengumuman tidak ditemukan : " + id));

        User user = userRepository.findByIdAndIsDeletedFalse(dto.getSiakUserId())
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "User Not Found"));

        mapper.toEntity(dto, entity);
        entity.setId(id);
        entity.setSiakUser(user);
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setBanner(FileUtils.compress(file.getBytes()));

        Pengumuman saved = pengumumanRepository.save(entity);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_PENGUMUMAN);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(UUID id, HttpServletRequest servletRequest) {
        Pengumuman entity = pengumumanRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Pengumuman tidak ditemukan : " + id));

        entity.setIsDeleted(true);
        Pengumuman saved = pengumumanRepository.save(entity);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_PENGUMUMAN);
        mapper.toDto(saved);
    }

    @Override
    public byte[] getBanner(UUID id) {
        Optional<Pengumuman> pengumuman = pengumumanRepository.findByIdAndIsDeletedFalse(id);
        return FileUtils.decompress(pengumuman.get().getBanner());
    }
}
