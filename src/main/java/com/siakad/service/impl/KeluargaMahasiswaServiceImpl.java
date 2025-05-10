package com.siakad.service.impl;

import com.siakad.dto.request.KeluargaMahasiswaReqDto;
import com.siakad.dto.response.KeluargaMahasiswaResDto;
import com.siakad.dto.transform.MahasiswaTransform;
import com.siakad.entity.KeluargaMahasiswa;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.KeluargaMahasiswaRepository;
import com.siakad.repository.MahasiswaRepository;
import com.siakad.service.KeluargaMahasiswaService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeluargaMahasiswaServiceImpl implements KeluargaMahasiswaService {

    private final KeluargaMahasiswaRepository keluargaMahasiswaRepository;
    private final MahasiswaRepository mahasiswaRepository;
    private final MahasiswaTransform mapper;
    private final UserActivityService service;

    @Override
    public KeluargaMahasiswaResDto create(UUID idMahasiswa, KeluargaMahasiswaReqDto request, HttpServletRequest servletRequest) {
         var found = mahasiswaRepository.findByIdAndIsDeletedFalse(idMahasiswa).orElse(null);

         if (found == null) {
             throw new ApplicationException(ExceptionType.USER_NOT_FOUND, "Mahasiswa tidak ditemukkan dengan id: " + idMahasiswa);
         }

         KeluargaMahasiswa keluargaMahasiswa = mapper.toEntity(request);
         keluargaMahasiswa.setIsDeleted(false);
         keluargaMahasiswa.setSiakMahasiswa(found);
         keluargaMahasiswaRepository.save(keluargaMahasiswa);

         service.saveUserActivity(servletRequest, MessageKey.CREATE_KELUARGA_MAHASISWA);

         return mapper.toDto(keluargaMahasiswa);
    }

    @Override
    public Page<KeluargaMahasiswaResDto> getPaginated(UUID idMahasiswa, int page, int size) {
        return null;
    }

    @Override
    public KeluargaMahasiswaResDto getOne(UUID idMahasiswa, UUID id) {
        var found = mahasiswaRepository.findByIdAndIsDeletedFalse(idMahasiswa).orElse(null);

        if (found == null)
            throw new ApplicationException(ExceptionType.USER_NOT_FOUND, "Mahasiswa tidak ditemukkan dengan id: " + idMahasiswa);

        KeluargaMahasiswa keluargaMahasiswa = keluargaMahasiswaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.USER_NOT_FOUND,
                        "Keluarga mahasiswa tidak ditemukkan dengan id: " + id));

        return mapper.toDto(keluargaMahasiswa);
    }

    @Override
    public KeluargaMahasiswaResDto update(UUID idMahasiswa, UUID id, KeluargaMahasiswaReqDto request, HttpServletRequest servletRequest) {
        var found = mahasiswaRepository.findByIdAndIsDeletedFalse(idMahasiswa).orElse(null);

        if (found == null)
            throw new ApplicationException(ExceptionType.USER_NOT_FOUND, "Mahasiswa tidak ditemukkan dengan id: " + idMahasiswa);

        KeluargaMahasiswa keluargaMahasiswa = keluargaMahasiswaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.USER_NOT_FOUND,
                        "Keluarga mahasiswa tidak ditemukkan dengan id: " + id));

        mapper.toEntity(request, keluargaMahasiswa);
        keluargaMahasiswa.setId(id);
        keluargaMahasiswa.setUpdatedAt(LocalDateTime.now());
        keluargaMahasiswaRepository.save(keluargaMahasiswa);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_KELUARGA_MAHASISWA);

        return mapper.toDto(keluargaMahasiswa);
    }

    @Override
    public void delete(UUID idMahasiswa, UUID id, HttpServletRequest servletRequest) {
        var found = mahasiswaRepository.findByIdAndIsDeletedFalse(idMahasiswa).orElse(null);

        if (found == null)
            throw new ApplicationException(ExceptionType.USER_NOT_FOUND, "Mahasiswa tidak ditemukkan dengan id: " + idMahasiswa);

        KeluargaMahasiswa keluargaMahasiswa = keluargaMahasiswaRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.USER_NOT_FOUND,
                        "Keluarga mahasiswa tidak ditemukkan dengan id: " + id));

        keluargaMahasiswa.setIsDeleted(true);
        keluargaMahasiswaRepository.save(keluargaMahasiswa);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_KELUARGA_MAHASISWA);
        mapper.toDto(keluargaMahasiswa);
    }
}
