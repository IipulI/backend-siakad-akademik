package com.siakad.service.impl;

import com.siakad.dto.request.CapaianMataKuliahReqDto;
import com.siakad.dto.response.CapaianMataKuliahResDto;
import com.siakad.dto.response.MataKuliahCpmkMappingDto;
import com.siakad.dto.transform.CapaianMataKuliahTransform;
import com.siakad.entity.CapaianMataKuliah;
import com.siakad.entity.CapaianPembelajaranLulusan;
import com.siakad.entity.MataKuliah;
import com.siakad.entity.service.CapaianMataKuliahSpecification;
import com.siakad.enums.ExceptionType;
import com.siakad.enums.MessageKey;
import com.siakad.exception.ApplicationException;
import com.siakad.repository.CapaianMataKuliahRepository;
import com.siakad.repository.CapaianPembelajaranLulusanRepository;
import com.siakad.repository.MataKuliahRepository;
import com.siakad.service.CapaianMataKuliahService;
import com.siakad.service.UserActivityService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CapaianMataKuliahServiceImpl implements CapaianMataKuliahService {

    private final MataKuliahRepository mataKuliahRepository;
    private final CapaianMataKuliahRepository capaianMataKuliahRepository;
    private final CapaianPembelajaranLulusanRepository capaianPembelajaranLulusanRepository;
    private final UserActivityService service;
    private final CapaianMataKuliahTransform mapper;
    private static final String MESSAGE_MATAKULIAH = "Mata kuliah tidak ditemukkan : ";

    @Override
    public CapaianMataKuliahResDto save(UUID idMataKuliah, CapaianMataKuliahReqDto dto, HttpServletRequest servletRequest) {
        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(idMataKuliah)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, MESSAGE_MATAKULIAH + idMataKuliah));

        CapaianMataKuliah entity = mapper.toEntity(dto);
        entity.setSiakMataKuliah(mataKuliah);
        entity.setIsDeleted(false);
        if(dto.getCapaianPembelajaranIds() != null && !dto.getCapaianPembelajaranIds().isEmpty()) {
            List<CapaianPembelajaranLulusan> capaianPembelajaranList = capaianPembelajaranLulusanRepository.findAllByIdInAndIsDeletedFalse(dto.getCapaianPembelajaranIds());
            entity.setCapaianPembelajaranLulusanList(capaianPembelajaranList);
        }
        CapaianMataKuliah saved = capaianMataKuliahRepository.save(entity);
        service.saveUserActivity(servletRequest, MessageKey.CREATE_CAPAIAN_MATA_KULIAH);
        return mapper.toDto(saved);
    }

    @Override
    public CapaianMataKuliahResDto getOne(UUID idMataKuliah, UUID id) {
        mataKuliahRepository.findByIdAndIsDeletedFalse(idMataKuliah)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, MESSAGE_MATAKULIAH + idMataKuliah));

        CapaianMataKuliah capaianMataKuliah = capaianMataKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Capaian Mata kuliah tidak ditemukan : " + id));

        if (!capaianMataKuliah.getSiakMataKuliah().getId().equals(idMataKuliah)) {
            throw new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Capaian Mata kuliah tidak dimiliki oleh mata kuliah: " + idMataKuliah);
        }
        return mapper.toDto(capaianMataKuliah);
    }

    @Override
    public Page<CapaianMataKuliahResDto> getPaginate(UUID idMataKuliah, Pageable pageable) {
        CapaianMataKuliahSpecification specBuilder = new CapaianMataKuliahSpecification();
        Specification<CapaianMataKuliah> spec = specBuilder.entitySearch(idMataKuliah);
        Page<CapaianMataKuliah> all = capaianMataKuliahRepository.findAll(spec, pageable);
        return all.map(mapper::toDto);
    }

    @Override
    public CapaianMataKuliahResDto update(UUID idMataKuliah, UUID id, CapaianMataKuliahReqDto dto, HttpServletRequest servletRequest) {
        CapaianMataKuliah capaianMataKuliah = capaianMataKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Capaian Mata kuliah tidak ditemukan : " + id));

        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(idMataKuliah)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, MESSAGE_MATAKULIAH + idMataKuliah));

        mapper.toEntity(dto, capaianMataKuliah);
        capaianMataKuliah.setSiakMataKuliah(mataKuliah);
        capaianMataKuliah.setUpdatedAt(LocalDateTime.now());
        if(dto.getCapaianPembelajaranIds() != null && !dto.getCapaianPembelajaranIds().isEmpty()) {
            List<CapaianPembelajaranLulusan> capaianPembelajaranList = capaianPembelajaranLulusanRepository.findAllByIdInAndIsDeletedFalse(dto.getCapaianPembelajaranIds());
            capaianMataKuliah.setCapaianPembelajaranLulusanList(capaianPembelajaranList);
        }
        CapaianMataKuliah saved = capaianMataKuliahRepository.save(capaianMataKuliah);

        service.saveUserActivity(servletRequest, MessageKey.UPDATE_CAPAIAN_MATA_KULIAH);
        return mapper.toDto(saved);
    }

    @Override
    public void delete(UUID idMataKuliah, UUID id, HttpServletRequest servletRequest) {
        MataKuliah mataKuliah = mataKuliahRepository.findByIdAndIsDeletedFalse(idMataKuliah)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, MESSAGE_MATAKULIAH + idMataKuliah));

        CapaianMataKuliah capaianMataKuliah = capaianMataKuliahRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ApplicationException(ExceptionType.RESOURCE_NOT_FOUND, "Capaian Mata Kuliah tidak ditemukkan : " + idMataKuliah));

        capaianMataKuliah.setSiakMataKuliah(mataKuliah);
        capaianMataKuliah.setIsDeleted(true);
        CapaianMataKuliah saved = capaianMataKuliahRepository.save(capaianMataKuliah);
        service.saveUserActivity(servletRequest, MessageKey.DELETE_CAPAIAN_MATA_KULIAH);
        mapper.toDto(saved);
    }

    @Override
    public List<MataKuliahCpmkMappingDto> getMataKuliahWithCpmkStatus(String tahunKurikulum, String namaProgramStudi, String namaMataKuliah) {
        return mataKuliahRepository.findAllMataKuliahWithCpmkStatus(tahunKurikulum, namaProgramStudi, namaMataKuliah);
    }
}
